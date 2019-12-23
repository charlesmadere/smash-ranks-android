package com.garpr.android.features.favoritePlayers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.garpr.android.R
import com.garpr.android.extensions.layoutInflater
import com.garpr.android.extensions.showAddOrRemoveFavoritePlayerDialog
import com.garpr.android.features.common.fragments.BaseFragment
import com.garpr.android.features.favoritePlayers.FavoritePlayersViewModel.ListItem
import com.garpr.android.features.player.PlayerActivity
import com.garpr.android.misc.ListLayout
import com.garpr.android.repositories.RegionRepository
import kotlinx.android.synthetic.main.fragment_favorite_players.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FavoritePlayersFragment : BaseFragment(), FavoritePlayerItemView.Listeners, ListLayout {

    private val adapter = Adapter(this)

    private val viewModel: FavoritePlayersViewModel by sharedViewModel()

    protected val regionRepository: RegionRepository by inject()

    companion object {
        fun create() = FavoritePlayersFragment()
    }

    override fun getRecyclerView(): RecyclerView? {
        return recyclerView
    }

    private fun initListeners() {
        viewModel.stateLiveData.observe(viewLifecycleOwner, Observer {
            refreshState(it)
        })
    }

    private fun initViews() {
        recyclerView.addItemDecoration(DividerItemDecoration(context,
                DividerItemDecoration.VERTICAL))
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
    }

    override fun onClick(v: FavoritePlayerItemView) {
        startActivity(PlayerActivity.getLaunchIntent(requireContext(), v.player, v.player.region))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_favorite_players, container, false)
    }

    override fun onLongClick(v: FavoritePlayerItemView) {
        childFragmentManager.showAddOrRemoveFavoritePlayerDialog(v.player)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initListeners()
    }

    private fun refreshState(state: FavoritePlayersViewModel.State) {
        if (state.isEmpty) {
            adapter.clear()
            recyclerView.visibility = View.GONE
            empty.visibility = View.VISIBLE
        } else {
            if (state.searchResults != null) {
                adapter.set(state.searchResults)
            } else {
                adapter.set(state.list)
            }

            empty.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }

        refreshLayout.isRefreshing = state.isFetching
    }

    private class Adapter(
            private val favoritePlayerItemViewListeners: FavoritePlayerItemView.Listeners
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private val list = mutableListOf<ListItem>()

        companion object {
            private const val VIEW_TYPE_FAVORITE_PLAYER = 0
        }

        init {
            setHasStableIds(true)
        }

        private fun bindFavoritePlayerViewHolder(holder: FavoritePlayerViewHolder,
                item: ListItem.FavoritePlayer) {
            holder.favoritePlayerItemView.setContent(item.player, item.isIdentity)
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
                is ListItem.FavoritePlayer -> VIEW_TYPE_FAVORITE_PLAYER
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            when (val item = list[position]) {
                is ListItem.FavoritePlayer -> bindFavoritePlayerViewHolder(
                        holder as FavoritePlayerViewHolder, item)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val inflater = parent.layoutInflater

            return when (viewType) {
                VIEW_TYPE_FAVORITE_PLAYER -> FavoritePlayerViewHolder(favoritePlayerItemViewListeners,
                        inflater.inflate(R.layout.item_favorite_player, parent, false))
                else -> throw IllegalArgumentException("unknown viewType: $viewType")
            }
        }

        internal fun set(list: List<ListItem>?) {
            this.list.clear()

            if (!list.isNullOrEmpty()) {
                this.list.addAll(list)
            }

            notifyDataSetChanged()
        }

    }

    private class FavoritePlayerViewHolder(
            listeners: FavoritePlayerItemView.Listeners,
            itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        internal val favoritePlayerItemView: FavoritePlayerItemView = itemView as FavoritePlayerItemView

        init {
            favoritePlayerItemView.listeners = listeners
        }
    }

}
