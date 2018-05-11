package com.garpr.android.preferences.persistent

import com.garpr.android.BaseTest
import com.garpr.android.preferences.KeyValueStore
import com.garpr.android.preferences.Preference
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class PersistentIntegerPreferenceTest : BaseTest() {

    @Inject
    protected lateinit var keyValueStore: KeyValueStore


    @Before
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)
    }

    @Test
    @Throws(Exception::class)
    fun testAddListener() {
        val preference = PersistentIntegerPreference("integer", null, keyValueStore)
        var value: Int? = null

        val listener = object : Preference.OnPreferenceChangeListener<Int> {
            override fun onPreferenceChange(preference: Preference<Int>) {
                value = preference.get()
            }
        }

        preference.addListener(listener)
        assertEquals(null, value)

        preference.set(10)
        assertEquals(10, value)

        preference.set(-100, false)
        assertNotEquals(-100, value)

        preference.set(20)
        assertEquals(20, value)
    }

    @Test
    @Throws(Exception::class)
    fun testDelete() {
        val preference = PersistentIntegerPreference("integer", -47, keyValueStore)
        assertEquals(-47, preference.get())

        preference.delete()
        assertEquals(-47, preference.get())

        preference.set(100)
        assertEquals(100, preference.get())

        preference.delete()
        assertEquals(-47, preference.get())
    }

    @Test
    @Throws(Exception::class)
    fun testExistsWithDefaultValue() {
        val preference = PersistentIntegerPreference("integer", 1989, keyValueStore)
        assertTrue(preference.exists)

        preference.delete()
        assertTrue(preference.exists)
    }

    @Test
    @Throws(Exception::class)
    fun testExistsWithNullDefaultValue() {
        val preference = PersistentIntegerPreference("integer", null, keyValueStore)
        assertFalse(preference.exists)

        preference.delete()
        assertFalse(preference.exists)

        preference.set(1996)
        assertTrue(preference.exists)

        preference.delete()
        assertFalse(preference.exists)
    }

    @Test
    @Throws(Exception::class)
    fun testGetAndSet() {
        val preference = PersistentIntegerPreference("integer", 900, keyValueStore)
        assertEquals(900, preference.get())

        preference.set(0)
        assertEquals(0, preference.get())

        preference.set(null)
        assertEquals(900, preference.get())
        assertEquals(preference.defaultValue, preference.get())

        preference.set(-20)
        assertEquals(-20, preference.get())
        assertNotEquals(preference.defaultValue, preference.get())
    }

    @Test
    @Throws(Exception::class)
    fun testGetKey() {
        val preference = PersistentIntegerPreference("integer", null, keyValueStore)
        assertEquals("integer", preference.key)
    }

    @Test
    @Throws(Exception::class)
    fun testNonNullGetDefaultValue() {
        val preference = PersistentIntegerPreference("integer", 128, keyValueStore)
        assertEquals(128, preference.defaultValue)

        preference.set(Integer.MAX_VALUE)
        assertEquals(128, preference.defaultValue)
    }

    @Test
    @Throws(Exception::class)
    fun testNullGetDefaultValue() {
        val preference = PersistentIntegerPreference("integer", null, keyValueStore)
        assertNull(preference.defaultValue)

        preference.set(20)
        assertNull(preference.defaultValue)
    }

    @Test
    @Throws(Exception::class)
    fun testRemoveListener() {
        val preference = PersistentIntegerPreference("integer", null, keyValueStore)
        var value: Int? = null

        val listener = object : Preference.OnPreferenceChangeListener<Int> {
            override fun onPreferenceChange(preference: Preference<Int>) {
                value = preference.get()
            }
        }

        preference.addListener(listener)

        preference.set(10)
        assertEquals(10, value)

        preference.set(20)
        assertEquals(20, value)

        preference.removeListener(listener)

        preference.set(100)
        assertNotEquals(100, value)
    }

    @Test
    @Throws(Exception::class)
    fun testSetAndGet() {
        val preference = PersistentIntegerPreference("integer", null, keyValueStore)

        preference.set(5)
        assertEquals(5, preference.get())

        preference.set(10)
        assertEquals(10, preference.get())

        preference.set(null)
        assertEquals(null, preference.get())

        preference.set(Integer.MIN_VALUE)
        assertEquals(Integer.MIN_VALUE, preference.get())
    }

}
