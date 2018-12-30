package com.garpr.android.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.viewpager.widget.ViewPager
import com.garpr.android.R
import com.garpr.android.adapters.HomePagerAdapter
import com.garpr.android.dialogs.ActivityRequirementsDialogFragment
import com.garpr.android.dialogs.ShareRegionDialogFragment
import com.garpr.android.extensions.appComponent
import com.garpr.android.extensions.currentItemAsHomeTab
import com.garpr.android.extensions.itemIdAsHomeTab
import com.garpr.android.extensions.putOptionalExtra
import com.garpr.android.managers.IdentityManager
import com.garpr.android.managers.RegionManager
import com.garpr.android.misc.HomeTab
import com.garpr.android.misc.RankingCriteriaHandle
import com.garpr.android.misc.SearchQueryHandle
import com.garpr.android.misc.Searchable
import com.garpr.android.misc.ShareUtils
import com.garpr.android.models.RankingCriteria
import com.garpr.android.sync.rankings.RankingsPollingManager
import com.garpr.android.sync.roster.SmashRosterSyncManager
import com.garpr.android.views.RankingsLayout
import com.garpr.android.views.toolbars.HomeToolbar
import com.garpr.android.views.toolbars.SearchToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_home.*
import javax.inject.Inject

class HomeActivity : BaseActivity(), BottomNavigationView.OnNavigationItemReselectedListener,
        BottomNavigationView.OnNavigationItemSelectedListener, HomeToolbar.Listeners,
        RankingCriteriaHandle, RankingsLayout.Listener, RegionManager.OnRegionChangeListener,
        Searchable, SearchQueryHandle, SearchToolbar.Listener {

    private var adapter: HomePagerAdapter? = null

    @Inject
    protected lateinit var identityManager: IdentityManager

    @Inject
    protected lateinit var rankingsPollingManager: RankingsPollingManager

    @Inject
    protected lateinit var regionManager: RegionManager

    @Inject
    protected lateinit var shareUtils: ShareUtils

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
                    .putOptionalExtra(EXTRA_INITIAL_POSITION, initialPosition)

            if (restartActivityTask) {
                intent = Intent.makeRestartActivityTask(intent.component)
            }

            return intent
        }
    }

    override val activityName = TAG

    override fun onActivityRequirementsClick(v: HomeToolbar) {
        val rankingCriteria = this.rankingCriteria ?: return
        val regionDisplayName = regionManager.getRegion(this).displayName
        val dialog = ActivityRequirementsDialogFragment.create(regionDisplayName, rankingCriteria)
        dialog.show(supportFragmentManager, ActivityRequirementsDialogFragment.TAG)
    }

    override fun onBackPressed() {
        if (toolbar.isSearchFieldExpanded) {
            toolbar.closeSearchField()
            return
        }

        if (viewPager.currentItemAsHomeTab != HomeTab.RANKINGS) {
            viewPager.currentItemAsHomeTab = HomeTab.RANKINGS
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
        regionManager.addListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        regionManager.removeListener(this)
    }

    override fun onNavigationItemReselected(item: MenuItem) {
        adapter?.onNavigationItemReselected(item.itemIdAsHomeTab)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        viewPager.currentItemAsHomeTab = item.itemIdAsHomeTab
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

    override fun onRegionChange(regionManager: RegionManager) {
        prepareMenuAndTitleAndSubtitle(null)
        adapter?.refresh()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_CURRENT_POSITION, viewPager.currentItemAsHomeTab)
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

        val adapter = HomePagerAdapter()
        viewPager.adapter = adapter
        this.adapter = adapter
    }

    override fun onViewYourselfClick(v: HomeToolbar) {
        val identity = identityManager.identity ?: throw NullPointerException("identity is null")
        startActivity(PlayerActivity.getLaunchIntent(this, identity,
                regionManager.getRegion(this)))
    }

    private fun prepareMenuAndTitleAndSubtitle(layout: RankingsLayout?) {
        val region = regionManager.getRegion(this)
        toolbar.titleText = region.displayName

        toolbar.subtitleText = layout?.rankingsBundle?.let {
            getString(R.string.updated_x, it.time.shortForm)
        } ?: getString(region.endpoint.title)

        toolbar.refresh()
    }

    override val rankingCriteria: RankingCriteria?
        get() = adapter?.rankingCriteria

    override fun search(query: String?) {
        adapter?.search(query)
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
            viewPager.currentItemAsHomeTab = initialPosition
        }
    }

    override val showSearchIcon: Boolean
        get() = toolbar.hasSubtitleText

    private fun updateSelectedBottomNavigationItem() {
        val itemId = when (viewPager.currentItemAsHomeTab) {
            HomeTab.RANKINGS -> R.id.actionRankings
            HomeTab.TOURNAMENTS -> R.id.actionTournaments
            HomeTab.FAVORITE_PLAYERS -> R.id.actionFavoritePlayers
        }

        bottomNavigationView.menu.findItem(itemId).isChecked = true
    }

}
