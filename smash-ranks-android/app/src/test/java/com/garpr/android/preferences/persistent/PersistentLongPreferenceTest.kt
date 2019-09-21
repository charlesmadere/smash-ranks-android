package com.garpr.android.preferences.persistent

import com.garpr.android.BaseTest
import com.garpr.android.preferences.KeyValueStore
import com.garpr.android.preferences.KeyValueStoreProvider
import com.garpr.android.preferences.Preference
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.inject
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class PersistentLongPreferenceTest : BaseTest() {

    protected val keyValueStoreProvider: KeyValueStoreProvider by inject()

    private lateinit var keyValueStore: KeyValueStore

    companion object {
        private const val TAG = "PersistentLongPreferenceTest"
    }

    @Before
    override fun setUp() {
        super.setUp()

        keyValueStore = keyValueStoreProvider.getKeyValueStore(TAG)
    }

    @Test
    fun testAddListener() {
        val preference = PersistentLongPreference("long", null, keyValueStore)
        var value: Long? = null

        val listener = object : Preference.OnPreferenceChangeListener<Long> {
            override fun onPreferenceChange(preference: Preference<Long>) {
                value = preference.get()
            }
        }

        preference.addListener(listener)
        assertEquals(null, value)

        preference.set(11L)
        assertEquals(11L, value)

        preference.set(21L)
        assertEquals(21L, value)
    }

    @Test
    fun testDelete() {
        val preference = PersistentLongPreference("long", -127L, keyValueStore)
        assertEquals(-127L, preference.get())

        preference.delete()
        assertEquals(-127L, preference.get())

        preference.set(61L)
        assertEquals(61L, preference.get())

        preference.delete()
        assertEquals(-127L, preference.get())
    }

    @Test
    fun testExistsWithDefaultValue() {
        val preference = PersistentLongPreference("long", 1989L, keyValueStore)
        assertTrue(preference.exists)

        preference.delete()
        assertTrue(preference.exists)
    }

    @Test
    fun testExistsWithNullDefaultValue() {
        val preference = PersistentLongPreference("long", null, keyValueStore)
        assertFalse(preference.exists)

        preference.delete()
        assertFalse(preference.exists)

        preference.set(1998L)
        assertTrue(preference.exists)

        preference.delete()
        assertFalse(preference.exists)
    }

    @Test
    fun testGetAndSet() {
        val preference = PersistentLongPreference("long", 863L, keyValueStore)
        assertEquals(863L, preference.get())

        preference.set(0L)
        assertEquals(0L, preference.get())

        preference.set(null)
        assertEquals(863L, preference.get())
        assertEquals(preference.defaultValue, preference.get())

        preference.set(-177L)
        assertEquals(-177L, preference.get())
        assertNotEquals(preference.defaultValue, preference.get())
    }

    @Test
    fun testGetKey() {
        val preference = PersistentLongPreference("long", null, keyValueStore)
        assertEquals("long", preference.key)
    }

    @Test
    fun testNonNullGetDefaultValue() {
        val preference = PersistentLongPreference("long", 130L, keyValueStore)
        assertEquals(130L, preference.defaultValue)

        preference.set(java.lang.Long.MAX_VALUE)
        assertEquals(130L, preference.defaultValue)
    }

    @Test
    fun testNullGetDefaultValue() {
        val preference = PersistentLongPreference("long", null, keyValueStore)
        assertNull(preference.defaultValue)

        preference.set(19L)
        assertNull(preference.defaultValue)
    }

    @Test
    fun testRemoveListener() {
        val preference = PersistentLongPreference("long", null, keyValueStore)
        var value: Long? = null

        val listener = object : Preference.OnPreferenceChangeListener<Long> {
            override fun onPreferenceChange(preference: Preference<Long>) {
                value = preference.get()
            }
        }

        preference.addListener(listener)

        preference.set(11L)
        assertEquals(11L, value)

        preference.set(25L)
        assertEquals(25L, value)

        preference.removeListener(listener)

        preference.set(139L)
        assertNotEquals(139L, value)
    }

    @Test
    fun testSetAndGet() {
        val preference = PersistentLongPreference("long", null, keyValueStore)

        preference.set(38L)
        assertEquals(38L, preference.get())

        preference.set(-22L)
        assertEquals(-22L, preference.get())

        preference.set(null)
        assertEquals(null, preference.get())

        preference.set(Long.MIN_VALUE)
        assertEquals(Long.MIN_VALUE, preference.get())
    }

}
