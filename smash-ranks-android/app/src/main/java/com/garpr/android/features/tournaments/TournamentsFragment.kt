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
import com.garpr.android.data.models.AbsTournament
import com.garpr.android.extensions.layoutInflater
import com.garpr.android.features.common.fragments.BaseFragment
import com.garpr.android.misc.ListLayout
import com.garpr.android.misc.Refreshable
import com.garpr.android.repositories.RegionRepository
import kotlinx.android.synthetic.main.fragment_tournaments.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class TournamentsFragment : BaseFragment(), ListLayout, RegionRepository.OnRegionChangeListener,
        Refreshable, SwipeRefreshLayout.OnRefreshListener {

    private val adapter = Adapter()

    private val viewModel: TournamentsViewModel by sharedViewModel()

    protected val regionRepository: RegionRepository by inject()

    companion object {
        fun create() = TournamentsFragment()
    }

    private fun fetchTournamentsBundle() {
        viewModel.fetchTournaments(regionRepository.getRegion(requireContext()))
    }

    override fun getRecyclerView(): RecyclerView? {
        return recyclerView
    }

    private fun initListeners() {
        regionRepository.addListener(this)

        viewModel.stateLiveData.observe(viewLifecycleOwner, Observer {
            refreshState(it)
        })
    }

    private fun initViews() {
        refreshLayout.setOnRefreshListener(this)
        recyclerView.addItemDecoration(DividerItemDecoration(context,
                DividerItemDecoration.VERTICAL))
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_tournaments, container, false)
    }

    override fun onDestroyView() {
        regionRepository.removeListener(this)
        super.onDestroyView()
    }

    override fun onRefresh() {
        refresh()
    }

    override fun onRegionChange(regionRepository: RegionRepository) {
        if (isAlive) {
            refresh()
        }
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
            showTournaments(state.searchResults)
        } else {
            showTournaments(state.tournamentsBundle?.tournaments)
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

    private fun showTournaments(tournaments: List<AbsTournament>?) {
        adapter.set(tournaments)
        empty.visibility = View.GONE
        error.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }

    private class Adapter : RecyclerView.Adapter<TournamentViewHolder>() {

        private val list = mutableListOf<AbsTournament>()

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

        override fun onBindViewHolder(holder: TournamentViewHolder, position: Int) {
            holder.tournamentItemView.setContent(list[position])
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TournamentViewHolder {
            val inflater = parent.layoutInflater
            return TournamentViewHolder(inflater.inflate(R.layout.item_tournament, parent,
                    false))
        }

        internal fun set(list: List<AbsTournament>?) {
            this.list.clear()

            if (!list.isNullOrEmpty()) {
                this.list.addAll(list)
            }

            notifyDataSetChanged()
        }

    }

    private class TournamentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val tournamentItemView: TournamentItemView = itemView as TournamentItemView
    }

}
