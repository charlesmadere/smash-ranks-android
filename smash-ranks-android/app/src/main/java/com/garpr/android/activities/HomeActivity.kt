package com.garpr.android.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.view.MenuItem
import com.garpr.android.App
import com.garpr.android.R
import com.garpr.android.adapters.HomePagerAdapter
import com.garpr.android.extensions.subtitle
import com.garpr.android.managers.IdentityManager
import com.garpr.android.managers.RegionManager
import com.garpr.android.misc.SearchQueryHandle
import com.garpr.android.misc.Searchable
import com.garpr.android.misc.ShareUtils
import com.garpr.android.sync.RankingsPollingSyncManager
import com.garpr.android.views.RankingsLayout
import com.garpr.android.views.toolbars.HomeToolbar
import com.garpr.android.views.toolbars.SearchToolbar
import kotterknife.bindView
import java.text.NumberFormat
import javax.inject.Inject

class HomeActivity : BaseActivity(), BottomNavigationView.OnNavigationItemReselectedListener,
        BottomNavigationView.OnNavigationItemSelectedListener, RankingsLayout.Listener,
        RegionManager.OnRegionChangeListener, Searchable, SearchQueryHandle,
        SearchToolbar.Listener {

    private lateinit var adapter: HomePagerAdapter

    @Inject
    protected lateinit var identityManager: IdentityManager

    @Inject
    protected lateinit var rankingsPollingSyncManager: RankingsPollingSyncManager

    @Inject
    protected lateinit var regionManager: RegionManager

    @Inject
    protected lateinit var shareUtils: ShareUtils

    private val bottomNavigationView: BottomNavigationView by bindView(R.id.bottomNavigationView)
    private val homeToolbar: HomeToolbar by bindView(R.id.toolbar)
    private val viewPager: ViewPager by bindView(R.id.viewPager)


    companion object {
        private const val TAG = "HomeActivity"
        private val CNAME = HomeActivity::class.java.canonicalName
        private val EXTRA_INITIAL_POSITION = "$CNAME.InitialPosition"
        private const val KEY_CURRENT_POSITION = "CurrentPosition"

        const val POSITION_RANKINGS = 0
        const val POSITION_TOURNAMENTS = 1
        const val POSITION_FAVORITE_PLAYERS = 2

        fun getLaunchIntent(context: Context, initialPosition: Int? = null): Intent {
            val intent = Intent.makeRestartActivityTask(
                    Intent(context, HomeActivity::class.java).component)

            if (initialPosition != null) {
                intent.putExtra(EXTRA_INITIAL_POSITION, initialPosition)
            }

            return intent
        }
    }

    override val activityName = TAG

    override fun onBackPressed() {
        if (homeToolbar.isSearchLayoutExpanded) {
            homeToolbar.closeSearchLayout()
            return
        }

        if (viewPager.currentItem != POSITION_RANKINGS) {
            viewPager.setCurrentItem(POSITION_RANKINGS, false)
            return
        }

        super.onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.get().appComponent.inject(this)
        setContentView(R.layout.activity_home)
        setInitialPosition(savedInstanceState)
        rankingsPollingSyncManager.enableOrDisable()
        regionManager.addListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        regionManager.removeListener(this)
    }

    override fun onNavigationItemReselected(item: MenuItem) {
        val position = when (item.itemId) {
            R.id.actionFavoritePlayers -> POSITION_FAVORITE_PLAYERS
            R.id.actionRankings -> POSITION_RANKINGS
            R.id.actionTournaments -> POSITION_TOURNAMENTS
            else -> throw RuntimeException("unknown item: ${item.title}")
        }

        adapter.onNavigationItemReselected(position)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val position = when (item.itemId) {
            R.id.actionFavoritePlayers -> POSITION_FAVORITE_PLAYERS
            R.id.actionRankings -> POSITION_RANKINGS
            R.id.actionTournaments -> POSITION_TOURNAMENTS
            else -> throw RuntimeException("unknown item: ${item.title}")
        }

        homeToolbar.closeSearchLayout()
        viewPager.setCurrentItem(position, false)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.miActivityRequirements -> {
                showActivityRequirements()
                true
            }

            R.id.miSettings -> {
                startActivity(SettingsActivity.getLaunchIntent(this))
                true
            }

            R.id.miShare -> {
                share()
                true
            }

            R.id.miViewAllPlayers -> {
                startActivity(PlayersActivity.getLaunchIntent(this))
                true
            }

            R.id.miViewYourself -> {
                identityManager.identity?.let {
                    startActivity(PlayerActivity.getLaunchIntent(this, it,
                            regionManager.getRegion(this)))
                } ?: throw NullPointerException("identity is null")
                true
            }

            else -> {
                super.onOptionsItemSelected(item)
            }
        }

    override fun onRankingsBundleFetched(layout: RankingsLayout) {
        prepareMenuAndTitleAndSubtitle(layout)
    }

    override fun onRegionChange(regionManager: RegionManager) {
        prepareMenuAndTitleAndSubtitle(null)
        adapter.refresh()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_CURRENT_POSITION, viewPager.currentItem)
    }

    override fun onViewsBound() {
        super.onViewsBound()

        bottomNavigationView.setOnNavigationItemReselectedListener(this)
        bottomNavigationView.setOnNavigationItemSelectedListener(this)
        viewPager.addOnPageChangeListener(onPageChangeListener)
        viewPager.pageMargin = resources.getDimensionPixelSize(R.dimen.root_padding)
        viewPager.offscreenPageLimit = 3

        adapter = HomePagerAdapter()
        viewPager.adapter = adapter
    }

    private fun prepareMenuAndTitleAndSubtitle(layout: RankingsLayout?) {
        val region = regionManager.getRegion(this)
        title = region.displayName

        subtitle = layout?.rankingsBundle?.let {
            getString(R.string.updated_x, it.time.shortForm)
        } ?: getString(region.endpoint.title)

        invalidateOptionsMenu()
    }

    override fun search(query: String?) {
        adapter.search(viewPager.currentItem, query)
    }

    override val searchQuery: CharSequence?
        get() = homeToolbar.searchQuery

    private fun setInitialPosition(savedInstanceState: Bundle?) {
        var initialPosition = -1

        if (savedInstanceState != null && !savedInstanceState.isEmpty) {
            initialPosition = savedInstanceState.getInt(KEY_CURRENT_POSITION, -1)
        }

        if (initialPosition == -1) {
            intent?.let { initialPosition = it.getIntExtra(EXTRA_INITIAL_POSITION, -1) }
        }

        if (initialPosition != -1) {
            viewPager.currentItem = initialPosition
        }
    }

    private fun share() {
        val region = regionManager.getRegion(this).displayName
        val items = arrayOf(getString(R.string.x_rankings, region),
                getString(R.string.x_tournaments, region))

        AlertDialog.Builder(this)
                .setItems(items) { dialog, which ->
                    when (which) {
                        0 -> shareUtils.shareRankings(this)
                        1 -> shareUtils.shareTournaments(this)
                        else -> throw RuntimeException("illegal which: $which")
                    }
                }
                .setTitle(R.string.share)
                .show()
    }

    private fun showActivityRequirements() {
        val rankingCriteria = adapter.rankingsBundle?.rankingCriteria ?: return
        val rankingNumTourneysAttended = rankingCriteria.rankingNumTourneysAttended
        val rankingActivityDayLimit = rankingCriteria.rankingActivityDayLimit

        if (rankingNumTourneysAttended == null || rankingActivityDayLimit == null) {
            throw RuntimeException("Region (${rankingCriteria.displayName}) is missing necessary data")
        }

        val numberFormat = NumberFormat.getIntegerInstance()
        val tournaments = resources.getQuantityString(R.plurals.x_tournaments,
                rankingNumTourneysAttended, numberFormat.format(rankingNumTourneysAttended))
        val days = resources.getQuantityString(R.plurals.x_days,
                rankingActivityDayLimit, numberFormat.format(rankingActivityDayLimit))

        AlertDialog.Builder(this)
                .setMessage(getString(R.string.x_within_the_last_y, tournaments, days))
                .setTitle(getString(R.string.x_activity_requirements, rankingCriteria.displayName))
                .show()
    }

    override val showSearchMenuItem: Boolean
        get() = subtitle?.isNotBlank() == true

    private fun updateSelectedBottomNavigationItem() {
        val itemId = when (viewPager.currentItem) {
            POSITION_FAVORITE_PLAYERS -> R.id.actionFavoritePlayers
            POSITION_RANKINGS -> R.id.actionRankings
            POSITION_TOURNAMENTS -> R.id.actionTournaments
            else -> throw RuntimeException("unknown current item: ${viewPager.currentItem}")
        }

        bottomNavigationView.menu.findItem(itemId).isChecked = true
    }

    private val onPageChangeListener = object : ViewPager.SimpleOnPageChangeListener() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            updateSelectedBottomNavigationItem()
        }
    }

}
