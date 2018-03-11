package com.garpr.android.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.View
import com.garpr.android.App
import com.garpr.android.R
import com.garpr.android.adapters.PlayersAdapter
import com.garpr.android.adapters.TournamentMatchesAdapter
import com.garpr.android.misc.Constants
import com.garpr.android.misc.RegionManager
import com.garpr.android.misc.SearchQueryHandle
import com.garpr.android.misc.Searchable
import com.garpr.android.models.*
import com.garpr.android.networking.ApiCall
import com.garpr.android.networking.ApiListener
import com.garpr.android.networking.ServerApi
import com.garpr.android.views.ErrorContentLinearLayout
import com.garpr.android.views.NoContentLinearLayout
import com.garpr.android.views.TournamentInfoView
import com.garpr.android.views.toolbars.SearchToolbar
import com.garpr.android.views.toolbars.TournamentToolbar
import kotterknife.bindView
import javax.inject.Inject

class TournamentActivity : BaseActivity(), ApiListener<FullTournament>, Searchable,
        SearchQueryHandle, SearchToolbar.Listener, SwipeRefreshLayout.OnRefreshListener {

    private var fullTournament: FullTournament? = null
    private lateinit var playersAdapter: PlayersAdapter
    private lateinit var tournamentId: String
    private lateinit var tournamentMatchesAdapter: TournamentMatchesAdapter

    @Inject
    protected lateinit var regionManager: RegionManager

    @Inject
    protected lateinit var serverApi: ServerApi

    private val error: ErrorContentLinearLayout by bindView(R.id.error)
    private val emptyMatches: NoContentLinearLayout by bindView(R.id.emptyMatches)
    private val emptyPlayers: NoContentLinearLayout by bindView(R.id.emptyPlayers)
    private val recyclerView: RecyclerView by bindView(R.id.recyclerView)
    private val refreshLayout: SwipeRefreshLayout by bindView(R.id.refreshLayout)
    private val tournamentInfoView: TournamentInfoView by bindView(R.id.tournamentInfoView)
    private val tournamentToolbar: TournamentToolbar by bindView(R.id.toolbar)


    companion object {
        private const val TAG = "TournamentActivity"
        private val CNAME = TournamentActivity::class.java.canonicalName
        private val EXTRA_TOURNAMENT_DATE = "$CNAME.TournamentDate"
        private val EXTRA_TOURNAMENT_ID = "$CNAME.TournamentId"
        private val EXTRA_TOURNAMENT_NAME = "$CNAME.TournamentName"

        fun getLaunchIntent(context: Context, tournament: AbsTournament,
                region: Region? = null): Intent {
            return getLaunchIntent(context, tournament.id, tournament.name, tournament.date, region)
        }

        fun getLaunchIntent(context: Context, match: Match, region: Region? = null): Intent {
            return getLaunchIntent(context, match.tournament.id, match.tournament.name,
                    match.tournament.date, region)
        }

        fun getLaunchIntent(context: Context, tournamentId: String, tournamentName: String?,
                tournamentDate: SimpleDate?, region: Region? = null): Intent {
            val intent = Intent(context, TournamentActivity::class.java)
                    .putExtra(EXTRA_TOURNAMENT_ID, tournamentId)

            if (tournamentName?.isNotBlank() == true) {
                intent.putExtra(EXTRA_TOURNAMENT_NAME, tournamentName)
            }

            if (tournamentDate != null) {
                intent.putExtra(EXTRA_TOURNAMENT_DATE, tournamentDate)
            }

            if (region != null) {
                intent.putExtra(EXTRA_REGION, region)
            }

            return intent
        }
    }

    override val activityName = TAG

    override fun failure(errorCode: Int) {
        fullTournament = null
        showError(errorCode)
    }

    private fun fetchFullTournament() {
        refreshLayout.isRefreshing = true
        serverApi.getTournament(regionManager.getRegion(this), tournamentId,
                ApiCall(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.get().appComponent.inject(this)
        setContentView(R.layout.activity_tournament)

        tournamentId = intent.getStringExtra(EXTRA_TOURNAMENT_ID)

        fetchFullTournament()
    }

    override fun onRefresh() {
        fetchFullTournament()
    }

    override fun onViewsBound() {
        super.onViewsBound()

        refreshLayout.setOnRefreshListener(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL))
        recyclerView.setHasFixedSize(true)
        ViewCompat.setNestedScrollingEnabled(recyclerView, true)

        playersAdapter = PlayersAdapter(this)
        tournamentMatchesAdapter = TournamentMatchesAdapter(this)
        recyclerView.adapter = tournamentMatchesAdapter
    }

    override fun search(query: String?) {
        // TODO
    }

    override val searchQuery: CharSequence?
        get() = tournamentToolbar.searchQuery

    private fun showError(errorCode: Int = Constants.ERROR_CODE_UNKNOWN) {
        tournamentInfoView.visibility = View.GONE
        recyclerView.visibility = View.GONE
        emptyMatches.visibility = View.GONE
        emptyPlayers.visibility = View.GONE
        error.setVisibility(View.VISIBLE, errorCode)
        invalidateOptionsMenu()
        refreshLayout.isRefreshing = false
    }

    private fun showFullTournament() {
        val tournament = fullTournament ?: throw NullPointerException("fullTournament is null")

        playersAdapter.set(tournament)
        tournamentMatchesAdapter.set(tournament)

        error.visibility = View.GONE

        tournamentInfoView.setContent(tournament)
        tournamentInfoView.visibility = View.VISIBLE

        if (recyclerView.adapter == playersAdapter) {
            if (playersAdapter.isEmpty) {
                recyclerView.visibility = View.GONE
                emptyPlayers.visibility = View.VISIBLE
            } else {
                emptyPlayers.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }
        } else if (recyclerView.adapter == tournamentMatchesAdapter) {
            if (tournamentMatchesAdapter.isEmpty) {
                recyclerView.visibility = View.GONE
                emptyMatches.visibility = View.VISIBLE
            } else {
                emptyMatches.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }
        } else {
            throw IllegalStateException("recyclerView has an unknown adapter: ${recyclerView.adapter}")
        }

        invalidateOptionsMenu()
        refreshLayout.isRefreshing = false
        refreshLayout.isEnabled = false
    }

    override val showSearchMenuItem: Boolean
        get() = fullTournament != null && recyclerView.visibility == View.VISIBLE &&
                recyclerView.adapter?.itemCount ?: 0 > 0

    override val showUpNavigation = true

    override fun success(`object`: FullTournament?) {
        fullTournament = `object`

        if (`object` == null) {
            showError()
        } else {
            showFullTournament()
        }
    }

}
