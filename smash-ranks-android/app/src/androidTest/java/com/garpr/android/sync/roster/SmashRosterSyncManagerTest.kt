package com.garpr.android.sync.roster

import androidx.work.Configuration
import androidx.work.WorkRequest
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.FullPlayer
import com.garpr.android.data.models.Rating
import com.garpr.android.data.models.SmashCompetitor
import com.garpr.android.misc.Schedulers
import com.garpr.android.misc.Timber
import com.garpr.android.preferences.SmashRosterPreferenceStore
import com.garpr.android.repositories.SmashRosterRepository
import com.garpr.android.test.BaseAndroidTest
import com.garpr.android.wrappers.WorkManagerWrapper
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.koin.core.inject

class SmashRosterSyncManagerTest : BaseAndroidTest() {

    private val smashRosterRepository = SmashRosterRepositoryOverride()
    private lateinit var smashRosterSyncManager: SmashRosterSyncManager
    private val workManagerWrapper = WorkManagerWrapperOverride()

    protected val schedulers: Schedulers by inject()
    protected val smashRosterPreferenceStore: SmashRosterPreferenceStore by inject()
    protected val smashRosterStorage: SmashRosterStorage by inject()
    protected val timber: Timber by inject()

    companion object {

        private val CHARLEZARD = FullPlayer(
                id = "587a951dd2994e15c7dea9fe",
                name = "Charlezard",
                aliases = listOf("charles"),
                ratings = mapOf(
                        "norcal" to Rating(10f, 2f)
                )
        )

        private val GAR = FullPlayer(
                id = "58523b44d2994e15c7dea945",
                name = "gaR",
                ratings = mapOf(
                        "norcal" to Rating(20f, 1.5f),
                        "googlemtv" to Rating(25f, 1f)
                )
        )

        private val HAX = FullPlayer(
                id = "53c64dba8ab65f6e6651f7bc",
                name = "Hax",
                aliases = listOf("hax$"),
                ratings = mapOf(
                        "norcal" to Rating(20f, 1.5f),
                        "nyc" to Rating(25f, 1f)
                )
        )

        private val IMYT = FullPlayer(
                id = "5877eb55d2994e15c7dea98b",
                name = "Imyt"
        )

        private val JAREBAIR = FullPlayer(
                id = "58a914c7d2994e756952ada0",
                name = "jarebair"
        )

        private val GAR_PR_ROSTER = mapOf(
                CHARLEZARD.id to SmashCompetitor(
                        id = CHARLEZARD.id,
                        name = "Charles",
                        tag = CHARLEZARD.name
                ),
                GAR.id to SmashCompetitor(
                        id = GAR.id,
                        name = "Ivan",
                        tag = GAR.name
                ),
                IMYT.id to SmashCompetitor(
                        id = IMYT.id,
                        name = "Declan",
                        tag = IMYT.name
                ),
                JAREBAIR.id to SmashCompetitor(
                        id = JAREBAIR.id,
                        name = "Jared",
                        tag = JAREBAIR.name
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

        smashRosterSyncManager = SmashRosterSyncManagerImpl(schedulers, smashRosterPreferenceStore,
                smashRosterRepository, smashRosterStorage, timber, workManagerWrapper)
    }

    @Test
    fun testEnableOrDisable() {
        assertNull(workManagerWrapper.status)

        smashRosterSyncManager.enableOrDisable()
        assertEquals(true, workManagerWrapper.status)

        smashRosterSyncManager.isEnabled = false
        assertEquals(false, workManagerWrapper.status)

        smashRosterSyncManager.isEnabled = true
        assertEquals(true, workManagerWrapper.status)
    }

    @Test
    fun testHajimeteSync() {
        assertEquals(true, smashRosterPreferenceStore.hajimeteSync.get())
        smashRosterSyncManager.enableOrDisable()
        assertEquals(false, smashRosterPreferenceStore.hajimeteSync.get())
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
        assertEquals(false, smashRosterSyncManager.syncResult?.success)

        smashRosterRepository.garPrRoster = emptyMap()
        smashRosterSyncManager.sync().blockingAwait()
        assertNotNull(smashRosterSyncManager.syncResult)
        assertEquals(false, smashRosterSyncManager.syncResult?.success)

        smashRosterRepository.notGarPrRoster = emptyMap()
        smashRosterSyncManager.sync().blockingAwait()
        assertNotNull(smashRosterSyncManager.syncResult)
        assertEquals(true, smashRosterSyncManager.syncResult?.success)

        smashRosterRepository.garPrRoster = null
        smashRosterRepository.notGarPrRoster = null
        smashRosterSyncManager.sync().blockingAwait()
        assertNotNull(smashRosterSyncManager.syncResult)
        assertEquals(false, smashRosterSyncManager.syncResult?.success)

        smashRosterRepository.garPrRoster = GAR_PR_ROSTER
        smashRosterRepository.notGarPrRoster = NOT_GAR_PR_ROSTER
        smashRosterSyncManager.sync().blockingAwait()
        assertNotNull(smashRosterSyncManager.syncResult)
        assertEquals(true, smashRosterSyncManager.syncResult?.success)
    }

    private class SmashRosterRepositoryOverride(
            internal var garPrRoster: Map<String, SmashCompetitor>? = null,
            internal var notGarPrRoster: Map<String, SmashCompetitor>? = null
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

    private class WorkManagerWrapperOverride : WorkManagerWrapper {

        internal var status: Boolean? = null

        override val configuration: Configuration
            get() = throw NotImplementedError()

        override fun cancelAllWorkByTag(tag: String) {
            status = false
        }

        override fun enqueue(workRequest: WorkRequest) {
            status = true
        }

    }

}
