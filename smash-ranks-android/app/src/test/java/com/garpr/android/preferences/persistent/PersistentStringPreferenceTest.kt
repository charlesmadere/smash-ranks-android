package com.garpr.android.preferences.persistent

import com.garpr.android.BaseTest
import com.garpr.android.preferences.KeyValueStore
import com.garpr.android.preferences.KeyValueStoreProvider
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class PersistentStringPreferenceTest : BaseTest() {

    private lateinit var keyValueStore: KeyValueStore

    @Inject
    protected lateinit var keyValueStoreProvider: KeyValueStoreProvider


    companion object {
        private const val TAG = "PersistentStringPreferenceTest"
    }

    @Before
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)

        keyValueStore = keyValueStoreProvider.getKeyValueStore(TAG)
    }

    @Test
    fun testDeleteWithDefaultValue() {
        val preference = PersistentStringPreference("string", "hello", keyValueStore)
        preference.delete()
        assertNotNull(preference.get())
        assertTrue(preference.exists)

        preference.set("world")
        preference.delete()
        assertNotNull(preference.get())
        assertTrue(preference.exists)
    }

    @Test
    fun testDeleteWithNullDefaultValue() {
        val preference = PersistentStringPreference("string", null, keyValueStore)
        preference.delete()
        assertNull(preference.get())
        assertFalse(preference.exists)

        preference.set("nintendo")
        preference.delete()
        assertNull(preference.get())
        assertFalse(preference.exists)
    }

    @Test
    fun testExistsWithDefaultValue() {
        val preference = PersistentStringPreference("string", "hello", keyValueStore)
        assertTrue(preference.exists)

        preference.delete()
        assertTrue(preference.exists)
    }

    @Test
    fun testExistsWithEmptyDefaultValue() {
        val preference = PersistentStringPreference("string", "", keyValueStore)
        assertTrue(preference.exists)

        preference.delete()
        assertTrue(preference.exists)
    }

    @Test
    fun testExistsWithNullDefaultValue() {
        val preference = PersistentStringPreference("string", null, keyValueStore)
        assertFalse(preference.exists)

        preference.delete()
        assertFalse(preference.exists)
    }

    @Test
    fun testExistsWithWhitespaceDefaultValue() {
        val preference = PersistentStringPreference("string", "   ", keyValueStore)
        assertTrue(preference.exists)

        preference.delete()
        assertTrue(preference.exists)
    }

}
