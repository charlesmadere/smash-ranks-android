package com.garpr.android.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.View
import com.garpr.android.App
import com.garpr.android.R
import com.garpr.android.adapters.TournamentAdapter
import com.garpr.android.misc.Constants
import com.garpr.android.misc.RegionManager
import com.garpr.android.misc.SearchQueryHandle
import com.garpr.android.misc.Searchable
import com.garpr.android.models.*
import com.garpr.android.networking.ApiCall
import com.garpr.android.networking.ApiListener
import com.garpr.android.networking.ServerApi
import com.garpr.android.views.ErrorContentLinearLayout
import com.garpr.android.views.TournamentInfoItemView
import com.garpr.android.views.TournamentTabsView
import com.garpr.android.views.toolbars.SearchToolbar
import com.garpr.android.views.toolbars.TournamentToolbar
import kotterknife.bindView
import javax.inject.Inject

class TournamentActivity : BaseActivity(), ApiListener<FullTournament>, Searchable,
        SearchQueryHandle, SearchToolbar.Listener, SwipeRefreshLayout.OnRefreshListener,
        TournamentTabsView.Listeners {

    private var fullTournament: FullTournament? = null
    private lateinit var tournamentId: String
    private lateinit var tournamentAdapter: TournamentAdapter
    private var _tournamentMode: TournamentMode = TournamentMode.MATCHES

    @Inject
    protected lateinit var regionManager: RegionManager

    @Inject
    protected lateinit var serverApi: ServerApi

    private val error: ErrorContentLinearLayout by bindView(R.id.error)
    private val recyclerView: RecyclerView by bindView(R.id.recyclerView)
    private val refreshLayout: SwipeRefreshLayout by bindView(R.id.refreshLayout)
    private val tournamentTabsView: TournamentTabsView by bindView(R.id.tournamentTabsView)
    private val tournamentToolbar: TournamentToolbar by bindView(R.id.toolbar)


    companion object {
        private const val TAG = "TournamentActivity"
        private val CNAME = TournamentActivity::class.java.canonicalName
        private val EXTRA_TOURNAMENT_DATE = "$CNAME.TournamentDate"
        private val EXTRA_TOURNAMENT_ID = "$CNAME.TournamentId"
        private val EXTRA_TOURNAMENT_NAME = "$CNAME.TournamentName"
        private const val KEY_TOURNAMENT_MODE = "TournamentMode"

        fun getLaunchIntent(context: Context, tournament: AbsTournament,
                region: Region? = null): Intent {
            return getLaunchIntent(context, tournament.id, tournament.name, tournament.date,
                    region)
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

    private fun checkTournamentTabViewScrollStates() {
        val view = recyclerView.getChildAt(0) as? TournamentInfoItemView

        if (view == null || view.tabsTop <= 0) {
            tournamentTabsView.animateIn()
        } else {
            tournamentTabsView.animateOut()
        }
    }

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
        _tournamentMode = savedInstanceState?.getParcelable(KEY_TOURNAMENT_MODE) ?: tournamentMode

        fetchFullTournament()
    }

    override fun onRefresh() {
        fetchFullTournament()
    }

    private val onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            checkTournamentTabViewScrollStates()
        }
    }

    override fun onTournamentModeClick(v: TournamentTabsView, tournamentMode: TournamentMode) {
        if (this.tournamentMode == tournamentMode) {
            return
        }

        _tournamentMode = tournamentMode

        val tournament = fullTournament ?: throw NullPointerException("fullTournament is null")
        tournamentAdapter.set(tournamentMode, tournament)
        tournamentTabsView.refresh()
    }

    override fun onViewsBound() {
        super.onViewsBound()

        refreshLayout.setOnRefreshListener(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL))
        recyclerView.setHasFixedSize(true)
        recyclerView.addOnScrollListener(onScrollListener)

        tournamentAdapter = TournamentAdapter(this)
        recyclerView.adapter = tournamentAdapter
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_TOURNAMENT_MODE, tournamentMode)
    }

    override fun search(query: String?) {
        // TODO
    }

    override val searchQuery: CharSequence?
        get() = tournamentToolbar.searchQuery

    private fun showError(errorCode: Int = Constants.ERROR_CODE_UNKNOWN) {
        recyclerView.visibility = View.GONE
        error.setVisibility(View.VISIBLE, errorCode)
        invalidateOptionsMenu()
        refreshLayout.isRefreshing = false
    }

    private fun showFullTournament() {
        val tournament = fullTournament ?: throw NullPointerException("fullTournament is null")
        tournamentAdapter.set(tournamentMode, tournament)

        error.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE

        invalidateOptionsMenu()

        refreshLayout.isRefreshing = false
        refreshLayout.isEnabled = false
    }

    override val showSearchMenuItem: Boolean
        get() = fullTournament?.matches?.isNotEmpty() == true ||
                fullTournament?.players?.isNotEmpty() == true

    override val showUpNavigation = true

    override fun success(`object`: FullTournament?) {
        fullTournament = `object`

        if (`object` == null) {
            showError()
        } else {
            showFullTournament()
        }
    }

    override val tournamentMode: TournamentMode
        get() = _tournamentMode

}
