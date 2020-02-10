package com.garpr.android.features.tournaments

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
import com.garpr.android.features.common.fragments.BaseFragment
import com.garpr.android.features.common.views.NoResultsItemView
import com.garpr.android.features.tournament.TournamentActivity
import com.garpr.android.features.tournaments.TournamentsViewModel.ListItem
import com.garpr.android.misc.ListLayout
import com.garpr.android.misc.Refreshable
import com.garpr.android.misc.RegionHandleUtils
import com.garpr.android.repositories.RegionRepository
import kotlinx.android.synthetic.main.fragment_tournaments.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class TournamentsFragment : BaseFragment(), ListLayout, Refreshable,
        SwipeRefreshLayout.OnRefreshListener, TournamentItemView.Listener {

    private val adapter = Adapter(this)

    private val viewModel: TournamentsViewModel by sharedViewModel()

    protected val regionHandleUtils: RegionHandleUtils by inject()
    protected val regionRepository: RegionRepository by inject()

    companion object {
        fun create() = TournamentsFragment()
    }

    private fun fetchTournamentsBundle() {
        viewModel.fetchTournaments(regionHandleUtils.getRegion(context))
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

    override fun onClick(v: TournamentItemView) {
        startActivity(TournamentActivity.getLaunchIntent(
                context = requireContext(),
                tournament = v.tournament,
                region = regionHandleUtils.getRegion(context)
        ))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_tournaments, container, false)
    }

    override fun onRefresh() {
        refresh()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initListeners()
        fetchTournamentsBundle()
    }

    override fun refresh() {
        fetchTournamentsBundle()
    }

    private fun refreshState(state: TournamentsViewModel.State) {
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
            private val tournamentItemViewListener: TournamentItemView.Listener
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private val list = mutableListOf<ListItem>()

        companion object {
            private const val VIEW_TYPE_NO_RESULTS = 0
            private const val VIEW_TYPE_TOURNAMENT = 1
        }

        init {
            setHasStableIds(true)
        }

        private fun bindNoResults(holder: NoResultsViewHolder, item: ListItem.NoResults) {
            holder.noResultsItemView.setContent(item.query)
        }

        private fun bindTournament(holder: TournamentViewHolder, item: ListItem.Tournament) {
            holder.tournamentItemView.setContent(item.tournament)
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
                is ListItem.NoResults -> VIEW_TYPE_NO_RESULTS
                is ListItem.Tournament -> VIEW_TYPE_TOURNAMENT
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            when (val item = list[position]) {
                is ListItem.NoResults -> bindNoResults(holder as NoResultsViewHolder, item)
                is ListItem.Tournament -> bindTournament(holder as TournamentViewHolder, item)
                else -> throw RuntimeException("unknown item: $item, position: $position")
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val inflater = parent.layoutInflater

            return when (viewType) {
                VIEW_TYPE_NO_RESULTS -> NoResultsViewHolder(inflater.inflate(
                        R.layout.item_no_results, parent, false))
                VIEW_TYPE_TOURNAMENT -> TournamentViewHolder(tournamentItemViewListener,
                        inflater.inflate(R.layout.item_tournament, parent, false))
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

    private class NoResultsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val noResultsItemView: NoResultsItemView = itemView as NoResultsItemView
    }

    private class TournamentViewHolder(
            listener: TournamentItemView.Listener,
            itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        internal val tournamentItemView: TournamentItemView = itemView as TournamentItemView

        init {
            tournamentItemView.listener = listener
        }
    }

}
