package com.garpr.android.features.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.garpr.android.R
import com.garpr.android.extensions.putOptionalExtra
import com.garpr.android.features.common.activities.BaseActivity
import com.garpr.android.features.home.shareRegion.ShareRegionDialogFragment
import com.garpr.android.features.players.PlayersActivity
import com.garpr.android.features.rankings.RankingsAndFavoritesViewModel
import com.garpr.android.features.settings.SettingsActivity
import com.garpr.android.features.tournaments.TournamentsViewModel
import com.garpr.android.misc.Searchable
import kotlinx.android.synthetic.main.activity_home.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeActivity : BaseActivity(), BottomNavigationView.Listeners, HomeToolbar.Listeners,
        Searchable {

    private lateinit var adapter: HomeFragmentPagerAdapter

    private var currentPage: HomeTab
        get() = HomeTab.values()[viewPager.currentItem]
        set(value) {
            viewPager.setCurrentItem(value.ordinal, false)
        }

    override val activityName = TAG

    private val onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)

            toolbar.searchFieldHint = getText(when (HomeTab.values()[position]) {
                HomeTab.HOME -> R.string.search_rankings_and_favorites_
                HomeTab.TOURNAMENTS -> R.string.search_tournaments_
            })

            bottomNavigationView.selection = HomeTab.values()[position]
        }
    }

    private val homeViewModel: HomeViewModel by viewModel()
    private val rankingsAndFavoritesViewModel: RankingsAndFavoritesViewModel by viewModel()
    private val tournamentsViewModel: TournamentsViewModel by viewModel()

    private fun initListeners() {
        homeViewModel.stateLiveData.observe(this, Observer {
            refreshState(it)
        })

        rankingsAndFavoritesViewModel.stateLiveData.observe(this, Observer {
            homeViewModel.onRankingsBundleChange(it.rankingsBundle, it.hasContent)
        })

        tournamentsViewModel.stateLiveData.observe(this, Observer {
            homeViewModel.onTournamentsBundleChange(it.isEmpty)
        })
    }

    override fun onBackPressed() {
        if (toolbar.isSearchFieldExpanded) {
            toolbar.closeSearchField()
            return
        }

        if (currentPage != HomeTab.HOME) {
            currentPage = HomeTab.HOME
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

    override fun onHomeTabClick(v: BottomNavigationView, homeTab: HomeTab) {
        currentPage = homeTab
    }

    override fun onHomeTabReselected(v: BottomNavigationView, homeTab: HomeTab) {
        adapter.onHomeTabReselected(homeTab)
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

        toolbar.searchable = this
        toolbar.listeners = this
        viewPager.registerOnPageChangeCallback(onPageChangeCallback)
        viewPager.isUserInputEnabled = false
        viewPager.offscreenPageLimit = HomeTab.values().size - 1
        bottomNavigationView.listeners = this

        adapter = HomeFragmentPagerAdapter(this)
        viewPager.adapter = adapter
    }

    private fun refreshState(state: HomeViewModel.State) {
        toolbar.titleText = state.title

        toolbar.subtitleText = if (state.subtitleDate.isNullOrBlank()) {
            null
        } else {
            getString(R.string.updated_x, state.subtitleDate)
        }

        toolbar.showSearchIcon = if (toolbar.isSearchFieldExpanded) false else state.showSearch
    }

    override fun search(query: String?) {
        rankingsAndFavoritesViewModel.searchQuery = query
        tournamentsViewModel.searchQuery = query
    }

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

}
