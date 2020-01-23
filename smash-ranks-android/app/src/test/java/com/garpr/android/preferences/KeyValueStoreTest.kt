package com.garpr.android.preferences

import com.garpr.android.test.BaseTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.koin.test.inject

class KeyValueStoreTest : BaseTest() {

    private lateinit var keyValueStore: KeyValueStore

    protected val keyValueStoreProvider: KeyValueStoreProvider by inject()

    @Before
    override fun setUp() {
        super.setUp()

        keyValueStore = keyValueStoreProvider.getKeyValueStore(TAG)
    }

    @Test
    fun testAll() {
        val all = keyValueStore.all
        assertTrue(all.isNullOrEmpty())
    }

    @Test
    fun testAllAndSet() {
        var all = keyValueStore.all
        assertTrue(all.isNullOrEmpty())

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
    fun testBatchEditNotNull() {
        assertNotNull(keyValueStore.batchEdit())
    }

    @Test
    fun testBatchEditPutAndApplyWithBoolean() {
        assertFalse("Boolean" in keyValueStore)

        keyValueStore.batchEdit()
                .putBoolean("Boolean", true)
                .apply()

        assertTrue("Boolean" in keyValueStore)
    }

    @Test
    fun testBatchEditPutAndApplyWithInteger() {
        assertFalse("String" in keyValueStore)

        keyValueStore.batchEdit()
                .putInteger("Integer", 100)
                .apply()

        assertTrue("Integer" in keyValueStore)
    }

    @Test
    fun testBatchEditPutAndApplyWithString() {
        assertFalse("String" in keyValueStore)

        keyValueStore.batchEdit()
                .putString("String", "Hello, World")
                .apply()

        assertTrue("String" in keyValueStore)
    }

    @Test
    fun testBatchEditPutAndApplyAndClear() {
        assertFalse("String" in keyValueStore)
        assertFalse("Integer" in keyValueStore)
        assertFalse("Boolean" in keyValueStore)

        keyValueStore.batchEdit()
                .putString("String", "Hello, World")
                .apply()

        assertTrue("String" in keyValueStore)
        assertFalse("Integer" in keyValueStore)
        assertFalse("Boolean" in keyValueStore)

        keyValueStore.batchEdit()
                .putInteger("Integer", 1000)
                .apply()

        assertTrue("String" in keyValueStore)
        assertTrue("Integer" in keyValueStore)
        assertFalse("Boolean" in keyValueStore)

        keyValueStore.batchEdit()
                .putBoolean("Boolean", true)
                .apply()

        assertTrue("String" in keyValueStore)
        assertTrue("Integer" in keyValueStore)
        assertTrue("Boolean" in keyValueStore)

        keyValueStore.batchEdit()
                .clear()
                .apply()

        assertFalse("String" in keyValueStore)
        assertFalse("Integer" in keyValueStore)
        assertFalse("Boolean" in keyValueStore)
        assertEquals(0, keyValueStore.all?.size ?: 0)
    }

    @Test
    fun testBatchEditPutWithoutApply() {
        assertFalse("String" in keyValueStore)

        keyValueStore.batchEdit()
                .putString("String", "Hello, World")

        assertFalse("String" in keyValueStore)
    }

    @Test
    fun testClear() {
        keyValueStore.clear()
        assertTrue(keyValueStore.all.isNullOrEmpty())
    }

    @Test
    fun testContains() {
        assertFalse("boolean" in keyValueStore)
    }

    @Test
    fun testContainsAndSet() {
        assertFalse("hello" in keyValueStore)

        keyValueStore.setString("hello", "world")
        assertTrue("hello" in keyValueStore)
    }

    @Test
    fun testContainsAndSetAndRemove() {
        assertFalse("hello" in keyValueStore)

        keyValueStore.setString("hello", "world")
        assertTrue("hello" in keyValueStore)

        keyValueStore.remove("hello")
        assertFalse("hello" in keyValueStore)
    }

    @Test
    fun testFallbackValue() {
        assertEquals(false, keyValueStore.getBoolean("bool", false))
        assertEquals(true, keyValueStore.getBoolean("bool", true))
        assertEquals(Float.MAX_VALUE, keyValueStore.getFloat("float", Float.MAX_VALUE))
        assertEquals(Float.MIN_VALUE, keyValueStore.getFloat("float", Float.MIN_VALUE))
        assertEquals(Int.MAX_VALUE, keyValueStore.getInteger("int", Int.MAX_VALUE))
        assertEquals(Int.MIN_VALUE, keyValueStore.getInteger("int", Int.MIN_VALUE))
        assertEquals(Long.MAX_VALUE, keyValueStore.getLong("long", Long.MAX_VALUE))
        assertEquals(Long.MIN_VALUE, keyValueStore.getLong("long", Long.MIN_VALUE))
        assertEquals("blah", keyValueStore.getString("string", "blah"))
        assertNull(keyValueStore.getString("string", null))
    }

    @Test
    fun testGetAllAndSetAndRemove() {
        var all = keyValueStore.all
        assertTrue(all.isNullOrEmpty())

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
        assertTrue(all.isNullOrEmpty())
    }

    @Test
    fun testGetBoolean() {
        assertFalse(keyValueStore.getBoolean("boolean", false))
        assertTrue(keyValueStore.getBoolean("boolean", true))
    }

    @Test
    fun testGetAndSetBoolean() {
        assertFalse(keyValueStore.getBoolean("boolean", false))

        keyValueStore.setBoolean("boolean", true)
        assertTrue(keyValueStore.getBoolean("boolean", false))

        keyValueStore.setBoolean("boolean", false)
        assertFalse(keyValueStore.getBoolean("boolean", false))
    }

    @Test
    fun testGetFloat() {
        assertEquals(Float.MIN_VALUE, keyValueStore.getFloat("float", Float.MIN_VALUE))
        assertEquals(Float.MAX_VALUE, keyValueStore.getFloat("float", Float.MAX_VALUE))
    }

    @Test
    fun testGetAndSetFloat() {
        assertEquals(Float.MIN_VALUE, keyValueStore.getFloat("float", Float.MIN_VALUE))
        assertEquals(Float.MAX_VALUE, keyValueStore.getFloat("float", Float.MAX_VALUE))

        keyValueStore.setFloat("float", Float.MAX_VALUE)
        assertEquals(Float.MAX_VALUE, keyValueStore.getFloat("float", Float.MIN_VALUE))

        keyValueStore.setFloat("float", Float.MIN_VALUE)
        assertEquals(Float.MIN_VALUE, keyValueStore.getFloat("float", Float.MIN_VALUE))
    }

    @Test
    fun testGetString() {
        assertNull(keyValueStore.getString("hello", null))
        assertEquals("world", keyValueStore.getString("hello", "world"))
    }

    @Test
    fun testGetAndSetString() {
        assertNull(keyValueStore.getString("string", null))

        keyValueStore.setString("string", "moose")
        assertEquals("moose", keyValueStore.getString("string", null))
        assertNull(keyValueStore.getString("moose", null))
    }

    @Test
    fun testRemove() {
        keyValueStore.remove("boolean")
        assertFalse("boolean" in keyValueStore)
    }

    @Test
    fun testRemoveAndSet() {
        keyValueStore.remove("long")
        assertFalse("long" in keyValueStore)

        keyValueStore.setLong("long", 100)
        assertTrue("long" in keyValueStore)

        keyValueStore.remove("long")
        assertFalse("long" in keyValueStore)
    }

    companion object {
        private const val TAG = "KeyValueStoreTest"
    }

}
