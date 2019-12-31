package com.garpr.android.features.settings

import com.garpr.android.BaseTest
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.LitePlayer
import com.garpr.android.data.models.NightMode
import com.garpr.android.data.models.PollFrequency
import com.garpr.android.data.models.Region
import com.garpr.android.data.models.SmashCompetitor
import com.garpr.android.features.settings.SettingsViewModel.FavoritePlayersState
import com.garpr.android.features.settings.SettingsViewModel.IdentityState
import com.garpr.android.features.settings.SettingsViewModel.SmashRosterState
import com.garpr.android.misc.Schedulers
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.misc.Timber
import com.garpr.android.preferences.SmashRosterPreferenceStore
import com.garpr.android.repositories.FavoritePlayersRepository
import com.garpr.android.repositories.IdentityRepository
import com.garpr.android.repositories.NightModeRepository
import com.garpr.android.repositories.RegionRepository
import com.garpr.android.repositories.SmashRosterRepository
import com.garpr.android.sync.rankings.RankingsPollingManager
import com.garpr.android.sync.roster.SmashRosterStorage
import com.garpr.android.sync.roster.SmashRosterSyncManager
import com.garpr.android.sync.roster.SmashRosterSyncManagerImpl
import com.garpr.android.wrappers.WorkManagerWrapper
import io.reactivex.Single
import org.junit.Assert.assertEquals
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
    private val smashRosterRepository = SmashRosterRepositoryOverride()

    protected val favoritePlayersRepository: FavoritePlayersRepository by inject()
    protected val identityRepository: IdentityRepository by inject()
    protected val nightModeRepository: NightModeRepository by inject()
    protected val rankingsPollingManager: RankingsPollingManager by inject()
    protected val regionRepository: RegionRepository by inject()
    protected val schedulers: Schedulers by inject()
    protected val smashRosterPreferenceStore: SmashRosterPreferenceStore by inject()
    protected val smashRosterStorage: SmashRosterStorage by inject()
    protected val threadUtils: ThreadUtils by inject()
    protected val timber: Timber by inject()
    protected val workManagerWrapper: WorkManagerWrapper by inject()

    companion object {
        private val CHARLEZARD: AbsPlayer = LitePlayer(
                id = "587a951dd2994e15c7dea9fe",
                name = "Charlezard"
        )

        private val HAX: AbsPlayer = LitePlayer(
                id = "53c64dba8ab65f6e6651f7bc",
                name = "Hax"
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

        private val GAR_PR_ROSTER = mapOf(
                CHARLEZARD.id to SmashCompetitor(
                        id = CHARLEZARD.id,
                        name = "Charles",
                        tag = CHARLEZARD.name
                ),
                MIKKUZ.id to SmashCompetitor(
                        id = MIKKUZ.id,
                        name = "Justin",
                        tag = MIKKUZ.name
                ),
                IMYT.id to SmashCompetitor(
                        id = IMYT.id,
                        name = "Declan",
                        tag = IMYT.name
                ),
                SNAP.id to SmashCompetitor(
                        id = SNAP.id,
                        name = "Danny",
                        tag = SNAP.name
                )
        )

        private val NOT_GAR_PR_ROSTER = mapOf(
                HAX.id to SmashCompetitor(
                        id = HAX.id,
                        name = "Aziz",
                        tag = HAX.name
                )
        )
    }

    @Before
    override fun setUp() {
        super.setUp()

        val smashRosterSyncManager: SmashRosterSyncManager = SmashRosterSyncManagerImpl(
                schedulers, smashRosterPreferenceStore, smashRosterRepository, smashRosterStorage,
                timber, workManagerWrapper)

        viewModel = SettingsViewModel(favoritePlayersRepository, identityRepository,
                nightModeRepository, rankingsPollingManager, regionRepository, schedulers,
                smashRosterSyncManager, threadUtils, timber)
    }

    @Test
    fun testDeleteAllFavoritePlayers() {
        favoritePlayersRepository.addPlayer(CHARLEZARD, NORCAL)
        favoritePlayersRepository.addPlayer(IMYT, NORCAL)
        favoritePlayersRepository.addPlayer(MIKKUZ, NORCAL)
        favoritePlayersRepository.addPlayer(SNAP, NORCAL)

        val states = mutableListOf<FavoritePlayersState>()

        viewModel.stateLiveData.observeForever {
            states.add(it.favoritePlayersState)
        }

        assertEquals(1, states.size)
        var state = states[0] as? FavoritePlayersState.Fetched
        assertEquals(4, state?.size)

        viewModel.deleteFavoritePlayers()
        state = states[1] as? FavoritePlayersState.Fetched
        assertEquals(0, state?.size)
    }

    @Test
    fun testDeleteAllFavoritePlayersWithEmptyFavoritesRepository() {
        val states = mutableListOf<FavoritePlayersState>()

        viewModel.stateLiveData.observeForever {
            states.add(it.favoritePlayersState)
        }

        assertEquals(1, states.size)
        var state = states[0] as? FavoritePlayersState.Fetched
        assertEquals(0, state?.size)

        viewModel.deleteFavoritePlayers()
        assertEquals(2, states.size)
        state = states[1] as? FavoritePlayersState.Fetched
        assertEquals(0, state?.size)
    }

    @Test
    fun testDeleteIdentityWithCharlezardAsIdentity() {
        identityRepository.setIdentity(CHARLEZARD, NORCAL)

        var state: SettingsViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        assertNotNull(state)
        assertTrue(state?.identityState is IdentityState.Fetched)
        assertEquals(CHARLEZARD, (state?.identityState as IdentityState.Fetched).identity)

        viewModel.deleteIdentity()
        assertNotNull(state)
        assertTrue(state?.identityState is IdentityState.Fetched)
        assertNull((state?.identityState as IdentityState.Fetched).identity)
    }

    @Test
    fun testDeleteIdentityWithNoIdentity() {
        var state: SettingsViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        assertNotNull(state)
        assertTrue(state?.identityState is IdentityState.Fetched)
        assertNull((state?.identityState as IdentityState.Fetched).identity)

        viewModel.deleteIdentity()
        assertNotNull(state)
        assertTrue(state?.identityState is IdentityState.Fetched)
        assertNull((state?.identityState as IdentityState.Fetched).identity)
    }

    @Test
    fun testInitialRankingsPollingState() {
        var state: SettingsViewModel.RankingsPollingState? = null

        viewModel.rankingsPollingStateLiveData.observeForever {
            state = it
        }

        assertNotNull(state)
        assertEquals(false, state?.isChargingRequired)
        assertEquals(true, state?.isEnabled)
        assertEquals(false, state?.isVibrationEnabled)
        assertEquals(true, state?.isWifiRequired)
        assertEquals(PollFrequency.DAILY, state?.pollFrequency)
        assertNull(state?.ringtoneUri)
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
        assertTrue(state?.identityState is IdentityState.Fetched)
        assertEquals(CHARLEZARD, (state?.identityState as IdentityState.Fetched).identity)
        assertTrue(state?.favoritePlayersState is FavoritePlayersState.Fetched)
        assertEquals(4, (state?.favoritePlayersState as FavoritePlayersState.Fetched).size)
        assertEquals(nightModeRepository.nightMode, state?.nightMode)
        assertEquals(regionRepository.getRegion(), state?.region)
        assertTrue(state?.smashRosterState is SmashRosterState.Fetched)
        assertNull((state?.smashRosterState as SmashRosterState.Fetched).result)
    }

    @Test
    fun testInitialStateWithEmptyFavoritePlayersRepositoryAndNoIdentity() {
        var state: SettingsViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        assertNotNull(state)
        assertTrue(state?.identityState is IdentityState.Fetched)
        assertNull((state?.identityState as IdentityState.Fetched).identity)
        assertTrue(state?.favoritePlayersState is FavoritePlayersState.Fetched)
        assertEquals(0, (state?.favoritePlayersState as FavoritePlayersState.Fetched).size)
        assertEquals(nightModeRepository.nightMode, state?.nightMode)
        assertEquals(regionRepository.getRegion(), state?.region)
        assertTrue(state?.smashRosterState is SmashRosterState.Fetched)
        assertNull((state?.smashRosterState as SmashRosterState.Fetched).result)
    }

    @Test
    fun testSetNightMode() {
        var state: SettingsViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        viewModel.setNightMode(NightMode.NIGHT_YES)
        assertEquals(NightMode.NIGHT_YES, state?.nightMode)

        viewModel.setNightMode(NightMode.AUTO)
        assertEquals(NightMode.AUTO, state?.nightMode)
    }

    @Test
    fun testSyncSmashRoster() {
        val states = mutableListOf<SmashRosterState>()

        viewModel.stateLiveData.observeForever {
            states.add(it.smashRosterState)
        }

        assertEquals(1, states.size)

        var fetchedState = states[0] as? SmashRosterState.Fetched
        assertNotNull(fetchedState)
        assertNull(fetchedState?.result)

        viewModel.syncSmashRoster()
        assertEquals(5, states.size)
        assertTrue(states[1] is SmashRosterState.Fetching)
        assertTrue(states[2] is SmashRosterState.Syncing)
        assertTrue(states[3] is SmashRosterState.Fetching)

        fetchedState = states[4] as? SmashRosterState.Fetched
        assertNotNull(fetchedState)
        assertEquals(true, fetchedState?.result?.success)
    }

    private class SmashRosterRepositoryOverride(
            internal var garPrRoster: Map<String, SmashCompetitor>? = GAR_PR_ROSTER,
            internal var notGarPrRoster: Map<String, SmashCompetitor>? = NOT_GAR_PR_ROSTER
    ) : SmashRosterRepository {
        override fun getSmashRoster(endpoint: Endpoint): Single<Map<String, SmashCompetitor>> {
            val roster = when (endpoint) {
                Endpoint.GAR_PR -> garPrRoster
                Endpoint.NOT_GAR_PR -> notGarPrRoster
            }

            return if (roster == null) {
                Single.error(NullPointerException())
            } else {
                Single.just(roster)
            }
        }
    }

}
