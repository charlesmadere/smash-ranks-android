package com.garpr.android.features.player

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.garpr.android.R
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.data.models.FullPlayer
import com.garpr.android.data.models.Region
import com.garpr.android.data.models.SmashCompetitor
import com.garpr.android.extensions.appComponent
import com.garpr.android.extensions.layoutInflater
import com.garpr.android.extensions.putOptionalExtra
import com.garpr.android.extensions.requireStringExtra
import com.garpr.android.extensions.verticalPositionInWindow
import com.garpr.android.extensions.viewModel
import com.garpr.android.features.common.activities.BaseActivity
import com.garpr.android.features.common.views.StringItemView
import com.garpr.android.features.headToHead.HeadToHeadActivity
import com.garpr.android.features.tournaments.TournamentDividerView
import com.garpr.android.misc.ColorListener
import com.garpr.android.misc.Refreshable
import com.garpr.android.misc.Searchable
import com.garpr.android.misc.ShareUtils
import com.garpr.android.repositories.RegionRepository
import kotlinx.android.synthetic.main.activity_player.*
import javax.inject.Inject

class PlayerActivity : BaseActivity(), ColorListener, MatchItemView.OnClickListener,
        PlayerProfileItemView.Listeners, Refreshable, Searchable,
        SwipeRefreshLayout.OnRefreshListener {

    private val adapter = Adapter(this)
    private val playerId: String by lazy { intent.requireStringExtra(EXTRA_PLAYER_ID) }
    private val viewModel by viewModel(this) { appComponent.playerViewModel }

    @Inject
    protected lateinit var regionRepository: RegionRepository

    @Inject
    protected lateinit var shareUtils: ShareUtils


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

    private fun fetchPlayer() {
        viewModel.fetchPlayer()
    }

    private fun initListeners() {
        viewModel.stateLiveData.observe(this, Observer {
            refreshState(it)
        })
    }

    override fun onBackPressed() {
        if (toolbar.isSearchFieldExpanded) {
            toolbar.closeSearchField()
        } else {
            super.onBackPressed()
        }
    }

    override fun onClick(v: MatchItemView) {
        val player = viewModel.player ?: return
        val match = v.match ?: return
        startActivity(HeadToHeadActivity.getLaunchIntent(this, player, match,
                regionRepository.getRegion(this)))
    }

    override fun onCompareClick(v: PlayerProfileItemView) {
        val identity = viewModel.identity ?: return
        val player = viewModel.player ?: return
        startActivity(HeadToHeadActivity.getLaunchIntent(this, identity, player,
                regionRepository.getRegion(this)))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        viewModel.initialize(regionRepository.getRegion(this), playerId)
        setContentView(R.layout.activity_player)
        initListeners()
        fetchPlayer()
    }

    override fun onFavoriteOrUnfavoriteClick(v: PlayerProfileItemView) {
        viewModel.addOrRemoveFromFavorites()
    }

    override fun onPaletteBuilt(palette: Palette?) {
        if (isAlive) {
            toolbar.animateToPaletteColors(window, palette)
        }
    }

    override fun onRefresh() {
        refresh()
    }

    override fun onShareClick(v: PlayerProfileItemView) {
        val player = requireNotNull(viewModel.player)
        shareUtils.sharePlayer(this, player)
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

    override fun onUrlClick(v: PlayerProfileItemView, url: String?) {
        shareUtils.openUrl(this, url)
    }

    override fun onViewsBound() {
        super.onViewsBound()

        refreshLayout.setOnRefreshListener(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.addOnScrollListener(onScrollListener)
        recyclerView.adapter = adapter
    }

    override fun refresh() {
        fetchPlayer()
    }

    private fun refreshState(state: PlayerViewModel.State) {
        toolbar.titleText = state.titleText
        toolbar.subtitleText = state.subtitleText
        toolbar.showSearchIcon = if (toolbar.isSearchFieldExpanded) false else state.showSearchIcon

        if (state.hasError) {
            adapter.clear()
            recyclerView.visibility = View.GONE
            error.visibility = View.VISIBLE
        } else {
            adapter.set(
                    list = state.searchResults ?: state.list,
                    isFavorited = state.isFavorited,
                    player = viewModel.player,
                    smashCompetitor = state.smashCompetitor
            )

            error.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }

        refreshLayout.isRefreshing = state.isFetching
    }

    override fun search(query: String?) {
        viewModel.search(query)
    }

    private class Adapter(
            private val playerProfileItemViewListeners: PlayerProfileItemView.Listeners? = null
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private var isFavorited: Boolean = false
        private var player: FullPlayer? = null
        private val list = mutableListOf<PlayerViewModel.ListItem>()
        private var smashCompetitor: SmashCompetitor? = null

        companion object {
            private const val VIEW_TYPE_MATCH = 0
            private const val VIEW_TYPE_NO_MATCHES = 1
            private const val VIEW_TYPE_PLAYER = 2
            private const val VIEW_TYPE_TOURNAMENT = 3
        }

        init {
            setHasStableIds(true)
        }

        private fun bindMatchViewHolder(holder: MatchViewHolder, item: PlayerViewModel.ListItem.Match) {
            holder.matchItemView.match = item.match
        }

        private fun bindPlayerViewHolder(holder: PlayerViewHolder) {
            val player = requireNotNull(this.player)
            holder.playerProfileItemView.setContent(isFavorited, player, smashCompetitor)
        }

        private fun bindTournamentViewHolder(holder: TournamentViewHolder, item: PlayerViewModel.ListItem.Tournament) {
            holder.tournamentDividerView.tournament = item.tournament
        }

        internal fun clear() {
            list.clear()
            notifyDataSetChanged()
        }

        override fun getItemCount(): Int = list.size

        override fun getItemId(position: Int): Long = list[position].listId

        override fun getItemViewType(position: Int): Int {
            return when (list[position]) {
                is PlayerViewModel.ListItem.Match -> VIEW_TYPE_MATCH
                is PlayerViewModel.ListItem.NoMatches -> VIEW_TYPE_NO_MATCHES
                is PlayerViewModel.ListItem.Player -> VIEW_TYPE_PLAYER
                is PlayerViewModel.ListItem.Tournament -> VIEW_TYPE_TOURNAMENT
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            when (val item = list[position]) {
                is PlayerViewModel.ListItem.Match -> bindMatchViewHolder(holder as MatchViewHolder, item)
                is PlayerViewModel.ListItem.NoMatches -> { /* intentionally empty */ }
                is PlayerViewModel.ListItem.Player -> bindPlayerViewHolder(holder as PlayerViewHolder)
                is PlayerViewModel.ListItem.Tournament -> bindTournamentViewHolder(holder as TournamentViewHolder, item)
                else -> throw RuntimeException("unknown item: $item, position: $position")
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val inflater = parent.layoutInflater

            return when (viewType) {
                VIEW_TYPE_MATCH -> MatchViewHolder(inflater.inflate(R.layout.item_match, parent,
                        false))
                VIEW_TYPE_NO_MATCHES -> NoMatchesViewHolder(
                        parent.context.getString(R.string.no_matches),
                        inflater.inflate(R.layout.item_string, parent, false))
                VIEW_TYPE_PLAYER -> PlayerViewHolder(playerProfileItemViewListeners,
                        inflater.inflate(R.layout.item_player_profile, parent, false))
                VIEW_TYPE_TOURNAMENT -> TournamentViewHolder(inflater.inflate(
                        R.layout.divider_tournament, parent, false))
                else -> throw IllegalArgumentException("unknown viewType: $viewType")
            }
        }

        internal fun set(list: List<PlayerViewModel.ListItem>?, isFavorited: Boolean,
                player: FullPlayer?, smashCompetitor: SmashCompetitor?) {
            this.list.clear()

            if (!list.isNullOrEmpty()) {
                this.list.addAll(list)
            }

            this.isFavorited = isFavorited
            this.player = player
            this.smashCompetitor = smashCompetitor

            notifyDataSetChanged()
        }

    }

    private class MatchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val matchItemView: MatchItemView = itemView as MatchItemView
    }

    private class NoMatchesViewHolder(
            text: String,
            itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        init {
            (itemView as StringItemView).setContent(text)
        }
    }

    private class PlayerViewHolder(
            playerProfileItemViewListeners: PlayerProfileItemView.Listeners?,
            itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        internal val playerProfileItemView: PlayerProfileItemView = itemView as PlayerProfileItemView

        init {
            playerProfileItemView.listeners = playerProfileItemViewListeners
        }
    }

    private class TournamentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val tournamentDividerView: TournamentDividerView = itemView as TournamentDividerView
    }

}
