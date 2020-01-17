package com.garpr.android.sync.rankings

import androidx.work.Configuration
import androidx.work.WorkRequest
import com.garpr.android.BaseTest
import com.garpr.android.data.models.PollFrequency
import com.garpr.android.misc.Timber
import com.garpr.android.preferences.RankingsPollingPreferenceStore
import com.garpr.android.wrappers.WorkManagerWrapper
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.koin.test.inject

class RankingsPollingManagerTest : BaseTest() {

    private lateinit var rankingsPollingManager: RankingsPollingManager
    private val workManagerWrapper = WorkManagerWrapperOverride()

    protected val rankingsPollingPreferenceStore: RankingsPollingPreferenceStore by inject()
    protected val timber: Timber by inject()

    @Before
    override fun setUp() {
        super.setUp()

        rankingsPollingManager = RankingsPollingManagerImpl(
                rankingsPollingPreferenceStore = rankingsPollingPreferenceStore,
                timber = timber,
                workManagerWrapper = workManagerWrapper
        )
    }

    @Test
    fun testEnableOrDisable() {
        assertNull(workManagerWrapper.status)

        rankingsPollingManager.enableOrDisable()
        assertEquals(true, workManagerWrapper.status)

        rankingsPollingManager.isEnabled = false
        assertEquals(false, workManagerWrapper.status)

        rankingsPollingManager.isEnabled = true
        assertEquals(true, workManagerWrapper.status)
    }

    @Test
    fun testIsChargingRequired() {
        assertFalse(rankingsPollingManager.isChargingRequired)

        rankingsPollingManager.isChargingRequired = true
        assertTrue(rankingsPollingManager.isChargingRequired)

        rankingsPollingManager.isChargingRequired = false
        assertFalse(rankingsPollingManager.isChargingRequired)
    }

    @Test
    fun testIsEnabled() {
        assertTrue(rankingsPollingManager.isEnabled)

        rankingsPollingManager.isEnabled = false
        assertFalse(rankingsPollingManager.isEnabled)

        rankingsPollingManager.isEnabled = true
        assertTrue(rankingsPollingManager.isEnabled)
    }

    @Test
    fun testIsWifiRequired() {
        assertTrue(rankingsPollingManager.isWifiRequired)

        rankingsPollingManager.isWifiRequired = false
        assertFalse(rankingsPollingManager.isWifiRequired)

        rankingsPollingManager.isWifiRequired = true
        assertTrue(rankingsPollingManager.isWifiRequired)
    }

    @Test
    fun testPollFrequency() {
        assertEquals(PollFrequency.DAILY, rankingsPollingManager.pollFrequency)

        rankingsPollingManager.pollFrequency = PollFrequency.EVERY_10_DAYS
        assertEquals(PollFrequency.EVERY_10_DAYS, rankingsPollingManager.pollFrequency)

        rankingsPollingManager.pollFrequency = PollFrequency.EVERY_8_HOURS
        assertEquals(PollFrequency.EVERY_8_HOURS, rankingsPollingManager.pollFrequency)
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
