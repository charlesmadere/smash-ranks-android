package com.garpr.android.sync.roster

import com.garpr.android.BaseTest
import com.garpr.android.data.models.Avatar
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.FullPlayer
import com.garpr.android.data.models.Rating
import com.garpr.android.data.models.Region
import com.garpr.android.data.models.SmashCharacter
import com.garpr.android.data.models.SmashCompetitor
import com.garpr.android.misc.Timber
import com.garpr.android.networking.AbsServerApi2
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
import org.robolectric.RobolectricTestRunner
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class SmashRosterSyncManagerTest : BaseTest() {

    @Inject
    protected lateinit var smashRosterPreferenceStore: SmashRosterPreferenceStore

    @Inject
    protected lateinit var smashRosterStorage: SmashRosterStorage

    @Inject
    protected lateinit var timber: Timber

    @Inject
    protected lateinit var workManagerWrapper: WorkManagerWrapper

    private val serverApiOverride = ServerApiOverride()
    private lateinit var smashRosterSyncManager: SmashRosterSyncManager

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
                    aliases = listOf(),
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

        private object Regions {
            val ATLANTA = Region(
                    displayName = "Atlanta",
                    id = "atlanta",
                    endpoint = Endpoint.NOT_GAR_PR
            )

            val NORCAL = Region(
                    displayName = "Norcal",
                    id = "norcal",
                    endpoint = Endpoint.GAR_PR
            )

            val NYC = Region(
                    displayName = "New York City",
                    id = "nyc",
                    endpoint = Endpoint.NOT_GAR_PR
            )
        }

        private object SmashCompetitors {
            val CHARLEZARD = SmashCompetitor(
                    mains = listOf(
                            SmashCharacter.SHEIK
                    ),
                    websites = mapOf(
                            "twitch" to "https://www.twitch.tv/chillinwithcharles",
                            "twitter" to "https://twitter.com/charlesmadere"
                    ),
                    id = Players.CHARLEZARD.id,
                    name = "Charles Madere",
                    tag = Players.CHARLEZARD.name
            )

            val HAX = SmashCompetitor(
                    mains = listOf(
                            SmashCharacter.FOX
                    ),
                    websites = mapOf(
                            "twitter" to "https://twitter.com/ssbmhax",
                            "youtube" to "https://www.youtube.com/channel/UCVJOIYcecIVO96ktK0qDKhQ"
                    ),
                    id = Players.HAX.id,
                    name = "Aziz Al-Yami",
                    tag = Players.HAX.name
            )

            val IMYT = SmashCompetitor(
                    mains = listOf(
                            SmashCharacter.SHEIK,
                            SmashCharacter.FOX
                    ),
                    websites = mapOf(
                            "twitch" to "https://www.twitch.tv/imyt",
                            "twitter" to "https://twitter.com/OnlyImyt"
                    ),
                    id = Players.IMYT.id,
                    name = "Declan Doyle",
                    tag = Players.IMYT.name
            )

            val JAREBAIR = SmashCompetitor(
                    avatar = Avatar(
                            original = "original.jpg"
                    ),
                    mains = listOf(
                            SmashCharacter.FALCO,
                            SmashCharacter.FOX
                    ),
                    id = Players.JAREBAIR.id,
                    name = "Jared",
                    tag = Players.JAREBAIR.name,
                    websites = mapOf(
                            "twitch" to "https://www.twitch.tv/prozd",
                            "twitter" to "https://twitter.com/prozdkp",
                            "youtube" to "https://www.youtube.com/user/ProZD"
                    )
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
        testAppComponent.inject(this)

        smashRosterSyncManager = SmashRosterSyncManagerImpl(serverApiOverride,
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

        serverApiOverride.garPrSmashRoster = emptyMap()
        smashRosterSyncManager.sync().blockingAwait()
        assertNotNull(smashRosterSyncManager.syncResult)
        assertTrue(smashRosterSyncManager.syncResult?.success == false)

        serverApiOverride.notGarPrSmashRoster = emptyMap()
        smashRosterSyncManager.sync().blockingAwait()
        assertNotNull(smashRosterSyncManager.syncResult)
        assertTrue(smashRosterSyncManager.syncResult?.success == false)

        serverApiOverride.garPrSmashRoster = SmashRosters.GAR_PR
        serverApiOverride.notGarPrSmashRoster = SmashRosters.NOT_GAR_PR
        smashRosterSyncManager.sync().blockingAwait()
        assertNotNull(smashRosterSyncManager.syncResult)
        assertTrue(smashRosterSyncManager.syncResult?.success == true)
    }

    private class ServerApiOverride(
            internal var garPrSmashRoster: Map<String, SmashCompetitor> = SmashRosters.GAR_PR,
            internal var notGarPrSmashRoster: Map<String, SmashCompetitor> = SmashRosters.NOT_GAR_PR
    ) : AbsServerApi2() {

        override fun getSmashRoster(endpoint: Endpoint): Single<Map<String, SmashCompetitor>> {
            return Single.just(when (endpoint) {
                Endpoint.GAR_PR -> garPrSmashRoster
                Endpoint.NOT_GAR_PR -> notGarPrSmashRoster
            })
        }

    }

}
