package com.garpr.android.data.models

import com.garpr.android.test.BaseTest
import org.junit.Assert.assertEquals
import org.junit.Test

class PollFrequencyTest : BaseTest() {

    @Test
    fun testTimeInSeconds() {
        assertEquals(28800L, PollFrequency.EVERY_8_HOURS.timeInSeconds)
        assertEquals(86400L, PollFrequency.DAILY.timeInSeconds)
        assertEquals(172800L, PollFrequency.EVERY_2_DAYS.timeInSeconds)
        assertEquals(259200L, PollFrequency.EVERY_3_DAYS.timeInSeconds)
        assertEquals(432000L, PollFrequency.EVERY_5_DAYS.timeInSeconds)
        assertEquals(604800L, PollFrequency.WEEKLY.timeInSeconds)
        assertEquals(864000L, PollFrequency.EVERY_10_DAYS.timeInSeconds)
        assertEquals(1209600L, PollFrequency.EVERY_2_WEEKS.timeInSeconds)
    }

}
