package com.garpr.android.preferences.persistent

import android.net.Uri
import com.garpr.android.BaseTest
import com.garpr.android.preferences.KeyValueStore
import com.garpr.android.preferences.KeyValueStoreProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.inject
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class PersistentUriPreferenceTest : BaseTest() {

    protected val keyValueStoreProvider: KeyValueStoreProvider by inject()

    private lateinit var keyValueStore: KeyValueStore

    companion object {
        private const val AMAZON = "https://www.amazon.com/"
        private const val GOOGLE = "https://www.google.com/"
        private const val POLYGON = "http://www.polygon.com/"
        private const val TAG = "PersistentUriPreferenceTest"
    }

    @Before
    override fun setUp() {
        super.setUp()

        keyValueStore = keyValueStoreProvider.getKeyValueStore(TAG)
    }

    @Test
    fun testGet() {
        val preference = PersistentUriPreference("test", null, keyValueStore)
        assertNull(preference.get())
    }

    @Test
    fun testGetAndSet() {
        val preference = PersistentUriPreference("test", null, keyValueStore)
        assertNull(preference.get())

        val google = Uri.parse(GOOGLE)
        preference.set(google)
        assertNotNull(preference.get())
        assertEquals(google, preference.get())
    }

    @Test
    fun testGetAndSetAndDelete() {
        val preference = PersistentUriPreference("test", null, keyValueStore)
        assertNull(preference.get())

        val amazon = Uri.parse(AMAZON)
        preference.set(amazon)
        assertNotNull(preference.get())
        assertEquals(amazon, preference.get())

        val polygon = Uri.parse(POLYGON)
        preference.set(polygon)
        assertNotNull(preference.get())
        assertEquals(polygon, preference.get())

        preference.delete()
        assertNull(preference.get())
    }

    @Test
    fun testGetAndSetAndDeleteAndExists() {
        val preference = PersistentUriPreference("test", null, keyValueStore)
        assertFalse(preference.exists)
        assertNull(preference.get())

        val amazon = Uri.parse(AMAZON)
        preference.set(amazon)
        assertTrue(preference.exists)
        assertNotNull(preference.get())
        assertEquals(amazon, preference.get())

        val polygon = Uri.parse(POLYGON)
        preference.set(polygon)
        assertTrue(preference.exists)
        assertNotNull(preference.get())
        assertEquals(polygon, preference.get())

        preference.set(Uri.EMPTY)
        assertTrue(preference.exists)
        assertNotNull(preference.get())
        assertEquals(Uri.EMPTY, preference.get())

        preference.delete()
        assertNull(preference.get())
    }

    @Test
    fun testGetDefaultValue() {
        val polygon = Uri.parse(POLYGON)
        val preference = PersistentUriPreference("test", polygon, keyValueStore)
        assertNotNull(preference.get())
        assertEquals(polygon, preference.get())
    }

}
