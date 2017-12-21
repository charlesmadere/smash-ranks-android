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
    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)
    }

    @Test
    @Throws(Exception::class)
    fun testIsEnabled() {
        assertTrue(smashRosterSyncManager.isEnabled)

        smashRosterSyncManager.isEnabled = false
        assertFalse(smashRosterSyncManager.isEnabled)

        smashRosterSyncManager.isEnabled = true
        assertTrue(smashRosterSyncManager.isEnabled)
    }

    @Test
    @Throws(Exception::class)
    fun testSyncResult() {
        assertNull(smashRosterSyncManager.syncResult)
    }

}
