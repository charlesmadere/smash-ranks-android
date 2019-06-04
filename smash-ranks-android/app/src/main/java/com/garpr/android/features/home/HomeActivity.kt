package com.garpr.android.features.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.viewpager.widget.ViewPager
import com.garpr.android.R
import com.garpr.android.data.models.RankingCriteria
import com.garpr.android.extensions.appComponent
import com.garpr.android.extensions.itemIdAsHomeTab
import com.garpr.android.extensions.putOptionalExtra
import com.garpr.android.features.common.activities.BaseActivity
import com.garpr.android.features.common.views.SearchToolbar
import com.garpr.android.features.player.PlayerActivity
import com.garpr.android.features.players.PlayersActivity
import com.garpr.android.features.rankings.RankingsLayout
import com.garpr.android.features.settings.SettingsActivity
import com.garpr.android.features.sync.rankings.RankingsPollingManager
import com.garpr.android.features.sync.roster.SmashRosterSyncManager
import com.garpr.android.misc.HomeTab
import com.garpr.android.misc.RankingCriteriaHandle
import com.garpr.android.misc.SearchQueryHandle
import com.garpr.android.misc.Searchable
import com.garpr.android.repositories.IdentityRepository
import com.garpr.android.repositories.RegionRepository
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_home.*
import javax.inject.Inject

class HomeActivity : BaseActivity(), BottomNavigationView.OnNavigationItemReselectedListener,
        BottomNavigationView.OnNavigationItemSelectedListener, HomeToolbar.Listeners,
        RankingCriteriaHandle, RankingsLayout.Listener, RegionRepository.OnRegionChangeListener,
        Searchable, SearchQueryHandle, SearchToolbar.Listener {

    private val adapter = HomePagerAdapter()

    @Inject
    protected lateinit var identityRepository: IdentityRepository

    @Inject
    protected lateinit var rankingsPollingManager: RankingsPollingManager

    @Inject
    protected lateinit var regionRepository: RegionRepository

    @Inject
    protected lateinit var smashRosterSyncManager: SmashRosterSyncManager


    companion object {
        const val TAG = "HomeActivity"
        private val CNAME = HomeActivity::class.java.canonicalName
        private val EXTRA_INITIAL_POSITION = "$CNAME.InitialPosition"
        private const val KEY_CURRENT_POSITION = "CurrentPosition"

        fun getLaunchIntent(context: Context, initialPosition: HomeTab? = null,
                restartActivityTask: Boolean = false): Intent {
            var intent = Intent(context, HomeActivity::class.java)

            if (restartActivityTask) {
                intent = Intent.makeRestartActivityTask(intent.component)
            }

            return intent.putOptionalExtra(EXTRA_INITIAL_POSITION, initialPosition)
        }
    }

    override val activityName = TAG

    override fun onActivityRequirementsClick(v: HomeToolbar) {
        val rankingCriteria = this.rankingCriteria ?: return
        val regionDisplayName = regionRepository.getRegion(this).displayName
        val dialog = ActivityRequirementsDialogFragment.create(regionDisplayName, rankingCriteria)
        dialog.show(supportFragmentManager, ActivityRequirementsDialogFragment.TAG)
    }

    override fun onBackPressed() {
        if (toolbar.isSearchFieldExpanded) {
            toolbar.closeSearchField()
            return
        }

        if (viewPager.currentTab != HomeTab.RANKINGS) {
            viewPager.currentTab = HomeTab.RANKINGS
            return
        }

        super.onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        setContentView(R.layout.activity_home)
        setInitialPosition(savedInstanceState)
        rankingsPollingManager.enableOrDisable()
        smashRosterSyncManager.enableOrDisable()
        regionRepository.addListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        regionRepository.removeListener(this)
    }

    override fun onNavigationItemReselected(item: MenuItem) {
        adapter.onNavigationItemReselected(item.itemIdAsHomeTab)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        viewPager.currentTab = item.itemIdAsHomeTab
        return true
    }

    private val onPageChangeListener = object : ViewPager.SimpleOnPageChangeListener() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            updateSelectedBottomNavigationItem()
        }
    }

    override fun onRankingsBundleFetched(layout: RankingsLayout) {
        prepareMenuAndTitleAndSubtitle(layout)
    }

    override fun onRegionChange(regionRepository: RegionRepository) {
        prepareMenuAndTitleAndSubtitle(null)
        adapter.refresh()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_CURRENT_POSITION, viewPager.currentTab)
    }

    override fun onSettingsClick(v: HomeToolbar) {
        startActivity(SettingsActivity.getLaunchIntent(this))
    }

    override fun onShareClick(v: HomeToolbar) {
        val dialog = ShareRegionDialogFragment.create()
        dialog.show(supportFragmentManager, ShareRegionDialogFragment.TAG)
    }

    override fun onViewAllPlayersClick(v: HomeToolbar) {
        startActivity(PlayersActivity.getLaunchIntent(this))
    }

    override fun onViewsBound() {
        super.onViewsBound()

        bottomNavigationView.setOnNavigationItemReselectedListener(this)
        bottomNavigationView.setOnNavigationItemSelectedListener(this)
        viewPager.addOnPageChangeListener(onPageChangeListener)
        viewPager.pageMargin = resources.getDimensionPixelSize(R.dimen.root_padding)
        viewPager.offscreenPageLimit = 3
        viewPager.adapter = adapter
    }

    override fun onViewYourselfClick(v: HomeToolbar) {
        val identity = identityRepository.identity ?: throw NullPointerException("identity is null")
        startActivity(PlayerActivity.getLaunchIntent(this, identity,
                regionRepository.getRegion(this)))
    }

    private fun prepareMenuAndTitleAndSubtitle(layout: RankingsLayout?) {
        val region = regionRepository.getRegion(this)
        toolbar.titleText = region.displayName

        toolbar.subtitleText = layout?.rankingsBundle?.let {
            getString(R.string.updated_x, it.time.shortForm)
        } ?: getString(region.endpoint.title)

        toolbar.refresh()
    }

    override val rankingCriteria: RankingCriteria?
        get() = adapter.rankingCriteria

    override fun search(query: String?) {
        adapter.search(query)
    }

    override val searchQuery: CharSequence?
        get() = toolbar.searchQuery

    private fun setInitialPosition(savedInstanceState: Bundle?) {
        var initialPosition: HomeTab? = null

        if (savedInstanceState != null && !savedInstanceState.isEmpty) {
            initialPosition = savedInstanceState.getParcelable(KEY_CURRENT_POSITION)
        }

        if (initialPosition == null) {
            initialPosition = intent?.getParcelableExtra(EXTRA_INITIAL_POSITION)
        }

        if (initialPosition != null) {
            viewPager.currentTab = initialPosition
        }
    }

    override val showSearchIcon: Boolean
        get() = toolbar.hasSubtitleText

    private fun updateSelectedBottomNavigationItem() {
        val itemId = when (viewPager.currentTab) {
            HomeTab.RANKINGS -> R.id.actionRankings
            HomeTab.TOURNAMENTS -> R.id.actionTournaments
            HomeTab.FAVORITE_PLAYERS -> R.id.actionFavoritePlayers
        }

        bottomNavigationView.menu.findItem(itemId).isChecked = true
    }

}
