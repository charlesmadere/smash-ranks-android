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
import com.garpr.android.misc.FavoritePlayersManager
import com.garpr.android.misc.IdentityManager
import com.garpr.android.misc.ListUtils
import com.garpr.android.misc.RegionManager
import com.garpr.android.misc.SearchQueryHandle
import com.garpr.android.misc.Searchable
import com.garpr.android.misc.ShareUtils
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.models.AbsPlayer
import com.garpr.android.models.FavoritePlayer
import com.garpr.android.models.FullPlayer
import com.garpr.android.models.Match
import com.garpr.android.models.MatchesBundle
import com.garpr.android.models.PlayerMatchesBundle
import com.garpr.android.models.Ranking
import com.garpr.android.models.Region
import com.garpr.android.networking.ApiCall
import com.garpr.android.networking.ApiListener
import com.garpr.android.networking.ServerApi
import com.garpr.android.views.ErrorLinearLayout
import com.garpr.android.views.MatchItemView
import com.garpr.android.views.TournamentDividerView
import com.garpr.android.views.toolbars.PlayerToolbar
import com.garpr.android.views.toolbars.SearchToolbar
import kotterknife.bindView
import java.util.*
import javax.inject.Inject

class PlayerActivity : BaseActivity(), ApiListener<PlayerMatchesBundle>,
        MatchItemView.OnClickListener, PlayerToolbar.DataProvider, Searchable, SearchQueryHandle,
        SearchToolbar.Listener, SwipeRefreshLayout.OnRefreshListener,
        TournamentDividerView.OnClickListener {

    private var mList: ArrayList<Any>? = null
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
        private val TAG = "PlayerActivity"
        private val CNAME = PlayerActivity::class.java.canonicalName
        private val EXTRA_PLAYER_ID = CNAME + ".PlayerId"
        private val EXTRA_PLAYER_NAME = CNAME + ".PlayerName"

        fun getLaunchIntent(context: Context, player: AbsPlayer, region: Region?): Intent {
            var region = region

            if (player is FavoritePlayer) {
                region = player.region
            }

            return getLaunchIntent(context, player.id, player.name, region)
        }

        fun getLaunchIntent(context: Context, ranking: Ranking, region: Region?): Intent {
            return getLaunchIntent(context, ranking.id, ranking.name, region)
        }

        fun getLaunchIntent(context: Context, playerId: String, playerName: String?,
                region: Region?): Intent {
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

    override val activityName: String
        get() = TAG

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

        if (mList == null || mList!!.isEmpty()) {
            return
        }

        mThreadUtils.run(object : ThreadUtils.Task {
            private var mList: List<Any>? = null

            override fun onBackground() {
                if (!isAlive || mResult != result) {
                    return
                }

                mList = ListUtils.filterPlayerMatchesList(result, this@PlayerActivity.mList)
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

    override fun getFullPlayer(): FullPlayer? {
        return if (mPlayerMatchesBundle == null) null else mPlayerMatchesBundle!!.fullPlayer
    }

    override fun getMatchesBundle(): MatchesBundle? {
        return if (mPlayerMatchesBundle == null) null else mPlayerMatchesBundle!!.matchesBundle
    }

    override fun getResult(): Match.Result? {
        return mResult
    }

    override fun getSearchQuery(): CharSequence? {
        return mPlayerToolbar.searchQuery
    }

    override fun onClick(v: MatchItemView) {
        val match = v.mContent ?: return
        startActivity(HeadToHeadActivity.getLaunchIntent(this,
                mPlayerMatchesBundle!!.fullPlayer, match))
    }

    override fun onClick(v: TournamentDividerView) {
        val tournament = v.mContent ?: return
        startActivity(TournamentActivity.getLaunchIntent(this, tournament.id, tournament.name,
                tournament.date, mRegionManager.getRegion(this)))
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
                mFavoritePlayersManager.addPlayer(mPlayerMatchesBundle!!.fullPlayer,
                        mRegionManager.getRegion(this))
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

            R.id.miShare -> {
                mShareUtils.sharePlayer(this, mPlayerMatchesBundle!!.fullPlayer)
                return true
            }

            R.id.miViewYourselfVsThisOpponent -> {
                startActivity(HeadToHeadActivity.getLaunchIntent(this, mIdentityManager.identity!!,
                        mPlayerMatchesBundle!!.fullPlayer))
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

    override fun search(query: String?) {
        mThreadUtils.run(object : ThreadUtils.Task {
            private var mList: List<Any>? = null

            override fun onBackground() {
                if (!isAlive || !TextUtils.equals(query, searchQuery)) {
                    return
                }

                mList = ListUtils.searchPlayerMatchesList(query, this@PlayerActivity.mList)
            }

            override fun onUi() {
                if (!isAlive || !TextUtils.equals(query, searchQuery)) {
                    return
                }

                mAdapter.set(mList)
            }
        })
    }

    private fun setTitle() {
        if (!TextUtils.isEmpty(title)) {
            return
        }

        if (mPlayerMatchesBundle != null) {
            title = mPlayerMatchesBundle!!.fullPlayer.name
        } else {
            val intent = intent

            if (intent.hasExtra(EXTRA_PLAYER_NAME)) {
                title = intent.getStringExtra(EXTRA_PLAYER_NAME)
            }
        }

        subtitle = mRegionManager.getRegion(this).displayName
    }

    private fun showAliases() {
        val aliases = mPlayerMatchesBundle!!.fullPlayer.aliases

        val items = arrayOfNulls<CharSequence>(aliases!!.size)
        aliases.toTypedArray<CharSequence>()

        AlertDialog.Builder(this)
                .setItems(items, null)
                .setTitle(R.string.aliases)
                .show()
    }

    private fun showData() {
        mList = ListUtils.createPlayerMatchesList(this, mRegionManager,
                mPlayerMatchesBundle!!.fullPlayer, mPlayerMatchesBundle!!.matchesBundle)
        mError.visibility = View.GONE

        if (mList == null || mList!!.isEmpty()) {
            mAdapter.clear()
            mRecyclerView.visibility = View.GONE
            mEmpty.visibility = View.VISIBLE
        } else {
            mAdapter.set(mList)
            mEmpty.visibility = View.GONE
            mRecyclerView.visibility = View.VISIBLE
        }

        mRefreshLayout.isRefreshing = false
        setTitle()
        supportInvalidateOptionsMenu()
    }

    private fun showError(errorCode: Int) {
        mAdapter.clear()
        mRecyclerView.visibility = View.GONE
        mEmpty.visibility = View.GONE
        mError.setVisibility(View.VISIBLE, errorCode)
        mRefreshLayout.isRefreshing = false
        supportInvalidateOptionsMenu()
    }

    override fun showSearchMenuItem(): Boolean {
        return mPlayerMatchesBundle != null && mPlayerMatchesBundle!!.hasMatchesBundle()
    }

    override fun showUpNavigation(): Boolean {
        return true
    }


    override fun success(playerMatchesBundle: PlayerMatchesBundle?) {
        mPlayerMatchesBundle = playerMatchesBundle
        mList = null
        mResult = null
        showData()
    }

}
