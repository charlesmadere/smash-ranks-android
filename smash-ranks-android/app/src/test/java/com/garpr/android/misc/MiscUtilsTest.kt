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

}
