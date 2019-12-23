package com.garpr.android.features.home

import com.garpr.android.BaseTest
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.LitePlayer
import com.garpr.android.data.models.RankingsBundle
import com.garpr.android.data.models.Region
import com.garpr.android.data.models.SimpleDate
import com.garpr.android.data.models.TournamentsBundle
import com.garpr.android.misc.Schedulers
import com.garpr.android.repositories.FavoritePlayersRepository
import com.garpr.android.repositories.IdentityRepository
import com.garpr.android.sync.rankings.RankingsPollingManager
import com.garpr.android.sync.roster.SmashRosterSyncManager
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.inject
import org.robolectric.RobolectricTestRunner
import java.util.Date

@RunWith(RobolectricTestRunner::class)
class HomeViewModelTest : BaseTest() {

    private lateinit var viewModel: HomeViewModel

    protected val favoritePlayersRepository: FavoritePlayersRepository by inject()
    protected val identityRepository: IdentityRepository by inject()
    protected val rankingsPollingManager: RankingsPollingManager by inject()
    protected val schedulers: Schedulers by inject()
    protected val smashRosterSyncManager: SmashRosterSyncManager by inject()

    companion object {
        private val NORCAL = Region(
                displayName = "Norcal",
                id = "norcal",
                endpoint = Endpoint.GAR_PR
        )

        private val CHARLEZARD: AbsPlayer = LitePlayer(
                id = "587a951dd2994e15c7dea9fe",
                name = "Charlezard"
        )

        private val EMPTY_RANKINGS_BUNDLE = RankingsBundle(
                rankingCriteria = NORCAL,
                time = SimpleDate(Date(1477897200000L)),
                id = "6f1ed002ab5595859014ebf0951522d9",
                region = "norcal"
        )

        private val EMPTY_TOURNAMENTS_BUNDLE = TournamentsBundle()
    }

    @Before
    override fun setUp() {
        super.setUp()

        viewModel = HomeViewModel(favoritePlayersRepository, identityRepository,
                rankingsPollingManager, schedulers, smashRosterSyncManager)
    }

    @Test
    fun testHasFavoritePlayers() {
        var state: HomeViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        favoritePlayersRepository.clear()
        assertEquals(false, state?.hasFavoritePlayers)

        favoritePlayersRepository.addPlayer(CHARLEZARD, NORCAL)
        assertEquals(true, state?.hasFavoritePlayers)

        favoritePlayersRepository.clear()
        assertEquals(false, state?.hasFavoritePlayers)
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
    fun testOnRankingsBundleChangeWithEmptyRankingsBundle() {
        var state: HomeViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        viewModel.onRankingsBundleChange(EMPTY_RANKINGS_BUNDLE, true)
        assertEquals(false, state?.hasRankings)
        assertEquals(false, state?.showActivityRequirements)
        assertFalse(state?.subtitleDate.isNullOrBlank())
        assertFalse(state?.title.isNullOrBlank())
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

}
