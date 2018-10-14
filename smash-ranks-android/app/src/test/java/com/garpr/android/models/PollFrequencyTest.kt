package com.garpr.android.models

import com.garpr.android.BaseTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class PollFrequencyTest : BaseTest() {

    @Test
    fun testTimeInSeconds() {
        assertEquals(28800L, PollFrequency.EVERY_8_HOURS.timeInSeconds)
        assertEquals(86400L, PollFrequency.DAILY.timeInSeconds)
        assertEquals(172800L, PollFrequency.EVERY_2_DAYS.timeInSeconds)
        assertEquals(259200L, PollFrequency.EVERY_3_DAYS.timeInSeconds)
        assertEquals(604800L, PollFrequency.WEEKLY.timeInSeconds)
    }

}
