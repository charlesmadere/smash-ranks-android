package com.garpr.android.features.rankings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.garpr.android.R
import com.garpr.android.extensions.layoutInflater
import com.garpr.android.extensions.showAddOrRemoveFavoritePlayerDialog
import com.garpr.android.features.common.fragments.BaseFragment
import com.garpr.android.features.common.views.FetchingItemView
import com.garpr.android.features.common.views.StringDividerView
import com.garpr.android.features.player.PlayerActivity
import com.garpr.android.features.rankings.RankingsAndFavoritesViewModel.ListItem
import com.garpr.android.misc.ListLayout
import com.garpr.android.misc.Refreshable
import com.garpr.android.misc.RegionHandleUtils
import com.garpr.android.repositories.RegionRepository
import kotlinx.android.synthetic.main.fragment_rankings_and_favorites.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.text.NumberFormat

class RankingsAndFavoritesFragment : BaseFragment(), IdentityCardView.Listener, ListLayout,
        RankingItemCardView.Listeners, Refreshable, SwipeRefreshLayout.OnRefreshListener {

    private val adapter = Adapter(this, this)

    private val viewModel: RankingsAndFavoritesViewModel by sharedViewModel()

    protected val regionHandleUtils: RegionHandleUtils by inject()
    protected val regionRepository: RegionRepository by inject()

    private fun fetchRankings() {
        viewModel.fetchRankings(regionHandleUtils.getRegion(context))
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
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
    }

    override fun onClick(v: IdentityCardView) {
        startActivity(PlayerActivity.getLaunchIntent(
                context = requireContext(),
                player = v.identity,
                region = regionHandleUtils.getRegion(context)
        ))
    }

    override fun onClick(v: RankingItemCardView) {
        startActivity(PlayerActivity.getLaunchIntent(
                context = requireContext(),
                player = v.player,
                region = regionHandleUtils.getRegion(context)
        ))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_rankings_and_favorites, container, false)
    }

    override fun onLongClick(v: RankingItemCardView) {
        childFragmentManager.showAddOrRemoveFavoritePlayerDialog(
                player = v.player,
                region = regionHandleUtils.getRegion(context)
        )
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

    private fun refreshState(state: RankingsAndFavoritesViewModel.State) {
        if (state.searchResults == null) {
            adapter.set(state.list)
        } else {
            adapter.set(state.searchResults)
        }

        refreshLayout.isRefreshing = state.isRefreshing
    }

    companion object {
        fun create() = RankingsAndFavoritesFragment()
    }

    private class Adapter(
            private val identityCardViewListener: IdentityCardView.Listener,
            private val rankingItemCardViewListeners: RankingItemCardView.Listeners
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private val list = mutableListOf<ListItem>()

        init {
            setHasStableIds(true)
        }

        private fun bindActivityRequirements(holder: ActivityRequirementsViewHolder,
                item: ListItem.ActivityRequirements) {
            val tournaments = holder.textView.resources.getQuantityString(
                    R.plurals.x_tournaments, item.rankingNumTourneysAttended,
                    NUMBER_FORMAT.format(item.rankingNumTourneysAttended))
            val days = holder.textView.resources.getQuantityString(
                    R.plurals.x_days, item.rankingActivityDayLimit,
                    NUMBER_FORMAT.format(item.rankingActivityDayLimit))
            holder.textView.text = holder.textView.resources.getString(
                    R.string.x_activity_requirements_are_y_within_the_last_z,
                    item.regionDisplayName, tournaments, days)
        }

        private fun bindEmpty(holder: EmptyViewHolder, item: ListItem.Empty) {
            holder.textView.setText(when (item) {
                is ListItem.Empty.Favorites -> R.string.empty_favorites_description
                is ListItem.Empty.Rankings -> R.string.no_rankings
            })
        }

        private fun bindError(holder: ErrorViewHolder, item: ListItem.Error) {
            holder.textView.setText(when (item) {
                is ListItem.Error.Rankings -> R.string.error_loading_rankings
            })
        }

        private fun bindFetching(holder: FetchingViewHolder, item: ListItem.Fetching) {
            holder.fetchingItemView.setContent(holder.fetchingItemView.context.getText(when (item) {
                is ListItem.Fetching.Favorites -> R.string.loading_favorite_players_
                is ListItem.Fetching.Rankings -> R.string.loading_rankings_
            }))
        }

        private fun bindHeader(holder: HeaderViewHolder, item: ListItem.Header) {
            holder.stringDividerView.setContent(holder.stringDividerView.context.getString(when (item) {
                is ListItem.Header.Favorites -> R.string.favorite_players
                is ListItem.Header.Rankings -> R.string.rankings
            }))
        }

        private fun bindIdentity(holder: IdentityViewHolder, item: ListItem.Identity) {
            holder.identityCardView.setContent(
                    player = item.player,
                    previousRank = item.previousRank,
                    avatar = item.avatar,
                    rank = item.rating,
                    rating = item.rating,
                    tag = item.tag
            )
        }

        private fun bindNoResults(holder: NoResultsViewHolder, item: ListItem.NoResults) {
            holder.textView.text = holder.textView.context.getString(
                    R.string.no_search_results_for_x, item.query)
        }

        private fun bindRanking(holder: RankingViewHolder, item: ListItem.Player) {
            holder.rankingItemCardView.setContent(
                    player = item.player,
                    isIdentity = item.isIdentity,
                    previousRank = item.previousRank,
                    rank = item.rank,
                    rating = item.rating,
                    regionDisplayName = item.regionDisplayName
            )
        }

        override fun getItemCount(): Int = list.size

        override fun getItemId(position: Int): Long = list[position].listId

        override fun getItemViewType(position: Int): Int {
            return when (list[position]) {
                is ListItem.ActivityRequirements -> VIEW_TYPE_ACTIVITY_REQUIREMENTS
                is ListItem.Empty -> VIEW_TYPE_EMPTY
                is ListItem.Error -> VIEW_TYPE_ERROR
                is ListItem.Fetching -> VIEW_TYPE_FETCHING
                is ListItem.Header -> VIEW_TYPE_HEADER
                is ListItem.Identity -> VIEW_TYPE_IDENTITY
                is ListItem.NoResults -> VIEW_TYPE_NO_RESULTS
                is ListItem.Player -> VIEW_TYPE_RANKING
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            when (val item = list[position]) {
                is ListItem.ActivityRequirements -> bindActivityRequirements(
                        holder as ActivityRequirementsViewHolder, item)
                is ListItem.Empty -> bindEmpty(holder as EmptyViewHolder, item)
                is ListItem.Error -> bindError(holder as ErrorViewHolder, item)
                is ListItem.Fetching -> bindFetching(holder as FetchingViewHolder, item)
                is ListItem.Header -> bindHeader(holder as HeaderViewHolder, item)
                is ListItem.Identity -> bindIdentity(holder as IdentityViewHolder, item)
                is ListItem.NoResults -> bindNoResults(holder as NoResultsViewHolder, item)
                is ListItem.Player -> bindRanking(holder as RankingViewHolder, item)
                else -> throw RuntimeException("unknown item: $item, position: $position")
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val inflater = parent.layoutInflater

            return when (viewType) {
                VIEW_TYPE_ACTIVITY_REQUIREMENTS -> ActivityRequirementsViewHolder(inflater.inflate(
                        R.layout.item_string_with_more_top_margin, parent, false))
                VIEW_TYPE_EMPTY -> EmptyViewHolder(inflater.inflate(
                        R.layout.item_string_with_background, parent, false))
                VIEW_TYPE_ERROR -> ErrorViewHolder(inflater.inflate(
                        R.layout.item_string_with_background, parent, false))
                VIEW_TYPE_FETCHING -> FetchingViewHolder(inflater.inflate(
                        R.layout.item_fetching, parent, false))
                VIEW_TYPE_HEADER -> HeaderViewHolder(inflater.inflate(
                        R.layout.divider_string, parent, false))
                VIEW_TYPE_IDENTITY -> IdentityViewHolder(identityCardViewListener,
                        inflater.inflate(R.layout.item_identity_card, parent, false))
                VIEW_TYPE_NO_RESULTS -> NoResultsViewHolder(inflater.inflate(
                        R.layout.item_no_results_with_background, parent, false))
                VIEW_TYPE_RANKING -> RankingViewHolder(rankingItemCardViewListeners,
                        inflater.inflate(R.layout.item_ranking_card, parent, false))
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

        companion object {
            private const val VIEW_TYPE_ACTIVITY_REQUIREMENTS = 0
            private const val VIEW_TYPE_EMPTY = 1
            private const val VIEW_TYPE_ERROR = 2
            private const val VIEW_TYPE_FETCHING = 3
            private const val VIEW_TYPE_HEADER = 4
            private const val VIEW_TYPE_IDENTITY = 5
            private const val VIEW_TYPE_NO_RESULTS = 6
            private const val VIEW_TYPE_RANKING = 7

            private val NUMBER_FORMAT = NumberFormat.getInstance()
        }

    }

    private class ActivityRequirementsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val textView: TextView = itemView as TextView
    }

    private class EmptyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val textView: TextView = itemView as TextView
    }

    private class ErrorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val textView: TextView = itemView as TextView
    }

    private class FetchingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val fetchingItemView: FetchingItemView = itemView as FetchingItemView
    }

    private class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val stringDividerView: StringDividerView = itemView as StringDividerView
    }

    private class IdentityViewHolder(
            listener: IdentityCardView.Listener,
            itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        internal val identityCardView: IdentityCardView = itemView as IdentityCardView

        init {
            identityCardView.listener = listener
        }
    }

    private class NoResultsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val textView: TextView = itemView as TextView
    }

    private class RankingViewHolder(
            listeners: RankingItemCardView.Listeners,
            itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        internal val rankingItemCardView: RankingItemCardView = itemView as RankingItemCardView

        init {
            rankingItemCardView.listeners = listeners
        }
    }

}
