package com.garpr.android.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v4.widget.SwipeRefreshLayout
import android.view.MenuItem
import android.view.View
import com.garpr.android.App
import com.garpr.android.R
import com.garpr.android.adapters.TournamentPagerAdapter
import com.garpr.android.extensions.closeKeyboard
import com.garpr.android.extensions.subtitle
import com.garpr.android.misc.RegionManager
import com.garpr.android.misc.SearchQueryHandle
import com.garpr.android.misc.Searchable
import com.garpr.android.misc.ShareUtils
import com.garpr.android.models.*
import com.garpr.android.networking.ApiCall
import com.garpr.android.networking.ApiListener
import com.garpr.android.networking.ServerApi
import com.garpr.android.views.ErrorContentLinearLayout
import com.garpr.android.views.toolbars.SearchToolbar
import com.garpr.android.views.toolbars.TournamentToolbar
import kotterknife.bindView
import javax.inject.Inject

class TournamentActivity : BaseActivity(), ApiListener<FullTournament>, Searchable,
        SearchQueryHandle, SearchToolbar.Listener, SwipeRefreshLayout.OnRefreshListener,
        TournamentToolbar.DataProvider {

    private var _fullTournament: FullTournament? = null
    private lateinit var tournamentId: String
    private lateinit var adapter: TournamentPagerAdapter

    @Inject
    protected lateinit var regionManager: RegionManager

    @Inject
    protected lateinit var serverApi: ServerApi

    @Inject
    protected lateinit var shareUtils: ShareUtils

    private val error: ErrorContentLinearLayout by bindView(R.id.error)
    private val refreshLayout: SwipeRefreshLayout by bindView(R.id.refreshLayout)
    private val tabLayout: TabLayout by bindView(R.id.tabLayout)
    private val tournamentToolbar: TournamentToolbar by bindView(R.id.toolbar)
    private val empty: View by bindView(R.id.empty)
    private val viewPager: ViewPager by bindView(R.id.viewPager)


    companion object {
        private const val TAG = "TournamentActivity"
        private val CNAME = TournamentActivity::class.java.canonicalName
        private val EXTRA_TOURNAMENT_DATE = CNAME + ".TournamentDate"
        private val EXTRA_TOURNAMENT_ID = CNAME + ".TournamentId"
        private val EXTRA_TOURNAMENT_NAME = CNAME + ".TournamentName"

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
        _fullTournament = null
        showError(errorCode)
    }

    private fun fetchFullTournament() {
        refreshLayout.isRefreshing = true
        serverApi.getTournament(regionManager.getRegion(this), tournamentId,
                ApiCall(this))
    }

    override val fullTournament: FullTournament?
        get() = _fullTournament

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.get().appComponent.inject(this)
        setContentView(R.layout.activity_tournament)

        tournamentId = intent.getStringExtra(EXTRA_TOURNAMENT_ID)

        prepareMenuAndTitles()
        fetchFullTournament()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.miShare -> {
                fullTournament?.let { shareUtils.shareTournament(this, it) }
                        ?: throw NullPointerException("fullTournament is null")
                true
            }

            R.id.miViewTournamentPage -> {
                fullTournament?.let { shareUtils.openUrl(this, it.url) }
                        ?: throw NullPointerException("fullTournament is null")
                true
            }

            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private val onPageChangeListener = object : ViewPager.SimpleOnPageChangeListener() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            closeKeyboard()
        }
    }

    override fun onRefresh() {
        fetchFullTournament()
    }

    private val onTabSelectedListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabReselected(tab: TabLayout.Tab?) {
            if (tab != null) {
                adapter.onTabReselected(tab.position)
            }
        }

        override fun onTabSelected(tab: TabLayout.Tab?) {
            // intentionally empty
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
            // intentionally empty
        }
    }

    override fun onViewsBound() {
        super.onViewsBound()

        refreshLayout.setOnRefreshListener(this)
        viewPager.pageMargin = resources.getDimensionPixelSize(R.dimen.root_padding)
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.addOnTabSelectedListener(onTabSelectedListener)
        viewPager.addOnPageChangeListener(onPageChangeListener)
    }

    private fun prepareMenuAndTitles() {
        if (title.isNullOrBlank()) {
            var title: String? = null
            val fullTournament = this.fullTournament

            if (fullTournament != null) {
                title = fullTournament.name
            } else if (intent.hasExtra(EXTRA_TOURNAMENT_NAME)) {
                title = intent.getStringExtra(EXTRA_TOURNAMENT_NAME)
            }

            if (title != null) {
                this.title = title
            }
        }

        if (subtitle.isNullOrBlank()) {
            var subtitle: SimpleDate? = null
            val fullTournament = this.fullTournament

            if (fullTournament != null) {
                subtitle = fullTournament.date
            } else if (intent.hasExtra(EXTRA_TOURNAMENT_DATE)) {
                subtitle = intent.getParcelableExtra(EXTRA_TOURNAMENT_DATE)
            }

            if (subtitle != null) {
                this.subtitle = subtitle.longForm
            }
        }

        invalidateOptionsMenu()
    }

    override fun search(query: String?) {
        adapter.search(query)
    }

    override val searchQuery: CharSequence?
        get() = tournamentToolbar.searchQuery

    private fun showEmpty() {
        error.visibility = View.GONE
        viewPager.visibility = View.GONE
        empty.visibility = View.VISIBLE
        prepareMenuAndTitles()
        refreshLayout.isRefreshing = false
    }

    private fun showError(errorCode: Int) {
        empty.visibility = View.GONE
        viewPager.visibility = View.GONE
        error.setVisibility(View.VISIBLE, errorCode)
        prepareMenuAndTitles()
        refreshLayout.isRefreshing = false
    }

    private fun showFullTournament() {
        val fullTournament = this.fullTournament ?: throw NullPointerException("fullTournament is null")
        adapter = TournamentPagerAdapter(this, fullTournament)
        viewPager.adapter = adapter

        empty.visibility = View.GONE
        error.visibility = View.GONE
        viewPager.visibility = View.VISIBLE
        prepareMenuAndTitles()
        refreshLayout.isRefreshing = false
        refreshLayout.isEnabled = false
    }

    override val showSearchMenuItem: Boolean
        get() = fullTournament != null

    override val showUpNavigation = true

    override fun success(`object`: FullTournament?) {
        _fullTournament = `object`

        if (`object`?.matches?.isNotEmpty() == true || `object`?.players?.isNotEmpty() == true) {
            showFullTournament()
        } else {
            showEmpty()
        }
    }

}
