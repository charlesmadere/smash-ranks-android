package com.garpr.android.managers

import android.app.Application
import com.garpr.android.BaseTest
import com.garpr.android.data.models.NightMode
import com.garpr.android.repositories.NightModeRepository
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
class NightModeRepositoryTest : BaseTest() {

    @Inject
    protected lateinit var application: Application

    @Inject
    protected lateinit var nightModeRepository: NightModeRepository


    @Before
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)
    }

    @Test
    fun testAddListener() {
        var nightMode: NightMode? = null

        val listener = object : NightModeRepository.OnNightModeChangeListener {
            override fun onNightModeChange(nightModeRepository: NightModeRepository) {
                nightMode = nightModeRepository.nightMode
            }
        }

        nightModeRepository.addListener(listener)
        assertNull(nightMode)

        nightModeRepository.nightMode = NightMode.NIGHT_NO
        assertEquals(NightMode.NIGHT_NO, nightMode)
    }

    @Test
    fun testAddListenerTwice() {
        var count = 0

        val listener = object : NightModeRepository.OnNightModeChangeListener {
            override fun onNightModeChange(nightModeRepository: NightModeRepository) {
                ++count
            }
        }

        nightModeRepository.addListener(listener)
        nightModeRepository.addListener(listener)
        nightModeRepository.nightMode = NightMode.SYSTEM
        assertEquals(1, count)
    }

    @Test
    fun testGetNightMode() {
        assertNotNull(nightModeRepository.nightMode)
    }

    @Test
    fun testGetNightModeStrings() {
        val strings = nightModeRepository.getNightModeStrings(application)
        assertFalse(strings.isNullOrEmpty())

        strings.forEach {
            assertFalse(it.isBlank())
        }
    }

    @Test
    fun testRemoveListener() {
        var nightMode: NightMode? = null

        val listener = object : NightModeRepository.OnNightModeChangeListener {
            override fun onNightModeChange(nightModeRepository: NightModeRepository) {
                nightMode = nightModeRepository.nightMode
            }
        }

        nightModeRepository.addListener(listener)
        assertNull(nightMode)

        nightModeRepository.nightMode = NightMode.NIGHT_YES
        assertEquals(NightMode.NIGHT_YES, nightMode)

        nightModeRepository.removeListener(listener)
        nightModeRepository.nightMode = NightMode.AUTO
        assertEquals(NightMode.NIGHT_YES, nightMode)
    }

    @Test
    fun testSetNightMode() {
        nightModeRepository.nightMode = NightMode.AUTO
        assertEquals(NightMode.AUTO, nightModeRepository.nightMode)

        nightModeRepository.nightMode = NightMode.NIGHT_YES
        assertEquals(NightMode.NIGHT_YES, nightModeRepository.nightMode)

        nightModeRepository.nightMode = NightMode.AUTO
        assertEquals(NightMode.AUTO, nightModeRepository.nightMode)

        nightModeRepository.nightMode = NightMode.SYSTEM
        assertEquals(NightMode.SYSTEM, nightModeRepository.nightMode)
    }

}
