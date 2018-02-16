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
import com.garpr.android.adapters.RankingsAdapter
import com.garpr.android.extensions.optActivity
import com.garpr.android.misc.*
import com.garpr.android.models.RankedPlayer
import com.garpr.android.models.RankingsBundle
import com.garpr.android.networking.ApiCall
import com.garpr.android.networking.ApiListener
import com.garpr.android.networking.ServerApi
import kotterknife.bindView
import javax.inject.Inject

class RankingsLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : SearchableRefreshLayout(context, attrs), ApiListener<RankingsBundle>, Refreshable,
        SwipeRefreshLayout.OnRefreshListener {

    private lateinit var adapter: RankingsAdapter

    @Inject
    protected lateinit var notificationsManager: NotificationsManager

    @Inject
    protected lateinit var regionManager: RegionManager

    @Inject
    protected lateinit var serverApi: ServerApi

    private val error: ErrorContentLinearLayout by bindView(R.id.error)
    private val empty: View by bindView(R.id.empty)


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
        adapter = RankingsAdapter(context)
        recyclerView.adapter = adapter

        fetchRankingsBundle()
    }

    private fun onRankingsBundleFetched() {
        if (!isAlive) {
            return
        }

        (context.optActivity() as? Listener)?.onRankingsBundleFetched(this)
    }

    override fun onRefresh() {
        fetchRankingsBundle()
    }

    var rankingsBundle: RankingsBundle? = null
        private set

    override val recyclerView: RecyclerView by bindView(R.id.recyclerView)

    override fun refresh() {
        notificationsManager.cancelAll()
        fetchRankingsBundle()
    }

    override fun search(query: String?) {
        val rankings = rankingsBundle?.rankings

        if (rankings == null || rankings.isEmpty()) {
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
