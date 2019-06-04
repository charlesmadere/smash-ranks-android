package com.garpr.android.features.tournament

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.garpr.android.R
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.AbsTournament
import com.garpr.android.data.models.FullTournament
import com.garpr.android.data.models.Match
import com.garpr.android.data.models.Region
import com.garpr.android.data.models.TournamentMode
import com.garpr.android.extensions.appComponent
import com.garpr.android.extensions.findTournamentInfoItemViewChild
import com.garpr.android.extensions.putOptionalExtra
import com.garpr.android.extensions.requireStringExtra
import com.garpr.android.extensions.smoothScrollToTop
import com.garpr.android.extensions.verticalPositionInWindow
import com.garpr.android.features.common.activities.BaseActivity
import com.garpr.android.features.common.views.SearchToolbar
import com.garpr.android.misc.Constants
import com.garpr.android.misc.ListUtils
import com.garpr.android.misc.SearchQueryHandle
import com.garpr.android.misc.Searchable
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.networking.ApiCall
import com.garpr.android.networking.ApiListener
import com.garpr.android.networking.ServerApi
import com.garpr.android.repositories.RegionRepository
import kotlinx.android.synthetic.main.activity_tournament.*
import javax.inject.Inject

class TournamentActivity : BaseActivity(), ApiListener<FullTournament>, Searchable,
        SearchQueryHandle, SearchToolbar.Listener, SwipeRefreshLayout.OnRefreshListener,
        TournamentTabsView.Listeners {

    private var fullTournament: FullTournament? = null
    private lateinit var adapter: TournamentAdapter
    private var _tournamentMode: TournamentMode = TournamentMode.MATCHES

    private val tournamentId: String by lazy { intent.requireStringExtra(EXTRA_TOURNAMENT_ID) }

    @Inject
    protected lateinit var regionRepository: RegionRepository

    @Inject
    protected lateinit var serverApi: ServerApi

    @Inject
    protected lateinit var threadUtils: ThreadUtils

    @Inject
    protected lateinit var tournamentAdapterManager: TournamentAdapterManager


    companion object {
        private const val TAG = "TournamentActivity"
        private val CNAME = TournamentActivity::class.java.canonicalName
        private val EXTRA_TOURNAMENT_ID = "$CNAME.TournamentId"
        private const val KEY_TOURNAMENT_MODE = "TournamentMode"

        fun getLaunchIntent(context: Context, tournament: AbsTournament,
                region: Region? = null): Intent {
            return getLaunchIntent(context, tournament.id, region)
        }

        fun getLaunchIntent(context: Context, match: Match, region: Region? = null): Intent {
            return getLaunchIntent(context, match.tournament.id, region)
        }

        fun getLaunchIntent(context: Context, tournamentId: String, region: Region? = null): Intent {
            return Intent(context, TournamentActivity::class.java)
                    .putExtra(EXTRA_TOURNAMENT_ID, tournamentId)
                    .putOptionalExtra(EXTRA_REGION, region)
        }
    }

    override val activityName = TAG

    private fun checkNameAndDateViewScrollStates() {
        val view = recyclerView.findTournamentInfoItemViewChild()

        if (view == null) {
            toolbar.fadeInTitleAndSubtitle()
            return
        }

        val dateVerticalPositionInWindow = view.dateVerticalPositionInWindow
        val toolbarVerticalPositionInWindow = toolbar.verticalPositionInWindow + toolbar.height

        if (dateVerticalPositionInWindow <= toolbarVerticalPositionInWindow) {
            toolbar.fadeInTitleAndSubtitle()
        } else {
            toolbar.fadeOutTitleAndSubtitle()
        }
    }

    private fun checkTournamentTabViewsScrollStates() {
        val view = recyclerView.findTournamentInfoItemViewChild()

        if (view == null) {
            toolbarTournamentTabsView.animateIn()
            return
        }

        val tabsVerticalPositionInWindow = view.tabsVerticalPositionInWindow
        val toolbarVerticalPositionInWindow = toolbar.verticalPositionInWindow + toolbar.height

        if (tabsVerticalPositionInWindow <= toolbarVerticalPositionInWindow) {
            toolbarTournamentTabsView.animateIn()
        } else {
            toolbarTournamentTabsView.animateOut()
        }
    }

    override fun failure(errorCode: Int) {
        fullTournament = null
        showError(errorCode)
    }

    private fun fetchFullTournament() {
        refreshLayout.isRefreshing = true
        serverApi.getTournament(regionRepository.getRegion(this), tournamentId,
                ApiCall(this))
    }

    override fun onBackPressed() {
        if (toolbar.isSearchFieldExpanded) {
            toolbar.closeSearchField()
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        setContentView(R.layout.activity_tournament)

        _tournamentMode = savedInstanceState?.getParcelable(KEY_TOURNAMENT_MODE) ?: tournamentMode

        setTitleAndSubtitle()
        fetchFullTournament()
    }

    override fun onRefresh() {
        fetchFullTournament()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_TOURNAMENT_MODE, tournamentMode)
    }

    private val onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            checkNameAndDateViewScrollStates()
            checkTournamentTabViewsScrollStates()
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            checkNameAndDateViewScrollStates()
            checkTournamentTabViewsScrollStates()
        }
    }

    override fun onTournamentModeClick(v: TournamentTabsView, tournamentMode: TournamentMode) {
        if (this.tournamentMode == tournamentMode) {
            recyclerView.smoothScrollToTop()
            return
        }

        _tournamentMode = tournamentMode
        toolbar.refresh()

        val tournament = fullTournament ?: throw NullPointerException("fullTournament is null")
        adapter.set(tournamentMode, tournament)
        toolbarTournamentTabsView.refresh()
        toolbar.closeSearchField()
    }

    override fun onViewsBound() {
        super.onViewsBound()

        refreshLayout.setOnRefreshListener(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL))
        recyclerView.setHasFixedSize(true)
        recyclerView.addOnScrollListener(onScrollListener)

        adapter = TournamentAdapter(tournamentAdapterManager)
        recyclerView.adapter = adapter
    }

    override fun search(query: String?) {
        when (tournamentMode) {
            TournamentMode.MATCHES -> {
                searchTournamentMatches(query, fullTournament?.matches)
            }

            TournamentMode.PLAYERS -> {
                searchTournamentPlayers(query, fullTournament?.players)
            }
        }
    }

    override val searchQuery: CharSequence?
        get() = toolbar.searchQuery

    private fun searchTournamentMatches(query: String?, matches: List<FullTournament.Match>?) {
        val tournament = fullTournament

        if (tournament == null || matches.isNullOrEmpty()) {
            return
        }

        threadUtils.run(object : ThreadUtils.Task {
            private var list: List<FullTournament.Match>? = null

            override fun onBackground() {
                if (isAlive && TextUtils.equals(query, searchQuery) &&
                        tournamentMode == TournamentMode.MATCHES) {
                    list = ListUtils.searchTournamentMatchesList(query, matches)
                }
            }

            override fun onUi() {
                if (isAlive && TextUtils.equals(query, searchQuery) &&
                        tournamentMode == TournamentMode.MATCHES) {
                    adapter.setSearchedMatchesList(tournament, list)
                }
            }
        })
    }

    private fun searchTournamentPlayers(query: String?, players: List<AbsPlayer>?) {
        val tournament = fullTournament

        if (tournament == null || players.isNullOrEmpty()) {
            return
        }

        threadUtils.run(object : ThreadUtils.Task {
            private var list: List<AbsPlayer>? = null

            override fun onBackground() {
                if (isAlive && TextUtils.equals(query, searchQuery) &&
                        tournamentMode == TournamentMode.PLAYERS) {
                    list = ListUtils.searchPlayerList(query, players)
                }
            }

            override fun onUi() {
                if (isAlive && TextUtils.equals(query, searchQuery) &&
                        tournamentMode == TournamentMode.PLAYERS) {
                    adapter.setSearchedPlayersList(tournament, list)
                }
            }
        })
    }

    private fun setTitleAndSubtitle() {
        if (toolbar.hasTitleText && toolbar.hasSubtitleText) {
            return
        }

        val tournament = fullTournament ?: return
        toolbar.titleText = tournament.name
        toolbar.subtitleText = regionRepository.getRegion(this).displayName
    }

    private fun showError(errorCode: Int = Constants.ERROR_CODE_UNKNOWN) {
        recyclerView.visibility = View.GONE
        error.setVisibility(View.VISIBLE, errorCode)
        toolbar.refresh()
        refreshLayout.isRefreshing = false
    }

    private fun showFullTournament() {
        val tournament = fullTournament ?: throw NullPointerException("fullTournament is null")
        adapter.set(tournamentMode, tournament)

        error.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE

        setTitleAndSubtitle()
        toolbar.refresh()

        refreshLayout.isRefreshing = false
        refreshLayout.isEnabled = false
    }

    override val showSearchIcon: Boolean
        get() = when (tournamentMode) {
                    TournamentMode.MATCHES -> {
                        fullTournament?.matches?.isNotEmpty() == true
                    }

                    TournamentMode.PLAYERS -> {
                        fullTournament?.players?.isNotEmpty() == true
                    }
                }

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
