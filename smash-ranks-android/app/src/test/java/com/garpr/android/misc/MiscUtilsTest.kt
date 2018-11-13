package com.garpr.android.misc

import android.graphics.Color
import com.garpr.android.BaseTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MiscUtilsTest : BaseTest() {

    @Test
    fun testBrightenOrDarkenColorIfLightnessIsWithBlack() {
        val color = MiscUtils.brightenOrDarkenColorIfLightnessIs(Color.BLACK, 0f, 0f)
        assertEquals(Color.BLACK, color)
    }

    @Test
    fun testBrightenOrDarkenColorIfLightnessIsWithWhite() {
        val color = MiscUtils.brightenOrDarkenColorIfLightnessIs(Color.WHITE, 0f, 0f)
        assertEquals(Color.BLACK, color)
    }

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
    fun testIsColorLightnessWithBlack() {
        assertTrue(MiscUtils.isColorLightness(Color.BLACK, 0f))
        assertFalse(MiscUtils.isColorLightness(Color.BLACK, 1f))
    }

    @Test
    fun testIsColorLightnessWithDarkGray() {
        assertTrue(MiscUtils.isColorLightness(Color.DKGRAY, 0f))
        assertFalse(MiscUtils.isColorLightness(Color.DKGRAY, 1f))
    }

    @Test
    fun testIsColorLightnessWithLightGray() {
        assertTrue(MiscUtils.isColorLightness(Color.LTGRAY, 0f))
        assertFalse(MiscUtils.isColorLightness(Color.LTGRAY, 1f))
    }

    @Test
    fun testIsColorLightnessWithWhite() {
        assertTrue(MiscUtils.isColorLightness(Color.WHITE, 0f))
        assertTrue(MiscUtils.isColorLightness(Color.WHITE, 1f))
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
