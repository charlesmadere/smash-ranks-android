package com.garpr.android.preferences.persistent

import com.garpr.android.data.models.Optional
import com.garpr.android.extensions.toJavaUri
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
import java.net.URI as JavaUri

class PersistentUriPreferenceTest : BaseTest() {

    protected val keyValueStoreProvider: KeyValueStoreProvider by inject()

    private lateinit var keyValueStore: KeyValueStore

    companion object {
        private const val TAG = "PersistentUriPreferenceTest"
        private val AMAZON = "https://www.amazon.com/".toJavaUri()
        private val GOOGLE = "https://www.google.com/".toJavaUri()
        private val POLYGON = "https://www.polygon.com/best-games/2019/12/13/21002670/best-games-2019-ps4-pc-xbox-one-nintendo-switch-ios".toJavaUri()
    }

    @Before
    override fun setUp() {
        super.setUp()

        keyValueStore = keyValueStoreProvider.getKeyValueStore(TAG)
    }

    @Test
    fun testExistsWithDefaultValue() {
        val preference: Preference<JavaUri> = PersistentUriPreference(
                key = "uri",
                defaultValue = GOOGLE,
                keyValueStore = keyValueStore
        )
        assertTrue(preference.exists)
    }

    @Test
    fun testExistsWithNullDefaultValue() {
        val preference: Preference<JavaUri> = PersistentUriPreference(
                key = "uri",
                defaultValue = null,
                keyValueStore = keyValueStore
        )
        assertFalse(preference.exists)
    }

    @Test
    fun testGetAndSetAndDeleteAndExistsWithDefaultValue() {
        val preference: Preference<JavaUri> = PersistentUriPreference(
                key = "uri",
                defaultValue = AMAZON,
                keyValueStore = keyValueStore
        )
        assertEquals(AMAZON, preference.get())
        assertTrue(preference.exists)

        preference.set(GOOGLE)
        assertEquals(GOOGLE, preference.get())
        assertTrue(preference.exists)

        preference.delete()
        assertEquals(AMAZON, preference.get())
        assertTrue(preference.exists)

        preference.set(POLYGON)
        assertEquals(POLYGON, preference.get())
        assertTrue(preference.exists)
    }

    @Test
    fun testGetAndSetAndDeleteAndExistsWithNullDefaultValue() {
        val preference: Preference<JavaUri> = PersistentUriPreference(
                key = "uri",
                defaultValue = null,
                keyValueStore = keyValueStore
        )
        assertNull(preference.get())
        assertFalse(preference.exists)

        preference.set(AMAZON)
        assertEquals(AMAZON, preference.get())
        assertTrue(preference.exists)

        preference.delete()
        assertNull(preference.get())
        assertFalse(preference.exists)

        preference.set(POLYGON)
        assertEquals(POLYGON, preference.get())
        assertTrue(preference.exists)
    }

    @Test
    fun testKey() {
        val preference: Preference<JavaUri> = PersistentUriPreference(
                key = "uri",
                defaultValue = null,
                keyValueStore = keyValueStore
        )
        assertEquals("uri", preference.key)
    }

    @Test
    fun testObservableWithDefaultValue() {
        val preference: Preference<JavaUri> = PersistentUriPreference(
                key = "uri",
                defaultValue = AMAZON,
                keyValueStore = keyValueStore
        )

        var value: Optional<JavaUri>? = null

        preference.observable.subscribe {
            value = it
        }

        assertNull(value)

        preference.set(GOOGLE)
        assertNotNull(value)
        assertEquals(GOOGLE, value?.orNull())

        preference.set(POLYGON)
        assertNotNull(value)
        assertEquals(POLYGON, value?.orNull())

        preference.delete()
        assertNotNull(value)
        assertEquals(AMAZON, value?.orNull())
    }

    @Test
    fun testObservableWithNullDefaultValue() {
        val preference: Preference<JavaUri> = PersistentUriPreference(
                key = "uri",
                defaultValue = null,
                keyValueStore = keyValueStore
        )

        var value: Optional<JavaUri>? = null

        preference.observable.subscribe {
            value = it
        }

        assertNull(value)

        preference.set(POLYGON)
        assertNotNull(value)
        assertEquals(POLYGON, value?.orNull())

        preference.set(GOOGLE)
        assertNotNull(value)
        assertEquals(GOOGLE, value?.orNull())

        preference.delete()
        assertNotNull(value)
        assertEquals(false, value?.isPresent())
    }

    @Test
    fun testSetWithNullCausesDelete() {
        val preference: Preference<JavaUri> = PersistentUriPreference(
                key = "uri",
                defaultValue = null,
                keyValueStore = keyValueStore
        )

        preference.set(GOOGLE)
        assertTrue(preference.exists)

        preference.set(null)
        assertFalse(preference.exists)
    }

}
