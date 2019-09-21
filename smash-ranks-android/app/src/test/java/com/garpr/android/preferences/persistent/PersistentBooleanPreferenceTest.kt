package com.garpr.android.preferences.persistent

import com.garpr.android.BaseTest
import com.garpr.android.preferences.KeyValueStore
import com.garpr.android.preferences.KeyValueStoreProvider
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.inject
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class PersistentBooleanPreferenceTest : BaseTest() {

    protected val keyValueStoreProvider: KeyValueStoreProvider by inject()

    private lateinit var keyValueStore: KeyValueStore

    companion object {
        private const val TAG = "PersistentBooleanPreferenceTest"
    }

    @Before
    override fun setUp() {
        super.setUp()

        keyValueStore = keyValueStoreProvider.getKeyValueStore(TAG)
    }

    @Test
    fun testNonNullGetDefaultValue() {
        val preference = PersistentBooleanPreference("boolean",false, keyValueStore)
        assertTrue(preference.defaultValue == false)

        preference.set(true)
        assertTrue(preference.defaultValue == false)
    }

    @Test
    fun testNullGetDefaultValue() {
        val preference = PersistentBooleanPreference("boolean", null, keyValueStore)
        assertNull(preference.defaultValue)

        preference.set(true)
        assertNull(preference.defaultValue)
    }

}
