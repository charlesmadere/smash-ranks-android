package com.garpr.android.preferences.persistent

import android.net.Uri
import com.garpr.android.BaseTest
import com.garpr.android.BuildConfig
import com.garpr.android.preferences.KeyValueStore
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class PersistentUriPreferenceTest : BaseTest() {

    @Inject
    lateinit protected var mKeyValueStore: KeyValueStore


    companion object {
        private const val AMAZON = "https://www.amazon.com/"
        private const val GOOGLE = "https://www.google.com/"
        private const val POLYGON = "http://www.polygon.com/"
    }

    @Before
    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)
    }

    @Test
    @Throws(Exception::class)
    fun testGet() {
        val preference = PersistentUriPreference("test", null, mKeyValueStore)
        assertNull(preference.get())
    }

    @Test
    @Throws(Exception::class)
    fun testGetAndSet() {
        val preference = PersistentUriPreference("test", null, mKeyValueStore)
        assertNull(preference.get())

        val google = Uri.parse(GOOGLE)
        preference.set(google)
        assertNotNull(preference.get())
        assertEquals(google, preference.get())
    }

    @Test
    @Throws(Exception::class)
    fun testGetAndSetAndDelete() {
        val preference = PersistentUriPreference("test", null, mKeyValueStore)
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
    @Throws(Exception::class)
    fun testGetAndSetAndDeleteAndExists() {
        val preference = PersistentUriPreference("test", null, mKeyValueStore)
        assertFalse(preference.exists())
        assertNull(preference.get())

        val amazon = Uri.parse(AMAZON)
        preference.set(amazon)
        assertTrue(preference.exists())
        assertNotNull(preference.get())
        assertEquals(amazon, preference.get())

        val polygon = Uri.parse(POLYGON)
        preference.set(polygon)
        assertTrue(preference.exists())
        assertNotNull(preference.get())
        assertEquals(polygon, preference.get())

        preference.set(Uri.EMPTY)
        assertTrue(preference.exists())
        assertNotNull(preference.get())
        assertEquals(Uri.EMPTY, preference.get())

        preference.delete()
        assertNull(preference.get())
    }

    @Test
    @Throws(Exception::class)
    fun testGetDefaultValue() {
        val polygon = Uri.parse(POLYGON)
        val preference = PersistentUriPreference("test", polygon, mKeyValueStore)
        assertNotNull(preference.get())
        assertEquals(polygon, preference.get())
    }

}
