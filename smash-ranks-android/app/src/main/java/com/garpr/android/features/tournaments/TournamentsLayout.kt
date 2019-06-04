package com.garpr.android.features.tournaments

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.garpr.android.R
import com.garpr.android.data.models.AbsTournament
import com.garpr.android.data.models.TournamentsBundle
import com.garpr.android.extensions.appComponent
import com.garpr.android.extensions.layoutInflater
import com.garpr.android.features.common.SearchableRefreshLayout
import com.garpr.android.misc.ListUtils
import com.garpr.android.misc.Refreshable
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.networking.ApiCall
import com.garpr.android.networking.ApiListener
import com.garpr.android.networking.ServerApi
import com.garpr.android.repositories.RegionManager
import kotlinx.android.synthetic.main.layout_tournaments.view.*
import javax.inject.Inject

class TournamentsLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : SearchableRefreshLayout(context, attrs), ApiListener<TournamentsBundle>, Refreshable,
        SwipeRefreshLayout.OnRefreshListener {

    private val adapter = TournamentsAdapter()

    @Inject
    protected lateinit var regionManager: RegionManager

    @Inject
    protected lateinit var serverApi: ServerApi


    companion object {
        fun inflate(parent: ViewGroup): TournamentsLayout = parent.layoutInflater.inflate(
                R.layout.layout_tournaments, parent, false) as TournamentsLayout
    }

    override fun failure(errorCode: Int) {
        tournamentsBundle = null
        showError(errorCode)
    }

    private fun fetchTournamentsBundle() {
        isRefreshing = true
        serverApi.getTournaments(regionManager.getRegion(context), ApiCall(this))
    }

    override fun getRecyclerView(): RecyclerView? {
        return recyclerView
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (isInEditMode) {
            return
        }

        appComponent.inject(this)

        setOnRefreshListener(this)
        recyclerView.addItemDecoration(DividerItemDecoration(context,
                DividerItemDecoration.VERTICAL))
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter

        fetchTournamentsBundle()
    }

    override fun onRefresh() {
        fetchTournamentsBundle()
    }

    override fun refresh() {
        fetchTournamentsBundle()
    }

    override fun search(query: String?) {
        val tournaments = tournamentsBundle?.tournaments

        if (tournaments.isNullOrEmpty()) {
            return
        }

        threadUtils.run(object : ThreadUtils.Task {
            private var list: List<AbsTournament>? = null

            override fun onBackground() {
                if (!isAlive || !TextUtils.equals(query, searchQuery)) {
                    return
                }

                list = ListUtils.searchTournamentList(query, tournaments)
            }

            override fun onUi() {
                if (!isAlive || !TextUtils.equals(query, searchQuery)) {
                    return
                }

                adapter.set(list)
            }
        })
    }

    private fun showEmpty() {
        adapter.clear()
        recyclerView.visibility = View.GONE
        error.visibility = View.GONE
        empty.visibility = View.VISIBLE
        isRefreshing = false
    }

    private fun showError(errorCode: Int) {
        adapter.clear()
        recyclerView.visibility = View.GONE
        empty.visibility = View.GONE
        error.setVisibility(View.VISIBLE, errorCode)
        isRefreshing = false
    }

    private fun showTournamentsBundle() {
        adapter.set(ListUtils.createTournamentList(tournamentsBundle))
        empty.visibility = View.GONE
        error.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        isRefreshing = false
    }

    override fun success(`object`: TournamentsBundle?) {
        tournamentsBundle = `object`

        if (`object`?.tournaments?.isNotEmpty() == true) {
            showTournamentsBundle()
        } else {
            showEmpty()
        }
    }

    var tournamentsBundle: TournamentsBundle? = null
        private set

}
