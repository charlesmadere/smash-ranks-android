package com.garpr.android.features.headToHead

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.garpr.android.R
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.data.models.FullTournament
import com.garpr.android.data.models.Region
import com.garpr.android.data.models.TournamentMatch
import com.garpr.android.extensions.layoutInflater
import com.garpr.android.extensions.putOptionalExtra
import com.garpr.android.extensions.requireStringExtra
import com.garpr.android.features.common.activities.BaseActivity
import com.garpr.android.features.headToHead.HeadToHeadViewModel.ListItem
import com.garpr.android.features.tournament.TournamentActivity
import com.garpr.android.features.tournaments.TournamentDividerView
import com.garpr.android.misc.Refreshable
import com.garpr.android.repositories.RegionRepository
import kotlinx.android.synthetic.main.activity_head_to_head.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class HeadToHeadActivity : BaseActivity(), HeadToHeadMatchItemView.Listener, Refreshable,
        SwipeRefreshLayout.OnRefreshListener, TournamentDividerView.Listener {

    private val adapter = Adapter(this, this)
    private val opponentId by lazy { intent.requireStringExtra(EXTRA_OPPONENT_ID) }
    private val playerId by lazy { intent.requireStringExtra(EXTRA_PLAYER_ID) }

    private val viewModel: HeadToHeadViewModel by viewModel()

    protected val regionRepository: RegionRepository by inject()

    companion object {
        private const val TAG = "HeadToHeadActivity"
        private val CNAME = HeadToHeadActivity::class.java.canonicalName
        private val EXTRA_OPPONENT_ID = "$CNAME.OpponentId"
        private val EXTRA_PLAYER_ID = "$CNAME.PlayerId"

        fun getLaunchIntent(context: Context, player: AbsPlayer, opponent: AbsPlayer,
                region: Region? = null): Intent {
            var regionCopy = region

            if (player is FavoritePlayer) {
                regionCopy = player.region
            }

            return getLaunchIntent(context, player.id, opponent.id, regionCopy)
        }

        fun getLaunchIntent(context: Context, player: AbsPlayer, match: TournamentMatch,
                region: Region? = null): Intent {
            var regionCopy = region

            if (player is FavoritePlayer) {
                regionCopy = player.region
            }

            return getLaunchIntent(context, player.id, match.opponent.id, regionCopy)
        }

        fun getLaunchIntent(context: Context, match: FullTournament.Match,
                region: Region? = null): Intent {
            return getLaunchIntent(context, match.winnerId, match.loserId, region)
        }

        fun getLaunchIntent(context: Context, playerId: String, opponentId: String,
                region: Region? = null): Intent {
            return Intent(context, HeadToHeadActivity::class.java)
                    .putExtra(EXTRA_PLAYER_ID, playerId)
                    .putExtra(EXTRA_OPPONENT_ID, opponentId)
                    .putOptionalExtra(EXTRA_REGION, region)
        }
    }

    override val activityName = TAG

    private fun fetchHeadToHead() {
        viewModel.fetchHeadToHead(regionRepository.getRegion(this), playerId, opponentId)
    }

    private fun initListeners() {
        viewModel.stateLiveData.observe(this, Observer {
            refreshState(it)
        })
    }

    override fun onClick(v: HeadToHeadMatchItemView) {
        val dialog = HeadToHeadDialogFragment.create(v.headToHeadMatch)
        dialog.show(supportFragmentManager, HeadToHeadDialogFragment.TAG)
    }

    override fun onClick(v: TournamentDividerView) {
        startActivity(TournamentActivity.getLaunchIntent(this, v.tournament,
                regionRepository.getRegion(this)))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_head_to_head)
        initListeners()
        fetchHeadToHead()
    }

    override fun onRefresh() {
        refresh()
    }

    override fun onViewsBound() {
        super.onViewsBound()

        refreshLayout.setOnRefreshListener(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
    }

    override fun refresh() {
        fetchHeadToHead()
    }

    private fun refreshState(state: HeadToHeadViewModel.State) {
        if (state.hasError) {
            adapter.clear()
            recyclerView.visibility = View.GONE
            error.visibility = View.VISIBLE
        } else {
            adapter.set(state.list)
            error.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }

        refreshLayout.isRefreshing = state.isFetching
    }

    private class Adapter(
            private val headToHeadMatchItemViewListener: HeadToHeadMatchItemView.Listener,
            private val tournamentDividerItemViewListener: TournamentDividerView.Listener
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private val list = mutableListOf<ListItem>()

        companion object {
            private const val VIEW_TYPE_MATCH = 0
            private const val VIEW_TYPE_NO_MATCHES = 1
            private const val VIEW_TYPE_TOURNAMENT = 2
            private const val VIEW_TYPE_WINS_LOSSES = 3
        }

        init {
            setHasStableIds(true)
        }

        private fun bindMatchViewHolder(holder: HeadToHeadMatchViewHolder, item: ListItem.Match) {
            holder.headToHeadMatchItemView.setContent(
                    match = item.match,
                    playerIsIdentity = item.playerIsIdentity,
                    opponentIsIdentity = item.opponentIsIdentity
            )
        }

        private fun bindTournamentViewHolder(holder: TournamentViewHolder,
                item: ListItem.Tournament) {
            holder.tournamentDividerView.setContent(item.tournament)
        }

        private fun bindWinsLossesViewHolder(holder: WinsLossesViewHolder,
                item: ListItem.WinsLosses) {
            holder.winsLossesView.setContent(item.winsLosses)
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
                is ListItem.Match -> VIEW_TYPE_MATCH
                is ListItem.NoMatches -> VIEW_TYPE_NO_MATCHES
                is ListItem.Tournament -> VIEW_TYPE_TOURNAMENT
                is ListItem.WinsLosses -> VIEW_TYPE_WINS_LOSSES
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            when (val item = list[position]) {
                is ListItem.Match -> bindMatchViewHolder(holder as HeadToHeadMatchViewHolder, item)
                is ListItem.NoMatches -> { /* intentionally empty */ }
                is ListItem.Tournament -> bindTournamentViewHolder(holder as TournamentViewHolder, item)
                is ListItem.WinsLosses -> bindWinsLossesViewHolder(holder as WinsLossesViewHolder, item)
                else -> throw RuntimeException("unknown item: $item, position: $position")
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val inflater = parent.layoutInflater

            return when (viewType) {
                VIEW_TYPE_MATCH -> HeadToHeadMatchViewHolder(headToHeadMatchItemViewListener,
                        inflater.inflate(R.layout.item_head_to_head_match, parent, false))
                VIEW_TYPE_NO_MATCHES -> NoMatchesViewHolder(
                        parent.resources.getString(R.string.no_matches),
                        inflater.inflate(R.layout.item_string, parent, false))
                VIEW_TYPE_TOURNAMENT -> TournamentViewHolder(tournamentDividerItemViewListener,
                        inflater.inflate(R.layout.divider_tournament, parent, false))
                VIEW_TYPE_WINS_LOSSES -> WinsLossesViewHolder(inflater.inflate(
                        R.layout.item_wins_losses, parent, false))
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

    private class HeadToHeadMatchViewHolder(
            listener: HeadToHeadMatchItemView.Listener,
            itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        internal val headToHeadMatchItemView: HeadToHeadMatchItemView = itemView as HeadToHeadMatchItemView

        init {
            headToHeadMatchItemView.listener = listener
        }
    }

    private class NoMatchesViewHolder(
            text: CharSequence,
            itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        init {
            (itemView as TextView).text = text
        }
    }

    private class TournamentViewHolder(
            listener: TournamentDividerView.Listener,
            itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        internal val tournamentDividerView: TournamentDividerView = itemView as TournamentDividerView

        init {
            tournamentDividerView.listener = listener
        }
    }

    private class WinsLossesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val winsLossesView: WinsLossesView = itemView as WinsLossesView
    }

}
