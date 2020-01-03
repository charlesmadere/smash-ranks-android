package com.garpr.android.features.rankings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.garpr.android.R
import com.garpr.android.extensions.layoutInflater
import com.garpr.android.extensions.showAddOrRemoveFavoritePlayerDialog
import com.garpr.android.features.common.fragments.BaseFragment
import com.garpr.android.features.common.views.NoResultsItemView
import com.garpr.android.features.player.PlayerActivity
import com.garpr.android.features.rankings.RankingsViewModel.ListItem
import com.garpr.android.misc.ListLayout
import com.garpr.android.misc.Refreshable
import com.garpr.android.misc.Searchable
import com.garpr.android.repositories.RegionRepository
import kotlinx.android.synthetic.main.fragment_rankings.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class RankingsFragment : BaseFragment(), IdentityRankingItemView.Listener, ListLayout,
        RankingItemView.Listeners, Refreshable, Searchable, SwipeRefreshLayout.OnRefreshListener {

    private val adapter = Adapter(this, this)

    private val viewModel: RankingsViewModel by sharedViewModel()

    protected val regionRepository: RegionRepository by inject()

    companion object {
        fun create() = RankingsFragment()
    }

    private fun fetchRankings() {
        viewModel.fetchRankings(regionRepository.getRegion(requireContext()))
    }

    override fun getRecyclerView(): RecyclerView? {
        return recyclerView
    }

    private fun initListeners() {
        onCreateViewDisposable.add(regionRepository.observable
                .subscribe {
                    refresh()
                })

        viewModel.stateLiveData.observe(viewLifecycleOwner, Observer {
            refreshState(it)
        })
    }

    private fun initViews() {
        refreshLayout.setOnRefreshListener(this)
        recyclerView.addItemDecoration(DividerItemDecoration(requireContext(),
                DividerItemDecoration.VERTICAL))
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
    }

    override fun onClick(v: IdentityRankingItemView) {
        val intent = PlayerActivity.getLaunchIntent(
                context = requireContext(),
                player = v.identity,
                region = regionRepository.getRegion(requireContext())
        )

        startActivity(intent)
    }

    override fun onClick(v: RankingItemView) {
        val intent = PlayerActivity.getLaunchIntent(
                context = requireContext(),
                player = v.player,
                region = regionRepository.getRegion(requireContext())
        )

        startActivity(intent)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_rankings, container, false)
    }

    override fun onLongClick(v: RankingItemView) {
        childFragmentManager.showAddOrRemoveFavoritePlayerDialog(v.player,
                regionRepository.getRegion(requireContext()))
    }

    override fun onRefresh() {
        refresh()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initListeners()
        fetchRankings()
    }

    override fun refresh() {
        fetchRankings()
    }

    private fun refreshState(state: RankingsViewModel.State) {
        if (state.hasError) {
            showError()
        } else if (state.isEmpty) {
            showEmpty()
        } else if (state.searchResults != null) {
            showList(state.searchResults)
        } else {
            showList(state.list)
        }

        refreshLayout.isRefreshing = state.isFetching
    }

    override fun search(query: String?) {
        viewModel.search(query)
    }

    private fun showEmpty() {
        adapter.clear()
        recyclerView.visibility = View.GONE
        error.visibility = View.GONE
        empty.visibility = View.VISIBLE
    }

    private fun showError() {
        adapter.clear()
        recyclerView.visibility = View.GONE
        empty.visibility = View.GONE
        error.visibility = View.VISIBLE
    }

    private fun showList(list: List<ListItem>?) {
        adapter.set(list)
        empty.visibility = View.GONE
        error.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }

    private class Adapter(
            private val identityRankingItemViewListener: IdentityRankingItemView.Listener,
            private val rankingItemViewListeners: RankingItemView.Listeners
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private val list = mutableListOf<ListItem>()

        companion object {
            private const val VIEW_TYPE_IDENTITY = 0
            private const val VIEW_TYPE_NO_RESULTS = 1
            private const val VIEW_TYPE_PLAYER = 2
        }

        init {
            setHasStableIds(true)
        }

        private fun bindIdentity(holder: IdentityViewHolder, item: ListItem.Identity) {
            holder.identityRankingItemView.setContent(
                    player = item.player,
                    previousRank = item.previousRank,
                    avatar = item.avatar,
                    name = item.name,
                    rank = item.rank,
                    rating = item.rating
            )
        }

        private fun bindNoResults(holder: NoResultsViewHolder, item: ListItem.NoResults) {
            holder.noResultsItemView.setContent(item.query)
        }

        private fun bindPlayer(holder: PlayerViewHolder, item: ListItem.Player) {
            holder.rankingItemView.setContent(
                    player = item.player,
                    isIdentity = item.isIdentity,
                    previousRank = item.previousRank,
                    rank = item.rank,
                    rating = item.rating
            )
        }

        internal fun clear() {
            list.clear()
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
                is ListItem.Identity -> VIEW_TYPE_IDENTITY
                is ListItem.NoResults -> VIEW_TYPE_NO_RESULTS
                is ListItem.Player -> VIEW_TYPE_PLAYER
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            when (val item = list[position]) {
                is ListItem.Identity -> bindIdentity(holder as IdentityViewHolder, item)
                is ListItem.NoResults -> bindNoResults(holder as NoResultsViewHolder, item)
                is ListItem.Player -> bindPlayer(holder as PlayerViewHolder, item)
                else -> throw RuntimeException("unknown item: $item, position: $position")
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val inflater = parent.layoutInflater

            return when (viewType) {
                VIEW_TYPE_IDENTITY -> IdentityViewHolder(identityRankingItemViewListener,
                        inflater.inflate(R.layout.item_identity_ranking, parent, false))
                VIEW_TYPE_NO_RESULTS -> NoResultsViewHolder(inflater.inflate(
                        R.layout.item_no_results, parent, false))
                VIEW_TYPE_PLAYER -> PlayerViewHolder(rankingItemViewListeners,
                        inflater.inflate(R.layout.item_ranking, parent, false))
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

    private class IdentityViewHolder(
            listener: IdentityRankingItemView.Listener,
            itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        internal val identityRankingItemView: IdentityRankingItemView = itemView as IdentityRankingItemView

        init {
            identityRankingItemView.listener = listener
        }
    }

    private class NoResultsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val noResultsItemView: NoResultsItemView = itemView as NoResultsItemView
    }

    private class PlayerViewHolder(
            listeners: RankingItemView.Listeners,
            itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        internal val rankingItemView: RankingItemView = itemView as RankingItemView

        init {
            rankingItemView.listeners = listeners
        }
    }

}
