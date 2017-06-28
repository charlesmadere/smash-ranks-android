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
import android.view.View
import butterknife.BindView
import com.garpr.android.App
import com.garpr.android.R
import com.garpr.android.adapters.PlayersAdapter
import com.garpr.android.misc.ListUtils
import com.garpr.android.misc.MiscUtils
import com.garpr.android.misc.RegionManager
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.models.AbsPlayer
import com.garpr.android.models.PlayersBundle
import com.garpr.android.networking.ApiCall
import com.garpr.android.networking.ApiListener
import com.garpr.android.networking.ServerApi
import javax.inject.Inject

class PlayersLayout : SearchableFrameLayout, ApiListener<PlayersBundle>,
        SwipeRefreshLayout.OnRefreshListener {

    lateinit private var mAdapter: PlayersAdapter
    var mPlayersBundle: PlayersBundle? = null
        private set

    @Inject
    lateinit internal var mRegionManager: RegionManager

    @Inject
    lateinit internal var mServerApi: ServerApi

    @BindView(R.id.error)
    lateinit internal var mError: ErrorLinearLayout

    @BindView(R.id.recyclerView)
    lateinit internal var mRecyclerView: RecyclerView

    @BindView(R.id.refreshLayout)
    lateinit internal var mRefreshLayout: SwipeRefreshLayout

    @BindView(R.id.empty)
    lateinit internal var mEmpty: View


    interface Listener {
        fun onPlayersBundleFetched(layout: PlayersLayout)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int,
            @StyleRes defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun failure(errorCode: Int) {
        mPlayersBundle = null
        showError(errorCode)
    }

    private fun fetchPlayersBundle() {
        mRefreshLayout.isRefreshing = true
        mServerApi.getPlayers(mRegionManager.getRegion(context), ApiCall(this))
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
        mAdapter = PlayersAdapter(context)
        mRecyclerView.adapter = mAdapter

        fetchPlayersBundle()
    }

    private fun onPlayersBundleFetched() {
        if (!isAlive) {
            return
        }

        val activity = MiscUtils.optActivity(context)

        if (activity is Listener) {
            activity.onPlayersBundleFetched(this)
        }
    }

    override fun onRefresh() {
        fetchPlayersBundle()
    }

    override fun search(query: String?) {
        if (mPlayersBundle == null || !mPlayersBundle!!.hasPlayers()) {
            return
        }

        mThreadUtils.run(object : ThreadUtils.Task {
            private var mList: List<AbsPlayer>? = null

            override fun onBackground() {
                if (!isAlive || !TextUtils.equals(query, searchQuery)) {
                    return
                }

                mList = ListUtils.searchPlayerList(query, mPlayersBundle!!.players)
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

    private fun showError(errorCode: Int) {
        mAdapter.clear()
        mRecyclerView.visibility = View.GONE
        mEmpty.visibility = View.GONE
        mError.setVisibility(View.VISIBLE, errorCode)
        mRefreshLayout.isRefreshing = false
    }

    private fun showPlayersBundle() {
        mAdapter.set(mPlayersBundle)
        mEmpty.visibility = View.GONE
        mError.visibility = View.GONE
        mRecyclerView.visibility = View.VISIBLE
        mRefreshLayout.isRefreshing = false
    }

    override fun success(playersBundle: PlayersBundle?) {
        this.mPlayersBundle = playersBundle
        onPlayersBundleFetched()

        if (this.mPlayersBundle != null && this.mPlayersBundle!!.hasPlayers()) {
            showPlayersBundle()
        } else {
            showEmpty()
        }
    }

}
