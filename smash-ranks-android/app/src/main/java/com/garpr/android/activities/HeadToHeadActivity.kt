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
import com.garpr.android.views.ErrorContentLinearLayout
import kotterknife.bindView
import javax.inject.Inject

class HeadToHeadActivity : BaseActivity(), ApiListener<HeadToHead>,
        SwipeRefreshLayout.OnRefreshListener {

    private var list: List<Any>? = null
    private var headToHead: HeadToHead? = null
    private lateinit var adapter: HeadToHeadAdapter
    private var matchResult: MatchResult? = null
    private lateinit var opponentId: String
    private var opponentName: String? = null
    private lateinit var playerId: String
    private var playerName: String? = null

    @Inject
    protected lateinit var regionManager: RegionManager

    @Inject
    protected lateinit var serverApi: ServerApi

    @Inject
    protected lateinit var threadUtils: ThreadUtils

    private val error: ErrorContentLinearLayout by bindView(R.id.error)
    private val recyclerView: RecyclerView by bindView(R.id.recyclerView)
    private val refreshLayout: SwipeRefreshLayout by bindView(R.id.refreshLayout)
    private val empty: View by bindView(R.id.empty)


    companion object {
        private const val TAG = "HeadToHeadActivity"
        private val CNAME = HeadToHeadActivity::class.java.canonicalName
        private val EXTRA_PLAYER_ID = "$CNAME.PlayerId"
        private val EXTRA_PLAYER_NAME = "$CNAME.PlayerName"
        private val EXTRA_OPPONENT_ID = "$CNAME.OpponentId"
        private val EXTRA_OPPONENT_NAME = "$CNAME.OpponentName"

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
        headToHead = null
        list = null
        matchResult = null
        showError(errorCode)
    }

    private fun fetchHeadToHead() {
        refreshLayout.isRefreshing = true
        serverApi.getHeadToHead(regionManager.getRegion(this), playerId, opponentId,
                ApiCall(this))
    }

    private fun filter(matchResult: MatchResult?) {
        this.matchResult = matchResult
        val list = this.list

        if (list == null || list.isEmpty()) {
            return
        }

        threadUtils.run(object : ThreadUtils.Task {
            private var list: List<Any>? = null

            override fun onBackground() {
                if (!isAlive || matchResult != this@HeadToHeadActivity.matchResult) {
                    return
                }

                this.list = ListUtils.filterPlayerMatchesList(matchResult, list)
            }

            override fun onUi() {
                if (!isAlive || matchResult != this@HeadToHeadActivity.matchResult) {
                    return
                }

                adapter.set(this.list)
                invalidateOptionsMenu()
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.get().appComponent.inject(this)
        setContentView(R.layout.activity_head_to_head)
        subtitle = regionManager.getRegion(this).displayName

        opponentId = intent.getStringExtra(EXTRA_OPPONENT_ID)
        playerId = intent.getStringExtra(EXTRA_PLAYER_ID)

        if (intent.hasExtra(EXTRA_OPPONENT_NAME) && intent.hasExtra(EXTRA_PLAYER_NAME)) {
            opponentName = intent.getStringExtra(EXTRA_OPPONENT_NAME)
            playerName = intent.getStringExtra(EXTRA_PLAYER_NAME)
        }

        fetchHeadToHead()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_head_to_head, menu)

        if (headToHead != null) {
            menu.findItem(R.id.miFilter).isVisible = true
            menu.findItem(R.id.miShowAll).isVisible = matchResult != null
            menu.findItem(R.id.miFilterToLosses).isVisible = matchResult != MatchResult.LOSE
            menu.findItem(R.id.miFilterToWins).isVisible = matchResult != MatchResult.WIN
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

        refreshLayout.setOnRefreshListener(this)
        recyclerView.setHasFixedSize(true)
        adapter = HeadToHeadAdapter(this)
        recyclerView.adapter = adapter
    }

    private fun prepareMenuAndSubtitle() {
        headToHead?.let {
            if (opponentName.isNullOrBlank()) {
                opponentName = it.opponent.name
            }

            if (playerName.isNullOrBlank()) {
                playerName = it.player.name
            }
        }

        invalidateOptionsMenu()
    }

    private fun showData() {
        list = ListUtils.createHeadToHeadList(this, headToHead)
        adapter.set(list)

        if (adapter.isEmpty) {
            error.visibility = View.GONE
            recyclerView.visibility = View.GONE
            empty.visibility = View.VISIBLE
        } else {
            empty.visibility = View.GONE
            error.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }

        refreshLayout.isRefreshing = false
        prepareMenuAndSubtitle()
    }

    private fun showError(errorCode: Int) {
        adapter.clear()
        empty.visibility = View.GONE
        recyclerView.visibility = View.GONE
        error.setVisibility(View.VISIBLE, errorCode)
        prepareMenuAndSubtitle()
        refreshLayout.isRefreshing = false
    }

    override val showUpNavigation = true

    override fun success(`object`: HeadToHead?) {
        headToHead = `object`
        list = null
        matchResult = null
        showData()
    }

}
