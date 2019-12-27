package com.garpr.android.features.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.garpr.android.R
import com.garpr.android.extensions.itemIdAsHomeTab
import com.garpr.android.extensions.putOptionalExtra
import com.garpr.android.features.common.activities.BaseActivity
import com.garpr.android.features.favoritePlayers.FavoritePlayersViewModel
import com.garpr.android.features.home.shareRegion.ShareRegionDialogFragment
import com.garpr.android.features.player.PlayerActivity
import com.garpr.android.features.players.PlayersActivity
import com.garpr.android.features.rankings.RankingsViewModel
import com.garpr.android.features.settings.SettingsActivity
import com.garpr.android.features.tournaments.TournamentsViewModel
import com.garpr.android.misc.SearchQueryHandle
import com.garpr.android.misc.Searchable
import com.garpr.android.repositories.RegionRepository
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_home.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeActivity : BaseActivity(), BottomNavigationView.OnNavigationItemReselectedListener,
        BottomNavigationView.OnNavigationItemSelectedListener, HomeToolbar.Listeners, Searchable,
        SearchQueryHandle {

    private lateinit var adapter: HomeFragmentPagerAdapter
    override val activityName = TAG

    private val favoritePlayersViewModel: FavoritePlayersViewModel by viewModel()
    private val homeViewModel: HomeViewModel by viewModel()
    private val rankingsViewModel: RankingsViewModel by viewModel()
    private val tournamentsViewModel: TournamentsViewModel by viewModel()

    protected val regionRepository: RegionRepository by inject()

    private var currentPage: HomeTab
        get() = HomeTab.values()[viewPager.currentItem]
        set(value) {
            viewPager.setCurrentItem(value.ordinal, false)
        }

    private val onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            updateSelectedBottomNavigationItem()
        }
    }

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

    private fun initListeners() {
        homeViewModel.stateLiveData.observe(this, Observer {
            refreshState(it)
        })

        rankingsViewModel.stateLiveData.observe(this, Observer {
            homeViewModel.onRankingsBundleChange(it.rankingsBundle, it.isEmpty)
        })

        tournamentsViewModel.stateLiveData.observe(this, Observer {
            homeViewModel.onTournamentsBundleChange(it.isEmpty)
        })
    }

    override fun onActivityRequirementsClick(v: HomeToolbar) {
        val rankingCriteria = rankingsViewModel.stateLiveData.value?.rankingsBundle?.rankingCriteria ?: return
        val dialog = ActivityRequirementsDialogFragment.create(
                rankingCriteria.rankingActivityDayLimit,
                rankingCriteria.rankingNumTourneysAttended,
                regionRepository.getRegion(this).displayName)
        dialog.show(supportFragmentManager, ActivityRequirementsDialogFragment.TAG)
    }

    override fun onBackPressed() {
        if (toolbar.isSearchFieldExpanded) {
            toolbar.closeSearchField()
            return
        }

        if (currentPage != HomeTab.RANKINGS) {
            currentPage = HomeTab.RANKINGS
            return
        }

        super.onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setInitialPosition(savedInstanceState)
        initListeners()
    }

    override fun onNavigationItemReselected(item: MenuItem) {
        adapter.onNavigationItemReselected(item.itemIdAsHomeTab)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        currentPage = item.itemIdAsHomeTab
        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_CURRENT_POSITION, currentPage)
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

        toolbar.listeners = this

        bottomNavigationView.setOnNavigationItemReselectedListener(this)
        bottomNavigationView.setOnNavigationItemSelectedListener(this)
        viewPager.registerOnPageChangeCallback(onPageChangeCallback)
        viewPager.isUserInputEnabled = false
        viewPager.offscreenPageLimit = HomeTab.values().size - 1

        adapter = HomeFragmentPagerAdapter(this)
        viewPager.adapter = adapter
    }

    override fun onViewYourselfClick(v: HomeToolbar) {
        val identity = homeViewModel.identity ?: return
        startActivity(PlayerActivity.getLaunchIntent(this, identity,
                regionRepository.getRegion(this)))
    }

    private fun refreshState(state: HomeViewModel.State) {
        toolbar.titleText = state.title

        toolbar.subtitleText = if (state.subtitleDate.isNullOrBlank()) {
            null
        } else {
            getString(R.string.updated_x, state.subtitleDate)
        }

        toolbar.isActivityRequirementsVisible = state.showActivityRequirements
        toolbar.isShowYourselfVisible = state.showYourself
        toolbar.showSearchIcon = if (toolbar.isSearchFieldExpanded) false else state.showSearch
    }

    override fun search(query: String?) {
        rankingsViewModel.search(query)
        tournamentsViewModel.search(query)
        favoritePlayersViewModel.search(query)
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
            currentPage = initialPosition
        }
    }

    private fun updateSelectedBottomNavigationItem() {
        val itemId = when (currentPage) {
            HomeTab.RANKINGS -> R.id.actionRankings
            HomeTab.TOURNAMENTS -> R.id.actionTournaments
            HomeTab.FAVORITE_PLAYERS -> R.id.actionFavoritePlayers
        }

        bottomNavigationView.menu.findItem(itemId).isChecked = true
    }

}
