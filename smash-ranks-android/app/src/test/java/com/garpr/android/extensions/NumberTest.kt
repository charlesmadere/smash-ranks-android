package com.garpr.android.extensions

import com.garpr.android.BaseTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class NumberTest : BaseTest() {

    @Test
    fun testTruncateDoubleMaxValue() {
        assertEquals("179,769,313,486,231,570,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000.000", Double.MAX_VALUE.truncate())
    }

    @Test
    fun testTruncateE() {
        assertEquals("2.718", Math.E.truncate())
    }

    @Test
    fun testTruncateDoubleMinValue() {
        assertEquals("0.000", Double.MIN_VALUE.truncate())
    }

    @Test
    fun testTruncateFloatMaxValue() {
        assertEquals("340,282,346,638,528,860,000,000,000,000,000,000,000.000", Float.MAX_VALUE.truncate())
    }

    @Test
    fun testTruncateFloatMinValue() {
        assertEquals("0.000", Float.MIN_VALUE.truncate())
    }

    @Test
    fun testTruncateImytRanking() {
        assertEquals("31.384", 31.384343063802955f.truncate())
    }

    @Test
    fun testTruncateIntMaxValue() {
        assertEquals("2,147,483,647.000", Int.MAX_VALUE.truncate())
    }

    @Test
    fun testTruncateIntMinValue() {
        assertEquals("-2,147,483,648.000", Int.MIN_VALUE.truncate())
    }

    @Test
    fun testTruncatePi() {
        assertEquals("3.142", Math.PI.truncate())
    }

    @Test
    fun testTruncateSfatRanking() {
        assertEquals("41.907", 41.906930141097256f.truncate())
    }

    @Test
    fun testTruncateZero() {
        assertEquals("0.000", 0.truncate())
    }

}
