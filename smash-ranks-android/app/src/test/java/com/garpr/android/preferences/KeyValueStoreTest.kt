package com.garpr.android.preferences

import com.garpr.android.BaseTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class KeyValueStoreTest : BaseTest() {

    @Inject
    protected lateinit var keyValueStore: KeyValueStore


    @Before
    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)
    }

    @Test
    @Throws(Exception::class)
    fun testAll() {
        val all = keyValueStore.all
        assertTrue(all == null || all.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun testAllAndSet() {
        var all = keyValueStore.all
        assertTrue(all == null || all.isEmpty())

        keyValueStore.setFloat("float", Float.MIN_VALUE)
        all = keyValueStore.all
        assertNotNull(all)
        assertEquals(1, all?.size)

        keyValueStore.setString("String", "Hello, World!")
        all = keyValueStore.all
        assertNotNull(all)
        assertEquals(2, all?.size)
    }

    @Test
    @Throws(Exception::class)
    fun testBatchEditNotNull() {
        assertNotNull(keyValueStore.batchEdit())
    }

    @Test
    @Throws(Exception::class)
    fun testBatchEditPutAndApplyWithInteger() {
        assertFalse(keyValueStore.contains("String"))

        keyValueStore.batchEdit()
                .putInteger("Integer", 100)
                .apply()

        assertTrue(keyValueStore.contains("Integer"))
    }

    @Test
    @Throws(Exception::class)
    fun testBatchEditPutAndApplyWithString() {
        assertFalse(keyValueStore.contains("String"))

        keyValueStore.batchEdit()
                .putString("String", "Hello, World")
                .apply()

        assertTrue(keyValueStore.contains("String"))
    }

    @Test
    @Throws(Exception::class)
    fun testBatchEditPutAndApplyAndClear() {
        assertFalse(keyValueStore.contains("String"))

        keyValueStore.batchEdit()
                .putString("String", "Hello, World")
                .apply()

        assertTrue(keyValueStore.contains("String"))

        keyValueStore.batchEdit()
                .putInteger("Integer", 1000)
                .apply()

        assertTrue(keyValueStore.contains("String"))
        assertTrue(keyValueStore.contains("Integer"))

        keyValueStore.batchEdit()
                .clear()
                .apply()

        assertFalse(keyValueStore.contains("String"))
        assertEquals(0, keyValueStore.all?.size ?: 0)
    }

    @Test
    @Throws(Exception::class)
    fun testBatchEditPutWithoutApply() {
        assertFalse(keyValueStore.contains("String"))

        keyValueStore.batchEdit()
                .putString("String", "Hello, World")

        assertFalse(keyValueStore.contains("String"))
    }

    @Test
    @Throws(Exception::class)
    fun testClear() {
        keyValueStore.clear()
        assertNull(keyValueStore.all)
    }

    @Test
    @Throws(Exception::class)
    fun testContains() {
        assertFalse("boolean" in keyValueStore)
    }

    @Test
    @Throws(Exception::class)
    fun testContainsAndSet() {
        assertFalse("hello" in keyValueStore)

        keyValueStore.setString("hello", "world")
        assertTrue("hello" in keyValueStore)
    }

    @Test
    @Throws(Exception::class)
    fun testContainsAndSetAndRemove() {
        assertFalse("hello" in keyValueStore)

        keyValueStore.setString("hello", "world")
        assertTrue("hello" in keyValueStore)

        keyValueStore.remove("hello")
        assertFalse("hello" in keyValueStore)
    }

    @Test
    @Throws(Exception::class)
    fun testFallbackValue() {
        assertEquals(keyValueStore.getBoolean("bool", false), false)
        assertEquals(keyValueStore.getBoolean("bool", true), true)
        assertEquals(keyValueStore.getFloat("float", Float.MAX_VALUE), Float.MAX_VALUE)
        assertEquals(keyValueStore.getFloat("float", Float.MIN_VALUE), Float.MIN_VALUE)
        assertEquals(keyValueStore.getInteger("int", Integer.MAX_VALUE), Integer.MAX_VALUE)
        assertEquals(keyValueStore.getInteger("int", Integer.MIN_VALUE), Integer.MIN_VALUE)
        assertEquals(keyValueStore.getLong("long", Long.MAX_VALUE), Long.MAX_VALUE)
        assertEquals(keyValueStore.getLong("long", Long.MIN_VALUE), Long.MIN_VALUE)
        assertEquals(keyValueStore.getString("string", "blah"), "blah")
    }

    @Test
    @Throws(Exception::class)
    fun testGetAllAndSetAndRemove() {
        var all = keyValueStore.all
        assertTrue(all == null || all.isEmpty())

        keyValueStore.setFloat("float", Float.MIN_VALUE)
        all = keyValueStore.all
        assertNotNull(all)
        assertEquals(1, all?.size)

        keyValueStore.setString("String", "Hello, World!")
        all = keyValueStore.all
        assertNotNull(all)
        assertEquals(2, all?.size)

        keyValueStore.remove("float")
        all = keyValueStore.all
        assertNotNull(all)
        assertEquals(1, all?.size)

        keyValueStore.remove("hello")
        all = keyValueStore.all
        assertNotNull(all)
        assertEquals(1, all?.size)

        keyValueStore.remove("String")
        all = keyValueStore.all
        assertTrue(all == null || all.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun testGetBoolean() {
        assertFalse(keyValueStore.getBoolean("boolean", false))
        assertTrue(keyValueStore.getBoolean("boolean", true))
    }

    @Test
    @Throws(Exception::class)
    fun testGetAndSetBoolean() {
        assertFalse(keyValueStore.getBoolean("boolean", false))

        keyValueStore.setBoolean("boolean", true)
        assertTrue(keyValueStore.getBoolean("boolean", false))

        keyValueStore.setBoolean("boolean", false)
        assertFalse(keyValueStore.getBoolean("boolean", false))
    }

    @Test
    @Throws(Exception::class)
    fun testGetFloat() {
        assertEquals(keyValueStore.getFloat("float", Float.MIN_VALUE), Float.MIN_VALUE)
        assertEquals(keyValueStore.getFloat("float", Float.MAX_VALUE), Float.MAX_VALUE)
    }

    @Test
    @Throws(Exception::class)
    fun testGetAndSetFloat() {
        assertEquals(keyValueStore.getFloat("float", Float.MIN_VALUE), Float.MIN_VALUE)
        assertEquals(keyValueStore.getFloat("float", Float.MAX_VALUE), Float.MAX_VALUE)

        keyValueStore.setFloat("float", Float.MAX_VALUE)
        assertEquals(keyValueStore.getFloat("float", Float.MIN_VALUE), Float.MAX_VALUE)

        keyValueStore.setFloat("float", Float.MIN_VALUE)
        assertEquals(keyValueStore.getFloat("float", Float.MIN_VALUE), Float.MIN_VALUE)
    }

    @Test
    @Throws(Exception::class)
    fun testRemove() {
        keyValueStore.remove("boolean")
        assertFalse(keyValueStore.contains("boolean"))
    }

    @Test
    @Throws(Exception::class)
    fun testRemoveAndSet() {
        keyValueStore.remove("long")
        assertFalse(keyValueStore.contains("long"))

        keyValueStore.setLong("long", 100)
        assertTrue(keyValueStore.contains("long"))

        keyValueStore.remove("long")
        assertFalse(keyValueStore.contains("long"))
    }

}
