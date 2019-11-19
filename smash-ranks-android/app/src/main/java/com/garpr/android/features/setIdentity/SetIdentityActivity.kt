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
import com.garpr.android.features.common.views.StringDividerView
import com.garpr.android.features.setIdentity.SetIdentityViewModel.ListItem
import com.garpr.android.misc.Refreshable
import com.garpr.android.misc.Searchable
import com.garpr.android.repositories.RegionRepository
import kotlinx.android.synthetic.main.activity_set_identity.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SetIdentityActivity : BaseActivity(), PlayerSelectionItemView.OnClickListener, Refreshable,
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

        toolbar.subtitleText = regionRepository.getRegion(this).displayName
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

        refreshLayout.isRefreshing = state.isFetching
        refreshLayout.isEnabled = state.isRefreshEnabled
    }

    override fun search(query: String?) {
        viewModel.search(query)
    }

    private class Adapter(
            private val playerClickListener: PlayerSelectionItemView.OnClickListener
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private var selectedIdentity: AbsPlayer? = null
        private val list = mutableListOf<ListItem>()

        companion object {
            private const val VIEW_TYPE_DIVIDER = 0
            private const val VIEW_TYPE_PLAYER = 1
        }

        init {
            setHasStableIds(true)
        }

        private fun bindDividerViewHolder(holder: DividerViewHolder, item: ListItem.Divider) {
            val content: String = when (item) {
                is ListItem.Divider.Digit -> holder.dividerItemView.resources.getString(R.string.number)
                is ListItem.Divider.Letter -> item.letter
                is ListItem.Divider.Other -> holder.dividerItemView.resources.getString(R.string.other)
            }

            holder.dividerItemView.setContent(content)
        }

        private fun bindPlayerViewHolder(holder: PlayerViewHolder, item: ListItem.Player) {
            holder.playerItemView.setContent(Pair(item.player, item.player == selectedIdentity))
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
                is ListItem.Divider -> VIEW_TYPE_DIVIDER
                is ListItem.Player -> VIEW_TYPE_PLAYER
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            when (val item = list[position]) {
                is ListItem.Divider -> bindDividerViewHolder(
                        holder as DividerViewHolder, item)
                is ListItem.Player -> bindPlayerViewHolder(
                        holder as PlayerViewHolder, item)
                else -> throw RuntimeException("unknown item: $item, position: $position")
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val inflater = parent.layoutInflater

            return when (viewType) {
                VIEW_TYPE_DIVIDER -> DividerViewHolder(inflater.inflate(R.layout.divider_string,
                        parent, false))
                VIEW_TYPE_PLAYER -> PlayerViewHolder(inflater.inflate(R.layout.item_player_selection,
                            parent, false), playerClickListener)
                else -> throw IllegalArgumentException("unknown viewType: $viewType")
            }
        }

        internal fun set(list: List<ListItem>?, selectedIdentity: AbsPlayer?) {
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

    private class PlayerViewHolder(
            itemView: View,
            onClickListener: PlayerSelectionItemView.OnClickListener
    ) : RecyclerView.ViewHolder(itemView) {
        internal val playerItemView: PlayerSelectionItemView = itemView as PlayerSelectionItemView

        init {
            playerItemView.onClickListener = onClickListener
        }
    }

}
