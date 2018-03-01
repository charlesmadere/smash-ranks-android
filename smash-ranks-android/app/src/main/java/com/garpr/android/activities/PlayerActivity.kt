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
import com.garpr.android.views.ErrorContentLinearLayout
import com.garpr.android.views.MatchItemView
import com.garpr.android.views.toolbars.PlayerToolbar
import com.garpr.android.views.toolbars.SearchToolbar
import kotterknife.bindView
import javax.inject.Inject

class PlayerActivity : BaseActivity(), ApiListener<PlayerMatchesBundle>,
        MatchItemView.OnClickListener, PlayerToolbar.DataProvider, Searchable, SearchQueryHandle,
        SearchToolbar.Listener, SwipeRefreshLayout.OnRefreshListener {

    private var list: List<Any>? = null
    private var _matchResult: MatchResult? = null
    private lateinit var adapter: PlayerAdapter
    private var playerMatchesBundle: PlayerMatchesBundle? = null
    private lateinit var playerId: String

    @Inject
    protected lateinit var favoritePlayersManager: FavoritePlayersManager

    @Inject
    protected lateinit var identityManager: IdentityManager

    @Inject
    protected lateinit var regionManager: RegionManager

    @Inject
    protected lateinit var serverApi: ServerApi

    @Inject
    protected lateinit var shareUtils: ShareUtils

    @Inject
    protected lateinit var threadUtils: ThreadUtils

    private val error: ErrorContentLinearLayout by bindView(R.id.error)
    private val playerToolbar: PlayerToolbar by bindView(R.id.toolbar)
    private val recyclerView: RecyclerView by bindView(R.id.recyclerView)
    private val refreshLayout: SwipeRefreshLayout by bindView(R.id.refreshLayout)
    private val empty: View by bindView(R.id.empty)


    companion object {
        private const val TAG = "PlayerActivity"
        private val CNAME = PlayerActivity::class.java.canonicalName
        private val EXTRA_PLAYER_ID = CNAME + ".PlayerId"
        private val EXTRA_PLAYER_NAME = CNAME + ".PlayerName"

        fun getLaunchIntent(context: Context, player: AbsPlayer, region: Region? = null): Intent {
            var regionCopy = region

            if (player is FavoritePlayer) {
                regionCopy = player.region
            }

            return getLaunchIntent(context, player.id, player.name, regionCopy)
        }

        fun getLaunchIntent(context: Context, playerId: String, playerName: String?,
                region: Region? = null): Intent {
            val intent = Intent(context, PlayerActivity::class.java)
                    .putExtra(EXTRA_PLAYER_ID, playerId)

            if (playerName?.isNotBlank() == true) {
                intent.putExtra(EXTRA_PLAYER_NAME, playerName)
            }

            if (region != null) {
                intent.putExtra(EXTRA_REGION, region)
            }

            return intent
        }
    }

    override val activityName = TAG

    private fun addToFavorites() {
        fullPlayer?.let {
            favoritePlayersManager.addPlayer(it, regionManager.getRegion(this))
        } ?: throw RuntimeException("fullPlayer is null")
    }

    override fun failure(errorCode: Int) {
        playerMatchesBundle = null
        list = null
        _matchResult = null
        showError(errorCode)
    }

    private fun fetchPlayerMatchesBundle() {
        refreshLayout.isRefreshing = true
        serverApi.getPlayerMatches(regionManager.getRegion(this), playerId,
                ApiCall(this))
    }

    private fun filter(matchResult: MatchResult?) {
        _matchResult = matchResult
        val list = this.list

        if (list == null || list.isEmpty()) {
            return
        }

        threadUtils.run(object : ThreadUtils.Task {
            private var list: List<Any>? = null

            override fun onBackground() {
                if (!isAlive || matchResult != _matchResult) {
                    return
                }

                this.list = ListUtils.filterPlayerMatchesList(matchResult, list)
            }

            override fun onUi() {
                if (!isAlive || matchResult != _matchResult) {
                    return
                }

                adapter.set(this.list)
                invalidateOptionsMenu()
            }
        })
    }

    override val fullPlayer: FullPlayer?
        get() = playerMatchesBundle?.fullPlayer

    override val matchesBundle: MatchesBundle?
        get() = playerMatchesBundle?.matchesBundle

    override fun onClick(v: MatchItemView) {
        val player = fullPlayer ?: return
        val match = v.match ?: return
        startActivity(HeadToHeadActivity.getLaunchIntent(this, player, match,
                regionManager.getRegion(this)))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.get().appComponent.inject(this)
        setContentView(R.layout.activity_player)

        playerId = intent.getStringExtra(EXTRA_PLAYER_ID)

        setTitle()
        fetchPlayerMatchesBundle()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.miAddToFavorites -> {
                addToFavorites()
                true
            }

            R.id.miAliases -> {
                showAliases()
                true
            }

            R.id.miFilterToLosses -> {
                filter(MatchResult.LOSE)
                true
            }

            R.id.miFilterToWins -> {
                filter(MatchResult.WIN)
                true
            }

            R.id.miRemoveFromFavorites -> {
                favoritePlayersManager.removePlayer(playerId)
                true
            }

            R.id.miSetAsYourIdentity -> {
                val player = fullPlayer ?: throw RuntimeException("fullPlayer is null")
                identityManager.setIdentity(player, regionManager.getRegion(this))
                true
            }

            R.id.miShare -> {
                share()
                true
            }

            R.id.miShowAll -> {
                filter(null)
                true
            }

            R.id.miViewYourselfVsThisOpponent -> {
                viewYourselfVsThisOpponent()
                true
            }

            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onRefresh() {
        fetchPlayerMatchesBundle()
    }

    override fun onViewsBound() {
        super.onViewsBound()

        refreshLayout.setOnRefreshListener(this)
        recyclerView.setHasFixedSize(true)
        adapter = PlayerAdapter(this)
        recyclerView.adapter = adapter
    }

    override val matchResult: MatchResult?
        get() = _matchResult

    override fun search(query: String?) {
        val list = this.list

        if (list == null || list.isEmpty()) {
            return
        }

        threadUtils.run(object : ThreadUtils.Task {
            private var list: List<Any>? = null

            override fun onBackground() {
                if (!isAlive || !TextUtils.equals(query, searchQuery)) {
                    return
                }

                this.list = ListUtils.searchPlayerMatchesList(query, list)
            }

            override fun onUi() {
                if (!isAlive || !TextUtils.equals(query, searchQuery)) {
                    return
                }

                adapter.set(this.list)
            }
        })
    }

    override val searchQuery: CharSequence?
        get() = playerToolbar.searchQuery

    private fun setTitle() {
        if (title.isNotBlank()) {
            return
        }

        val playerMatchesBundle = this.playerMatchesBundle

        if (playerMatchesBundle == null) {
            if (intent.hasExtra(EXTRA_PLAYER_NAME)) {
                title = intent.getStringExtra(EXTRA_PLAYER_NAME)
            }
        } else {
            title = playerMatchesBundle.fullPlayer.name
        }

        subtitle = regionManager.getRegion(this).displayName
    }

    private fun share() {
        fullPlayer?.let {
            shareUtils.sharePlayer(this, it)
        } ?: throw RuntimeException("fullPlayer is null")
    }

    private fun showAliases() {
        val aliases = fullPlayer?.aliases

        if (aliases == null || aliases.isEmpty()) {
            return
        }

        AlertDialog.Builder(this)
                .setItems(aliases.toTypedArray<CharSequence>(), null)
                .setTitle(R.string.aliases)
                .show()
    }

    private fun showData() {
        val playerMatchesBundle = this.playerMatchesBundle ?: throw RuntimeException(
                "playerMatchesBundle is null")
        val player = fullPlayer ?: throw RuntimeException("fullPlayer is null")
        val list = ListUtils.createPlayerMatchesList(this, regionManager, player,
                playerMatchesBundle.matchesBundle)
        this.list = list

        error.visibility = View.GONE

        if (list == null || list.isEmpty()) {
            adapter.clear()
            recyclerView.visibility = View.GONE
            empty.visibility = View.VISIBLE
        } else {
            adapter.set(list)
            empty.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }

        refreshLayout.isRefreshing = false
        setTitle()
        invalidateOptionsMenu()
    }

    private fun showError(errorCode: Int) {
        adapter.clear()
        recyclerView.visibility = View.GONE
        empty.visibility = View.GONE
        error.setVisibility(View.VISIBLE, errorCode)
        refreshLayout.isRefreshing = false
        invalidateOptionsMenu()
    }

    override val showSearchMenuItem: Boolean
        get() = playerMatchesBundle?.matchesBundle?.matches?.isNotEmpty() == true

    override val showUpNavigation = true

    override fun success(`object`: PlayerMatchesBundle?) {
        playerMatchesBundle = `object`
        list = null
        _matchResult = null
        showData()
    }

    private fun viewYourselfVsThisOpponent() {
        val identity = identityManager.identity ?: throw RuntimeException("identity is null")
        val fullPlayer = fullPlayer ?: throw RuntimeException("fullPlayer is null")
        startActivity(HeadToHeadActivity.getLaunchIntent(this, identity, fullPlayer,
                regionManager.getRegion(this)))
    }

}
