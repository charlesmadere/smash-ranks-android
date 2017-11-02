package com.garpr.android.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.garpr.android.App
import com.garpr.android.R
import com.garpr.android.adapters.HeadToHeadAdapter
import com.garpr.android.extensions.subtitle
import com.garpr.android.misc.ListUtils
import com.garpr.android.misc.RegionManager
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.models.*
import com.garpr.android.networking.ApiCall
import com.garpr.android.networking.ApiListener
import com.garpr.android.networking.ServerApi
import com.garpr.android.views.ErrorLinearLayout
import kotterknife.bindView
import javax.inject.Inject

class HeadToHeadActivity : BaseActivity(), ApiListener<HeadToHead>,
        SwipeRefreshLayout.OnRefreshListener {

    private var mList: List<Any>? = null
    private var mHeadToHead: HeadToHead? = null
    private lateinit var mAdapter: HeadToHeadAdapter
    private var mMatchResult: MatchResult? = null
    private lateinit var mOpponentId: String
    private var mOpponentName: String? = null
    private lateinit var mPlayerId: String
    private var mPlayerName: String? = null

    @Inject
    protected lateinit var mRegionManager: RegionManager

    @Inject
    protected lateinit var mServerApi: ServerApi

    @Inject
    protected lateinit var mThreadUtils: ThreadUtils

    private val mError: ErrorLinearLayout by bindView(R.id.error)
    private val mRecyclerView: RecyclerView by bindView(R.id.recyclerView)
    private val mRefreshLayout: SwipeRefreshLayout by bindView(R.id.refreshLayout)
    private val mEmpty : View by bindView(R.id.empty)


    companion object {
        private const val TAG = "HeadToHeadActivity"
        private val CNAME = HeadToHeadActivity::class.java.canonicalName
        private val EXTRA_PLAYER_ID = CNAME + ".PlayerId"
        private val EXTRA_PLAYER_NAME = CNAME + ".PlayerName"
        private val EXTRA_OPPONENT_ID = CNAME + ".OpponentId"
        private val EXTRA_OPPONENT_NAME = CNAME + ".OpponentName"

        fun getLaunchIntent(context: Context, player: AbsPlayer, opponent: AbsPlayer,
                region: Region? = null): Intent {
            var regionCopy = region

            if (player is FavoritePlayer) {
                regionCopy = player.region
            }

            return getLaunchIntent(context, player.id, player.name, opponent.id, opponent.name,
                    regionCopy)
        }

        fun getLaunchIntent(context: Context, player: AbsPlayer, match: Match,
                region: Region? = null): Intent {
            var regionCopy = region

            if (player is FavoritePlayer) {
                regionCopy = player.region
            }

            return getLaunchIntent(context, player.id, player.name, match.opponent.id,
                    match.opponent.name, regionCopy)
        }

        fun getLaunchIntent(context: Context, match: FullTournament.Match,
                region: Region? = null): Intent {
            return getLaunchIntent(context, match.winnerId, match.winnerName, match.loserId,
                    match.loserName, region)
        }

        fun getLaunchIntent(context: Context, playerId: String, playerName: String?,
                opponentId: String, opponentName: String?, region: Region? = null): Intent {
            val intent = Intent(context, HeadToHeadActivity::class.java)
                    .putExtra(EXTRA_PLAYER_ID, playerId)
                    .putExtra(EXTRA_OPPONENT_ID, opponentId)

            if (playerName?.isNotBlank() == true && opponentName?.isNotBlank() == true) {
                intent.putExtra(EXTRA_PLAYER_NAME, playerName)
                intent.putExtra(EXTRA_OPPONENT_NAME, opponentName)
            }

            if (region != null) {
                intent.putExtra(EXTRA_REGION, region)
            }

            return intent
        }
    }

    override val activityName = TAG

    override fun failure(errorCode: Int) {
        mHeadToHead = null
        mList = null
        mMatchResult = null
        showError(errorCode)
    }

    private fun fetchHeadToHead() {
        mRefreshLayout.isRefreshing = true
        mServerApi.getHeadToHead(mRegionManager.getRegion(this), mPlayerId, mOpponentId,
                ApiCall(this))
    }

    private fun filter(matchResult: MatchResult?) {
        mMatchResult = matchResult
        val list = mList

        if (list == null || list.isEmpty()) {
            return
        }

        mThreadUtils.run(object : ThreadUtils.Task {
            private var mList: List<Any>? = null

            override fun onBackground() {
                if (!isAlive || mMatchResult != matchResult) {
                    return
                }

                mList = ListUtils.filterPlayerMatchesList(matchResult, list)
            }

            override fun onUi() {
                if (!isAlive || mMatchResult != matchResult) {
                    return
                }

                mAdapter.set(mList)
                invalidateOptionsMenu()
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.get().appComponent.inject(this)
        setContentView(R.layout.activity_head_to_head)
        subtitle = mRegionManager.getRegion(this).displayName

        mOpponentId = intent.getStringExtra(EXTRA_OPPONENT_ID)
        mPlayerId = intent.getStringExtra(EXTRA_PLAYER_ID)

        if (intent.hasExtra(EXTRA_OPPONENT_NAME) && intent.hasExtra(EXTRA_PLAYER_NAME)) {
            mOpponentName = intent.getStringExtra(EXTRA_OPPONENT_NAME)
            mPlayerName = intent.getStringExtra(EXTRA_PLAYER_NAME)
        }

        fetchHeadToHead()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_head_to_head, menu)

        if (mHeadToHead != null) {
            menu.findItem(R.id.miFilter).isVisible = true
            menu.findItem(R.id.miShowAll).isVisible = mMatchResult != null
            menu.findItem(R.id.miFilterToLosses).isVisible = mMatchResult != MatchResult.LOSE
            menu.findItem(R.id.miFilterToWins).isVisible = mMatchResult != MatchResult.WIN
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.miFilterToLosses -> {
                filter(MatchResult.LOSE)
                true
            }

            R.id.miFilterToWins -> {
                filter(MatchResult.WIN)
                true
            }

            R.id.miShowAll -> {
                filter(null)
                true
            }

            else -> {
                super.onOptionsItemSelected(item)
            }
        }

    override fun onRefresh() {
        fetchHeadToHead()
    }

    override fun onViewsBound() {
        super.onViewsBound()

        mRefreshLayout.setOnRefreshListener(this)
        mRecyclerView.setHasFixedSize(true)
        mAdapter = HeadToHeadAdapter(this)
        mRecyclerView.adapter = mAdapter
    }

    private fun prepareMenuAndSubtitle() {
        mHeadToHead?.let {
            if (mOpponentName.isNullOrBlank()) {
                mOpponentName = it.opponent.name
            }

            if (mPlayerName.isNullOrBlank()) {
                mPlayerName = it.player.name
            }
        }

        invalidateOptionsMenu()
    }

    private fun showData() {
        mList = ListUtils.createHeadToHeadList(this, mHeadToHead)
        mAdapter.set(mList)

        if (mAdapter.isEmpty) {
            mError.visibility = View.GONE
            mRecyclerView.visibility = View.GONE
            mEmpty.visibility = View.VISIBLE
        } else {
            mEmpty.visibility = View.GONE
            mError.visibility = View.GONE
            mRecyclerView.visibility = View.VISIBLE
        }

        prepareMenuAndSubtitle()
        mRefreshLayout.isRefreshing = false
    }

    private fun showError(errorCode: Int) {
        mAdapter.clear()
        mEmpty.visibility = View.GONE
        mRecyclerView.visibility = View.GONE
        mError.setVisibility(View.VISIBLE, errorCode)
        prepareMenuAndSubtitle()
        mRefreshLayout.isRefreshing = false
    }

    override val showUpNavigation = true

    override fun success(`object`: HeadToHead?) {
        mHeadToHead = `object`
        showData()
    }

}
