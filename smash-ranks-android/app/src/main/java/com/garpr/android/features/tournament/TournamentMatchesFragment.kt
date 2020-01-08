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
import com.garpr.android.features.common.fragments.BaseFragment
import com.garpr.android.features.headToHead.HeadToHeadActivity
import com.garpr.android.features.tournament.TournamentViewModel.MatchListItem
import com.garpr.android.misc.ListLayout
import com.garpr.android.repositories.RegionRepository
import kotlinx.android.synthetic.main.fragment_tournament_matches.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class TournamentMatchesFragment : BaseFragment(), ListLayout, TournamentMatchItemView.Listener {

    private val adapter = Adapter(this)

    private val viewModel: TournamentViewModel by sharedViewModel()

    protected val regionRepository: RegionRepository by inject()

    companion object {
        fun create(): TournamentMatchesFragment = TournamentMatchesFragment()
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
        recyclerView.addItemDecoration(DividerItemDecoration(requireContext(),
                DividerItemDecoration.VERTICAL))
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
    }

    override fun onClick(v: TournamentMatchItemView) {
        startActivity(HeadToHeadActivity.getLaunchIntent(
                context = requireContext(),
                match = v.match,
                region = regionRepository.getRegion(requireContext())
        ))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_tournament_matches, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initListeners()
    }

    private fun refreshState(state: TournamentViewModel.State) {
        if (state.showMatchesEmpty) {
            adapter.clear()
            recyclerView.visibility = View.GONE
            empty.visibility = View.VISIBLE
        } else {
            if (state.matchesSearchResults != null) {
                adapter.set(state.matchesSearchResults)
            } else {
                adapter.set(state.matches)
            }

            empty.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }

    private class Adapter(
            private val tournamentMatchItemViewListener: TournamentMatchItemView.Listener
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private val list = mutableListOf<MatchListItem>()

        companion object {
            private const val VIEW_TYPE_MATCH = 0
        }

        init {
            setHasStableIds(true)
        }

        private fun bindMatch(holder: TournamentMatchViewHolder, item: MatchListItem.Match) {
            holder.tournamentMatchItemView.setContent(
                    match = item.match,
                    winnerIsIdentity = item.winnerIsIdentity,
                    loserIsIdentity = item.loserIsIdentity
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
                is MatchListItem.Match -> VIEW_TYPE_MATCH
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            when (val item = list[position]) {
                is MatchListItem.Match -> bindMatch(holder as TournamentMatchViewHolder, item)
                else -> throw RuntimeException("unknown item: $item, position: $position")
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val inflater = parent.layoutInflater

            return when (viewType) {
                VIEW_TYPE_MATCH -> TournamentMatchViewHolder(tournamentMatchItemViewListener,
                        inflater.inflate(R.layout.item_tournament_match, parent, false))
                else -> throw IllegalArgumentException("unknown viewType: $viewType")
            }
        }

        internal fun set(list: List<MatchListItem>?) {
            this.list.clear()

            if (!list.isNullOrEmpty()) {
                this.list.addAll(list)
            }

            notifyDataSetChanged()
        }

    }

    private class TournamentMatchViewHolder(
            listener: TournamentMatchItemView.Listener,
            itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        internal val tournamentMatchItemView: TournamentMatchItemView = itemView as TournamentMatchItemView

        init {
            tournamentMatchItemView.listener = listener
        }
    }

}
