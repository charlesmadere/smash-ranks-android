package com.garpr.android.preferences.persistent

import com.garpr.android.data.models.Optional
import com.garpr.android.preferences.KeyValueStore
import com.garpr.android.preferences.KeyValueStoreProvider
import com.garpr.android.preferences.Preference
import com.garpr.android.test.BaseTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.koin.test.inject

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
    fun testExistsWithDefaultValue() {
        val preference: Preference<Long> = PersistentLongPreference(
                key = "long",
                defaultValue = 1972L,
                keyValueStore = keyValueStore
        )
        assertTrue(preference.exists)
    }

    @Test
    fun testExistsWithNullDefaultValue() {
        val preference: Preference<Long> = PersistentLongPreference(
                key = "long",
                defaultValue = null,
                keyValueStore = keyValueStore
        )
        assertFalse(preference.exists)
    }

    @Test
    fun testGetAndSetAndDeleteAndExistsWithDefaultValue() {
        val preference: Preference<Long> = PersistentLongPreference(
                key = "long",
                defaultValue = 863L,
                keyValueStore = keyValueStore
        )
        assertEquals(863L, preference.get())
        assertTrue(preference.exists)

        preference.set(0L)
        assertEquals(0L, preference.get())
        assertTrue(preference.exists)

        preference.delete()
        assertEquals(863L, preference.get())
        assertTrue(preference.exists)

        preference.set(-177L)
        assertEquals(-177L, preference.get())
        assertTrue(preference.exists)
    }

    @Test
    fun testGetAndSetAndDeleteAndExistsWithNullDefaultValue() {
        val preference: Preference<Long> = PersistentLongPreference(
                key = "long",
                defaultValue = null,
                keyValueStore = keyValueStore
        )
        assertNull(preference.get())
        assertFalse(preference.exists)

        preference.set(10000L)
        assertEquals(10000L, preference.get())
        assertTrue(preference.exists)

        preference.delete()
        assertNull(preference.get())
        assertFalse(preference.exists)

        preference.set(-675L)
        assertEquals(-675L, preference.get())
        assertTrue(preference.exists)
    }

    @Test
    fun testKey() {
        val preference: Preference<Long> = PersistentLongPreference(
                key = "long",
                defaultValue = null,
                keyValueStore = keyValueStore
        )
        assertEquals("long", preference.key)
    }

    @Test
    fun testObservableWithDefaultValue() {
        val preference: Preference<Long> = PersistentLongPreference(
                key = "integer",
                defaultValue = -50L,
                keyValueStore = keyValueStore
        )

        var value: Optional<Long>? = null

        preference.observable.subscribe {
            value = it
        }

        assertNull(value)

        preference.set(-93L)
        assertNotNull(value)
        assertEquals(-93L, value?.orNull())

        preference.set(13L)
        assertNotNull(value)
        assertEquals(13L, value?.orNull())

        preference.delete()
        assertNotNull(value)
        assertEquals(-50L, value?.orNull())
    }

    @Test
    fun testObservableWithNullDefaultValue() {
        val preference: Preference<Long> = PersistentLongPreference(
                key = "long",
                defaultValue = null,
                keyValueStore = keyValueStore
        )

        var value: Optional<Long>? = null

        preference.observable.subscribe {
            value = it
        }

        assertNull(value)

        preference.set(11L)
        assertNotNull(value)
        assertEquals(11L, value?.orNull())

        preference.set(21L)
        assertNotNull(value)
        assertEquals(21L, value?.orNull())

        preference.delete()
        assertNotNull(value)
        assertEquals(false, value?.isPresent())
    }

    @Test
    fun testSetWithNullCausesDelete() {
        val preference: Preference<Long> = PersistentLongPreference(
                key = "long",
                defaultValue = null,
                keyValueStore = keyValueStore
        )

        preference.set(-1234L)
        assertTrue(preference.exists)

        preference.set(null)
        assertFalse(preference.exists)
    }

}
