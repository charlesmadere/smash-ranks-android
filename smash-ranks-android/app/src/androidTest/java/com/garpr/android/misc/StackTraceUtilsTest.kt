package com.garpr.android.misc

import com.garpr.android.test.BaseAndroidTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.koin.core.inject

class StackTraceUtilsTest : BaseAndroidTest() {

    protected val stackTraceUtils: StackTraceUtils by inject()

    @Test
    fun testToStringWithIllegalArgumentException() {
        val throwable: Throwable = IllegalArgumentException()
        assertFalse(stackTraceUtils.toString(throwable).isBlank())
    }

    @Test
    fun testToStringWithNull() {
        assertTrue(stackTraceUtils.toString(null).isEmpty())
    }

    @Test
    fun testToStringWithRuntimeException() {
        val throwable: Throwable = RuntimeException()
        assertFalse(stackTraceUtils.toString(throwable).isBlank())
    }

    @Test
    fun testToStringWithThrowable() {
        val throwable = Throwable()
        assertFalse(stackTraceUtils.toString(throwable).isBlank())
    }

}
