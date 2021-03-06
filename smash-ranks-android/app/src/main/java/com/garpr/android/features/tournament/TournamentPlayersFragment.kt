package com.garpr.android.features.tournament

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
import com.garpr.android.features.common.views.NoResultsItemView
import com.garpr.android.features.player.PlayerActivity
import com.garpr.android.features.tournament.TournamentViewModel.PlayerListItem
import com.garpr.android.misc.ListLayout
import com.garpr.android.misc.RegionHandleUtils
import kotlinx.android.synthetic.main.fragment_tournament_players.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class TournamentPlayersFragment : BaseFragment(), ListLayout, TournamentPlayerItemView.Listeners {

    private val adapter = Adapter(this)

    private val viewModel: TournamentViewModel by sharedViewModel()

    protected val regionHandleUtils: RegionHandleUtils by inject()

    override fun getRecyclerView(): RecyclerView? {
        return recyclerView
    }

    private fun initListeners() {
        viewModel.playersStateLiveData.observe(viewLifecycleOwner, Observer {
            refreshState(it)
        })
    }

    private fun initViews() {
        recyclerView.addItemDecoration(DividerItemDecoration(requireContext(),
                DividerItemDecoration.VERTICAL))
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
    }

    override fun onClick(v: TournamentPlayerItemView) {
        startActivity(PlayerActivity.getLaunchIntent(
                context = requireContext(),
                player = v.player,
                region = regionHandleUtils.getRegion(context)
        ))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_tournament_players, container, false)
    }

    override fun onLongClick(v: TournamentPlayerItemView) {
        childFragmentManager.showAddOrRemoveFavoritePlayerDialog(v.player,
                regionHandleUtils.getRegion(context))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initListeners()
    }

    private fun refreshState(state: TournamentViewModel.PlayersState) {
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
    }

    companion object {
        fun create() = TournamentPlayersFragment()
    }

    private class Adapter(
            private val tournamentPlayerItemViewListeners: TournamentPlayerItemView.Listeners
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private val list = mutableListOf<PlayerListItem>()

        init {
            setHasStableIds(true)
        }

        private fun bindNoResults(holder: NoResultsViewHolder, item: PlayerListItem.NoResults) {
            holder.noResultsItemView.setContent(item.query)
        }

        private fun bindPlayer(holder: TournamentPlayerViewHolder, item: PlayerListItem.Player) {
            holder.tournamentPlayerItemView.setContent(
                    player = item.player,
                    isIdentity = item.isIdentity
            )
        }

        internal fun clear() {
            list.clear()
            notifyDataSetChanged()
        }

        override fun getItemCount(): Int = list.size

        override fun getItemId(position: Int): Long = list[position].listId

        override fun getItemViewType(position: Int): Int {
            return when (list[position]) {
                is PlayerListItem.NoResults -> VIEW_TYPE_NO_RESULTS
                is PlayerListItem.Player -> VIEW_TYPE_PLAYER
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            when (val item = list[position]) {
                is PlayerListItem.NoResults -> bindNoResults(holder as NoResultsViewHolder, item)
                is PlayerListItem.Player -> bindPlayer(holder as TournamentPlayerViewHolder, item)
                else -> throw RuntimeException("unknown item: $item, position: $position")
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val inflater = parent.layoutInflater

            return when (viewType) {
                VIEW_TYPE_NO_RESULTS -> NoResultsViewHolder(inflater.inflate(
                        R.layout.item_no_results, parent, false))
                VIEW_TYPE_PLAYER -> TournamentPlayerViewHolder(tournamentPlayerItemViewListeners,
                        inflater.inflate(R.layout.item_tournament_player, parent, false))
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

        companion object {
            private const val VIEW_TYPE_NO_RESULTS = 0
            private const val VIEW_TYPE_PLAYER = 1
        }

    }

    private class NoResultsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val noResultsItemView: NoResultsItemView = itemView as NoResultsItemView
    }

    private class TournamentPlayerViewHolder(
            listeners: TournamentPlayerItemView.Listeners,
            itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        internal val tournamentPlayerItemView: TournamentPlayerItemView = itemView as TournamentPlayerItemView

        init {
            tournamentPlayerItemView.listeners = listeners
        }
    }

}
