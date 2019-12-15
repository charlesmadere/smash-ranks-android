package com.garpr.android.features.settings

import com.garpr.android.BaseTest
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.LitePlayer
import com.garpr.android.data.models.Region
import com.garpr.android.features.settings.SettingsViewModel.FavoritePlayersState
import com.garpr.android.features.settings.SettingsViewModel.IdentityState
import com.garpr.android.features.settings.SettingsViewModel.SmashRosterState
import com.garpr.android.misc.Schedulers
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.misc.Timber
import com.garpr.android.repositories.FavoritePlayersRepository
import com.garpr.android.repositories.IdentityRepository
import com.garpr.android.repositories.NightModeRepository
import com.garpr.android.repositories.RegionRepository
import com.garpr.android.sync.roster.SmashRosterSyncManager
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.inject
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SettingsViewModelTest : BaseTest() {

    private lateinit var viewModel: SettingsViewModel

    protected val favoritePlayersRepository: FavoritePlayersRepository by inject()
    protected val identityRepository: IdentityRepository by inject()
    protected val nightModeRepository: NightModeRepository by inject()
    protected val regionRepository: RegionRepository by inject()
    protected val schedulers: Schedulers by inject()
    protected val smashRosterSyncManager: SmashRosterSyncManager by inject()
    protected val threadUtils: ThreadUtils by inject()
    protected val timber: Timber by inject()

    companion object {
        private val CHARLEZARD: AbsPlayer = LitePlayer(
                id = "587a951dd2994e15c7dea9fe",
                name = "Charlezard"
        )

        private val IMYT: AbsPlayer = LitePlayer(
                id = "5877eb55d2994e15c7dea98b",
                name = "Imyt"
        )

        private val MIKKUZ: AbsPlayer = LitePlayer(
                id = "583a4a15d2994e0577b05c74",
                name = "mikkuz"
        )

        private val SNAP: AbsPlayer = LitePlayer(
                id = "59213f1ad2994e1d79144956",
                name = "Snap"
        )

        private val NORCAL = Region(
                displayName = "Norcal",
                id = "norcal",
                endpoint = Endpoint.GAR_PR
        )
    }

    @Before
    override fun setUp() {
        super.setUp()

        viewModel = SettingsViewModel(favoritePlayersRepository, identityRepository,
                nightModeRepository, regionRepository, schedulers, smashRosterSyncManager,
                threadUtils, timber)
    }

    @Test
    fun testDeleteAllFavoritePlayers() {
        favoritePlayersRepository.addPlayer(CHARLEZARD, NORCAL)
        favoritePlayersRepository.addPlayer(IMYT, NORCAL)
        favoritePlayersRepository.addPlayer(MIKKUZ, NORCAL)
        favoritePlayersRepository.addPlayer(SNAP, NORCAL)

        var state: SettingsViewModel.State? = null
        val fetchedStates = mutableListOf<FavoritePlayersState>()

        viewModel.stateLiveData.observeForever {
            state = it
            fetchedStates.add(it.favoritePlayersState)
        }

        assertFalse(favoritePlayersRepository.isEmpty)
        assertNotNull(state)

        var fetchedState = state?.favoritePlayersState as FavoritePlayersState.Fetched
        assertEquals(4, fetchedState.size)
        assertEquals(1, fetchedStates.size)
        assertTrue(fetchedStates[0] is FavoritePlayersState.Fetched)

        viewModel.deleteFavoritePlayers()
        assertTrue(favoritePlayersRepository.isEmpty)
        assertNotNull(state)

        fetchedState = state?.favoritePlayersState as FavoritePlayersState.Fetched
        assertEquals(0, fetchedState.size)
        assertEquals(3, fetchedStates.size)
        assertTrue(fetchedStates[0] is FavoritePlayersState.Fetched)
        assertTrue(fetchedStates[1] is FavoritePlayersState.Fetching)
        assertTrue(fetchedStates[2] is FavoritePlayersState.Fetched)
    }

    @Test
    fun testDeleteAllFavoritePlayersWithEmptyFavoritesRepository() {
        var state: SettingsViewModel.State? = null
        val fetchedStates = mutableListOf<FavoritePlayersState>()

        viewModel.stateLiveData.observeForever {
            state = it
            fetchedStates.add(it.favoritePlayersState)
        }

        assertTrue(favoritePlayersRepository.isEmpty)
        assertNotNull(state)

        var fetchedState = state?.favoritePlayersState as FavoritePlayersState.Fetched
        assertEquals(0, fetchedState.size)
        assertEquals(1, fetchedStates.size)
        assertTrue(fetchedStates[0] is FavoritePlayersState.Fetched)

        viewModel.deleteFavoritePlayers()
        assertTrue(favoritePlayersRepository.isEmpty)
        assertNotNull(state)

        fetchedState = state?.favoritePlayersState as FavoritePlayersState.Fetched
        assertEquals(0, fetchedState.size)
        assertEquals(3, fetchedStates.size)
        assertTrue(fetchedStates[0] is FavoritePlayersState.Fetched)
        assertTrue(fetchedStates[1] is FavoritePlayersState.Fetching)
        assertTrue(fetchedStates[2] is FavoritePlayersState.Fetched)
    }

    @Test
    fun testInitialState() {
        favoritePlayersRepository.addPlayer(CHARLEZARD, NORCAL)
        favoritePlayersRepository.addPlayer(IMYT, NORCAL)
        favoritePlayersRepository.addPlayer(MIKKUZ, NORCAL)
        favoritePlayersRepository.addPlayer(SNAP, NORCAL)
        identityRepository.setIdentity(CHARLEZARD, NORCAL)

        var state: SettingsViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        assertNotNull(state)
        assertEquals((state?.identityState as IdentityState.Fetched).identity, CHARLEZARD)
        assertEquals(4, (state?.favoritePlayersState as FavoritePlayersState.Fetched).size)
        assertEquals(nightModeRepository.nightMode, state?.nightMode)
        assertEquals(regionRepository.getRegion(), state?.region)
        assertEquals(SmashRosterState.NotYetSynced, state?.smashRosterState)
    }

    @Test
    fun testInitialStateWithEmptyFavoritePlayersRepositoryAndNoIdentity() {
        var state: SettingsViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        assertNotNull(state)
        assertNull((state?.identityState as IdentityState.Fetched).identity)
        assertEquals(0, (state?.favoritePlayersState as FavoritePlayersState.Fetched).size)
        assertEquals(nightModeRepository.nightMode, state?.nightMode)
        assertEquals(regionRepository.getRegion(), state?.region)
        assertEquals(SmashRosterState.NotYetSynced, state?.smashRosterState)
    }

}
