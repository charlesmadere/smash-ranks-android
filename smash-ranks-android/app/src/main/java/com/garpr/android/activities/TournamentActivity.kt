package com.garpr.android.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v4.widget.SwipeRefreshLayout
import android.text.TextUtils
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
import com.garpr.android.views.ErrorLinearLayout
import com.garpr.android.views.toolbars.SearchToolbar
import com.garpr.android.views.toolbars.TournamentToolbar
import kotterknife.bindView
import javax.inject.Inject

class TournamentActivity : BaseActivity(), ApiListener<FullTournament>, Searchable,
        SearchQueryHandle, SearchToolbar.Listener, SwipeRefreshLayout.OnRefreshListener,
        TournamentToolbar.DataProvider {

    private var mFullTournament: FullTournament? = null
    lateinit private var mTournamentId: String
    lateinit private var mAdapter: TournamentPagerAdapter

    @Inject
    lateinit protected var mRegionManager: RegionManager

    @Inject
    lateinit protected var mServerApi: ServerApi

    @Inject
    lateinit protected var mShareUtils: ShareUtils

    private val mError: ErrorLinearLayout by bindView(R.id.error)
    private val mRefreshLayout: SwipeRefreshLayout by bindView(R.id.refreshLayout)
    private val mTabLayout: TabLayout by bindView(R.id.tabLayout)
    private val mTournamentToolbar: TournamentToolbar by bindView(R.id.toolbar)
    private val mEmpty: View by bindView(R.id.empty)
    private val mViewPager: ViewPager by bindView(R.id.viewPager)


    companion object {
        private const val TAG = "TournamentActivity"
        private val CNAME = TournamentActivity::class.java.canonicalName
        private val EXTRA_TOURNAMENT_DATE = CNAME + ".TournamentDate"
        private val EXTRA_TOURNAMENT_ID = CNAME + ".TournamentId"
        private val EXTRA_TOURNAMENT_NAME = CNAME + ".TournamentName"

        fun getLaunchIntent(context: Context, tournament: AbsTournament, region: Region?): Intent {
            return getLaunchIntent(context, tournament.id, tournament.name, tournament.date, region)
        }

        fun getLaunchIntent(context: Context, match: Match, region: Region?): Intent {
            return getLaunchIntent(context, match.tournament.id, match.tournament.name,
                    match.tournament.date, region)
        }

        fun getLaunchIntent(context: Context, tournamentId: String, tournamentName: String?,
                tournamentDate: SimpleDate?, region: Region?): Intent {
            val intent = Intent(context, TournamentActivity::class.java)
                    .putExtra(EXTRA_TOURNAMENT_ID, tournamentId)

            if (!TextUtils.isEmpty(tournamentName)) {
                intent.putExtra(EXTRA_TOURNAMENT_NAME, tournamentName)
            }

            if (tournamentDate != null) {
                intent.putExtra(EXTRA_TOURNAMENT_DATE, tournamentDate)
            }

            if (region != null) {
                intent.putExtra(BaseActivity.EXTRA_REGION, region)
            }

            return intent
        }
    }

    override val activityName = TAG

    override fun failure(errorCode: Int) {
        mFullTournament = null
        showError(errorCode)
    }

    private fun fetchFullTournament() {
        mRefreshLayout.isRefreshing = true
        mServerApi.getTournament(mRegionManager.getRegion(this), mTournamentId, ApiCall(this))
    }

    override val fullTournament: FullTournament?
        get() = mFullTournament

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.get().appComponent.inject(this)
        setContentView(R.layout.activity_tournament)

        mTournamentId = intent.getStringExtra(EXTRA_TOURNAMENT_ID)

        prepareMenuAndTitles()
        fetchFullTournament()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.miShare -> {
                val fullTournament = mFullTournament ?: throw RuntimeException()
                mShareUtils.shareTournament(this, fullTournament)
                return true
            }

            R.id.miViewTournamentPage -> {
                val fullTournament = mFullTournament ?: throw RuntimeException()
                mShareUtils.openUrl(this, fullTournament.url)
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onRefresh() {
        fetchFullTournament()
    }

    override fun onViewsBound() {
        super.onViewsBound()

        mRefreshLayout.setOnRefreshListener(this)
        mViewPager.pageMargin = resources.getDimensionPixelSize(R.dimen.root_padding)
        mTabLayout.setupWithViewPager(mViewPager)
        mTabLayout.addOnTabSelectedListener(mOnTabSelectedListener)
        mViewPager.addOnPageChangeListener(mOnPageChangeListener)
    }

    private fun prepareMenuAndTitles() {
        if (title.isNullOrBlank()) {
            var title: String? = null
            val fullTournament = mFullTournament

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
            val fullTournament = mFullTournament

            if (fullTournament != null) {
                subtitle = fullTournament.date
            } else if (intent.hasExtra(EXTRA_TOURNAMENT_DATE)) {
                subtitle = intent.getParcelableExtra<SimpleDate>(EXTRA_TOURNAMENT_DATE)
            }

            if (subtitle != null) {
                this.subtitle = subtitle.longForm
            }
        }

        invalidateOptionsMenu()
    }

    override fun search(query: String?) = mAdapter.search(query)

    override val searchQuery: CharSequence?
        get() = mTournamentToolbar.searchQuery

    private fun showEmpty() {
        mError.visibility = View.GONE
        mViewPager.visibility = View.GONE
        mEmpty.visibility = View.VISIBLE
        prepareMenuAndTitles()
        mRefreshLayout.isRefreshing = false
    }

    private fun showError(errorCode: Int) {
        mEmpty.visibility = View.GONE
        mViewPager.visibility = View.GONE
        mError.setVisibility(View.VISIBLE, errorCode)
        prepareMenuAndTitles()
        mRefreshLayout.isRefreshing = false
    }

    private fun showFullTournament() {
        val fullTournament = mFullTournament ?: throw RuntimeException()
        mAdapter = TournamentPagerAdapter(this, fullTournament)
        mViewPager.adapter = mAdapter

        mEmpty.visibility = View.GONE
        mError.visibility = View.GONE
        mViewPager.visibility = View.VISIBLE
        prepareMenuAndTitles()
        mRefreshLayout.isRefreshing = false
        mRefreshLayout.isEnabled = false
    }

    override val showSearchMenuItem: Boolean
        get() = mFullTournament != null

    override val showUpNavigation = true

    override fun success(`object`: FullTournament?) {
        mFullTournament = `object`

        if (`object`?.matches?.isNotEmpty() == true || `object`?.players?.isNotEmpty() == true) {
            showFullTournament()
        } else {
            showEmpty()
        }
    }

    private val mOnPageChangeListener = object : ViewPager.SimpleOnPageChangeListener() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            closeKeyboard()
        }
    }

    private val mOnTabSelectedListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabReselected(tab: TabLayout.Tab?) {
            if (tab != null) {
                mAdapter.onTabReselected(tab.position)
            }
        }

        override fun onTabSelected(tab: TabLayout.Tab?) {
            // intentionally empty
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
            // intentionally empty
        }
    }

}
