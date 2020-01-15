package com.garpr.android.preferences.persistent

import com.garpr.android.BaseKoinTest
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.AbsRegion
import com.garpr.android.data.models.LitePlayer
import com.garpr.android.data.models.LiteRegion
import com.garpr.android.data.models.Optional
import com.garpr.android.preferences.KeyValueStore
import com.garpr.android.preferences.KeyValueStoreProvider
import com.garpr.android.preferences.Preference
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.koin.test.inject

class PersistentMoshiPreferenceTest : BaseKoinTest() {

    protected val keyValueStoreProvider: KeyValueStoreProvider by inject()
    protected val moshi: Moshi by inject()

    private lateinit var absPlayerAdapter: JsonAdapter<AbsPlayer>
    private lateinit var absRegionAdapter: JsonAdapter<AbsRegion>
    private lateinit var keyValueStore: KeyValueStore

    companion object {
        private val HMW: AbsPlayer = LitePlayer(
                id = "583a4a15d2994e0577b05c74",
                name = "homemadewaffles"
        )

        private val MIKKUZ: AbsPlayer = LitePlayer(
                id = "583a4a15d2994e0577b05c74",
                name = "mikkuz"
        )

        private val SNAP: AbsPlayer = LitePlayer(
                id = "59213f1ad2994e1d79144956",
                name = "Snap"
        )

        private val SPARK: AbsPlayer = LitePlayer(
                id = "5877eb55d2994e15c7dea97e",
                name = "Spark"
        )

        private val GOOGLE_MTV: AbsRegion = LiteRegion(
                displayName = "Google MTV",
                id = "googlemtv"
        )

        private val NORCAL: AbsRegion = LiteRegion(
                displayName = "Norcal",
                id = "norcal"
        )

        private const val TAG = "PersistentMoshiPreferenceTest"
    }

    @Before
    override fun setUp() {
        super.setUp()

        absPlayerAdapter = moshi.adapter(AbsPlayer::class.java)
        absRegionAdapter = moshi.adapter(AbsRegion::class.java)
        keyValueStore = keyValueStoreProvider.getKeyValueStore(TAG)
    }

    @Test
    fun testExistsWithDefaultValue() {
        val preference: Preference<AbsRegion> = PersistentMoshiPreference(
                key = "moshi",
                defaultValue = NORCAL,
                keyValueStore = keyValueStore,
                jsonAdapter = absRegionAdapter
        )
        assertTrue(preference.exists)
    }

    @Test
    fun testExistsWithNullDefaultValue() {
        val preference: Preference<AbsPlayer> = PersistentMoshiPreference(
                key = "moshi",
                defaultValue = null,
                keyValueStore = keyValueStore,
                jsonAdapter = absPlayerAdapter
        )
        assertFalse(preference.exists)
    }

    @Test
    fun testKeyWithHmw() {
        val preference: Preference<AbsPlayer> = PersistentMoshiPreference(
                key = "hmw",
                defaultValue = HMW,
                keyValueStore = keyValueStore,
                jsonAdapter = absPlayerAdapter
        )
        assertEquals("hmw", preference.key)
    }

    @Test
    fun testKeyWithSpark() {
        val preference: Preference<AbsPlayer> = PersistentMoshiPreference(
                key = "spark",
                defaultValue = SPARK,
                keyValueStore = keyValueStore,
                jsonAdapter = absPlayerAdapter
        )
        assertEquals("spark", preference.key)
    }

    @Test
    fun testGetAndSetAndDeleteAndExistsWithDefaultValue() {
        val preference: Preference<AbsPlayer> = PersistentMoshiPreference(
                key = "player",
                defaultValue = SPARK,
                keyValueStore = keyValueStore,
                jsonAdapter = absPlayerAdapter
        )
        assertEquals(SPARK, preference.get())
        assertTrue(preference.exists)

        preference.set(HMW)
        assertEquals(HMW, preference.get())
        assertTrue(preference.exists)

        preference.delete()
        assertEquals(SPARK, preference.get())
        assertTrue(preference.exists)

        preference.set(SNAP)
        assertEquals(SNAP, preference.get())
        assertTrue(preference.exists)
    }

    @Test
    fun testGetAndSetAndDeleteAndExistsWithNullDefaultValue() {
        val preference: Preference<AbsPlayer> = PersistentMoshiPreference(
                key = "player",
                defaultValue = null,
                keyValueStore = keyValueStore,
                jsonAdapter = absPlayerAdapter
        )
        assertNull(preference.get())
        assertFalse(preference.exists)

        preference.set(MIKKUZ)
        assertEquals(MIKKUZ, preference.get())
        assertTrue(preference.exists)

        preference.delete()
        assertNull(preference.get())
        assertFalse(preference.exists)

        preference.set(SNAP)
        assertEquals(SNAP, preference.get())
        assertTrue(preference.exists)
    }

    @Test
    fun testObservableWithDefaultValue() {
        val preference: Preference<AbsPlayer> = PersistentMoshiPreference(
                key = "player",
                defaultValue = SNAP,
                keyValueStore = keyValueStore,
                jsonAdapter = absPlayerAdapter
        )

        var value: Optional<AbsPlayer>? = null

        preference.observable.subscribe {
            value = it
        }

        assertNull(value)

        preference.set(MIKKUZ)
        assertNotNull(value)
        assertEquals(MIKKUZ, value?.item)

        preference.set(HMW)
        assertNotNull(value)
        assertEquals(HMW, value?.item)

        preference.delete()
        assertNotNull(value)
        assertEquals(SNAP, value?.item)
    }

    @Test
    fun testObservableWithNullDefaultValue() {
        val preference: Preference<AbsPlayer> = PersistentMoshiPreference(
                key = "player",
                defaultValue = null,
                keyValueStore = keyValueStore,
                jsonAdapter = absPlayerAdapter
        )

        var value: Optional<AbsPlayer>? = null

        preference.observable.subscribe {
            value = it
        }

        assertNull(value)

        preference.set(SPARK)
        assertNotNull(value)
        assertEquals(SPARK, value?.item)

        preference.set(MIKKUZ)
        assertNotNull(value)
        assertEquals(MIKKUZ, value?.item)

        preference.delete()
        assertNotNull(value)
        assertEquals(false, value?.isPresent)
    }

    @Test
    fun testSetWithNullCausesDelete() {
        val preference: Preference<AbsRegion> = PersistentMoshiPreference(
                key = "region",
                defaultValue = null,
                keyValueStore = keyValueStore,
                jsonAdapter = absRegionAdapter
        )

        preference.set(GOOGLE_MTV)
        assertTrue(preference.exists)

        preference.set(null)
        assertFalse(preference.exists)
    }

}
