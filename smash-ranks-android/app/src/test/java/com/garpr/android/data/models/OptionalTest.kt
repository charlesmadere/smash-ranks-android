package com.garpr.android.data.models

import com.garpr.android.test.BaseTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class OptionalTest : BaseTest() {

    @Test
    fun testGetWithDouble() {
        val optional = Optional.of(Double.MAX_VALUE)
        var throwable: Throwable? = null
        var item: Double? = null

        try {
            item = optional.get()
        } catch (t: Throwable) {
            throwable = t
        }

        assertNull(throwable)
        assertEquals(Double.MAX_VALUE, item)
    }

    @Test
    fun testGetWithNullAbsRegion() {
        val optional = Optional.empty<AbsRegion>()
        var item: AbsRegion? = null

        assertThrows(NoSuchElementException::class.java) {
            item = optional.get()
        }

        assertNull(item)
    }

    @Test
    fun testGetWithEmptyOptional() {
        val optional = Optional.empty<Unit>()
        var item: Unit? = null

        assertThrows(NoSuchElementException::class.java) {
            item = optional.get()
        }

        assertNull(item)
    }

    @Test
    fun testIsPresentWithAbsPlayer() {
        val optional = Optional.of(CHARLEZARD)
        assertTrue(optional.isPresent())
    }

    @Test
    fun testIsPresentWithBoolean() {
        val optional = Optional.of(false)
        assertTrue(optional.isPresent())
    }

    @Test
    fun testIsPresentWithChar() {
        val optional = Optional.of('c')
        assertTrue(optional.isPresent())
    }

    @Test
    fun testIsPresentWithEmptyOptional() {
        val optional = Optional.empty<Unit>()
        assertFalse(optional.isPresent())
    }

    @Test
    fun testIsPresentWithNullInt() {
        val optional = Optional.empty<Int>()
        assertFalse(optional.isPresent())
    }

    @Test
    fun testIsPresentWithNullString() {
        val optional = Optional.empty<String>()
        assertFalse(optional.isPresent())
    }

    @Test
    fun testOfNullableWithAbsRegion() {
        val optional = Optional.ofNullable(NORCAL)
        assertSame(NORCAL, optional.orNull())
    }

    @Test
    fun testOfNullableWithNullFloat() {
        val optional = Optional.ofNullable(null as Float?)
        assertNull(optional.orNull())
    }

    @Test
    fun testOfNullableWithShort() {
        val optional = Optional.ofNullable(Short.MIN_VALUE)
        assertEquals(Short.MIN_VALUE, optional.orNull())
    }

    @Test
    fun testOrElseWithAbsPlayer() {
        val optional = Optional.of(CHARLEZARD)
        assertSame(CHARLEZARD, optional.orElse(IMYT))
    }

    @Test
    fun testOrElseWithNullBoolean() {
        val optional = Optional.ofNullable<Boolean>(null)
        assertEquals(false, optional.orElse(false))
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
    fun testOrNullWithAbsRegion() {
        val optional = Optional.of(NORCAL)
        assertSame(NORCAL, optional.orNull())
    }

    @Test
    fun testOrNullWithEmptyOptional() {
        val optional = Optional.empty<Unit>()
        assertNull(optional.orNull())
    }

    @Test
    fun testOrNullWithLong() {
        val optional = Optional.of(Long.MAX_VALUE)
        assertEquals(Long.MAX_VALUE, optional.orNull())
    }

    @Test
    fun testOrNullWithNullFloat() {
        val optional = Optional.empty<Float>()
        assertNull(optional.orNull())
    }

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

}
