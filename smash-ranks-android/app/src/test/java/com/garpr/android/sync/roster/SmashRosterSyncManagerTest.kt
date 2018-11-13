package com.garpr.android.sync.roster

import com.garpr.android.BaseTest
import com.garpr.android.misc.SmashRosterStorage
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.misc.Timber
import com.garpr.android.models.Endpoint
import com.garpr.android.models.ServerResponse
import com.garpr.android.models.SmashCompetitor
import com.garpr.android.networking.AbsServerApi
import com.garpr.android.preferences.SmashRosterPreferenceStore
import com.garpr.android.wrappers.WorkManagerWrapper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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
    protected lateinit var gson: Gson

    @Inject
    protected lateinit var smashRosterPreferenceStore: SmashRosterPreferenceStore

    @Inject
    protected lateinit var smashRosterStorage: SmashRosterStorage

    @Inject
    protected lateinit var threadUtils: ThreadUtils

    @Inject
    protected lateinit var timber: Timber

    @Inject
    protected lateinit var workManagerWrapper: WorkManagerWrapper

    private lateinit var serverApiOverride: ServerApiOverride
    private lateinit var smashRosterSyncManager: SmashRosterSyncManager


    companion object {
        private const val JSON_GAR_PR_SMASH_ROSTER = "{\"5888542ad2994e3bbfa52de4\":{\"name\":\"Leon Zhou\",\"tag\":\"ycz6\",\"mains\":[\"sam\"],\"websites\":{\"twitter\":\"https://twitter.com/ycz6\"},\"id\":\"5888542ad2994e3bbfa52de4\"},\"587a951dd2994e15c7dea9fe\":{\"name\":\"Charles Madere\",\"tag\":\"Charlezard\",\"mains\":[\"shk\"],\"websites\":{\"twitch\":\"https://www.twitch.tv/chillinwithcharles\",\"twitter\":\"https://twitter.com/charlesmadere\",\"other\":\"https://github.com/charlesmadere\"},\"avatar\":{\"original\":\"avatars/587a951dd2994e15c7dea9fe/original.jpg\",\"small\":\"avatars/587a951dd2994e15c7dea9fe/small.jpg\",\"medium\":\"avatars/587a951dd2994e15c7dea9fe/medium.jpg\",\"large\":\"avatars/587a951dd2994e15c7dea9fe/large.jpg\"},\"id\":\"587a951dd2994e15c7dea9fe\"},\"588999c5d2994e713ad63c6f\":{\"name\":\"Vincent Chan\",\"tag\":\"Pimp Jong Illest\",\"mains\":[\"fox\",\"ics\",\"dnk\"],\"websites\":{\"twitch\":\"https://www.twitch.tv/commonyoshii\",\"twitter\":\"https://twitter.com/Pimp_Jong_Illst\",\"youtube\":\"https://www.youtube.com/watch?v=-iDtR1yKJM0\",\"other\":\"https://www.youtube.com/watch?v=oX-hCzATFDQ\"},\"avatar\":{\"original\":\"avatars/588999c5d2994e713ad63c6f/original.jpg\",\"small\":\"avatars/588999c5d2994e713ad63c6f/small.jpg\",\"medium\":\"avatars/588999c5d2994e713ad63c6f/medium.jpg\",\"large\":\"avatars/588999c5d2994e713ad63c6f/large.jpg\"},\"id\":\"588999c5d2994e713ad63c6f\"},\"5877eb55d2994e15c7dea98b\":{\"name\":\"Declan Doyle\",\"tag\":\"Imyt\",\"mains\":[\"shk\",\"fox\",\"doc\"],\"websites\":{\"twitch\":\"https://www.twitch.tv/imyt\",\"twitter\":\"https://twitter.com/OnlyImyt\"},\"avatar\":{\"original\":\"avatars/5877eb55d2994e15c7dea98b/original.jpg\",\"small\":\"avatars/5877eb55d2994e15c7dea98b/small.jpg\",\"medium\":\"avatars/5877eb55d2994e15c7dea98b/medium.jpg\"},\"id\":\"5877eb55d2994e15c7dea98b\"},\"588999c5d2994e713ad63b35\":{\"name\":\"Scott\",\"tag\":\"dimsum\",\"mains\":[\"fox\",\"ics\"],\"avatar\":{\"original\":\"avatars/588999c5d2994e713ad63b35/original.jpg\",\"small\":\"avatars/588999c5d2994e713ad63b35/small.jpg\",\"medium\":\"avatars/588999c5d2994e713ad63b35/medium.jpg\",\"large\":\"avatars/588999c5d2994e713ad63b35/large.jpg\"},\"id\":\"588999c5d2994e713ad63b35\"}}"
        private const val JSON_NOT_GAR_PR_SMASH_ROSTER = "{\"5778339fe592575dfd89bd0e\":{\"name\":\"Rishi Fishi\",\"tag\":\"Rishi\",\"mains\":[\"mrt\"],\"websites\":{\"twitch\":\"https://www.twitch.tv/smashg0d\",\"twitter\":\"https://twitter.com/SmashG0D\"},\"id\":\"5778339fe592575dfd89bd0e\"}}"
    }

    @Before
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)

        serverApiOverride = ServerApiOverride(gson)
        serverApiOverride.jsonGarPrSmashRoster = JSON_GAR_PR_SMASH_ROSTER
        serverApiOverride.jsonNotGarPrSmashRoster = JSON_NOT_GAR_PR_SMASH_ROSTER

        smashRosterSyncManager = SmashRosterSyncManagerImpl(serverApiOverride,
                smashRosterPreferenceStore, smashRosterStorage, threadUtils, timber,
                workManagerWrapper)
    }

    @Test
    fun testAddListener() {
        var syncBeginTimes = 0
        var syncCompleteTimes = 0

        val listener = object : SmashRosterSyncManager.OnSyncListeners {
            override fun onSmashRosterSyncBegin(smashRosterSyncManager: SmashRosterSyncManager) {
                ++syncBeginTimes
            }

            override fun onSmashRosterSyncComplete(smashRosterSyncManager: SmashRosterSyncManager) {
                ++syncCompleteTimes
            }
        }

        smashRosterSyncManager.addListener(listener)
        assertEquals(0, syncBeginTimes)
        assertEquals(0, syncCompleteTimes)

        smashRosterSyncManager.sync()
        assertEquals(1, syncBeginTimes)
        assertEquals(1, syncCompleteTimes)

        smashRosterSyncManager.sync()
        assertEquals(2, syncBeginTimes)
        assertEquals(2, syncCompleteTimes)

        smashRosterSyncManager.addListener(listener)
        assertEquals(2, syncBeginTimes)
        assertEquals(2, syncCompleteTimes)

        smashRosterSyncManager.sync()
        assertEquals(3, syncBeginTimes)
        assertEquals(3, syncCompleteTimes)
    }

    @Test
    fun testAddAndRemoveListener() {
        var syncBeginTimes = 0
        var syncCompleteTimes = 0

        val listener = object : SmashRosterSyncManager.OnSyncListeners {
            override fun onSmashRosterSyncBegin(smashRosterSyncManager: SmashRosterSyncManager) {
                ++syncBeginTimes
            }

            override fun onSmashRosterSyncComplete(smashRosterSyncManager: SmashRosterSyncManager) {
                ++syncCompleteTimes
            }
        }

        smashRosterSyncManager.addListener(listener)
        assertEquals(0, syncBeginTimes)
        assertEquals(0, syncCompleteTimes)

        smashRosterSyncManager.removeListener(listener)
        assertEquals(0, syncBeginTimes)
        assertEquals(0, syncCompleteTimes)

        smashRosterSyncManager.sync()
        assertEquals(0, syncBeginTimes)
        assertEquals(0, syncCompleteTimes)

        smashRosterSyncManager.addListener(listener)
        assertEquals(0, syncBeginTimes)
        assertEquals(0, syncCompleteTimes)

        smashRosterSyncManager.sync()
        assertEquals(1, syncBeginTimes)
        assertEquals(1, syncCompleteTimes)

        smashRosterSyncManager.sync()
        assertEquals(2, syncBeginTimes)
        assertEquals(2, syncCompleteTimes)

        smashRosterSyncManager.removeListener(listener)
        assertEquals(2, syncBeginTimes)
        assertEquals(2, syncCompleteTimes)

        smashRosterSyncManager.sync()
        assertEquals(2, syncBeginTimes)
        assertEquals(2, syncCompleteTimes)

        smashRosterSyncManager.removeListener(listener)
        assertEquals(2, syncBeginTimes)
        assertEquals(2, syncCompleteTimes)
    }

    @Test
    fun testHajimeteSync() {
        assertTrue(smashRosterPreferenceStore.hajimeteSync.get() == true)
        smashRosterSyncManager.sync()
        assertTrue(smashRosterPreferenceStore.hajimeteSync.get() == false)
    }

    @Test
    fun testInitialSyncResult() {
        assertNull(smashRosterSyncManager.syncResult)
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
    fun testIsSyncing() {
        assertFalse(smashRosterSyncManager.isSyncing)
        var didSync = false

        val listener = object : SmashRosterSyncManager.OnSyncListeners {
            override fun onSmashRosterSyncBegin(smashRosterSyncManager: SmashRosterSyncManager) {
                didSync = smashRosterSyncManager.isSyncing
            }

            override fun onSmashRosterSyncComplete(smashRosterSyncManager: SmashRosterSyncManager) {
                // intentionally empty
            }
        }

        smashRosterSyncManager.addListener(listener)
        assertFalse(didSync)
        assertFalse(smashRosterSyncManager.isSyncing)

        smashRosterSyncManager.sync()
        assertTrue(didSync)
        assertFalse(smashRosterSyncManager.isSyncing)
    }

    @Test
    fun testSync() {
        serverApiOverride.jsonGarPrSmashRoster = JSON_GAR_PR_SMASH_ROSTER
        serverApiOverride.jsonNotGarPrSmashRoster = JSON_NOT_GAR_PR_SMASH_ROSTER
        smashRosterSyncManager.sync()

        assertNotNull(smashRosterSyncManager.syncResult)
        assertTrue(smashRosterSyncManager.syncResult?.success == true)

        serverApiOverride.jsonGarPrSmashRoster = null
        smashRosterSyncManager.sync()

        assertNotNull(smashRosterSyncManager.syncResult)
        assertTrue(smashRosterSyncManager.syncResult?.success == false)

        serverApiOverride.jsonNotGarPrSmashRoster = null
        smashRosterSyncManager.sync()

        assertNotNull(smashRosterSyncManager.syncResult)
        assertTrue(smashRosterSyncManager.syncResult?.success == false)

        serverApiOverride.jsonGarPrSmashRoster = JSON_GAR_PR_SMASH_ROSTER
        serverApiOverride.jsonNotGarPrSmashRoster = JSON_NOT_GAR_PR_SMASH_ROSTER
        smashRosterSyncManager.sync()

        assertNotNull(smashRosterSyncManager.syncResult)
        assertTrue(smashRosterSyncManager.syncResult?.success == true)
    }

    private class ServerApiOverride(
            private val gson: Gson
    ) : AbsServerApi() {
        var jsonGarPrSmashRoster: String? = null
        var jsonNotGarPrSmashRoster: String? = null

        override fun getSmashRoster(endpoint: Endpoint): ServerResponse<Map<String, SmashCompetitor>> {
            val body: Map<String, SmashCompetitor>?

            when (endpoint) {
                Endpoint.GAR_PR -> {
                    body = if (jsonGarPrSmashRoster?.isNotBlank() == true) {
                        gson.fromJson(jsonGarPrSmashRoster,
                                object : TypeToken<Map<String, SmashCompetitor>>(){}.type)
                    } else {
                        null
                    }
                }

                Endpoint.NOT_GAR_PR -> {
                    body = if (jsonNotGarPrSmashRoster?.isNotBlank() == true) {
                        gson.fromJson(jsonNotGarPrSmashRoster,
                                object : TypeToken<Map<String, SmashCompetitor>>(){}.type)
                    } else {
                        null
                    }
                }
            }

            return ServerResponse(body, body != null, 200)
        }
    }

}
