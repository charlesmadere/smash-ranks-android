package com.garpr.android.features.tournament

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.garpr.android.R
import com.garpr.android.data.models.AbsTournament
import com.garpr.android.data.models.Region
import com.garpr.android.data.models.TournamentMatch
import com.garpr.android.data.models.TournamentMode
import com.garpr.android.extensions.putOptionalExtra
import com.garpr.android.extensions.requireStringExtra
import com.garpr.android.extensions.verticalPositionInWindow
import com.garpr.android.features.common.activities.BaseActivity
import com.garpr.android.misc.Refreshable
import com.garpr.android.misc.RegionHandleUtils
import com.garpr.android.misc.Searchable
import com.garpr.android.misc.ShareUtils
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_tournament.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class TournamentActivity : BaseActivity(), AppBarLayout.OnOffsetChangedListener, Refreshable,
        Searchable, SwipeRefreshLayout.OnRefreshListener, TournamentInfoView.Listeners,
        TournamentTabsView.OnTabClickListener {

    private lateinit var adapter: TournamentFragmentPagerAdapter
    private val tournamentId by lazy { intent.requireStringExtra(EXTRA_TOURNAMENT_ID) }

    protected val regionHandleUtils: RegionHandleUtils by inject()
    protected val shareUtils: ShareUtils by inject()

    private val viewModel: TournamentViewModel by viewModel()

    private var currentPage: TournamentMode
        get() = TournamentMode.values()[viewPager.currentItem]
        set(value) {
            viewPager.currentItem = value.ordinal
        }

    private val onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            tournamentTabsView.tournamentMode = currentPage
        }
    }

    override val activityName = TAG

    private fun checkNameAndDateViewScrollStates() {
        val datePositionInWindow = tournamentInfoView.dateVerticalPositionInWindow
        val toolbarPositionInWindow = toolbar.verticalPositionInWindow + toolbar.height

        if (!tournamentInfoView.isShown) {
            return
        }

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
        viewModel.initialize(regionHandleUtils.getRegion(this), tournamentId)
        setContentView(R.layout.activity_tournament)
        initListeners()
        fetchFullTournament()
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        checkNameAndDateViewScrollStates()
    }

    override fun onOpenLinkClick(v: TournamentInfoView) {
        shareUtils.openUrl(this, v.tournament.url)
    }

    override fun onRefresh() {
        refresh()
    }

    override fun onShareClick(v: TournamentInfoView) {
        shareUtils.shareTournament(this, v.tournament)
    }

    override fun onTabClick(v: TournamentTabsView) {
        if (currentPage == tournamentTabsView.tournamentMode) {
            adapter.onNavigationItemReselected(tournamentTabsView.tournamentMode)
        } else {
            currentPage = tournamentTabsView.tournamentMode
        }
    }

    override fun onViewsBound() {
        super.onViewsBound()

        toolbar.searchable = this
        appBarLayout.addOnOffsetChangedListener(this)
        tournamentInfoView.listeners = this
        tournamentTabsView.onTabClickListener = this
        viewPager.setPageTransformer(MarginPageTransformer(resources.getDimensionPixelSize(R.dimen.root_padding)))
        viewPager.registerOnPageChangeCallback(onPageChangeCallback)
        adapter = TournamentFragmentPagerAdapter(this)
        viewPager.adapter = adapter
    }

    override fun refresh() {
        fetchFullTournament()
    }

    private fun refreshState(state: TournamentViewModel.State) {
        toolbar.titleText = state.titleText
        toolbar.subtitleText = state.subtitleText
        toolbar.showSearchIcon = if (toolbar.isSearchFieldExpanded) false else state.showSearchIcon

        if (state.hasError) {
            tournamentInfoView.visibility = View.GONE
            tournamentTabsView.visibility = View.GONE
            viewPager.visibility = View.GONE
            error.visibility = View.VISIBLE
        } else if (state.tournament != null) {
            error.visibility = View.GONE

            tournamentInfoView.setContent(
                    tournament = state.tournament,
                    region = regionHandleUtils.getRegion(this)
            )

            tournamentInfoView.visibility = View.VISIBLE
            tournamentTabsView.visibility = View.VISIBLE
            viewPager.visibility = View.VISIBLE
        }

        if (state.isFetching) {
            refreshLayout.isEnabled = true
            refreshLayout.isRefreshing = true
        } else {
            refreshLayout.isRefreshing = false
            refreshLayout.isEnabled = state.isRefreshEnabled
        }
    }

    override fun search(query: String?) {
        viewModel.search(query)
    }

    companion object {
        private const val TAG = "TournamentActivity"
        private val CNAME = TournamentActivity::class.java.canonicalName
        private val EXTRA_TOURNAMENT_ID = "$CNAME.TournamentId"

        fun getLaunchIntent(context: Context, tournament: AbsTournament,
                region: Region? = null): Intent {
            return getLaunchIntent(context, tournament.id, region)
        }

        fun getLaunchIntent(context: Context, match: TournamentMatch, region: Region? = null): Intent {
            return getLaunchIntent(context, match.tournament.id, region)
        }

        fun getLaunchIntent(context: Context, tournamentId: String, region: Region? = null): Intent {
            return Intent(context, TournamentActivity::class.java)
                    .putExtra(EXTRA_TOURNAMENT_ID, tournamentId)
                    .putOptionalExtra(EXTRA_REGION, region)
        }
    }

}
