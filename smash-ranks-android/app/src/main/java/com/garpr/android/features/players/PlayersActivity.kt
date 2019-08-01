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
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.Region
import com.garpr.android.extensions.appComponent
import com.garpr.android.extensions.layoutInflater
import com.garpr.android.extensions.putOptionalExtra
import com.garpr.android.extensions.viewModel
import com.garpr.android.features.common.activities.BaseActivity
import com.garpr.android.misc.Refreshable
import com.garpr.android.misc.Searchable
import com.garpr.android.repositories.RegionRepository
import kotlinx.android.synthetic.main.activity_players.*
import javax.inject.Inject

class PlayersActivity : BaseActivity(), Refreshable, Searchable,
        SwipeRefreshLayout.OnRefreshListener {

    private val adapter = Adapter()

    private val viewModel by viewModel(this) { appComponent.playersViewModel }

    @Inject
    protected lateinit var regionRepository: RegionRepository


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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        setContentView(R.layout.activity_players)
        initListeners()
        fetchPlayers()
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
                adapter.set(state.playersBundle?.players)
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

    private class Adapter : RecyclerView.Adapter<PlayerViewHolder>() {

        private val list = mutableListOf<AbsPlayer>()

        init {
            setHasStableIds(true)
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

        override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
            holder.playerItemView.player = list[position]
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
            val inflater = parent.layoutInflater
            return PlayerViewHolder(inflater.inflate(R.layout.item_player, parent,
                    false))
        }

        internal fun set(list: List<AbsPlayer>?) {
            this.list.clear()

            if (!list.isNullOrEmpty()) {
                this.list.addAll(list)
            }

            notifyDataSetChanged()
        }

    }

    private class PlayerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val playerItemView: PlayerItemView = itemView as PlayerItemView
    }

}
