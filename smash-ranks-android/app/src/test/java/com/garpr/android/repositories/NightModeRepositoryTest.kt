package com.garpr.android.repositories

import com.garpr.android.BaseTest
import com.garpr.android.data.models.NightMode
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.inject
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class NightModeRepositoryTest : BaseTest() {

    protected val nightModeRepository: NightModeRepository by inject()

    @Test
    fun testInitialNightMode() {
        assertEquals(NightMode.SYSTEM, nightModeRepository.nightMode)
    }

    @Test
    fun testObservable() {
        var value: NightMode? = null

        nightModeRepository.observable.subscribe {
            value = it
        }

        assertEquals(NightMode.SYSTEM, value)

        nightModeRepository.nightMode = NightMode.NIGHT_NO
        assertEquals(NightMode.NIGHT_NO, value)

        nightModeRepository.nightMode = NightMode.AUTO
        assertEquals(NightMode.AUTO, value)
    }

    @Test
    fun testSetNightModeAndGetNightMode() {
        nightModeRepository.nightMode = NightMode.AUTO
        assertEquals(NightMode.AUTO, nightModeRepository.nightMode)

        nightModeRepository.nightMode = NightMode.NIGHT_YES
        assertEquals(NightMode.NIGHT_YES, nightModeRepository.nightMode)

        nightModeRepository.nightMode = NightMode.AUTO
        assertEquals(NightMode.AUTO, nightModeRepository.nightMode)

        nightModeRepository.nightMode = NightMode.SYSTEM
        assertEquals(NightMode.SYSTEM, nightModeRepository.nightMode)

        nightModeRepository.nightMode = NightMode.NIGHT_NO
        assertEquals(NightMode.NIGHT_NO, nightModeRepository.nightMode)
    }

}
