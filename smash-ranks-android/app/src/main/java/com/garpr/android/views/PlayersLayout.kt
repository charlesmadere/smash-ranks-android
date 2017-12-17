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
import com.garpr.android.App
import com.garpr.android.R
import com.garpr.android.adapters.PlayersAdapter
import com.garpr.android.extensions.optActivity
import com.garpr.android.misc.ListUtils
import com.garpr.android.misc.RegionManager
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.models.AbsPlayer
import com.garpr.android.models.PlayersBundle
import com.garpr.android.networking.ApiCall
import com.garpr.android.networking.ApiListener
import com.garpr.android.networking.ServerApi
import kotterknife.bindView
import javax.inject.Inject

class PlayersLayout : SearchableFrameLayout, ApiListener<PlayersBundle>,
        SwipeRefreshLayout.OnRefreshListener {

    lateinit private var mAdapter: PlayersAdapter
    var mPlayersBundle: PlayersBundle? = null
        private set

    @Inject
    protected lateinit var mRegionManager: RegionManager

    @Inject
    protected lateinit var mServerApi: ServerApi

    private val mError: ErrorContentLinearLayout by bindView(R.id.error)
    private val mRecyclerView: RecyclerView by bindView(R.id.recyclerView)
    private val mRefreshLayout: SwipeRefreshLayout by bindView(R.id.refreshLayout)
    private val mEmpty: View by bindView(R.id.empty)


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

        val activity = context.optActivity()

        if (activity is Listener) {
            activity.onPlayersBundleFetched(this)
        }
    }

    override fun onRefresh() {
        fetchPlayersBundle()
    }

    override val recyclerView: RecyclerView?
        get() = mRecyclerView

    override fun refresh() {
        fetchPlayersBundle()
    }

    override fun search(query: String?) {
        val players = mPlayersBundle?.players

        if (players == null || players.isEmpty()) {
            return
        }

        mThreadUtils.run(object : ThreadUtils.Task {
            private var mList: List<AbsPlayer>? = null

            override fun onBackground() {
                if (!isAlive || !TextUtils.equals(query, searchQuery)) {
                    return
                }

                mList = ListUtils.searchPlayerList(query, players)
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

    override fun success(`object`: PlayersBundle?) {
        mPlayersBundle = `object`
        onPlayersBundleFetched()

        if (`object`?.players?.isNotEmpty() == true) {
            showPlayersBundle()
        } else {
            showEmpty()
        }
    }

}
