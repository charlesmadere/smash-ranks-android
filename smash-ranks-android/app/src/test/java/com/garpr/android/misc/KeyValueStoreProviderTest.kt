package com.garpr.android.misc

import com.garpr.android.BaseTest
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class KeyValueStoreProviderTest : BaseTest() {

    @Inject
    protected lateinit var keyValueStoreProvider: KeyValueStoreProvider


    companion object {
        private const val TAG = "KeyValueStoreProviderTest"
    }

    @Before
    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)
    }

    @Test
    @Throws(Exception::class)
    fun testGetKeyValueStore() {
        assertNotNull(keyValueStoreProvider.getKeyValueStore(TAG))
    }

}
