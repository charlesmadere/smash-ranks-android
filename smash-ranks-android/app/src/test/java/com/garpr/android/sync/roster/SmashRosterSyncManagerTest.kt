package com.garpr.android.sync.roster

import com.garpr.android.BaseTest
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.FullPlayer
import com.garpr.android.data.models.Rating
import com.garpr.android.data.models.SmashCompetitor
import com.garpr.android.misc.Schedulers
import com.garpr.android.misc.Timber
import com.garpr.android.networking.AbsServerApi
import com.garpr.android.preferences.SmashRosterPreferenceStore
import com.garpr.android.wrappers.WorkManagerWrapper
import io.reactivex.Single
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
class SmashRosterSyncManagerTest : BaseTest() {

    private val serverApi = ServerApiOverride()
    private lateinit var smashRosterSyncManager: SmashRosterSyncManager

    protected val schedulers: Schedulers by inject()
    protected val smashRosterPreferenceStore: SmashRosterPreferenceStore by inject()
    protected val smashRosterStorage: SmashRosterStorage by inject()
    protected val timber: Timber by inject()
    protected val workManagerWrapper: WorkManagerWrapper by inject()

    companion object {

        private object Players {
            val CHARLEZARD = FullPlayer(
                    id = "2",
                    name = "Charlezard",
                    aliases = listOf("charles"),
                    ratings = mapOf(
                            "norcal" to Rating(10f, 2f)
                    )
            )

            val GAR = FullPlayer(
                    id = "4",
                    name = "gaR",
                    aliases = emptyList(),
                    ratings = mapOf(
                            "norcal" to Rating(20f, 1.5f),
                            "googlemtv" to Rating(25f, 1f)
                    )
            )

            val HAX = FullPlayer(
                    id = "3",
                    name = "Hax",
                    aliases = listOf("hax$"),
                    ratings = mapOf(
                            "norcal" to Rating(20f, 1.5f),
                            "nyc" to Rating(25f, 1f)
                    )
            )

            val IMYT = FullPlayer(
                    id = "1",
                    name = "Imyt"
            )

            val JAREBAIR = FullPlayer(
                    id = "5",
                    name = "jarebair"
            )
        }

        private object SmashRosters {
            val GAR_PR = mapOf(
                    Players.CHARLEZARD.id to SmashCompetitor(
                            id = Players.CHARLEZARD.id,
                            name = "Charles Madere",
                            tag = "Charlezard"
                    ),

                    Players.GAR.id to SmashCompetitor(
                            id = Players.GAR.id,
                            name = "Ivan Van",
                            tag = "gaR"
                    )
            )

            val NOT_GAR_PR = mapOf(
                    Players.HAX.id to SmashCompetitor(
                            id = Players.HAX.id,
                            name = "Aziz",
                            tag = "Hax"
                    )
            )
        }

    }

    @Before
    override fun setUp() {
        super.setUp()

        smashRosterSyncManager = SmashRosterSyncManagerImpl(schedulers, serverApi,
                smashRosterPreferenceStore, smashRosterStorage, timber, workManagerWrapper)
    }

    @Test
    fun testHajimeteSync() {
        assertTrue(smashRosterPreferenceStore.hajimeteSync.get() == true)
        smashRosterSyncManager.sync().blockingAwait()
        assertTrue(smashRosterPreferenceStore.hajimeteSync.get() == false)
    }

    @Test
    fun testInitialIsEnabled() {
        assertTrue(smashRosterSyncManager.isEnabled)
    }

    @Test
    fun testInitialSyncResult() {
        assertNull(smashRosterSyncManager.syncResult)
    }

    @Test
    fun testInitialSyncState() {
        assertEquals(SmashRosterSyncManager.State.NOT_SYNCING, smashRosterSyncManager.syncState)
    }

    @Test
    fun testIsEnabled() {
        assertTrue(smashRosterSyncManager.isEnabled)

        smashRosterSyncManager.isEnabled = false
        assertFalse(smashRosterSyncManager.isEnabled)

        smashRosterSyncManager.isEnabled = true
        assertTrue(smashRosterSyncManager.isEnabled)
    }

    @Test
    fun testSync() {
        smashRosterSyncManager.sync().blockingAwait()
        assertNotNull(smashRosterSyncManager.syncResult)
        assertTrue(smashRosterSyncManager.syncResult?.success == true)

        serverApi.garPrSmashRoster = emptyMap()
        smashRosterSyncManager.sync().blockingAwait()
        assertNotNull(smashRosterSyncManager.syncResult)
        assertTrue(smashRosterSyncManager.syncResult?.success == false)

        serverApi.notGarPrSmashRoster = emptyMap()
        smashRosterSyncManager.sync().blockingAwait()
        assertNotNull(smashRosterSyncManager.syncResult)
        assertTrue(smashRosterSyncManager.syncResult?.success == false)

        serverApi.garPrSmashRoster = SmashRosters.GAR_PR
        serverApi.notGarPrSmashRoster = SmashRosters.NOT_GAR_PR
        smashRosterSyncManager.sync().blockingAwait()
        assertNotNull(smashRosterSyncManager.syncResult)
        assertTrue(smashRosterSyncManager.syncResult?.success == true)
    }

    private class ServerApiOverride(
            internal var garPrSmashRoster: Map<String, SmashCompetitor>? = SmashRosters.GAR_PR,
            internal var notGarPrSmashRoster: Map<String, SmashCompetitor>? = SmashRosters.NOT_GAR_PR
    ) : AbsServerApi() {

        override fun getSmashRoster(endpoint: Endpoint): Single<Map<String, SmashCompetitor>> {
            val roster = when (endpoint) {
                Endpoint.GAR_PR -> garPrSmashRoster
                Endpoint.NOT_GAR_PR -> notGarPrSmashRoster
            }

            return if (roster == null) {
                Single.error(NullPointerException())
            } else {
                Single.just(roster)
            }
        }

    }

}
