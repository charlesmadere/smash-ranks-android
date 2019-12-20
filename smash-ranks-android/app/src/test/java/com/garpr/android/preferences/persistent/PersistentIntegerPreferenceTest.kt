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
class PersistentIntegerPreferenceTest : BaseTest() {

    protected val keyValueStoreProvider: KeyValueStoreProvider by inject()

    private lateinit var keyValueStore: KeyValueStore

    companion object {
        private const val TAG = "PersistentIntegerPreferenceTest"
    }

    @Before
    override fun setUp() {
        super.setUp()

        keyValueStore = keyValueStoreProvider.getKeyValueStore(TAG)
    }

    @Test
    fun testExistsWithDefaultValue() {
        val preference: Preference<Int> = PersistentIntegerPreference(
                key = "integer",
                defaultValue = 1989,
                keyValueStore = keyValueStore
        )
        assertTrue(preference.exists)
    }

    @Test
    fun testExistsWithNullDefaultValue() {
        val preference: Preference<Int> = PersistentIntegerPreference(
                key = "integer",
                defaultValue = null,
                keyValueStore = keyValueStore
        )
        assertFalse(preference.exists)
    }

    @Test
    fun testGetAndSetAndDeleteAndExistsWithDefaultValue() {
        val preference: Preference<Int> = PersistentIntegerPreference(
                key = "integer",
                defaultValue = 900,
                keyValueStore = keyValueStore
        )
        assertEquals(900, preference.get())
        assertTrue(preference.exists)

        preference.set(0)
        assertEquals(0, preference.get())
        assertTrue(preference.exists)

        preference.delete()
        assertEquals(900, preference.get())
        assertTrue(preference.exists)

        preference.set(-20)
        assertEquals(-20, preference.get())
        assertTrue(preference.exists)
    }

    @Test
    fun testGetAndSetAndDeleteAndExistsWithNullDefaultValue() {
        val preference: Preference<Int> = PersistentIntegerPreference(
                key = "integer",
                defaultValue = null,
                keyValueStore = keyValueStore
        )
        assertNull(preference.get())
        assertFalse(preference.exists)

        preference.set(88)
        assertEquals(88, preference.get())
        assertTrue(preference.exists)

        preference.delete()
        assertNull(preference.get())
        assertFalse(preference.exists)

        preference.set(-20)
        assertEquals(-20, preference.get())
        assertTrue(preference.exists)
    }

    @Test
    fun testKey() {
        val preference: Preference<Int> = PersistentIntegerPreference(
                key = "integer",
                defaultValue = null,
                keyValueStore = keyValueStore
        )
        assertEquals("integer", preference.key)
    }

    @Test
    fun testObserveWithDefaultValue() {
        val preference: Preference<Int> = PersistentIntegerPreference(
                key = "integer",
                defaultValue = -31,
                keyValueStore = keyValueStore
        )

        var value: Optional<Int>? = null

        preference.observable.subscribe {
            value = it
        }

        assertNull(value)

        preference.set(25)
        assertNotNull(value)
        assertEquals(25, value?.item)

        preference.set(10)
        assertNotNull(value)
        assertEquals(10, value?.item)

        preference.delete()
        assertNotNull(value)
        assertEquals(-31, value?.item)
    }

    @Test
    fun testObserveWithNullDefaultValue() {
        val preference: Preference<Int> = PersistentIntegerPreference(
                key = "integer",
                defaultValue = null,
                keyValueStore = keyValueStore
        )

        var value: Optional<Int>? = null

        preference.observable.subscribe {
            value = it
        }

        assertNull(value)

        preference.set(11)
        assertNotNull(value)
        assertEquals(11, value?.item)

        preference.set(168)
        assertNotNull(value)
        assertEquals(168, value?.item)

        preference.delete()
        assertNotNull(value)
        assertEquals(false, value?.isPresent)
    }

    @Test
    fun testSetWithNullCausesDelete() {
        val preference: Preference<Int> = PersistentIntegerPreference(
                key = "integer",
                defaultValue = null,
                keyValueStore = keyValueStore
        )

        preference.set(-73)
        assertTrue(preference.exists)

        preference.set(null)
        assertFalse(preference.exists)
    }

}
