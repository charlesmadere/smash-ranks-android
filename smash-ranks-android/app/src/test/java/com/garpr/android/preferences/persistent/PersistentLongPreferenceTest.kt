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
class PersistentLongPreferenceTest : BaseTest() {

    @Inject
    protected lateinit var keyValueStore: KeyValueStore


    @Before
    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)
    }

    @Test
    @Throws(Exception::class)
    fun testAddListener() {
        val preference = PersistentLongPreference("long", null, keyValueStore)
        val array = arrayOfNulls<Long>(1)

        val listener = object : Preference.OnPreferenceChangeListener<Long> {
            override fun onPreferenceChange(preference: Preference<Long>) {
                array[0] = preference.get()
            }
        }

        preference.addListener(listener)
        assertEquals(null, array[0])

        preference.set(11L)
        assertEquals(11L, array[0])

        preference.set(-9815L, false)
        assertNotEquals(-9851L, array[0])

        preference.set(21L)
        assertEquals(21L, array[0])
    }

    @Test
    @Throws(Exception::class)
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
    @Throws(Exception::class)
    fun testExistsWithDefaultValue() {
        val preference = PersistentLongPreference("long", 1989L, keyValueStore)
        assertTrue(preference.exists)

        preference.delete()
        assertTrue(preference.exists)
    }

    @Test
    @Throws(Exception::class)
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
    @Throws(Exception::class)
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
    @Throws(Exception::class)
    fun testGetKey() {
        val preference = PersistentLongPreference("long", null, keyValueStore)
        assertEquals("long", preference.key)
    }

    @Test
    @Throws(Exception::class)
    fun testNonNullGetDefaultValue() {
        val preference = PersistentLongPreference("long", 130L, keyValueStore)
        assertEquals(130L, preference.defaultValue)

        preference.set(java.lang.Long.MAX_VALUE)
        assertEquals(130L, preference.defaultValue)
    }

    @Test
    @Throws(Exception::class)
    fun testNullGetDefaultValue() {
        val preference = PersistentLongPreference("long", null, keyValueStore)
        assertNull(preference.defaultValue)

        preference.set(19L)
        assertNull(preference.defaultValue)
    }

    @Test
    @Throws(Exception::class)
    fun testRemoveListener() {
        val preference = PersistentLongPreference("long", null, keyValueStore)
        val array = arrayOfNulls<Long>(1)

        val listener = object : Preference.OnPreferenceChangeListener<Long> {
            override fun onPreferenceChange(preference: Preference<Long>) {
                array[0] = preference.get()
            }
        }

        preference.addListener(listener)

        preference.set(11L)
        assertEquals(11L, array[0])

        preference.set(25L)
        assertEquals(25L, array[0])

        preference.removeListener(listener)

        preference.set(139L)
        assertNotEquals(139L, array[0])
    }

    @Test
    @Throws(Exception::class)
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
