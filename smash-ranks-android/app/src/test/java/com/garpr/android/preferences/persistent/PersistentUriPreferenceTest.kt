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
import java.net.URI as JavaUri

@RunWith(RobolectricTestRunner::class)
class PersistentUriPreferenceTest : BaseTest() {

    protected val keyValueStoreProvider: KeyValueStoreProvider by inject()

    private lateinit var keyValueStore: KeyValueStore

    companion object {
        private const val TAG = "PersistentUriPreferenceTest"
        private val AMAZON = JavaUri.create("https://www.amazon.com/")
        private val GOOGLE = JavaUri.create("https://www.google.com/")
        private val POLYGON = JavaUri.create("https://www.polygon.com/best-games/2019/12/13/21002670/best-games-2019-ps4-pc-xbox-one-nintendo-switch-ios")
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
        assertEquals(GOOGLE, value?.item)

        preference.set(POLYGON)
        assertNotNull(value)
        assertEquals(POLYGON, value?.item)

        preference.delete()
        assertNotNull(value)
        assertEquals(AMAZON, value?.item)
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
        assertEquals(POLYGON, value?.item)

        preference.set(GOOGLE)
        assertNotNull(value)
        assertEquals(GOOGLE, value?.item)

        preference.delete()
        assertNotNull(value)
        assertEquals(false, value?.isPresent)
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
