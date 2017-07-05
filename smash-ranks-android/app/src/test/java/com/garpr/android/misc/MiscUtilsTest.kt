package com.garpr.android.misc

import com.garpr.android.BaseTest
import com.garpr.android.BuildConfig
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class MiscUtilsTest : BaseTest() {

    @Test
    @Throws(Exception::class)
    fun testTruncateFloatImytRanking() {
        val value = MiscUtils.truncateFloat(31.384343063802955f)
        assertEquals(value, "31.384")
    }

    @Test
    @Throws(Exception::class)
    fun testTruncateFloatMaxValue() {
        val value = MiscUtils.truncateFloat(java.lang.Float.MAX_VALUE)
        assertEquals(value, "340282346638528860000000000000000000000.000")
    }

    @Test
    @Throws(Exception::class)
    fun testTruncateFloatMinValue() {
        val value = MiscUtils.truncateFloat(java.lang.Float.MIN_VALUE)
        assertEquals(value, "0.000")
    }

    @Test
    @Throws(Exception::class)
    fun testTruncateFloatPi() {
        val value = MiscUtils.truncateFloat(Math.PI.toFloat())
        assertEquals(value, "3.142")
    }

    @Test
    @Throws(Exception::class)
    fun testTruncateFloatSfatRanking() {
        val value = MiscUtils.truncateFloat(41.906930141097256f)
        assertEquals(value, "41.907")
    }

}
