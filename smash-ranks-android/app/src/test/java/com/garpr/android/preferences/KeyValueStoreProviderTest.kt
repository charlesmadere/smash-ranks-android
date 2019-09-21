package com.garpr.android.preferences

import com.garpr.android.BaseTest
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNotSame
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.inject
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class KeyValueStoreProviderTest : BaseTest() {

    protected val keyValueStoreProvider: KeyValueStoreProvider by inject()

    companion object {
        private const val STORE_0 = "Store0"
        private const val STORE_1 = "Store1"
    }

    @Test
    fun testGetKeyValueStoreIsNotNull() {
        assertNotNull(keyValueStoreProvider.getKeyValueStore(STORE_0))
    }

    @Test
    fun testGetKeyValueStoreNotSame() {
        val store0 = keyValueStoreProvider.getKeyValueStore(STORE_0)
        val store1 = keyValueStoreProvider.getKeyValueStore(STORE_1)
        assertNotSame(store0, store1)
    }

}
