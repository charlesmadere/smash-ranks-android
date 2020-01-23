package com.garpr.android.features.settings

import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.LitePlayer
import com.garpr.android.data.models.NightMode
import com.garpr.android.data.models.PollFrequency
import com.garpr.android.data.models.Region
import com.garpr.android.data.models.SmashCompetitor
import com.garpr.android.extensions.toJavaUri
import com.garpr.android.features.common.viewModels.BaseViewModelTest
import com.garpr.android.features.settings.SettingsViewModel.FavoritePlayersState
import com.garpr.android.features.settings.SettingsViewModel.IdentityState
import com.garpr.android.features.settings.SettingsViewModel.RankingsPollingState
import com.garpr.android.features.settings.SettingsViewModel.SmashRosterState
import com.garpr.android.misc.Schedulers
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
import org.koin.core.inject

class SettingsViewModelTest : BaseViewModelTest() {

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

        // Ringtone URIs taken from Android using the debugger
        private const val BEAT_BOX_ANDROID = "content://media/internal/audio/media/60"
        private const val DEFAULT_NOTIFICATION_SOUND = "content://settings/system/notification_sound"
    }

    @Before
    override fun setUp() {
        super.setUp()

        val smashRosterSyncManager: SmashRosterSyncManager = SmashRosterSyncManagerImpl(
                schedulers, smashRosterPreferenceStore, smashRosterRepository, smashRosterStorage,
                timber, workManagerWrapper)

        viewModel = SettingsViewModel(favoritePlayersRepository, identityRepository,
                nightModeRepository, rankingsPollingManager, regionRepository, schedulers,
                smashRosterSyncManager, timber)
    }

    @Test
    fun testDeleteAllFavoritePlayers() {
        favoritePlayersRepository.addPlayer(CHARLEZARD, NORCAL)
        favoritePlayersRepository.addPlayer(IMYT, NORCAL)
        favoritePlayersRepository.addPlayer(MIKKUZ, NORCAL)
        favoritePlayersRepository.addPlayer(SNAP, NORCAL)

        val states = mutableListOf<FavoritePlayersState>()

        viewModel.favoritePlayersStateLiveData.observeForever {
            states.add(it)
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

        viewModel.favoritePlayersStateLiveData.observeForever {
            states.add(it)
        }

        assertEquals(1, states.size)
        var state = states[0] as FavoritePlayersState.Fetched
        assertEquals(0, state.size)

        viewModel.deleteFavoritePlayers()
        assertEquals(2, states.size)
        state = states[1] as FavoritePlayersState.Fetched
        assertEquals(0, state.size)
    }

    @Test
    fun testDeleteIdentityWithCharlezardAsIdentity() {
        identityRepository.setIdentity(CHARLEZARD, NORCAL)

        val states = mutableListOf<IdentityState>()

        viewModel.identityStateLiveData.observeForever {
            states.add(it)
        }

        assertEquals(1, states.size)
        var state = states[0] as IdentityState.Fetched
        assertEquals(CHARLEZARD, state.identity)

        viewModel.deleteIdentity()
        assertEquals(2, states.size)
        state = states[1] as IdentityState.Fetched
        assertNull(state.identity)
    }

    @Test
    fun testDeleteIdentityWithNoIdentity() {
        val states = mutableListOf<IdentityState>()

        viewModel.identityStateLiveData.observeForever {
            states.add(it)
        }

        assertEquals(1, states.size)
        var state = states[0] as IdentityState.Fetched
        assertNull(state.identity)

        viewModel.deleteIdentity()
        assertEquals(2, states.size)
        state = states[1] as IdentityState.Fetched
        assertNull(state.identity)
    }

    @Test
    fun testInitialNightMode() {
        var nightMode: NightMode? = null

        viewModel.nightModeLiveData.observeForever {
            nightMode = it
        }

        assertEquals(NightMode.SYSTEM, nightMode)
    }

    @Test
    fun testInitialRankingsPollingState() {
        var state: RankingsPollingState? = null

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
    fun testInitialRegion() {
        var region: Region? = null

        viewModel.regionLiveData.observeForever {
            region = it
        }

        assertEquals(NORCAL, region)
    }

    @Test
    fun testInitialFavoritePlayersState() {
        favoritePlayersRepository.addPlayer(IMYT, NORCAL)
        favoritePlayersRepository.addPlayer(SNAP, NORCAL)

        val states = mutableListOf<FavoritePlayersState>()

        viewModel.favoritePlayersStateLiveData.observeForever {
            states.add(it)
        }

        assertEquals(1, states.size)
        assertEquals(2, (states[0] as FavoritePlayersState.Fetched).size)
    }

    @Test
    fun testInitialFavoritePlayersStateWithNoFavoritePlayers() {
        val states = mutableListOf<FavoritePlayersState>()

        viewModel.favoritePlayersStateLiveData.observeForever {
            states.add(it)
        }

        assertEquals(1, states.size)
        assertEquals(0, (states[0] as FavoritePlayersState.Fetched).size)
    }

    @Test
    fun testInitialIdentityStateWithMikkuzAsIdentity() {
        identityRepository.setIdentity(MIKKUZ, NORCAL)

        val states = mutableListOf<IdentityState>()

        viewModel.identityStateLiveData.observeForever {
            states.add(it)
        }

        assertEquals(1, states.size)
        assertEquals(MIKKUZ, (states[0] as IdentityState.Fetched).identity)
    }

    @Test
    fun testInitialIdentityStateWithNoIdentity() {
        val states = mutableListOf<IdentityState>()

        viewModel.identityStateLiveData.observeForever {
            states.add(it)
        }

        assertEquals(1, states.size)
        assertNull((states[0] as IdentityState.Fetched).identity)
    }

    @Test
    fun testInitialSmashRosterState() {
        val states = mutableListOf<SmashRosterState>()

        viewModel.smashRosterStateLiveData.observeForever {
            states.add(it)
        }

        assertEquals(1, states.size)
        assertNull((states[0] as SmashRosterState.Fetched).result)
    }

    @Test
    fun testSetNightMode() {
        var nightMode: NightMode? = null

        viewModel.nightModeLiveData.observeForever {
            nightMode = it
        }

        viewModel.setNightMode(NightMode.NIGHT_YES)
        assertEquals(NightMode.NIGHT_YES, nightMode)

        viewModel.setNightMode(NightMode.AUTO)
        assertEquals(NightMode.AUTO, nightMode)
    }

    @Test
    fun testSetRankingsPollingIsChargingRequired() {
        var state: RankingsPollingState? = null

        viewModel.rankingsPollingStateLiveData.observeForever {
            state = it
        }

        assertEquals(false, state?.isChargingRequired)

        viewModel.setRankingsPollingIsChargingRequired(true)
        assertEquals(true, state?.isChargingRequired)

        viewModel.setRankingsPollingIsChargingRequired(false)
        assertEquals(false, state?.isChargingRequired)
    }

    @Test
    fun testSetRankingsPollingIsEnabled() {
        var state: RankingsPollingState? = null

        viewModel.rankingsPollingStateLiveData.observeForever {
            state = it
        }

        assertEquals(true, state?.isEnabled)

        viewModel.setRankingsPollingIsEnabled(false)
        assertEquals(false, state?.isEnabled)

        viewModel.setRankingsPollingIsEnabled(true)
        assertEquals(true, state?.isEnabled)
    }

    @Test
    fun testSetRankingsPollingIsVibrationEnabled() {
        var state: RankingsPollingState? = null

        viewModel.rankingsPollingStateLiveData.observeForever {
            state = it
        }

        assertEquals(false, state?.isVibrationEnabled)

        viewModel.setRankingsPollingIsVibrationEnabled(true)
        assertEquals(true, state?.isVibrationEnabled)

        viewModel.setRankingsPollingIsVibrationEnabled(false)
        assertEquals(false, state?.isVibrationEnabled)
    }

    @Test
    fun testSetRankingsPollingIsWifiRequired() {
        var state: RankingsPollingState? = null

        viewModel.rankingsPollingStateLiveData.observeForever {
            state = it
        }

        assertEquals(true, state?.isWifiRequired)

        viewModel.setRankingsPollingIsWifiRequired(false)
        assertEquals(false, state?.isWifiRequired)

        viewModel.setRankingsPollingIsWifiRequired(true)
        assertEquals(true, state?.isWifiRequired)
    }

    @Test
    fun testSetRankingsPollingPollFrequency() {
        var state: RankingsPollingState? = null

        viewModel.rankingsPollingStateLiveData.observeForever {
            state = it
        }

        assertEquals(PollFrequency.DAILY, state?.pollFrequency)

        viewModel.setRankingsPollingPollFrequency(PollFrequency.EVERY_3_DAYS)
        assertEquals(PollFrequency.EVERY_3_DAYS, state?.pollFrequency)

        viewModel.setRankingsPollingPollFrequency(PollFrequency.EVERY_2_WEEKS)
        assertEquals(PollFrequency.EVERY_2_WEEKS, state?.pollFrequency)
    }

    @Test
    fun testSetRankingsPollingRingtoneUri() {
        var state: RankingsPollingState? = null

        viewModel.rankingsPollingStateLiveData.observeForever {
            state = it
        }

        assertNull(state?.ringtoneUri)

        viewModel.setRankingsPollingRingtoneUri(DEFAULT_NOTIFICATION_SOUND)
        assertEquals(DEFAULT_NOTIFICATION_SOUND.toJavaUri(), state?.ringtoneUri)

        viewModel.setRankingsPollingRingtoneUri(BEAT_BOX_ANDROID)
        assertEquals(BEAT_BOX_ANDROID.toJavaUri(), state?.ringtoneUri)

        viewModel.setRankingsPollingRingtoneUri(null)
        assertNull(state?.ringtoneUri)
    }

    @Test
    fun testSyncSmashRoster() {
        val states = mutableListOf<SmashRosterState>()

        viewModel.smashRosterStateLiveData.observeForever {
            states.add(it)
        }

        assertEquals(1, states.size)

        var fetchedState = states[0] as? SmashRosterState.Fetched
        assertNotNull(fetchedState)
        assertNull(fetchedState?.result)

        viewModel.syncSmashRoster()
        assertEquals(3, states.size)
        assertTrue(states[1] is SmashRosterState.Syncing)

        fetchedState = states[2] as SmashRosterState.Fetched
        assertEquals(true, fetchedState.result?.success)
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
