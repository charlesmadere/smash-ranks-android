package com.garpr.android.features.setIdentity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.garpr.android.R
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.extensions.layoutInflater
import com.garpr.android.features.common.activities.BaseActivity
import com.garpr.android.features.common.views.NoResultsItemView
import com.garpr.android.features.common.views.StringDividerView
import com.garpr.android.misc.PlayerListBuilder.PlayerListItem
import com.garpr.android.misc.Refreshable
import com.garpr.android.misc.Searchable
import com.garpr.android.repositories.RegionRepository
import kotlinx.android.synthetic.main.activity_set_identity.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SetIdentityActivity : BaseActivity(), PlayerSelectionItemView.Listener, Refreshable,
        Searchable, SetIdentityToolbar.Listener, SwipeRefreshLayout.OnRefreshListener {

    private val adapter = Adapter(this)

    private val viewModel: SetIdentityViewModel by viewModel()

    protected val regionRepository: RegionRepository by inject()

    companion object {
        private const val TAG = "SetIdentityActivity"

        fun getLaunchIntent(context: Context) = Intent(context, SetIdentityActivity::class.java)
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

    override fun navigateUp() {
        if (!viewModel.warnBeforeClose) {
            super.navigateUp()
            return
        }

        AlertDialog.Builder(this)
                .setMessage(R.string.youve_selected_an_identity_but_havent_saved)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.yes) { dialog, which ->
                    super@SetIdentityActivity.navigateUp()
                }
                .show()
    }

    override fun onBackPressed() {
        if (toolbar.isSearchFieldExpanded) {
            toolbar.closeSearchField()
            return
        }

        if (!viewModel.warnBeforeClose) {
            super.onBackPressed()
            return
        }

        AlertDialog.Builder(this)
                .setMessage(R.string.youve_selected_an_identity_but_havent_saved)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.yes) { dialog, which ->
                    super@SetIdentityActivity.onBackPressed()
                }
                .show()
    }

    override fun onClick(v: PlayerSelectionItemView) {
        viewModel.selectedIdentity = v.player
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_identity)
        initListeners()
        fetchPlayers()
    }

    override fun onRefresh() {
        refresh()
    }

    override fun onSaveClick(v: SetIdentityToolbar) {
        viewModel.saveSelectedIdentity(regionRepository.getRegion(this))
        setResult(Activity.RESULT_OK)
        supportFinishAfterTransition()
    }

    override fun onViewsBound() {
        super.onViewsBound()

        val region = regionRepository.getRegion(this)
        toolbar.subtitleText = getString(R.string.region_endpoint_format, region.displayName,
                getText(region.endpoint.title))
        toolbar.listener = this

        refreshLayout.setOnRefreshListener(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
    }

    override fun refresh() {
        fetchPlayers()
    }

    private fun refreshState(state: SetIdentityViewModel.State) {
        when (state.saveIconStatus) {
            SetIdentityViewModel.SaveIconStatus.DISABLED -> {
                toolbar.showSaveIcon = true
                toolbar.enableSaveIcon = false
            }

            SetIdentityViewModel.SaveIconStatus.ENABLED -> {
                toolbar.showSaveIcon = true
                toolbar.enableSaveIcon = true
            }

            SetIdentityViewModel.SaveIconStatus.GONE -> {
                toolbar.showSaveIcon = false
                toolbar.enableSaveIcon = false
            }
        }

        toolbar.showSearchIcon = state.showSearchIcon

        if (state.hasError) {
            adapter.clear()
            recyclerView.visibility = View.GONE
            empty.visibility = View.GONE
            error.visibility = View.VISIBLE
        } else if (state.isEmpty) {
            adapter.clear()
            recyclerView.visibility = View.GONE
            error.visibility = View.GONE
            empty.visibility = View.VISIBLE
        } else {
            if (state.searchResults != null) {
                adapter.set(state.searchResults, state.selectedIdentity)
            } else {
                adapter.set(state.list, state.selectedIdentity)
            }

            error.visibility = View.GONE
            empty.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }

        if (state.isFetching) {
            refreshLayout.isEnabled = true
            refreshLayout.isRefreshing = true
        } else {
            refreshLayout.isRefreshing = false
            refreshLayout.isEnabled = state.isRefreshEnabled
        }
    }

    override fun search(query: String?) {
        viewModel.search(query)
    }

    private class Adapter(
            private val playerItemViewListener: PlayerSelectionItemView.Listener
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private var selectedIdentity: AbsPlayer? = null
        private val list = mutableListOf<PlayerListItem>()

        companion object {
            private const val VIEW_TYPE_DIVIDER = 0
            private const val VIEW_TYPE_NO_RESULTS = 1
            private const val VIEW_TYPE_PLAYER = 2
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

        private fun bindNoResultsViewHolder(holder: NoResultsViewHolder, item: PlayerListItem.NoResults) {
            holder.noResultsViewHolder.setContent(item.query)
        }

        private fun bindPlayerViewHolder(holder: PlayerViewHolder, item: PlayerListItem.Player) {
            holder.playerSelectionItemView.setContent(item.player, item.player == selectedIdentity)
        }

        internal fun clear() {
            list.clear()
            selectedIdentity = null
            notifyDataSetChanged()
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun getItemId(position: Int): Long {
            return list[position].listId
        }

        override fun getItemViewType(position: Int): Int {
            return when (list[position]) {
                is PlayerListItem.Divider -> VIEW_TYPE_DIVIDER
                is PlayerListItem.NoResults -> VIEW_TYPE_NO_RESULTS
                is PlayerListItem.Player -> VIEW_TYPE_PLAYER
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            when (val item = list[position]) {
                is PlayerListItem.Divider -> bindDividerViewHolder(holder as DividerViewHolder, item)
                is PlayerListItem.NoResults -> bindNoResultsViewHolder(holder as NoResultsViewHolder, item)
                is PlayerListItem.Player -> bindPlayerViewHolder(holder as PlayerViewHolder, item)
                else -> throw RuntimeException("unknown item: $item, position: $position")
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val inflater = parent.layoutInflater

            return when (viewType) {
                VIEW_TYPE_DIVIDER -> DividerViewHolder(inflater.inflate(R.layout.divider_string,
                        parent, false))
                VIEW_TYPE_NO_RESULTS -> NoResultsViewHolder(inflater.inflate(
                        R.layout.item_no_results, parent, false))
                VIEW_TYPE_PLAYER -> PlayerViewHolder(playerItemViewListener, inflater.inflate(
                        R.layout.item_player_selection, parent, false))
                else -> throw IllegalArgumentException("unknown viewType: $viewType")
            }
        }

        internal fun set(list: List<PlayerListItem>?, selectedIdentity: AbsPlayer?) {
            this.list.clear()

            if (!list.isNullOrEmpty()) {
                this.list.addAll(list)
            }

            this.selectedIdentity = selectedIdentity
            notifyDataSetChanged()
        }

    }

    private class DividerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val dividerItemView: StringDividerView = itemView as StringDividerView
    }

    private class NoResultsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val noResultsViewHolder: NoResultsItemView = itemView as NoResultsItemView
    }

    private class PlayerViewHolder(
            listener: PlayerSelectionItemView.Listener,
            itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        internal val playerSelectionItemView: PlayerSelectionItemView = itemView as PlayerSelectionItemView

        init {
            playerSelectionItemView.listener = listener
        }
    }

}
