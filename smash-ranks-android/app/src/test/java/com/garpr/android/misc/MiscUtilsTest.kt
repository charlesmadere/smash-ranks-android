package com.garpr.android.misc

import android.graphics.Color
import com.garpr.android.BaseTest
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MiscUtilsTest : BaseTest() {

    @Test
    fun testHashCodeDouble() {
        val double: Double = Math.PI
        assertEquals(double.hashCode(), MiscUtils.hashCode(double))
    }

    @Test
    fun testHashCodeInteger() {
        val integer = 100
        assertEquals(integer.hashCode(), MiscUtils.hashCode(integer))
    }

    @Test
    fun testHashCodeString() {
        val string = "hello"
        assertEquals(string.hashCode(), MiscUtils.hashCode(string))
    }

    @Test
    fun testIsColorCloserToWhiteWithBlack() {
        assertFalse(MiscUtils.isColorCloserToWhite(Color.BLACK))
    }

    @Test
    fun testIsColorCloserToWhiteWithDarkGray() {
        assertFalse(MiscUtils.isColorCloserToWhite(Color.DKGRAY))
    }

    @Test
    fun testIsColorCloserToWhiteWithLightGray() {
        assertTrue(MiscUtils.isColorCloserToWhite(Color.LTGRAY))
    }

    @Test
    fun testIsColorCloserToWhiteWithWhite() {
        assertTrue(MiscUtils.isColorCloserToWhite(Color.WHITE))
    }

    @Test
    fun testTruncateFloatImytRanking() {
        val value = MiscUtils.truncateFloat(31.384343063802955f)
        assertEquals("31.384", value)
    }

    @Test
    fun testTruncateFloatMaxValue() {
        val value = MiscUtils.truncateFloat(Float.MAX_VALUE)
        assertEquals("340282346638528860000000000000000000000.000", value)
    }

    @Test
    fun testTruncateFloatMinValue() {
        val value = MiscUtils.truncateFloat(Float.MIN_VALUE)
        assertEquals("0.000", value)
    }

    @Test
    fun testTruncateFloatPi() {
        val value = MiscUtils.truncateFloat(Math.PI.toFloat())
        assertEquals("3.142", value)
    }

    @Test
    fun testTruncateFloatSfatRanking() {
        val value = MiscUtils.truncateFloat(41.906930141097256f)
        assertEquals("41.907", value)
    }

}
