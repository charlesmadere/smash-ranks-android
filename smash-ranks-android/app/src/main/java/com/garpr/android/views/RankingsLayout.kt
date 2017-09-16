package com.garpr.android.views

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.support.annotation.AttrRes
import android.support.annotation.StyleRes
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

class RankingsLayout : SearchableFrameLayout, ApiListener<RankingsBundle>, Refreshable,
        SwipeRefreshLayout.OnRefreshListener {

    lateinit private var mAdapter: RankingsAdapter
    var mRankingsBundle: RankingsBundle? = null
        private set

    @Inject
    lateinit protected var mNotificationsManager: NotificationsManager

    @Inject
    lateinit protected var mRegionManager: RegionManager

    @Inject
    lateinit protected var mServerApi: ServerApi

    private val mRecyclerView: RecyclerView by bindView(R.id.recyclerView)
    private val mRefreshLayout: SwipeRefreshLayout by bindView(R.id.refreshLayout)
    private val mEmpty: View by bindView(R.id.empty)
    private val mError: View by bindView(R.id.error)


    interface Listener {
        fun onRankingsBundleFetched(layout: RankingsLayout)
    }

    companion object {
        fun inflate(parent: ViewGroup): RankingsLayout {
            val inflater = LayoutInflater.from(parent.context)
            return inflater.inflate(R.layout.layout_rankings, parent, false) as RankingsLayout
        }
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int,
            @StyleRes defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun failure(errorCode: Int) {
        mRankingsBundle = null
        mNotificationsManager.cancelAll()
        onRankingsBundleFetched()
        showError()
    }

    private fun fetchRankingsBundle() {
        mRefreshLayout.isRefreshing = true
        mServerApi.getRankings(mRegionManager.getRegion(context), ApiCall(this))
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (isInEditMode) {
            return
        }

        App.get().appComponent.inject(this)

        mRefreshLayout.setOnRefreshListener(this)
        mRecyclerView.addItemDecoration(DividerItemDecoration(context,
                DividerItemDecoration.VERTICAL))
        mRecyclerView.setHasFixedSize(true)
        mAdapter = RankingsAdapter(context)
        mRecyclerView.adapter = mAdapter

        fetchRankingsBundle()
    }

    private fun onRankingsBundleFetched() {
        if (!isAlive) {
            return
        }

        val activity = context.optActivity()

        if (activity is Listener) {
            activity.onRankingsBundleFetched(this)
        }
    }

    override fun onRefresh() {
        fetchRankingsBundle()
    }

    override val recyclerView: RecyclerView?
        get() = mRecyclerView

    override fun refresh() {
        mNotificationsManager.cancelAll()
        fetchRankingsBundle()
    }

    override fun search(query: String?) {
        val rankings = mRankingsBundle?.rankings

        if (rankings == null || rankings.isEmpty()) {
            return
        }

        mThreadUtils.run(object : ThreadUtils.Task {
            private var mList: List<RankedPlayer>? = null

            override fun onBackground() {
                if (!isAlive || !TextUtils.equals(query, searchQuery)) {
                    return
                }

                mList = ListUtils.searchRankingList(query, rankings)
            }

            override fun onUi() {
                if (!isAlive || !TextUtils.equals(query, searchQuery)) {
                    return
                }

                mAdapter.set(mList)
            }
        })
    }

    private fun showEmpty() {
        mAdapter.clear()
        mRecyclerView.visibility = View.GONE
        mError.visibility = View.GONE
        mEmpty.visibility = View.VISIBLE
        mRefreshLayout.isRefreshing = false
    }

    private fun showError() {
        mAdapter.clear()
        mRecyclerView.visibility = View.GONE
        mEmpty.visibility = View.GONE
        mError.visibility = View.VISIBLE
        mRefreshLayout.isRefreshing = false
    }

    private fun showRankingsBundle() {
        mAdapter.set(mRankingsBundle)
        mEmpty.visibility = View.GONE
        mError.visibility = View.GONE
        mRecyclerView.visibility = View.VISIBLE
        mRefreshLayout.isRefreshing = false
    }

    override fun success(`object`: RankingsBundle?) {
        mRankingsBundle = `object`
        mNotificationsManager.cancelAll()
        onRankingsBundleFetched()

        if (`object`?.rankings?.isNotEmpty() == true) {
            showRankingsBundle()
        } else {
            showEmpty()
        }
    }

}
