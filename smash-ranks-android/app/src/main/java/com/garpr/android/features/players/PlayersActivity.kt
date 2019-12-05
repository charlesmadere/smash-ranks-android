package com.garpr.android.features.players

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.garpr.android.R
import com.garpr.android.data.models.Region
import com.garpr.android.extensions.layoutInflater
import com.garpr.android.extensions.putOptionalExtra
import com.garpr.android.extensions.showAddOrRemoveFavoritePlayerDialog
import com.garpr.android.features.common.activities.BaseActivity
import com.garpr.android.features.common.views.StringDividerView
import com.garpr.android.features.player.PlayerActivity
import com.garpr.android.misc.PlayerListBuilder.PlayerListItem
import com.garpr.android.misc.Refreshable
import com.garpr.android.misc.Searchable
import com.garpr.android.repositories.RegionRepository
import kotlinx.android.synthetic.main.activity_players.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayersActivity : BaseActivity(), PlayerItemView.Listeners, Refreshable, Searchable,
        SwipeRefreshLayout.OnRefreshListener {

    private val adapter = Adapter(this)

    private val viewModel: PlayersViewModel by viewModel()

    protected val regionRepository: RegionRepository by inject()

    companion object {
        private const val TAG = "PlayersActivity"

        fun getLaunchIntent(context: Context, region: Region? = null): Intent {
            return Intent(context, PlayersActivity::class.java)
                    .putOptionalExtra(EXTRA_REGION, region)
        }
    }

    override val activityName = TAG

    private fun fetchPlayers() {
        viewModel.fetchPlayers(regionRepository.getRegion(this))
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

    override fun onClick(v: PlayerItemView) {
        val intent = PlayerActivity.getLaunchIntent(
                context = this,
                player = v.player,
                region = regionRepository.getRegion(this)
        )

        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_players)
        initListeners()
        fetchPlayers()
    }

    override fun onLongClick(v: PlayerItemView) {
        supportFragmentManager.showAddOrRemoveFavoritePlayerDialog(v.player,
                regionRepository.getRegion(this))
    }

    override fun onRefresh() {
        fetchPlayers()
    }

    override fun onViewsBound() {
        super.onViewsBound()

        toolbar.subtitleText = regionRepository.getRegion(this).displayName

        refreshLayout.setOnRefreshListener(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL))
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
    }

    override fun refresh() {
        fetchPlayers()
    }

    private fun refreshState(state: PlayersViewModel.State) {
        toolbar.showSearchIcon = if (toolbar.isSearchFieldExpanded) false else state.showSearchIcon

        if (state.hasError) {
            adapter.clear()
            empty.visibility = View.GONE
            recyclerView.visibility = View.GONE
            error.visibility = View.VISIBLE
        } else if (state.isEmpty) {
            adapter.clear()
            error.visibility = View.GONE
            recyclerView.visibility = View.GONE
            empty.visibility = View.VISIBLE
        } else {
            if (state.searchResults == null) {
                adapter.set(state.list)
            } else {
                adapter.set(state.searchResults)
            }

            error.visibility = View.GONE
            empty.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }

        refreshLayout.isRefreshing = state.isFetching
    }

    override fun search(query: String?) {
        viewModel.search(query)
    }

    private class Adapter(
            private val playerItemViewListeners: PlayerItemView.Listeners
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private val list = mutableListOf<PlayerListItem>()

        companion object {
            private const val VIEW_TYPE_DIVIDER = 0
            private const val VIEW_TYPE_PLAYER = 1
        }

        init {
            setHasStableIds(true)
        }

        private fun bindDividerViewHolder(holder: DividerViewHolder, item: PlayerListItem.Divider) {
            val content: String = when (item) {
                is PlayerListItem.Divider.Digit -> holder.dividerItemView.resources.getString(R.string.number)
                is PlayerListItem.Divider.Letter -> item.letter
                is PlayerListItem.Divider.Other -> holder.dividerItemView.resources.getString(R.string.other)
            }

            holder.dividerItemView.setContent(content)
        }

        private fun bindPlayerViewHolder(holder: PlayerViewHolder, item: PlayerListItem.Player) {
            holder.playerItemView.setContent(item.player, item.isIdentity)
        }

        internal fun clear() {
            list.clear()
            notifyDataSetChanged()
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun getItemId(position: Int): Long {
            return list[position].hashCode().toLong()
        }

        override fun getItemViewType(position: Int): Int {
            return when (list[position]) {
                is PlayerListItem.Divider -> VIEW_TYPE_DIVIDER
                is PlayerListItem.Player -> VIEW_TYPE_PLAYER
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            when (val item = list[position]) {
                is PlayerListItem.Divider -> bindDividerViewHolder(holder as DividerViewHolder, item)
                is PlayerListItem.Player -> bindPlayerViewHolder(holder as PlayerViewHolder, item)
                else -> throw RuntimeException("unknown item: $item, position: $position")
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val inflater = parent.layoutInflater

            return when (viewType) {
                VIEW_TYPE_DIVIDER -> DividerViewHolder(inflater.inflate(R.layout.divider_string,
                                parent, false))
                VIEW_TYPE_PLAYER -> PlayerViewHolder(playerItemViewListeners,
                        inflater.inflate(R.layout.item_player, parent, false))
                else -> throw IllegalArgumentException("unknown viewType: $viewType")
            }
        }

        internal fun set(list: List<PlayerListItem>?) {
            this.list.clear()

            if (!list.isNullOrEmpty()) {
                this.list.addAll(list)
            }

            notifyDataSetChanged()
        }

    }

    private class DividerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val dividerItemView: StringDividerView = itemView as StringDividerView
    }

    private class PlayerViewHolder(
            playerItemViewListeners: PlayerItemView.Listeners,
            itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        internal val playerItemView: PlayerItemView = itemView as PlayerItemView

        init {
            playerItemView.listeners = playerItemViewListeners
        }
    }

}
