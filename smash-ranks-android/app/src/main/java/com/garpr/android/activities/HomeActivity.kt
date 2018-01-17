package com.garpr.android.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.view.MenuItem
import com.garpr.android.App
import com.garpr.android.R
import com.garpr.android.adapters.HomePagerAdapter
import com.garpr.android.extensions.subtitle
import com.garpr.android.misc.*
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

    private lateinit var mAdapter: HomePagerAdapter

    @Inject
    protected lateinit var mIdentityManager: IdentityManager

    @Inject
    protected lateinit var mRankingsPollingSyncManager: RankingsPollingSyncManager

    @Inject
    protected lateinit var mRegionManager: RegionManager

    @Inject
    protected lateinit var mShareUtils: ShareUtils

    private val mBottomNavigationView: BottomNavigationView by bindView(R.id.bottomNavigationView)
    private val mHomeToolbar: HomeToolbar by bindView(R.id.toolbar)
    private val mViewPager: ViewPager by bindView(R.id.viewPager)


    companion object {
        private const val TAG = "HomeActivity"
        private val CNAME = HomeActivity::class.java.canonicalName
        private val EXTRA_INITIAL_POSITION = CNAME + ".InitialPosition"
        private val KEY_CURRENT_POSITION = "CurrentPosition"

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
        if (mHomeToolbar.isSearchLayoutExpanded) {
            mHomeToolbar.closeSearchLayout()
            return
        }

        if (mViewPager.currentItem != 0) {
            mViewPager.setCurrentItem(0, false)
            return
        }

        super.onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.get().appComponent.inject(this)
        setContentView(R.layout.activity_home)
        setInitialPosition(savedInstanceState)
        mRankingsPollingSyncManager.enableOrDisable()
        mRegionManager.addListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        mRegionManager.removeListener(this)
    }

    override fun onNavigationItemReselected(item: MenuItem) {
        val position = when (item.itemId) {
            R.id.actionFavoritePlayers -> POSITION_FAVORITE_PLAYERS
            R.id.actionRankings -> POSITION_RANKINGS
            R.id.actionTournaments -> POSITION_TOURNAMENTS
            else -> throw RuntimeException("unknown item: ${item.title}")
        }

        mAdapter.onNavigationItemReselected(position)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.actionFavoritePlayers -> {
                mViewPager.setCurrentItem(POSITION_FAVORITE_PLAYERS, false)
                return true
            }

            R.id.actionRankings -> {
                mViewPager.setCurrentItem(POSITION_RANKINGS, false)
                return true
            }

            R.id.actionTournaments -> {
                mViewPager.setCurrentItem(POSITION_TOURNAMENTS, false)
                return true
            }

            else -> throw RuntimeException("unknown item: ${item.title}")
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) =
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
                val identity = mIdentityManager.identity ?: throw NullPointerException("identity is null")
                startActivity(PlayerActivity.getLaunchIntent(this, identity,
                        mRegionManager.getRegion(this)))
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
        mAdapter.refresh()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_CURRENT_POSITION, mViewPager.currentItem)
    }

    override fun onViewsBound() {
        super.onViewsBound()

        mBottomNavigationView.setOnNavigationItemReselectedListener(this)
        mBottomNavigationView.setOnNavigationItemSelectedListener(this)
        mViewPager.addOnPageChangeListener(mOnPageChangeListener)
        mViewPager.pageMargin = resources.getDimensionPixelSize(R.dimen.root_padding)
        mViewPager.offscreenPageLimit = 3

        mAdapter = HomePagerAdapter()
        mViewPager.adapter = mAdapter
    }

    private fun prepareMenuAndTitleAndSubtitle(layout: RankingsLayout?) {
        title = mRegionManager.getRegion(this).displayName

        val region = mRegionManager.getRegion(this)

        subtitle = layout?.rankingsBundle?.let {
            getString(R.string.updated_x, it.time.shortForm)
        } ?: getString(region.endpoint.title)

        invalidateOptionsMenu()
    }

    override fun search(query: String?) = mAdapter.search(query)

    override val searchQuery: CharSequence?
        get() = mHomeToolbar.searchQuery

    private fun setInitialPosition(savedInstanceState: Bundle?) {
        var initialPosition = -1

        if (savedInstanceState != null && !savedInstanceState.isEmpty) {
            initialPosition = savedInstanceState.getInt(KEY_CURRENT_POSITION, -1)
        }

        if (initialPosition == -1) {
            intent?.let { initialPosition = it.getIntExtra(EXTRA_INITIAL_POSITION, -1) }
        }

        if (initialPosition != -1) {
            mViewPager.currentItem = initialPosition
        }
    }

    private fun share() {
        val region = mRegionManager.getRegion(this).displayName
        val items = arrayOf(getString(R.string.x_rankings, region),
                getString(R.string.x_tournaments, region))

        AlertDialog.Builder(this)
                .setItems(items) { dialog, which ->
                    when (which) {
                        0 -> mShareUtils.shareRankings(this)
                        1 -> mShareUtils.shareTournaments(this)
                        else -> throw RuntimeException("illegal which: $which")
                    }
                }
                .setTitle(R.string.share)
                .show()
    }

    private fun showActivityRequirements() {
        val region = mRegionManager.getRegion(this)
        val rankingNumTourneysAttended = region.rankingNumTourneysAttended
        val rankingActivityDayLimit = region.rankingActivityDayLimit

        if (rankingNumTourneysAttended == null || rankingActivityDayLimit == null) {
            throw RuntimeException("Region (${region.displayName}) is missing necessary data")
        }

        val numberFormat = NumberFormat.getInstance()
        val tournaments = resources.getQuantityString(R.plurals.x_tournaments,
                rankingNumTourneysAttended, numberFormat.format(rankingNumTourneysAttended))
        val days = resources.getQuantityString(R.plurals.x_days,
                rankingActivityDayLimit, numberFormat.format(rankingActivityDayLimit))

        AlertDialog.Builder(this)
                .setMessage(getString(R.string.x_within_the_last_y, tournaments, days))
                .setTitle(getString(R.string.x_activity_requirements, region.displayName))
                .show()
    }

    override val showSearchMenuItem: Boolean
        get() = !TextUtils.isEmpty(subtitle)

    private fun updateSelectedBottomNavigationItem() {
        val itemId = when (mViewPager.currentItem) {
            POSITION_FAVORITE_PLAYERS -> R.id.actionFavoritePlayers
            POSITION_RANKINGS -> R.id.actionRankings
            POSITION_TOURNAMENTS -> R.id.actionTournaments
            else -> throw RuntimeException("unknown current item: ${mViewPager.currentItem}")
        }

        mBottomNavigationView.menu.findItem(itemId).isChecked = true
    }

    private val mOnPageChangeListener = object : ViewPager.SimpleOnPageChangeListener() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            updateSelectedBottomNavigationItem()
        }
    }

}
