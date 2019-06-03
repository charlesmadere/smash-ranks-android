package com.garpr.android.features.sync.rankings

import com.garpr.android.BaseTest
import com.garpr.android.data.models.PollFrequency
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class RankingsPollingManagerTest : BaseTest() {

    @Inject
    protected lateinit var rankingsPollingManager: RankingsPollingManager


    @Before
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)
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

}
