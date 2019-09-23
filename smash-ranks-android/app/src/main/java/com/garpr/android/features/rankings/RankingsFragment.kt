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
import com.garpr.android.data.models.RankedPlayer
import com.garpr.android.extensions.layoutInflater
import com.garpr.android.features.common.fragments.BaseFragment
import com.garpr.android.misc.ListLayout
import com.garpr.android.misc.Refreshable
import com.garpr.android.misc.Searchable
import com.garpr.android.repositories.RegionRepository
import kotlinx.android.synthetic.main.fragment_rankings.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class RankingsFragment : BaseFragment(), ListLayout, RegionRepository.OnRegionChangeListener,
        Refreshable, Searchable, SwipeRefreshLayout.OnRefreshListener {

    private val adapter = Adapter()

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
        regionRepository.addListener(this)

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_rankings, container, false)
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
            showRankings(state.searchResults)
        } else {
            showRankings(state.rankingsBundle?.rankings)
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

    private fun showRankings(rankings: List<RankedPlayer>?) {
        adapter.set(rankings)
        empty.visibility = View.GONE
        error.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }

    private class Adapter : RecyclerView.Adapter<RankingViewHolder>() {

        private val list = mutableListOf<RankedPlayer>()

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

        override fun onBindViewHolder(holder: RankingViewHolder, position: Int) {
            holder.rankingItemView.setContent(list[position])
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankingViewHolder {
            val inflater = parent.layoutInflater
            return RankingViewHolder(inflater.inflate(R.layout.item_ranking, parent,
                    false))
        }

        internal fun set(list: List<RankedPlayer>?) {
            this.list.clear()

            if (!list.isNullOrEmpty()) {
                this.list.addAll(list)
            }

            notifyDataSetChanged()
        }

    }

    private class RankingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val rankingItemView: RankingItemView = itemView as RankingItemView
    }

}
