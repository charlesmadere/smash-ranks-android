package com.garpr.android.sync

import com.garpr.android.BaseTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class SmashRosterSyncManagerTest : BaseTest() {

    @Inject
    protected lateinit var smashRosterSyncManager: SmashRosterSyncManager


    @Before
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)
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
    fun testIsEnabled() {
        assertTrue(smashRosterSyncManager.isEnabled)

        smashRosterSyncManager.isEnabled = false
        assertFalse(smashRosterSyncManager.isEnabled)

        smashRosterSyncManager.isEnabled = true
        assertTrue(smashRosterSyncManager.isEnabled)
    }

    @Test
    fun testSyncResult() {
        assertNull(smashRosterSyncManager.syncResult)
    }

}
