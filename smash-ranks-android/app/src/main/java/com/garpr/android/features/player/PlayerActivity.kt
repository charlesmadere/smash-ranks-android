package com.garpr.android.features.player

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.garpr.android.R
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.data.models.PlayerMatchesBundle
import com.garpr.android.data.models.Region
import com.garpr.android.extensions.appComponent
import com.garpr.android.extensions.putOptionalExtra
import com.garpr.android.extensions.requireStringExtra
import com.garpr.android.extensions.verticalPositionInWindow
import com.garpr.android.features.base.BaseActivity
import com.garpr.android.features.common.SearchToolbar
import com.garpr.android.features.headToHead.HeadToHeadActivity
import com.garpr.android.misc.ColorListener
import com.garpr.android.misc.ListUtils
import com.garpr.android.misc.SearchQueryHandle
import com.garpr.android.misc.Searchable
import com.garpr.android.misc.SmashRosterStorage
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.networking.ApiCall
import com.garpr.android.networking.ApiListener
import com.garpr.android.networking.ServerApi
import com.garpr.android.repositories.FavoritePlayersRepository
import com.garpr.android.repositories.IdentityManager
import com.garpr.android.repositories.RegionManager
import kotlinx.android.synthetic.main.activity_player.*
import javax.inject.Inject

class PlayerActivity : BaseActivity(), ApiListener<PlayerMatchesBundle>, ColorListener,
        MatchItemView.OnClickListener, Searchable, SearchQueryHandle, SearchToolbar.Listener,
        SwipeRefreshLayout.OnRefreshListener {

    private var list: List<Any>? = null
    private val adapter = PlayerAdapter()
    private var playerMatchesBundle: PlayerMatchesBundle? = null

    private val playerId: String by lazy { intent.requireStringExtra(EXTRA_PLAYER_ID) }

    @Inject
    protected lateinit var favoritePlayersRepository: FavoritePlayersRepository

    @Inject
    protected lateinit var identityManager: IdentityManager

    @Inject
    protected lateinit var regionManager: RegionManager

    @Inject
    protected lateinit var serverApi: ServerApi

    @Inject
    protected lateinit var smashRosterStorage: SmashRosterStorage

    @Inject
    protected lateinit var threadUtils: ThreadUtils


    companion object {
        private const val TAG = "PlayerActivity"
        private val CNAME = PlayerActivity::class.java.canonicalName
        private val EXTRA_PLAYER_ID = "$CNAME.PlayerId"

        fun getLaunchIntent(context: Context, player: AbsPlayer, region: Region? = null): Intent {
            var regionCopy = region

            if (player is FavoritePlayer) {
                regionCopy = player.region
            }

            return getLaunchIntent(context, player.id, regionCopy)
        }

        fun getLaunchIntent(context: Context, playerId: String, region: Region? = null): Intent {
            return Intent(context, PlayerActivity::class.java)
                    .putExtra(EXTRA_PLAYER_ID, playerId)
                    .putOptionalExtra(EXTRA_REGION, region)
        }
    }

    override val activityName = TAG

    private fun checkNameAndRegionViewScrollStates() {
        val view = recyclerView.getChildAt(0) as? PlayerProfileItemView

        if (view == null) {
            toolbar.fadeInTitleAndSubtitle()
            return
        }

        val ratingVerticalPositionInWindow = view.ratingVerticalPositionInWindow
        val toolbarVerticalPositionInWindow = toolbar.verticalPositionInWindow + toolbar.height

        if (ratingVerticalPositionInWindow <= toolbarVerticalPositionInWindow) {
            toolbar.fadeInTitleAndSubtitle()
        } else {
            toolbar.fadeOutTitleAndSubtitle()
        }
    }

    override fun failure(errorCode: Int) {
        playerMatchesBundle = null
        list = null
        showError(errorCode)
    }

    private fun fetchPlayerMatchesBundle() {
        refreshLayout.isRefreshing = true
        serverApi.getPlayerMatches(regionManager.getRegion(this), playerId,
                ApiCall(this))
    }

    override fun onBackPressed() {
        if (toolbar.isSearchFieldExpanded) {
            toolbar.closeSearchField()
        } else {
            super.onBackPressed()
        }
    }

    override fun onClick(v: MatchItemView) {
        val player = playerMatchesBundle?.fullPlayer ?: return
        val match = v.match ?: return
        startActivity(HeadToHeadActivity.getLaunchIntent(this, player, match,
                regionManager.getRegion(this)))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        setContentView(R.layout.activity_player)

        setTitleAndSubtitle()
        fetchPlayerMatchesBundle()
    }

    override fun onPaletteBuilt(palette: Palette?) {
        if (isAlive) {
            toolbar.animateToPaletteColors(window, palette)
        }
    }

    override fun onRefresh() {
        fetchPlayerMatchesBundle()
    }

    private val onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            checkNameAndRegionViewScrollStates()
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            checkNameAndRegionViewScrollStates()
        }
    }

    override fun onViewsBound() {
        super.onViewsBound()

        refreshLayout.setOnRefreshListener(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.addOnScrollListener(onScrollListener)

        recyclerView.adapter = adapter
    }

    override fun search(query: String?) {
        val list = this.list

        if (list.isNullOrEmpty()) {
            return
        }

        threadUtils.run(object : ThreadUtils.Task {
            private var list: List<Any>? = null

            override fun onBackground() {
                if (isAlive && TextUtils.equals(query, searchQuery)) {
                    this.list = ListUtils.searchPlayerMatchesList(query, list)
                }
            }

            override fun onUi() {
                if (isAlive && TextUtils.equals(query, searchQuery)) {
                    adapter.set(this.list)
                }
            }
        })
    }

    override val searchQuery: CharSequence?
        get() = toolbar.searchQuery

    private fun setTitleAndSubtitle() {
        if (toolbar.hasTitleText && toolbar.hasSubtitleText) {
            return
        }

        val region = regionManager.getRegion(this)
        val smashCompetitor = smashRosterStorage.getSmashCompetitor(region, playerId)
        val title = smashCompetitor?.tag ?: playerMatchesBundle?.fullPlayer?.name

        if (title.isNullOrBlank()) {
            return
        }

        toolbar.titleText = title
        toolbar.subtitleText = region.displayName
    }

    private fun showData() {
        val bundle = playerMatchesBundle ?: throw RuntimeException("playerMatchesBundle is null")
        val list = ListUtils.createPlayerMatchesList(this, bundle.fullPlayer,
                bundle.matchesBundle)
        this.list = list

        error.visibility = View.GONE

        if (list.isNullOrEmpty()) {
            adapter.clear()
            recyclerView.visibility = View.GONE
            empty.visibility = View.VISIBLE
        } else {
            adapter.set(list)
            empty.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }

        setTitleAndSubtitle()
        toolbar.refresh()

        refreshLayout.isRefreshing = false
    }

    private fun showError(errorCode: Int) {
        adapter.clear()
        recyclerView.visibility = View.GONE
        empty.visibility = View.GONE
        error.setVisibility(View.VISIBLE, errorCode)
        refreshLayout.isRefreshing = false
        toolbar.refresh()
    }

    override val showSearchIcon: Boolean
        get() = playerMatchesBundle?.matchesBundle?.matches?.isNotEmpty() == true

    override fun success(`object`: PlayerMatchesBundle?) {
        playerMatchesBundle = `object`
        list = null
        showData()
    }

}
