package com.garpr.android.features.tournament

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import com.garpr.android.R
import com.garpr.android.data.models.AbsTournament
import com.garpr.android.data.models.Match
import com.garpr.android.data.models.Region
import com.garpr.android.extensions.putOptionalExtra
import com.garpr.android.extensions.requireStringExtra
import com.garpr.android.extensions.verticalPositionInWindow
import com.garpr.android.features.common.activities.BaseActivity
import com.garpr.android.misc.Refreshable
import com.garpr.android.misc.Searchable
import com.garpr.android.repositories.RegionRepository
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_tournament.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class TournamentActivity : BaseActivity(), AppBarLayout.OnOffsetChangedListener, Refreshable,
        Searchable, SwipeRefreshLayout.OnRefreshListener, TournamentTabsView.OnTabClickListener,
        ViewPager.OnPageChangeListener {

    private lateinit var adapter: TournamentFragmentPagerAdapter
    private val tournamentId by lazy { intent.requireStringExtra(EXTRA_TOURNAMENT_ID) }

    protected val regionRepository: RegionRepository by inject()

    private val viewModel: TournamentViewModel by viewModel()

    companion object {
        private const val TAG = "TournamentActivity"
        private val CNAME = TournamentActivity::class.java.canonicalName
        private val EXTRA_TOURNAMENT_ID = "$CNAME.TournamentId"

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
        val datePositionInWindow = tournamentInfoView.dateVerticalPositionInWindow
        val toolbarPositionInWindow = toolbar.verticalPositionInWindow + toolbar.height

        if (datePositionInWindow <= toolbarPositionInWindow) {
            toolbar.fadeInTitleAndSubtitle()
        } else {
            toolbar.fadeOutTitleAndSubtitle()
        }
    }

    private fun fetchFullTournament() {
        viewModel.fetchTournament()
    }

    private fun initListeners() {
        viewModel.stateLiveData.observe(this, Observer {
            refreshState(it)
        })
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
        viewModel.initialize(regionRepository.getRegion(this), tournamentId)
        setContentView(R.layout.activity_tournament)
        initListeners()
        fetchFullTournament()
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        checkNameAndDateViewScrollStates()
    }

    override fun onRefresh() {
        refresh()
    }

    override fun onTabClick(v: TournamentTabsView) {
        if (viewPager.currentTab == tournamentTabsView.tournamentMode) {
            adapter.onNavigationItemReselected(tournamentTabsView.tournamentMode)
        } else {
            viewPager.currentTab = tournamentTabsView.tournamentMode
        }
    }

    override fun onPageScrollStateChanged(state: Int) {
        // intentionally empty
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        // intentionally empty
    }

    override fun onPageSelected(position: Int) {
        tournamentTabsView.tournamentMode = viewPager.currentTab
    }

    override fun onViewsBound() {
        super.onViewsBound()

        appBarLayout.addOnOffsetChangedListener(this)
        tournamentTabsView.onTabClickListener = this
        viewPager.pageMargin = resources.getDimensionPixelSize(R.dimen.root_padding)
        viewPager.addOnPageChangeListener(this)
        adapter = TournamentFragmentPagerAdapter(supportFragmentManager)
        viewPager.adapter = adapter
    }

    override fun refresh() {
        fetchFullTournament()
    }

    private fun refreshState(state: TournamentViewModel.State) {
        toolbar.titleText = state.titleText
        toolbar.subtitleText = state.subtitleText
        toolbar.showSearchIcon = state.showSearchIcon

        if (state.hasError) {
            tournamentInfoView.tournament = null
            tournamentInfoView.visibility = View.GONE
            tournamentTabsView.visibility = View.GONE
            viewPager.visibility = View.GONE
            error.visibility = View.VISIBLE
        } else {
            error.visibility = View.GONE
            tournamentInfoView.tournament = state.tournament
            tournamentInfoView.visibility = View.VISIBLE
            tournamentTabsView.visibility = View.VISIBLE
            viewPager.visibility = View.VISIBLE
        }

        refreshLayout.isRefreshing = state.isFetching
        refreshLayout.isEnabled = state.isRefreshEnabled
    }

    override fun search(query: String?) {
        viewModel.search(query)
    }

}
