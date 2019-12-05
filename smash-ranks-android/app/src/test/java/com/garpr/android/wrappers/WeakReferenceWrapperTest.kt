package com.garpr.android.wrappers

import com.garpr.android.BaseTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class WeakReferenceWrapperTest : BaseTest() {

    @Test
    fun testEqualsForIntegers() {
        val a = WeakReferenceWrapper(5)
        assertEquals(a, a)

        val b = WeakReferenceWrapper(5)
        assertEquals(a, b)

        val c = WeakReferenceWrapper(Integer.MIN_VALUE)
        val d = WeakReferenceWrapper(Integer.MAX_VALUE)
        assertNotEquals(c, d)

        val e = WeakReferenceWrapper<Int>(null)
        val f = WeakReferenceWrapper<Int>(null)
        assertEquals(e, f)

        assertNotEquals(a, e)
        assertNotEquals(c, f)
    }

    @Test
    fun testEqualsForStrings() {
        val a = WeakReferenceWrapper("Hello, World!")
        assertEquals(a, a)

        val b = WeakReferenceWrapper("Hello, World!")
        assertEquals(a, b)

        val c = WeakReferenceWrapper("abc")
        val d = WeakReferenceWrapper("xyz")
        assertNotEquals(c, d)

        val e = WeakReferenceWrapper("")
        val f = WeakReferenceWrapper("")
        assertEquals(e, f)

        val g = WeakReferenceWrapper<String>(null)
        val h = WeakReferenceWrapper<String>(null)
        assertEquals(g, h)

        assertNotEquals(a, g)
        assertNotEquals(a, h)
        assertNotEquals(c, g)
        assertNotEquals(c, h)
        assertNotEquals(e, g)
        assertNotEquals(e, h)
    }

    @Test
    fun testHashCodeForIntegers() {
        val a = WeakReferenceWrapper(5)
        assertEquals(a.hashCode(), a.hashCode())

        val b = WeakReferenceWrapper(5)
        assertEquals(a.hashCode(), b.hashCode())

        val c = WeakReferenceWrapper(Integer.MIN_VALUE)
        val d = WeakReferenceWrapper(Integer.MAX_VALUE)
        assertNotEquals(c.hashCode(), d.hashCode())
    }

    @Test
    fun testHashCodeForStrings() {
        val a = WeakReferenceWrapper("Hello, World!")
        assertEquals(a.hashCode(), a.hashCode())

        val b = WeakReferenceWrapper("Hello, World!")
        assertEquals(a.hashCode(), b.hashCode())

        val c = WeakReferenceWrapper("abc")
        val d = WeakReferenceWrapper("xyz")
        assertNotEquals(c.hashCode(), d.hashCode())
    }

    @Test
    fun testSetAddForIntegers() {
        val a = WeakReferenceWrapper(0)
        val b = WeakReferenceWrapper(1)

        val set = mutableSetOf<WeakReferenceWrapper<Int>>()
        set.add(a)
        set.add(b)
        assertEquals(2, set.size)

        val c = WeakReferenceWrapper(2)
        set.add(c)
        assertEquals(3, set.size)

        val d = WeakReferenceWrapper(0)
        set.add(d)
        assertEquals(3, set.size)

        set.add(a)
        assertEquals(3, set.size)
    }

    @Test
    fun testSetAddForStrings() {
        val a = WeakReferenceWrapper("Hello")
        val b = WeakReferenceWrapper("World")

        val set = mutableSetOf<WeakReferenceWrapper<String>>()
        set.add(a)
        set.add(b)
        assertEquals(2, set.size)

        val c = WeakReferenceWrapper("Internet")
        set.add(c)
        assertEquals(3, set.size)

        val d = WeakReferenceWrapper("Hello")
        set.add(d)
        assertEquals(3, set.size)

        set.add(a)
        assertEquals(3, set.size)
    }

}
