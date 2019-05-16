package com.garpr.android.preferences.persistent

import com.garpr.android.BaseTest
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.LitePlayer
import com.garpr.android.preferences.KeyValueStore
import com.squareup.moshi.Moshi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class PersistentMoshiPreferenceTest : BaseTest() {

    @Inject
    protected lateinit var keyValueStore: KeyValueStore

    @Inject
    protected lateinit var moshi: Moshi


    companion object {
        private val PLAYER_0 = LitePlayer(
                id = "583a4a15d2994e0577b05c74",
                name = "homemadewaffles"
        )

        private val PLAYER_1 = LitePlayer(
                id = "5877eb55d2994e15c7dea97e",
                name = "Spark"
        )
    }

    @Before
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)
    }

    @Test
    fun testDefaultValueWithNull() {
        val preference = PersistentMoshiPreference("DEFAULT_VALUE", null,
                keyValueStore, moshi, AbsPlayer::class.java)
        assertNull(preference.defaultValue)
    }

    @Test
    fun testDefaultValueWithPlayer0() {
        val preference = PersistentMoshiPreference("DEFAULT_VALUE", PLAYER_0, keyValueStore,
                moshi, AbsPlayer::class.java)
        assertEquals(PLAYER_0, preference.defaultValue)
        assertEquals(preference.defaultValue, preference.get())
    }

    @Test
    fun testDeleteWithDefaultValue() {
        val preference = PersistentMoshiPreference("DELETE", PLAYER_0, keyValueStore, moshi,
                AbsPlayer::class.java)

        assertEquals(PLAYER_0, preference.defaultValue)
        assertEquals(PLAYER_0, preference.get())

        preference.delete()

        assertEquals(PLAYER_0, preference.defaultValue)
        assertEquals(PLAYER_0, preference.get())
    }

    @Test
    fun testDeleteWithNullDefaultValue() {
        val preference = PersistentMoshiPreference("DELETE", null, keyValueStore,
                moshi, AbsPlayer::class.java)

        assertNull(preference.defaultValue)
        assertNull(preference.get())

        preference.delete()

        assertNull(preference.defaultValue)
        assertNull(preference.get())
    }

    @Test
    fun testExistsWithDefaultValue() {
        val preference = PersistentMoshiPreference("EXISTS", PLAYER_1, keyValueStore, moshi,
                AbsPlayer::class.java)
        assertTrue(preference.exists)
    }

    @Test
    fun testExistsWithNullDefaultValue() {
        val preference = PersistentMoshiPreference("EXISTS", null, keyValueStore,
                moshi, AbsPlayer::class.java)
        assertFalse(preference.exists)
    }

    @Test
    fun testGetAndSetAndDelete() {
        val preference = PersistentMoshiPreference<AbsPlayer>("PREFERENCE", null,
                keyValueStore, moshi, AbsPlayer::class.java)

        assertFalse(preference.exists)
        assertNull(preference.get())

        preference.set(PLAYER_1)
        assertTrue(preference.exists)
        assertEquals(PLAYER_1, preference.get())

        preference.delete()
        assertFalse(preference.exists)
        assertNull(preference.get())

        preference.set(PLAYER_0)
        assertTrue(preference.exists)
        assertEquals(PLAYER_0, preference.get())
    }

    @Test
    fun testGetAndSetWithNullAndDefaultValue() {
        val preference = PersistentMoshiPreference("GET", PLAYER_0, keyValueStore, moshi,
                AbsPlayer::class.java)
        preference.set(null)
        assertEquals(PLAYER_0, preference.get())
    }

    @Test
    fun testGetAndSetWithNullAndNullDefaultValue() {
        val preference = PersistentMoshiPreference("GET", null, keyValueStore,
                moshi, AbsPlayer::class.java)
        preference.set(null)
        assertNull(preference.get())
    }

    @Test
    fun testGetAndSetWithPlayer0AndNullDefaultValue() {
        val preference = PersistentMoshiPreference<AbsPlayer>("GET", null,
                keyValueStore, moshi, AbsPlayer::class.java)
        preference.set(PLAYER_0)
        assertEquals(PLAYER_0, preference.get())
    }

    @Test
    fun testGetAndSetWithPlayer1AndPlayer0DefaultValue() {
        val preference = PersistentMoshiPreference("GET", PLAYER_0, keyValueStore, moshi,
                AbsPlayer::class.java)
        preference.set(PLAYER_1)
        assertEquals(PLAYER_1, preference.get())
    }

}
