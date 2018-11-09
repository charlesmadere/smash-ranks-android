package com.garpr.android.preferences

import com.garpr.android.BaseTest
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class KeyValueStoreProviderTest : BaseTest() {

    @Inject
    protected lateinit var keyValueStoreProvider: KeyValueStoreProvider


    companion object {
        private const val TAG = "KeyValueStoreProviderTest"
    }

    @Before
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)
    }

    @Test
    fun testGetKeyValueStore() {
        assertNotNull(keyValueStoreProvider.getKeyValueStore(TAG))
    }

}
