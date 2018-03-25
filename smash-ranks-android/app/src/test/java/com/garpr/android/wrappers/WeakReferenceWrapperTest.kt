package com.garpr.android.wrappers

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class WeakReferenceWrapperTest {

    @Test
    fun testEqualsForIntegers() {
        val a = WeakReferenceWrapper(5)
        val b = WeakReferenceWrapper(5)
        assertEquals(a, b)

        val c = WeakReferenceWrapper(10)
        val d = WeakReferenceWrapper(20)
        assertNotEquals(c, d)
    }

    @Test
    fun testEqualsForStrings() {
        val a = WeakReferenceWrapper("Hello, World!")
        val b = WeakReferenceWrapper("Hello, World!")
        assertEquals(a, b)

        val c = WeakReferenceWrapper("abc")
        val d = WeakReferenceWrapper("xyz")
        assertNotEquals(c, d)
    }

    @Test
    fun testHashCodeForIntegers() {
        val a = WeakReferenceWrapper(5)
        val b = WeakReferenceWrapper(5)
        assertEquals(a.hashCode(), b.hashCode())

        val c = WeakReferenceWrapper(Integer.MIN_VALUE)
        val d = WeakReferenceWrapper(Integer.MAX_VALUE)
        assertNotEquals(c.hashCode(), d.hashCode())
    }

    @Test
    fun testHashCodeForStrings() {
        val a = WeakReferenceWrapper("Hello, World!")
        val b = WeakReferenceWrapper("Hello, World!")
        assertEquals(a.hashCode(), b.hashCode())

        val c = WeakReferenceWrapper("abc")
        val d = WeakReferenceWrapper("xyz")
        assertNotEquals(c.hashCode(), d.hashCode())
    }

}
