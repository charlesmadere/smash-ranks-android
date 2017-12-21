package com.garpr.android.sync

import com.garpr.android.BaseTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class RankingsPollingSyncManagerTest : BaseTest() {

    @Inject
    protected lateinit var rankingsPollingSyncManager: RankingsPollingSyncManager


    @Before
    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)
    }

    @Test
    @Throws(Exception::class)
    fun testIsChargingRequired() {
        assertFalse(rankingsPollingSyncManager.isChargingRequired)

        rankingsPollingSyncManager.isChargingRequired = true
        assertTrue(rankingsPollingSyncManager.isChargingRequired)

        rankingsPollingSyncManager.isChargingRequired = false
        assertFalse(rankingsPollingSyncManager.isChargingRequired)
    }

    @Test
    @Throws(Exception::class)
    fun testIsEnabled() {
        assertTrue(rankingsPollingSyncManager.isEnabled)

        rankingsPollingSyncManager.isEnabled = false
        assertFalse(rankingsPollingSyncManager.isEnabled)

        rankingsPollingSyncManager.isEnabled = true
        assertTrue(rankingsPollingSyncManager.isEnabled)
    }

    @Test
    @Throws(Exception::class)
    fun testIsWifiRequired() {
        assertTrue(rankingsPollingSyncManager.isWifiRequired)

        rankingsPollingSyncManager.isWifiRequired = false
        assertFalse(rankingsPollingSyncManager.isWifiRequired)

        rankingsPollingSyncManager.isWifiRequired = true
        assertTrue(rankingsPollingSyncManager.isWifiRequired)
    }

}
