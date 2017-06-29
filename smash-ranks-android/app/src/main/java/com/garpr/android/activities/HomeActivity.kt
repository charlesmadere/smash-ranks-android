package com.garpr.android.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.content.IntentCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.view.MenuItem
import butterknife.BindView
import butterknife.OnPageChange
import com.garpr.android.App
import com.garpr.android.R
import com.garpr.android.adapters.HomePagerAdapter
import com.garpr.android.misc.IdentityManager
import com.garpr.android.misc.NotificationManager
import com.garpr.android.misc.RegionManager
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

class HomeActivity : BaseKotlinActivity(), BottomNavigationView.OnNavigationItemSelectedListener,
        RankingsLayout.Listener, RegionManager.OnRegionChangeListener, Searchable,
        SearchQueryHandle, SearchToolbar.Listener {

    lateinit private var mAdapter: HomePagerAdapter

    @Inject
    lateinit internal var mIdentityManager: IdentityManager

    @Inject
    lateinit internal var mNotificationManager: NotificationManager

    @Inject
    lateinit internal var mRankingsPollingSyncManager: RankingsPollingSyncManager

    @Inject
    lateinit internal var mRegionManager: RegionManager

    @Inject
    lateinit internal var mShareUtils: ShareUtils

    private val mBottomNavigationView: BottomNavigationView by bindView(R.id.bottomNavigationView)
    private val mHomeToolbar: HomeToolbar by bindView(R.id.toolbar)
    private val mViewPager: ViewPager by bindView(R.id.viewPager)


    companion object {
        private val TAG = "HomeActivity"
        private val CNAME = HomeActivity::class.java.canonicalName
        private val EXTRA_INITIAL_POSITION = CNAME + ".InitialPosition"
        private val KEY_CURRENT_POSITION = "CurrentPosition"

        val PositionRankings = 0
        val PositionTournaments = 1
        val PositionFavoritePlayers = 2

        @JvmOverloads
        fun getLaunchIntent(context: Context, initialPosition: Int? = null): Intent {
            var intent = Intent(context, HomeActivity::class.java)
            intent = IntentCompat.makeRestartActivityTask(intent.component)

            if (initialPosition != null) {
                intent.putExtra(EXTRA_INITIAL_POSITION, initialPosition.toInt())
            }

            return intent
        }
    }

    public override fun getActivityName(): String {
        return TAG
    }

    override fun getSearchQuery(): CharSequence? {
        return mHomeToolbar.searchQuery
    }

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

        mRankingsPollingSyncManager.enableOrDisable()
        mNotificationManager.cancelAll()

        setInitialPosition(savedInstanceState)

        mRegionManager.addListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        mRegionManager.removeListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (item.isChecked) {
            return false
        }

        when (item.itemId) {
            R.id.actionFavoritePlayers -> {
                mViewPager.setCurrentItem(PositionFavoritePlayers, false)
                return true
            }

            R.id.actionRankings -> {
                mViewPager.setCurrentItem(PositionRankings, false)
                return true
            }

            R.id.actionTournaments -> {
                mViewPager.setCurrentItem(PositionTournaments, false)
                return true
            }

            else -> throw RuntimeException("unknown item: " + item.title)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.miActivityRequirements -> {
                showActivityRequirements()
                return true
            }

            R.id.miSettings -> {
                startActivity(SettingsActivity.getLaunchIntent(this))
                return true
            }

            R.id.miShare -> {
                share()
                return true
            }

            R.id.miViewAllPlayers -> {
                startActivity(PlayersActivity.getLaunchIntent(this))
                return true
            }

            R.id.miViewYourself -> {
                startActivity(PlayerActivity.getLaunchIntent(this, mIdentityManager.identity!!,
                        mRegionManager.getRegion(this)))
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    @OnPageChange(R.id.viewPager)
    internal fun onPageChange() {
        updateSelectedBottomNavigationItem()
    }

    override fun onRankingsBundleFetched(layout: RankingsLayout) {
        val region = mRegionManager.getRegion(this)
        setTitle(region.endpoint.getName())

        val rankingsBundle = layout.rankingsBundle

        if (rankingsBundle == null) {
            subtitle = region.displayName
        } else {
            subtitle = getString(R.string.x_updated_y, region.displayName,
                    rankingsBundle.time.shortForm)
        }

        supportInvalidateOptionsMenu()
    }

    override fun onRegionChange(regionManager: RegionManager) {
        val region = mRegionManager.getRegion(this)
        setTitle(region.endpoint.getName())

        mAdapter.refresh()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_CURRENT_POSITION, mViewPager.currentItem)
    }

    override fun onViewsBound() {
        super.onViewsBound()

        mBottomNavigationView.setOnNavigationItemSelectedListener(this)
        mViewPager.pageMargin = resources.getDimensionPixelSize(R.dimen.root_padding)
        mViewPager.offscreenPageLimit = 3

        mAdapter = HomePagerAdapter()
        mViewPager.adapter = mAdapter
    }

    override fun search(query: String?) {
        mAdapter.search(query)
    }

    private fun setInitialPosition(savedInstanceState: Bundle?) {
        var initialPosition = -1

        if (savedInstanceState != null && !savedInstanceState.isEmpty) {
            initialPosition = savedInstanceState.getInt(KEY_CURRENT_POSITION, -1)
        }

        if (initialPosition == -1 && intent != null && intent.hasExtra(EXTRA_INITIAL_POSITION)) {
            initialPosition = intent.getIntExtra(EXTRA_INITIAL_POSITION, -1)
        }

        if (initialPosition != -1) {
            mViewPager.currentItem = initialPosition
        }
    }

    private fun share() {
        val items = arrayOf(getText(R.string.rankings), getText(R.string.tournaments))

        AlertDialog.Builder(this)
                .setItems(items) { dialog, which ->
                    when (which) {
                        0 -> mShareUtils.shareRankings(this@HomeActivity)
                        1 -> mShareUtils.shareTournaments(this@HomeActivity)
                        else -> throw RuntimeException("illegal which: " + which)
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
            throw RuntimeException("Region ($region) is missing necessary data")
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

    override fun showSearchMenuItem(): Boolean {
        return !TextUtils.isEmpty(subtitle)
    }

    private fun updateSelectedBottomNavigationItem() {
        when (mViewPager.currentItem) {
            PositionFavoritePlayers -> mBottomNavigationView.menu.findItem(
                    R.id.actionFavoritePlayers).isChecked = true

            PositionRankings -> mBottomNavigationView.menu.findItem(
                    R.id.actionRankings).isChecked = true

            PositionTournaments -> mBottomNavigationView.menu.findItem(
                    R.id.actionTournaments).isChecked = true

            else -> throw RuntimeException("unknown current item: " + mViewPager.currentItem)
        }
    }

}
