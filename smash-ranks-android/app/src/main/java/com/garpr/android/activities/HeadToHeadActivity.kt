package com.garpr.android.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
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
import com.garpr.android.models.AbsPlayer
import com.garpr.android.models.FullTournament
import com.garpr.android.models.HeadToHead
import com.garpr.android.models.Match
import com.garpr.android.networking.ApiCall
import com.garpr.android.networking.ApiListener
import com.garpr.android.networking.ServerApi
import com.garpr.android.views.ErrorLinearLayout
import kotterknife.bindView
import java.util.*
import javax.inject.Inject

class HeadToHeadActivity : BaseActivity(), ApiListener<HeadToHead>,
        SwipeRefreshLayout.OnRefreshListener {

    private var mList: ArrayList<Any>? = null
    private var mHeadToHead: HeadToHead? = null
    lateinit private var mAdapter: HeadToHeadAdapter
    private var mResult: Match.Result? = null
    lateinit private var mOpponentId: String
    private var mOpponentName: String? = null
    lateinit private var mPlayerId: String
    private var mPlayerName: String? = null

    @Inject
    lateinit protected var mRegionManager: RegionManager

    @Inject
    lateinit protected var mServerApi: ServerApi

    @Inject
    lateinit protected var mThreadUtils: ThreadUtils

    private val mError: ErrorLinearLayout by bindView(R.id.error)
    private val mRecyclerView: RecyclerView by bindView(R.id.recyclerView)
    private val mRefreshLayout: SwipeRefreshLayout by bindView(R.id.refreshLayout)
    private val mEmpty: View by bindView(R.id.empty)


    companion object {
        private const val TAG = "HeadToHeadActivity"
        private val CNAME = HeadToHeadActivity::class.java.canonicalName
        private val EXTRA_PLAYER_ID = CNAME + ".PlayerId"
        private val EXTRA_PLAYER_NAME = CNAME + ".PlayerName"
        private val EXTRA_OPPONENT_ID = CNAME + ".OpponentId"
        private val EXTRA_OPPONENT_NAME = CNAME + ".OpponentName"

        fun getLaunchIntent(context: Context, player: AbsPlayer, opponent: AbsPlayer): Intent {
            return getLaunchIntent(context, player.id, player.name, opponent.id, opponent.name)
        }

        fun getLaunchIntent(context: Context, player: AbsPlayer, match: Match): Intent {
            return getLaunchIntent(context, player.id, player.name, match.opponent.id,
                    match.opponent.name)
        }

        fun getLaunchIntent(context: Context, match: FullTournament.Match): Intent {
            return getLaunchIntent(context, match.winnerId, match.winnerName, match.loserId,
                    match.loserName)
        }

        fun getLaunchIntent(context: Context, playerId: String, playerName: String?,
                opponentId: String, opponentName: String?): Intent {
            val intent = Intent(context, HeadToHeadActivity::class.java)
                    .putExtra(EXTRA_PLAYER_ID, playerId)
                    .putExtra(EXTRA_OPPONENT_ID, opponentId)

            if (!TextUtils.isEmpty(playerName) && !TextUtils.isEmpty(opponentName)) {
                intent.putExtra(EXTRA_PLAYER_NAME, playerName)
                intent.putExtra(EXTRA_OPPONENT_NAME, opponentName)
            }

            return intent
        }
    }

    override val activityName = TAG

    override fun failure(errorCode: Int) {
        mHeadToHead = null
        mList = null
        mResult = null
        showError(errorCode)
    }

    private fun fetchHeadToHead() {
        mRefreshLayout.isRefreshing = true
        mServerApi.getHeadToHead(mRegionManager.getRegion(this), mPlayerId, mOpponentId,
                ApiCall(this))
    }

    private fun filter(result: Match.Result?) {
        mResult = result
        val list = mList

        if (list == null || list.isEmpty()) {
            return
        }

        mThreadUtils.run(object : ThreadUtils.Task {
            private var mList: List<Any>? = null

            override fun onBackground() {
                if (!isAlive || mResult != result) {
                    return
                }

                mList = ListUtils.filterPlayerMatchesList(result, list)
            }

            override fun onUi() {
                if (!isAlive || mResult != result) {
                    return
                }

                mAdapter.set(mList)
                supportInvalidateOptionsMenu()
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.get().appComponent.inject(this)
        setContentView(R.layout.activity_head_to_head)

        mOpponentId = intent.getStringExtra(EXTRA_OPPONENT_ID)
        mPlayerId = intent.getStringExtra(EXTRA_PLAYER_ID)

        if (intent.hasExtra(EXTRA_OPPONENT_NAME) && intent.hasExtra(EXTRA_PLAYER_NAME)) {
            mOpponentName = intent.getStringExtra(EXTRA_OPPONENT_NAME)
            mPlayerName = intent.getStringExtra(EXTRA_PLAYER_NAME)
            setSubtitle()
        }

        fetchHeadToHead()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_head_to_head, menu)

        if (mHeadToHead != null) {
            menu.findItem(R.id.miFilter).isVisible = true
            menu.findItem(R.id.miFilterAll).isVisible = mResult != null
            menu.findItem(R.id.miFilterLosses).isVisible = mResult != Match.Result.LOSE
            menu.findItem(R.id.miFilterWins).isVisible = mResult != Match.Result.WIN
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.miFilterAll -> {
                filter(null)
                return true
            }

            R.id.miFilterLosses -> {
                filter(Match.Result.LOSE)
                return true
            }

            R.id.miFilterWins -> {
                filter(Match.Result.WIN)
                return true
            }

            R.id.miViewOpponent -> {
                startActivity(PlayerActivity.getLaunchIntent(this, mOpponentId, mOpponentName,
                        mRegionManager.getRegion(this)))
                return true
            }

            R.id.miViewPlayer -> {
                startActivity(PlayerActivity.getLaunchIntent(this, mPlayerId, mPlayerName,
                        mRegionManager.getRegion(this)))
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        menu.findItem(R.id.miFilter).isVisible = mHeadToHead != null

        if (!TextUtils.isEmpty(mOpponentName) && !TextUtils.isEmpty(mPlayerName)) {
            val viewOpponent = menu.findItem(R.id.miViewOpponent)
            viewOpponent.title = getString(R.string.view_x, mOpponentName)
            viewOpponent.isVisible = true

            val viewPlayer = menu.findItem(R.id.miViewPlayer)
            viewPlayer.title = getString(R.string.view_x, mPlayerName)
            viewPlayer.isVisible = true
        }

        return super.onPrepareOptionsMenu(menu)
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
        if (mHeadToHead != null) {
            if (TextUtils.isEmpty(mOpponentName)) {
                mOpponentName = mHeadToHead!!.opponent.name
            }

            if (TextUtils.isEmpty(mPlayerName)) {
                mPlayerName = mHeadToHead!!.player.name
            }
        }

        setSubtitle()
        supportInvalidateOptionsMenu()
    }

    private fun showData() {
        mList = ListUtils.createHeadToHeadList(this, mHeadToHead)
        mAdapter.set(mList)
        mEmpty.visibility = View.GONE
        mError.visibility = View.GONE
        mRecyclerView.visibility = View.VISIBLE
        prepareMenuAndSubtitle()
        mRefreshLayout.isRefreshing = false
    }

    private fun showEmpty() {
        mAdapter.clear()
        mRecyclerView.visibility = View.GONE
        mError.visibility = View.GONE
        mEmpty.visibility = View.VISIBLE
        prepareMenuAndSubtitle()
        mRefreshLayout.isRefreshing = false
    }

    private fun showError(errorCode: Int) {
        mAdapter.clear()
        mRecyclerView.visibility = View.GONE
        mEmpty.visibility = View.GONE
        mError.setVisibility(View.VISIBLE, errorCode)
        prepareMenuAndSubtitle()
        mRefreshLayout.isRefreshing = false
    }

    override val showUpNavigation = true

    private fun setSubtitle() {
        if (TextUtils.isEmpty(subtitle) && !TextUtils.isEmpty(mPlayerName) &&
                !TextUtils.isEmpty(mOpponentName)) {
            subtitle = getString(R.string.x_vs_y, mPlayerName, mOpponentName)
        }
    }

    override fun success(`object`: HeadToHead?) {
        mHeadToHead = `object`

        if (`object` == null) {
            showEmpty()
        } else {
            showData()
        }
    }

}
