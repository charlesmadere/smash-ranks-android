package com.garpr.android.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import com.garpr.android.App
import com.garpr.android.R
import com.garpr.android.adapters.PlayerAdapter
import com.garpr.android.extensions.subtitle
import com.garpr.android.misc.*
import com.garpr.android.models.*
import com.garpr.android.networking.ApiCall
import com.garpr.android.networking.ApiListener
import com.garpr.android.networking.ServerApi
import com.garpr.android.views.ErrorLinearLayout
import com.garpr.android.views.MatchItemView
import com.garpr.android.views.TournamentDividerView
import com.garpr.android.views.toolbars.PlayerToolbar
import com.garpr.android.views.toolbars.SearchToolbar
import kotterknife.bindView
import javax.inject.Inject

class PlayerActivity : BaseActivity(), ApiListener<PlayerMatchesBundle>,
        MatchItemView.OnClickListener, PlayerToolbar.DataProvider, Searchable, SearchQueryHandle,
        SearchToolbar.Listener, SwipeRefreshLayout.OnRefreshListener,
        TournamentDividerView.OnClickListener {

    private var mList: List<Any>? = null
    private var mResult: Match.Result? = null
    lateinit private var mAdapter: PlayerAdapter
    private var mPlayerMatchesBundle: PlayerMatchesBundle? = null
    lateinit private var mPlayerId: String

    @Inject
    lateinit protected var mFavoritePlayersManager: FavoritePlayersManager

    @Inject
    lateinit protected var mIdentityManager: IdentityManager

    @Inject
    lateinit protected var mRegionManager: RegionManager

    @Inject
    lateinit protected var mServerApi: ServerApi

    @Inject
    lateinit protected var mShareUtils: ShareUtils

    @Inject
    lateinit protected var mThreadUtils: ThreadUtils

    private val mError: ErrorLinearLayout by bindView(R.id.error)
    private val mPlayerToolbar: PlayerToolbar by bindView(R.id.toolbar)
    private val mRecyclerView: RecyclerView by bindView(R.id.recyclerView)
    private val mRefreshLayout: SwipeRefreshLayout by bindView(R.id.refreshLayout)
    private val mEmpty: View by bindView(R.id.empty)


    companion object {
        private const val TAG = "PlayerActivity"
        private val CNAME = PlayerActivity::class.java.canonicalName
        private val EXTRA_PLAYER_ID = CNAME + ".PlayerId"
        private val EXTRA_PLAYER_NAME = CNAME + ".PlayerName"

        fun getLaunchIntent(context: Context, player: AbsPlayer, region: LiteRegion?): Intent {
            var _region = region

            if (player is FavoritePlayer) {
                _region = player.region
            }

            return getLaunchIntent(context, player.id, player.name, _region)
        }

        fun getLaunchIntent(context: Context, ranking: Ranking, region: LiteRegion?): Intent {
            return getLaunchIntent(context, ranking.player.id, ranking.player.name, region)
        }

        fun getLaunchIntent(context: Context, playerId: String, playerName: String?,
                region: AbsRegion?): Intent {
            val intent = Intent(context, PlayerActivity::class.java)
                    .putExtra(EXTRA_PLAYER_ID, playerId)

            if (!TextUtils.isEmpty(playerName)) {
                intent.putExtra(EXTRA_PLAYER_NAME, playerName)
            }

            if (region != null) {
                intent.putExtra(BaseActivity.EXTRA_REGION, region)
            }

            return intent
        }
    }

    override val activityName = TAG

    private fun addToFavorites() {
        mPlayerMatchesBundle?.fullPlayer?.let {
            mFavoritePlayersManager.addPlayer(it, mRegionManager.getRegion(this))
        } ?: throw RuntimeException("fullPlayer is null")
    }

    override fun failure(errorCode: Int) {
        mPlayerMatchesBundle = null
        mList = null
        mResult = null
        showError(errorCode)
    }

    private fun fetchPlayerMatchesBundle() {
        mRefreshLayout.isRefreshing = true
        mServerApi.getPlayerMatches(mRegionManager.getRegion(this), mPlayerId, ApiCall(this))
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
                invalidateOptionsMenu()
            }
        })
    }

    override val fullPlayer: FullPlayer?
        get() = mPlayerMatchesBundle?.fullPlayer

    override val matchesBundle: MatchesBundle?
        get() = mPlayerMatchesBundle?.matchesBundle

    override fun onClick(v: MatchItemView) {
        val fullPlayer = mPlayerMatchesBundle?.fullPlayer ?: return
        val content = v.mContent ?: return
        startActivity(HeadToHeadActivity.getLaunchIntent(this, fullPlayer, content))
    }

    override fun onClick(v: TournamentDividerView) {
        val content = v.mContent ?: return
        startActivity(TournamentActivity.getLaunchIntent(this, content.id, content.name,
                content.date, mRegionManager.getRegion(this)))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.get().appComponent.inject(this)
        setContentView(R.layout.activity_player)

        val intent = intent
        mPlayerId = intent.getStringExtra(EXTRA_PLAYER_ID)

        setTitle()
        fetchPlayerMatchesBundle()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.miAddToFavorites -> {
                addToFavorites()
                return true
            }

            R.id.miAliases -> {
                showAliases()
                return true
            }

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

            R.id.miRemoveFromFavorites -> {
                mFavoritePlayersManager.removePlayer(mPlayerId)
                return true
            }

            R.id.miSetAsYourIdentity -> {
                val player = fullPlayer ?: throw RuntimeException("fullPlayer is null")
                mIdentityManager.setIdentity(player, mRegionManager.getRegion(this))
                return true
            }

            R.id.miShare -> {
                share()
                return true
            }

            R.id.miViewYourselfVsThisOpponent -> {
                viewYourselfVsThisOpponent()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onRefresh() {
        fetchPlayerMatchesBundle()
    }

    override fun onViewsBound() {
        super.onViewsBound()

        mRefreshLayout.setOnRefreshListener(this)
        mRecyclerView.setHasFixedSize(true)
        mAdapter = PlayerAdapter(this)
        mRecyclerView.adapter = mAdapter
    }

    override val result: Match.Result?
        get() = mResult

    override fun search(query: String?) {
        val list = mList

        if (list == null || list.isEmpty()) {
            return
        }

        mThreadUtils.run(object : ThreadUtils.Task {
            private var mList: List<Any>? = null

            override fun onBackground() {
                if (!isAlive || !TextUtils.equals(query, searchQuery)) {
                    return
                }

                mList = ListUtils.searchPlayerMatchesList(query, list)
            }

            override fun onUi() {
                if (!isAlive || !TextUtils.equals(query, searchQuery)) {
                    return
                }

                mAdapter.set(mList)
            }
        })
    }

    override val searchQuery: CharSequence?
        get() = mPlayerToolbar.searchQuery

    private fun setTitle() {
        if (title.isNotBlank()) {
            return
        }

        val playerMatchesBundle = mPlayerMatchesBundle

        if (playerMatchesBundle == null) {
            if (intent.hasExtra(EXTRA_PLAYER_NAME)) {
                title = intent.getStringExtra(EXTRA_PLAYER_NAME)
            }
        } else {
            title = playerMatchesBundle.fullPlayer.name
        }

        subtitle = mRegionManager.getRegion(this).displayName
    }

    private fun share() {
        mPlayerMatchesBundle?.fullPlayer?.let {
            mShareUtils.sharePlayer(this, it)
        } ?: throw RuntimeException("fullPlayer is null")
    }

    private fun showAliases() {
        val aliases = mPlayerMatchesBundle?.fullPlayer?.aliases

        if (aliases == null || aliases.isEmpty()) {
            return
        }

        val items = arrayOfNulls<CharSequence>(aliases.size)
        aliases.toTypedArray<CharSequence>()

        AlertDialog.Builder(this)
                .setItems(items, null)
                .setTitle(R.string.aliases)
                .show()
    }

    private fun showData() {
        val playerMatchesBundle = mPlayerMatchesBundle ?: throw RuntimeException(
                "mPlayerMatchesBundle is null")
        val list = ListUtils.createPlayerMatchesList(this, mRegionManager,
                playerMatchesBundle.fullPlayer, playerMatchesBundle.matchesBundle)
        mList = list

        mError.visibility = View.GONE

        if (list == null || list.isEmpty()) {
            mAdapter.clear()
            mRecyclerView.visibility = View.GONE
            mEmpty.visibility = View.VISIBLE
        } else {
            mAdapter.set(list)
            mEmpty.visibility = View.GONE
            mRecyclerView.visibility = View.VISIBLE
        }

        mRefreshLayout.isRefreshing = false
        setTitle()
        invalidateOptionsMenu()
    }

    private fun showError(errorCode: Int) {
        mAdapter.clear()
        mRecyclerView.visibility = View.GONE
        mEmpty.visibility = View.GONE
        mError.setVisibility(View.VISIBLE, errorCode)
        mRefreshLayout.isRefreshing = false
        invalidateOptionsMenu()
    }

    override val showSearchMenuItem: Boolean
        get() = mPlayerMatchesBundle?.matchesBundle?.matches?.isNotEmpty() == true

    override val showUpNavigation = true

    override fun success(`object`: PlayerMatchesBundle?) {
        mPlayerMatchesBundle = `object`
        mList = null
        mResult = null
        showData()
    }

    private fun viewYourselfVsThisOpponent() {
        val identity = mIdentityManager.identity ?: throw RuntimeException("identity is null")
        val fullPlayer = mPlayerMatchesBundle?.fullPlayer ?: throw RuntimeException(
                "fullPlayer is null")
        startActivity(HeadToHeadActivity.getLaunchIntent(this, identity, fullPlayer))
    }

}
