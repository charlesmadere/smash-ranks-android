package com.garpr.android.preferences.persistent

import com.garpr.android.BaseTest
import com.garpr.android.data.models.Optional
import com.garpr.android.preferences.KeyValueStore
import com.garpr.android.preferences.KeyValueStoreProvider
import com.garpr.android.preferences.Preference
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
    fun testExistsWithFalseDefaultValue() {
        val preference: Preference<Boolean> = PersistentBooleanPreference(
                key = "boolean",
                defaultValue = false,
                keyValueStore = keyValueStore
        )
        assertTrue(preference.exists)
    }

    @Test
    fun testExistsWithNullDefaultValue() {
        val preference: Preference<Boolean> = PersistentBooleanPreference(
                key = "boolean",
                defaultValue = null,
                keyValueStore = keyValueStore
        )
        assertFalse(preference.exists)
    }

    @Test
    fun testExistsWithTrueDefaultValue() {
        val preference: Preference<Boolean> = PersistentBooleanPreference(
                key = "boolean",
                defaultValue = true,
                keyValueStore = keyValueStore
        )
        assertTrue(preference.exists)
    }

    @Test
    fun testGetAndSetAndDeleteAndExistsWithDefaultValue() {
        val preference: Preference<Boolean> = PersistentBooleanPreference(
                key = "boolean",
                defaultValue = true,
                keyValueStore = keyValueStore
        )
        assertEquals(true, preference.get())
        assertTrue(preference.exists)

        preference.set(true)
        assertEquals(true, preference.get())
        assertTrue(preference.exists)

        preference.delete()
        assertEquals(true, preference.get())
        assertTrue(preference.exists)

        preference.set(false)
        assertEquals(false, preference.get())
        assertTrue(preference.exists)
    }

    @Test
    fun testGetAndSetAndDeleteAndExistsWithNullDefaultValue() {
        val preference: Preference<Boolean> = PersistentBooleanPreference(
                key = "boolean",
                defaultValue = null,
                keyValueStore = keyValueStore
        )
        assertNull(preference.get())
        assertFalse(preference.exists)

        preference.set(true)
        assertEquals(true, preference.get())
        assertTrue(preference.exists)

        preference.delete()
        assertNull(preference.get())
        assertFalse(preference.exists)

        preference.set(false)
        assertEquals(false, preference.get())
        assertTrue(preference.exists)
    }

    @Test
    fun testKey() {
        val preference: Preference<Boolean> = PersistentBooleanPreference(
                key = "boolean",
                defaultValue = null,
                keyValueStore = keyValueStore
        )
        assertEquals("boolean", preference.key)
    }

    @Test
    fun testObserveWithDefaultValue() {
        val preference: Preference<Boolean> = PersistentBooleanPreference(
                key = "boolean",
                defaultValue = true,
                keyValueStore = keyValueStore
        )

        var value: Optional<Boolean>? = null

        preference.observable.subscribe {
            value = it
        }

        assertNull(value)

        preference.set(true)
        assertNotNull(value)
        assertEquals(true, value?.item)

        preference.set(false)
        assertNotNull(value)
        assertEquals(false, value?.item)

        preference.delete()
        assertNotNull(value)
        assertEquals(true, value?.item)
    }

    @Test
    fun testObserveWithNullDefaultValue() {
        val preference: Preference<Boolean> = PersistentBooleanPreference(
                key = "boolean",
                defaultValue = null,
                keyValueStore = keyValueStore
        )

        var value: Optional<Boolean>? = null

        preference.observable.subscribe {
            value = it
        }

        assertNull(value)

        preference.set(true)
        assertNotNull(value)
        assertEquals(true, value?.item)

        preference.set(false)
        assertNotNull(value)
        assertEquals(false, value?.item)

        preference.delete()
        assertNotNull(value)
        assertEquals(false, value?.isPresent)
    }

    @Test
    fun testSetWithNullCausesDelete() {
        val preference: Preference<Boolean> = PersistentBooleanPreference(
                key = "boolean",
                defaultValue = null,
                keyValueStore = keyValueStore
        )

        preference.set(true)
        assertTrue(preference.exists)

        preference.set(null)
        assertFalse(preference.exists)
    }

}
