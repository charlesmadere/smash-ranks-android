package com.garpr.android.extensions

import com.garpr.android.BaseTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CharTest : BaseTest() {

    @Test
    fun testSafeEqualsWithDigits() {
        val a = '1'
        val b = '2'

        assertFalse(a.safeEquals(b, false))
        assertFalse(b.safeEquals(a, false))

        assertFalse(a.safeEquals(b, true))
        assertFalse(b.safeEquals(a, true))
    }

    @Test
    fun testSafeEqualsWithLetters() {
        val a = 'A'
        val b = 'a'

        assertTrue(a.safeEquals(a, false))
        assertTrue(a.safeEquals(a, true))

        assertTrue(b.safeEquals(b, false))
        assertTrue(b.safeEquals(b, true))

        assertFalse(a.safeEquals(b, false))
        assertFalse(b.safeEquals(a, false))

        assertTrue(a.safeEquals(b, true))
        assertTrue(b.safeEquals(a, true))
    }

    @Test
    fun testSafeEqualsWithMixedNullness() {
        val a = 'X'
        val b: Char? = null

        assertFalse(a.safeEquals(b, false))
        assertFalse(b.safeEquals(a, false))

        assertFalse(a.safeEquals(b, true))
        assertFalse(b.safeEquals(a, true))
    }

    @Test
    fun testSafeEqualsWithNull() {
        val a: Char? = null
        val b: Char? = null

        assertTrue(a.safeEquals(b, false))
        assertTrue(b.safeEquals(a, false))

        assertTrue(a.safeEquals(b, true))
        assertTrue(b.safeEquals(a, true))
    }

}
