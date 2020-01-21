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

class PersistentStringPreferenceTest : BaseTest() {

    protected val keyValueStoreProvider: KeyValueStoreProvider by inject()

    private lateinit var keyValueStore: KeyValueStore

    companion object {
        private const val TAG = "PersistentStringPreferenceTest"
    }

    @Before
    override fun setUp() {
        super.setUp()

        keyValueStore = keyValueStoreProvider.getKeyValueStore(TAG)
    }

    @Test
    fun testExistsWithDefaultValue() {
        val preference: Preference<String> = PersistentStringPreference(
                key = "string",
                defaultValue = "Hello, World!",
                keyValueStore = keyValueStore
        )
        assertTrue(preference.exists)
    }

    @Test
    fun testExistsWithEmptyDefaultValue() {
        val preference: Preference<String> = PersistentStringPreference(
                key = "string",
                defaultValue = "",
                keyValueStore = keyValueStore
        )
        assertTrue(preference.exists)
    }

    @Test
    fun testExistsWithNullDefaultValue() {
        val preference: Preference<String> = PersistentStringPreference(
                key = "string",
                defaultValue = null,
                keyValueStore = keyValueStore
        )
        assertFalse(preference.exists)
    }

    @Test
    fun testExistsWithWhitespaceDefaultValue() {
        val preference: Preference<String> = PersistentStringPreference(
                key = "string",
                defaultValue = " ",
                keyValueStore = keyValueStore
        )
        assertTrue(preference.exists)
    }

    @Test
    fun testGetAndSetAndDeleteAndExistsWithDefaultValue() {
        val preference: Preference<String> = PersistentStringPreference(
                key = "string",
                defaultValue = "Hello, World!",
                keyValueStore = keyValueStore
        )
        assertEquals("Hello, World!", preference.get())
        assertTrue(preference.exists)

        preference.set("")
        assertEquals("", preference.get())
        assertTrue(preference.exists)

        preference.delete()
        assertEquals("Hello, World!", preference.get())
        assertTrue(preference.exists)

        preference.set("Google")
        assertEquals("Google", preference.get())
        assertTrue(preference.exists)
    }

    @Test
    fun testGetAndSetAndDeleteAndExistsWithNullDefaultValue() {
        val preference: Preference<String> = PersistentStringPreference(
                key = "string",
                defaultValue = null,
                keyValueStore = keyValueStore
        )
        assertNull(preference.get())
        assertFalse(preference.exists)

        preference.set("")
        assertEquals("", preference.get())
        assertTrue(preference.exists)

        preference.delete()
        assertNull(preference.get())
        assertFalse(preference.exists)

        preference.set("Microsoft")
        assertEquals("Microsoft", preference.get())
        assertTrue(preference.exists)
    }

    @Test
    fun testKey() {
        val preference: Preference<String> = PersistentStringPreference(
                key = "string",
                defaultValue = null,
                keyValueStore = keyValueStore
        )
        assertEquals("string", preference.key)
    }

    @Test
    fun testObservableWithDefaultValue() {
        val preference: Preference<String> = PersistentStringPreference(
                key = "string",
                defaultValue = "Hello, World!",
                keyValueStore = keyValueStore
        )

        var value: Optional<String>? = null

        preference.observable.subscribe {
            value = it
        }

        assertNull(value)

        preference.set("blah")
        assertNotNull(value)
        assertEquals("blah", value?.item)

        preference.set("melee")
        assertNotNull(value)
        assertEquals("melee", value?.item)

        preference.delete()
        assertNotNull(value)
        assertEquals("Hello, World!", value?.item)
    }

    @Test
    fun testObservableWithNullDefaultValue() {
        val preference: Preference<String> = PersistentStringPreference(
                key = "string",
                defaultValue = null,
                keyValueStore = keyValueStore
        )

        var value: Optional<String>? = null

        preference.observable.subscribe {
            value = it
        }

        assertNull(value)

        preference.set("nintendo")
        assertNotNull(value)
        assertEquals("nintendo", value?.item)

        preference.set("game")
        assertNotNull(value)
        assertEquals("game", value?.item)

        preference.delete()
        assertNotNull(value)
        assertEquals(false, value?.isPresent)
    }

    @Test
    fun testSetWithNullCausesDelete() {
        val preference: Preference<String> = PersistentStringPreference(
                key = "string",
                defaultValue = null,
                keyValueStore = keyValueStore
        )

        preference.set("Hello, World!")
        assertTrue(preference.exists)

        preference.set(null)
        assertFalse(preference.exists)
    }

}

