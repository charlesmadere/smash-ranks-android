package com.garpr.android.extensions

import com.garpr.android.BaseTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class ListTest : BaseTest() {

    @Test
    fun testRequireWithNullItemInList() {
        val list = listOf("Hello", "World", null)
        assertEquals("Hello", list.require(0))
        assertEquals("World", list.require(1))

        var item: Any? = null
        var throwable: Throwable? = null

        try {
            item = list.require(2)
        } catch (t: Throwable) {
            throwable = t
        }

        assertNull(item)
        assertNotNull(throwable)
    }

    @Test
    fun testRequireWithNullList() {
        val list: List<Any?>? = null
        var item: Any? = null
        var throwable: Throwable? = null

        try {
            item = list.require(0)
        } catch (t: Throwable) {
            throwable = t
        }

        assertNull(item)
        assertNotNull(throwable)
    }

    @Test
    fun testRequireWithStringList() {
        val list = listOf("Hello", " ", "World")
        assertEquals("Hello", list.require(0))
        assertEquals(" ", list.require(1))
        assertEquals("World", list.require(2))
    }

}
