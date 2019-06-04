package com.garpr.android.managers

import android.app.Application
import com.garpr.android.BaseTest
import com.garpr.android.data.models.NightMode
import com.garpr.android.repositories.NightModeManager
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class NightModeManagerTest : BaseTest() {

    @Inject
    protected lateinit var application: Application

    @Inject
    protected lateinit var nightModeManager: NightModeManager


    @Before
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)
    }

    @Test
    fun testAddListener() {
        var nightMode: NightMode? = null

        val listener = object : NightModeManager.OnNightModeChangeListener {
            override fun onNightModeChange(nightModeManager: NightModeManager) {
                nightMode = nightModeManager.nightMode
            }
        }

        nightModeManager.addListener(listener)
        assertNull(nightMode)

        nightModeManager.nightMode = NightMode.NIGHT_NO
        assertEquals(NightMode.NIGHT_NO, nightMode)
    }

    @Test
    fun testAddListenerTwice() {
        var count = 0

        val listener = object : NightModeManager.OnNightModeChangeListener {
            override fun onNightModeChange(nightModeManager: NightModeManager) {
                ++count
            }
        }

        nightModeManager.addListener(listener)
        nightModeManager.addListener(listener)
        nightModeManager.nightMode = NightMode.SYSTEM
        assertEquals(1, count)
    }

    @Test
    fun testGetNightMode() {
        assertNotNull(nightModeManager.nightMode)
    }

    @Test
    fun testGetNightModeStrings() {
        val strings = nightModeManager.getNightModeStrings(application)
        assertFalse(strings.isNullOrEmpty())

        strings.forEach {
            assertFalse(it.isBlank())
        }
    }

    @Test
    fun testRemoveListener() {
        var nightMode: NightMode? = null

        val listener = object : NightModeManager.OnNightModeChangeListener {
            override fun onNightModeChange(nightModeManager: NightModeManager) {
                nightMode = nightModeManager.nightMode
            }
        }

        nightModeManager.addListener(listener)
        assertNull(nightMode)

        nightModeManager.nightMode = NightMode.NIGHT_YES
        assertEquals(NightMode.NIGHT_YES, nightMode)

        nightModeManager.removeListener(listener)
        nightModeManager.nightMode = NightMode.AUTO
        assertEquals(NightMode.NIGHT_YES, nightMode)
    }

    @Test
    fun testSetNightMode() {
        nightModeManager.nightMode = NightMode.AUTO
        assertEquals(NightMode.AUTO, nightModeManager.nightMode)

        nightModeManager.nightMode = NightMode.NIGHT_YES
        assertEquals(NightMode.NIGHT_YES, nightModeManager.nightMode)

        nightModeManager.nightMode = NightMode.AUTO
        assertEquals(NightMode.AUTO, nightModeManager.nightMode)

        nightModeManager.nightMode = NightMode.SYSTEM
        assertEquals(NightMode.SYSTEM, nightModeManager.nightMode)
    }

}
