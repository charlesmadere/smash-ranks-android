package com.garpr.android.preferences

import com.garpr.android.test.BaseAndroidTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNotSame
import org.junit.Assert.assertTrue
import org.junit.Test
import org.koin.test.inject

class AndroidKeyValueStoreProviderTest : BaseAndroidTest() {

    protected val keyValueStoreProvider: KeyValueStoreProvider by inject()

    @Test
    fun testGetKeyValueStoreIsNotNull() {
        assertNotNull(keyValueStoreProvider.getKeyValueStore(STORE_0))
    }

    @Test
    fun testGetKeyValueStoreNotSame() {
        val store0 = keyValueStoreProvider.getKeyValueStore(STORE_0)
        val store1 = keyValueStoreProvider.getKeyValueStore(STORE_1)
        assertNotSame(store0, store1)

        store0.setBoolean("Hello", true)
        assertTrue("Hello" in store0)
        assertTrue(store0.getBoolean("Hello", false))
        assertFalse("Hello" in store1)

        store1.setInteger("World", 100)
        assertFalse("World" in store0)
        assertTrue("World" in store1)
        assertEquals(100, store1.getInteger("World", Int.MAX_VALUE))
    }

    companion object {
        private const val STORE_0 = "Store0"
        private const val STORE_1 = "Store1"
    }

}
