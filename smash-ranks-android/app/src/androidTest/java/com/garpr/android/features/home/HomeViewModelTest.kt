package com.garpr.android.features.home

import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.LitePlayer
import com.garpr.android.data.models.RankingsBundle
import com.garpr.android.data.models.Region
import com.garpr.android.data.models.SimpleDate
import com.garpr.android.features.common.viewModels.BaseViewModelTest
import com.garpr.android.misc.Schedulers
import com.garpr.android.repositories.IdentityRepository
import com.garpr.android.repositories.RegionRepository
import com.garpr.android.sync.rankings.RankingsPollingManager
import com.garpr.android.sync.roster.SmashRosterSyncManager
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.koin.core.inject
import java.util.Calendar

class HomeViewModelTest : BaseViewModelTest() {

    private lateinit var viewModel: HomeViewModel

    protected val identityRepository: IdentityRepository by inject()
    protected val rankingsPollingManager: RankingsPollingManager by inject()
    protected val regionRepository: RegionRepository by inject()
    protected val schedulers: Schedulers by inject()
    protected val smashRosterSyncManager: SmashRosterSyncManager by inject()

    @Before
    override fun setUp() {
        super.setUp()

        viewModel = HomeViewModel(identityRepository, rankingsPollingManager, regionRepository,
                schedulers, smashRosterSyncManager)
    }

    @Test
     fun testIdentity() {
        assertNull(viewModel.identity)

        identityRepository.setIdentity(CHARLEZARD, NORCAL)
        assertEquals(CHARLEZARD, viewModel.identity)

        identityRepository.removeIdentity()
        assertNull(viewModel.identity)
    }

    @Test
    fun testOnRankingsBundleChange() {
        var state: HomeViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        viewModel.onRankingsBundleChange(RANKINGS_BUNDLE, true)
        assertEquals(true, state?.hasHomeContent)
        assertFalse(state?.subtitleDate.isNullOrBlank())

        viewModel.onRankingsBundleChange(RANKINGS_BUNDLE, false)
        assertEquals(false, state?.hasHomeContent)
        assertFalse(state?.subtitleDate.isNullOrBlank())
    }

    @Test
    fun testOnRankingsBundleChangeWithEmptyRankingsBundle() {
        var state: HomeViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        viewModel.onRankingsBundleChange(EMPTY_RANKINGS_BUNDLE, true)
        assertEquals(true, state?.hasHomeContent)
        assertFalse(state?.subtitleDate.isNullOrBlank())

        viewModel.onRankingsBundleChange(EMPTY_RANKINGS_BUNDLE, false)
        assertEquals(false, state?.hasHomeContent)
        assertFalse(state?.subtitleDate.isNullOrBlank())
    }

    @Test
    fun testOnTournamentBundleChange() {
        var state: HomeViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        viewModel.onTournamentsBundleChange(true)
        assertEquals(false, state?.hasTournaments)
    }

    @Test
    fun testShowYourself() {
        var state: HomeViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        identityRepository.removeIdentity()
        assertEquals(false, state?.showYourself)

        identityRepository.setIdentity(CHARLEZARD, NORCAL)
        assertEquals(true, state?.showYourself)

        identityRepository.removeIdentity()
        assertEquals(false, state?.showYourself)
    }

    @Test
    fun testTitle() {
        var title: CharSequence? = null

        viewModel.stateLiveData.observeForever {
            title = it.title
        }

        assertEquals(NORCAL.displayName, title)

        regionRepository.region = NYC
        assertEquals(NYC.displayName, title)

        regionRepository.region = NORCAL
        assertEquals(NORCAL.displayName, title)
    }

    companion object {
        private val NORCAL = Region(
                displayName = "Norcal",
                id = "norcal",
                endpoint = Endpoint.GAR_PR
        )

        private val NYC = Region(
                displayName = "New York City",
                id = "nyc",
                endpoint = Endpoint.NOT_GAR_PR
        )

        private val CHARLEZARD: AbsPlayer = LitePlayer(
                id = "587a951dd2994e15c7dea9fe",
                name = "Charlezard"
        )

        private val EMPTY_RANKINGS_BUNDLE = RankingsBundle(
                rankingCriteria = NORCAL,
                time = with(Calendar.getInstance()) {
                    clear()
                    set(Calendar.YEAR, 2019)
                    set(Calendar.MONTH, Calendar.SEPTEMBER)
                    set(Calendar.DAY_OF_MONTH, 26)
                    SimpleDate(time)
                },
                id = "476E660A",
                region = NORCAL.id
        )

        private val RANKINGS_BUNDLE = RankingsBundle(
                rankingCriteria = Region(
                        displayName = NORCAL.displayName,
                        id = NORCAL.id,
                        rankingActivityDayLimit = 60,
                        rankingNumTourneysAttended = 2,
                        tournamentQualifiedDayLimit = 1000,
                        endpoint = NORCAL.endpoint
                ),
                time = with(Calendar.getInstance()) {
                    clear()
                    set(Calendar.YEAR, 2018)
                    set(Calendar.MONTH, Calendar.JUNE)
                    set(Calendar.DAY_OF_MONTH, 15)
                    SimpleDate(time)
                },
                id = "ED31752D",
                region = NORCAL.id
        )
    }

}
