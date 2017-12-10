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
    protected lateinit var mKeyValueStore: KeyValueStore


    @Before
    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)
    }

    @Test
    @Throws(Exception::class)
    fun testAll() {
        val all = mKeyValueStore.all
        assertTrue(all == null || all.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun testAllAndSet() {
        var all = mKeyValueStore.all
        assertTrue(all == null || all.isEmpty())

        mKeyValueStore.setFloat("float", Float.MIN_VALUE)
        all = mKeyValueStore.all
        assertNotNull(all)
        assertEquals(1, all?.size)

        mKeyValueStore.setString("String", "Hello, World!")
        all = mKeyValueStore.all
        assertNotNull(all)
        assertEquals(2, all?.size)
    }

    @Test
    @Throws(Exception::class)
    fun testBatchEditNotNull() {
        assertNotNull(mKeyValueStore.batchEdit())
    }

    @Test
    @Throws(Exception::class)
    fun testBatchEditPutAndApplyWithInteger() {
        assertFalse(mKeyValueStore.contains("String"))

        mKeyValueStore.batchEdit()
                .putInteger("Integer", 100)
                .apply()

        assertTrue(mKeyValueStore.contains("Integer"))
    }

    @Test
    @Throws(Exception::class)
    fun testBatchEditPutAndApplyWithString() {
        assertFalse(mKeyValueStore.contains("String"))

        mKeyValueStore.batchEdit()
                .putString("String", "Hello, World")
                .apply()

        assertTrue(mKeyValueStore.contains("String"))
    }

    @Test
    @Throws(Exception::class)
    fun testBatchEditPutAndApplyAndClear() {
        assertFalse(mKeyValueStore.contains("String"))

        mKeyValueStore.batchEdit()
                .putString("String", "Hello, World")
                .apply()

        assertTrue(mKeyValueStore.contains("String"))

        mKeyValueStore.batchEdit()
                .putInteger("Integer", 1000)
                .apply()

        assertTrue(mKeyValueStore.contains("String"))
        assertTrue(mKeyValueStore.contains("Integer"))

        mKeyValueStore.batchEdit()
                .clear()
                .apply()

        assertFalse(mKeyValueStore.contains("String"))
        assertEquals(0, mKeyValueStore.all?.size ?: 0)
    }

    @Test
    @Throws(Exception::class)
    fun testBatchEditPutWithoutApply() {
        assertFalse(mKeyValueStore.contains("String"))

        mKeyValueStore.batchEdit()
                .putString("String", "Hello, World")

        assertFalse(mKeyValueStore.contains("String"))
    }

    @Test
    @Throws(Exception::class)
    fun testClear() {
        mKeyValueStore.clear()
        assertNull(mKeyValueStore.all)
    }

    @Test
    @Throws(Exception::class)
    fun testContains() {
        assertFalse("boolean" in mKeyValueStore)
    }

    @Test
    @Throws(Exception::class)
    fun testContainsAndSet() {
        assertFalse("hello" in mKeyValueStore)

        mKeyValueStore.setString("hello", "world")
        assertTrue("hello" in mKeyValueStore)
    }

    @Test
    @Throws(Exception::class)
    fun testContainsAndSetAndRemove() {
        assertFalse("hello" in mKeyValueStore)

        mKeyValueStore.setString("hello", "world")
        assertTrue("hello" in mKeyValueStore)

        mKeyValueStore.remove("hello")
        assertFalse("hello" in mKeyValueStore)
    }

    @Test
    @Throws(Exception::class)
    fun testFallbackValue() {
        assertEquals(mKeyValueStore.getBoolean("bool", false), false)
        assertEquals(mKeyValueStore.getBoolean("bool", true), true)
        assertEquals(mKeyValueStore.getFloat("float", Float.MAX_VALUE), Float.MAX_VALUE)
        assertEquals(mKeyValueStore.getFloat("float", Float.MIN_VALUE), Float.MIN_VALUE)
        assertEquals(mKeyValueStore.getInteger("int", Integer.MAX_VALUE), Integer.MAX_VALUE)
        assertEquals(mKeyValueStore.getInteger("int", Integer.MIN_VALUE), Integer.MIN_VALUE)
        assertEquals(mKeyValueStore.getLong("long", Long.MAX_VALUE), Long.MAX_VALUE)
        assertEquals(mKeyValueStore.getLong("long", Long.MIN_VALUE), Long.MIN_VALUE)
        assertEquals(mKeyValueStore.getString("string", "blah"), "blah")
    }

    @Test
    @Throws(Exception::class)
    fun testGetAllAndSetAndRemove() {
        var all = mKeyValueStore.all
        assertTrue(all == null || all.isEmpty())

        mKeyValueStore.setFloat("float", Float.MIN_VALUE)
        all = mKeyValueStore.all
        assertNotNull(all)
        assertEquals(1, all?.size)

        mKeyValueStore.setString("String", "Hello, World!")
        all = mKeyValueStore.all
        assertNotNull(all)
        assertEquals(2, all?.size)

        mKeyValueStore.remove("float")
        all = mKeyValueStore.all
        assertNotNull(all)
        assertEquals(1, all?.size)

        mKeyValueStore.remove("hello")
        all = mKeyValueStore.all
        assertNotNull(all)
        assertEquals(1, all?.size)

        mKeyValueStore.remove("String")
        all = mKeyValueStore.all
        assertTrue(all == null || all.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun testGetBoolean() {
        assertFalse(mKeyValueStore.getBoolean("boolean", false))
        assertTrue(mKeyValueStore.getBoolean("boolean", true))
    }

    @Test
    @Throws(Exception::class)
    fun testGetAndSetBoolean() {
        assertFalse(mKeyValueStore.getBoolean("boolean", false))

        mKeyValueStore.setBoolean("boolean", true)
        assertTrue(mKeyValueStore.getBoolean("boolean", false))

        mKeyValueStore.setBoolean("boolean", false)
        assertFalse(mKeyValueStore.getBoolean("boolean", false))
    }

    @Test
    @Throws(Exception::class)
    fun testGetFloat() {
        assertEquals(mKeyValueStore.getFloat("float", Float.MIN_VALUE), Float.MIN_VALUE)
        assertEquals(mKeyValueStore.getFloat("float", Float.MAX_VALUE), Float.MAX_VALUE)
    }

    @Test
    @Throws(Exception::class)
    fun testGetAndSetFloat() {
        assertEquals(mKeyValueStore.getFloat("float", Float.MIN_VALUE), Float.MIN_VALUE)
        assertEquals(mKeyValueStore.getFloat("float", Float.MAX_VALUE), Float.MAX_VALUE)

        mKeyValueStore.setFloat("float", Float.MAX_VALUE)
        assertEquals(mKeyValueStore.getFloat("float", Float.MIN_VALUE), Float.MAX_VALUE)

        mKeyValueStore.setFloat("float", Float.MIN_VALUE)
        assertEquals(mKeyValueStore.getFloat("float", Float.MIN_VALUE), Float.MIN_VALUE)
    }

    @Test
    @Throws(Exception::class)
    fun testRemove() {
        mKeyValueStore.remove("boolean")
        assertFalse(mKeyValueStore.contains("boolean"))
    }

    @Test
    @Throws(Exception::class)
    fun testRemoveAndSet() {
        mKeyValueStore.remove("long")
        assertFalse(mKeyValueStore.contains("long"))

        mKeyValueStore.setLong("long", 100)
        assertTrue(mKeyValueStore.contains("long"))

        mKeyValueStore.remove("long")
        assertFalse(mKeyValueStore.contains("long"))
    }

}
