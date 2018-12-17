package com.garpr.android.views

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.garpr.android.R
import com.garpr.android.adapters.RankingsAdapter
import com.garpr.android.extensions.activity
import com.garpr.android.extensions.appComponent
import com.garpr.android.managers.NotificationsManager
import com.garpr.android.managers.RegionManager
import com.garpr.android.misc.ListUtils
import com.garpr.android.misc.Refreshable
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.models.RankedPlayer
import com.garpr.android.models.RankingsBundle
import com.garpr.android.networking.ApiCall
import com.garpr.android.networking.ApiListener
import com.garpr.android.networking.ServerApi
import kotlinx.android.synthetic.main.layout_rankings.view.*
import javax.inject.Inject

class RankingsLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : SearchableRefreshLayout(context, attrs), ApiListener<RankingsBundle>, Refreshable,
        SwipeRefreshLayout.OnRefreshListener {

    @Inject
    protected lateinit var notificationsManager: NotificationsManager

    @Inject
    protected lateinit var regionManager: RegionManager

    @Inject
    protected lateinit var serverApi: ServerApi

    private lateinit var adapter: RankingsAdapter

    var rankingsBundle: RankingsBundle? = null
        private set


    interface Listener {
        fun onRankingsBundleFetched(layout: RankingsLayout)
    }

    companion object {
        fun inflate(parent: ViewGroup): RankingsLayout = LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_rankings, parent, false) as RankingsLayout
    }

    override fun failure(errorCode: Int) {
        rankingsBundle = null
        notificationsManager.cancelAll()
        onRankingsBundleFetched()
        showError(errorCode)
    }

    private fun fetchRankingsBundle() {
        isRefreshing = true
        serverApi.getRankings(regionManager.getRegion(context), ApiCall(this))
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
        adapter = RankingsAdapter(context)
        recyclerView.adapter = adapter

        fetchRankingsBundle()
    }

    private fun onRankingsBundleFetched() {
        if (isAlive) {
            (activity as? Listener)?.onRankingsBundleFetched(this)
        }
    }

    override fun onRefresh() {
        fetchRankingsBundle()
    }

    override fun refresh() {
        notificationsManager.cancelAll()
        fetchRankingsBundle()
    }

    override fun search(query: String?) {
        val rankings = rankingsBundle?.rankings

        if (rankings.isNullOrEmpty()) {
            return
        }

        threadUtils.run(object : ThreadUtils.Task {
            private var list: List<RankedPlayer>? = null

            override fun onBackground() {
                if (!isAlive || !TextUtils.equals(query, searchQuery)) {
                    return
                }

                list = ListUtils.searchRankingList(query, rankings)
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

    private fun showRankingsBundle() {
        adapter.set(rankingsBundle)
        empty.visibility = View.GONE
        error.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        isRefreshing = false
    }

    override fun success(`object`: RankingsBundle?) {
        rankingsBundle = `object`
        notificationsManager.cancelAll()

        if (`object`?.rankings?.isNotEmpty() == true) {
            showRankingsBundle()
        } else {
            showEmpty()
        }

        onRankingsBundleFetched()
    }

}
