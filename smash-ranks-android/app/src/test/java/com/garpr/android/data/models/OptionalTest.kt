package com.garpr.android.data.models

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

class OptionalTest {

    companion object {
        private val CHARLEZARD: AbsPlayer = LitePlayer(
                id = "587a951dd2994e15c7dea9fe",
                name = "Charlezard"
        )

        private val IMYT: AbsPlayer = LitePlayer(
                id = "5877eb55d2994e15c7dea98b",
                name = "Imyt"
        )

        private val NORCAL: AbsRegion = LiteRegion(
                id = "norcal",
                displayName = "Norcal"
        )
    }

    @Test
    fun testItemWithAbsRegion() {
        val optional = Optional.of(NORCAL)
        assertSame(NORCAL, optional.item)
    }

    @Test
    fun testItemWithEmptyOptional() {
        val optional = Optional.empty<Unit>()
        assertNull(optional.item)
    }

    @Test
    fun testItemWithLong() {
        val optional = Optional.of(Long.MAX_VALUE)
        assertEquals(Long.MAX_VALUE, optional.item)
    }

    @Test
    fun testItemWithNullFloat() {
        val optional = Optional.empty<Float>()
        assertNull(optional.item)
    }

    @Test
    fun testIsPresentWithAbsPlayer() {
        val optional = Optional.of(CHARLEZARD)
        assertTrue(optional.isPresent)
    }

    @Test
    fun testIsPresentWithBoolean() {
        val optional = Optional.of(false)
        assertTrue(optional.isPresent)
    }

    @Test
    fun testIsPresentWithChar() {
        val optional = Optional.of('c')
        assertTrue(optional.isPresent)
    }

    @Test
    fun testIsPresentWithEmptyOptional() {
        val optional = Optional.empty<Unit>()
        assertFalse(optional.isPresent)
    }

    @Test
    fun testIsPresentWithNullInt() {
        val optional = Optional.empty<Int>()
        assertFalse(optional.isPresent)
    }

    @Test
    fun testIsPresentWithNullString() {
        val optional = Optional.empty<String>()
        assertFalse(optional.isPresent)
    }

    @Test
    fun testOfNullableWithAbsRegion() {
        val optional = Optional.ofNullable(NORCAL)
        assertSame(NORCAL, optional.item)
    }

    @Test
    fun testOfNullableWithNullFloat() {
        val optional = Optional.ofNullable(null as Float?)
        assertNull(optional.item)
    }

    @Test
    fun testOfNullableWithShort() {
        val optional = Optional.ofNullable(Short.MIN_VALUE)
        assertEquals(Short.MIN_VALUE, optional.item)
    }

    @Test
    fun testOrElseWithAbsPlayer() {
        val optional = Optional.of(CHARLEZARD)
        assertSame(CHARLEZARD, optional.orElse(IMYT))
    }

    @Test
    fun testOrElseWithInt() {
        val optional = Optional.of(100)
        assertEquals(100, optional.orElse(0))
    }

    @Test
    fun testOrElseWithNullString() {
        val optional = Optional.empty<String>()
        assertEquals("hello", optional.orElse("hello"))
    }

    @Test
    fun testRequireWithDouble() {
        val optional = Optional.of(Double.MAX_VALUE)
        var throwable: Throwable? = null
        var item: Double? = null

        try {
            item = optional.require()
        } catch (t: Throwable) {
            throwable = t
        }

        assertNull(throwable)
        assertEquals(Double.MAX_VALUE, item)
    }

    @Test
    fun testRequireWithNullAbsRegion() {
        val optional = Optional.empty<AbsRegion>()
        var throwable: Throwable? = null
        var item: AbsRegion? = null

        try {
            item = optional.require()
        } catch (t: Throwable) {
            throwable = t
        }

        assertNotNull(throwable)
        assertNull(item)
    }

    @Test
    fun testRequireWithEmptyOptional() {
        val optional = Optional.empty<Unit>()
        var throwable: Throwable? = null
        var item: Unit? = null

        try {
            item = optional.require()
        } catch (t: Throwable) {
            throwable = t
        }

        assertNotNull(throwable)
        assertNull(item)
    }

}
