package com.garpr.android.views

import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.garpr.android.App
import com.garpr.android.R
import com.garpr.android.adapters.TournamentsAdapter
import com.garpr.android.managers.RegionManager
import com.garpr.android.misc.ListUtils
import com.garpr.android.misc.Refreshable
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.models.AbsTournament
import com.garpr.android.models.TournamentsBundle
import com.garpr.android.networking.ApiCall
import com.garpr.android.networking.ApiListener
import com.garpr.android.networking.ServerApi
import kotterknife.bindView
import javax.inject.Inject

class TournamentsLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : SearchableRefreshLayout(context, attrs), ApiListener<TournamentsBundle>, Refreshable,
        SwipeRefreshLayout.OnRefreshListener {

    private lateinit var adapter: TournamentsAdapter

    @Inject
    protected lateinit var regionManager: RegionManager

    @Inject
    protected lateinit var serverApi: ServerApi

    private val error: ErrorContentLinearLayout by bindView(R.id.error)
    private val empty: View by bindView(R.id.empty)


    companion object {
        fun inflate(parent: ViewGroup): TournamentsLayout = LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_tournaments, parent, false) as TournamentsLayout
    }

    override fun failure(errorCode: Int) {
        tournamentsBundle = null
        showError(errorCode)
    }

    private fun fetchTournamentsBundle() {
        isRefreshing = true
        serverApi.getTournaments(regionManager.getRegion(context), ApiCall(this))
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (isInEditMode) {
            return
        }

        App.get().appComponent.inject(this)

        setOnRefreshListener(this)
        recyclerView.addItemDecoration(DividerItemDecoration(context,
                DividerItemDecoration.VERTICAL))
        recyclerView.setHasFixedSize(true)
        adapter = TournamentsAdapter(context)
        recyclerView.adapter = adapter

        fetchTournamentsBundle()
    }

    override fun onRefresh() {
        fetchTournamentsBundle()
    }

    override val recyclerView: RecyclerView by bindView(R.id.recyclerView)

    override fun refresh() {
        fetchTournamentsBundle()
    }

    override fun search(query: String?) {
        val tournaments = tournamentsBundle?.tournaments

        if (tournaments == null || tournaments.isEmpty()) {
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
