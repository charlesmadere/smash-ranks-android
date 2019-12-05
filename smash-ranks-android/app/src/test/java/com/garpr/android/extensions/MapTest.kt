package com.garpr.android.extensions

import com.garpr.android.BaseTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MapTest : BaseTest() {

    @Test
    fun testRequireWithNullItemInMap() {
        val map = mapOf(
                0 to "Hello",
                1 to "World",
                2 to null
        )

        var item: Any? = null
        var throwable: Throwable? = null

        try {
            item = map.require(2)
        } catch (t: Throwable) {
            throwable = t
        }

        assertNull(item)
        assertNotNull(throwable)
    }

    @Test
    fun testRequireWithNullMap() {
        val map: Map<Any?, Any?>? = null
        var item: Any? = null
        var throwable: Throwable? = null

        try {
            item = map.require(0)
        } catch (t: Throwable) {
            throwable = t
        }

        assertNull(item)
        assertNotNull(throwable)
    }

    @Test
    fun testRequireWithStringMap() {
        val map = mapOf(
                0 to "Hello",
                1 to " ",
                2 to "World"
        )

        assertEquals("Hello", map.require(0))
        assertEquals(" ", map.require(1))
        assertEquals("World", map.require(2))
    }

}
