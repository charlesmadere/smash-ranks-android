package com.garpr.android.preferences.persistent

import com.garpr.android.BaseTest
import com.garpr.android.models.AbsPlayer
import com.garpr.android.preferences.KeyValueStore
import com.google.gson.Gson
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class PersistentGsonPreferenceTest : BaseTest() {

    @Inject
    protected lateinit var gson: Gson

    @Inject
    protected lateinit var keyValueStore: KeyValueStore


    companion object {
        private const val JSON_LITE_PLAYER_1 = "{\"id\":\"583a4a15d2994e0577b05c74\",\"name\":\"homemadewaffles\"}"
        private const val JSON_LITE_PLAYER_2 = "{\"id\":\"5877eb55d2994e15c7dea97e\",\"name\":\"Spark\"}"
    }

    @Before
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)
    }

    @Test
    @Throws(Exception::class)
    fun testDeleteWithDefaultValue() {
        val player = gson.fromJson(JSON_LITE_PLAYER_1, AbsPlayer::class.java)
        val preference = PersistentGsonPreference("gson", player, keyValueStore,
                AbsPlayer::class.java, gson)
        preference.delete()
        assertNotNull(preference.get())
        assertEquals(player, preference.get())
    }

    @Test
    @Throws(Exception::class)
    fun testDeleteWithNullDefaultValue() {
        val preference = PersistentGsonPreference<AbsPlayer>("gson", null,
                keyValueStore, AbsPlayer::class.java, gson)
        preference.delete()
        assertNull(preference.get())
    }

    @Test
    @Throws(Exception::class)
    fun testExistsWithDefaultValue() {
        val player = gson.fromJson(JSON_LITE_PLAYER_1, AbsPlayer::class.java)
        val preference = PersistentGsonPreference("gson", player, keyValueStore,
                AbsPlayer::class.java, gson)
        assertTrue(preference.exists)

        preference.delete()
        assertTrue(preference.exists)
    }

    @Test
    @Throws(Exception::class)
    fun testExistsWithNullDefaultValue() {
        val preference = PersistentGsonPreference<AbsPlayer>("gson", null,
                keyValueStore, AbsPlayer::class.java, gson)
        assertFalse(preference.exists)

        preference.delete()
        assertFalse(preference.exists)
    }

    @Test
    @Throws(Exception::class)
    fun testGetDefaultValue() {
        val player = gson.fromJson(JSON_LITE_PLAYER_2, AbsPlayer::class.java)
        val preference = PersistentGsonPreference("gson", player, keyValueStore,
                AbsPlayer::class.java, gson)
        assertNotNull(preference.defaultValue)
        assertEquals(player, preference.defaultValue)
    }

    @Test
    @Throws(Exception::class)
    fun testGetDefaultValueWithNull() {
        val preference = PersistentGsonPreference<AbsPlayer>("gson", null,
                keyValueStore, AbsPlayer::class.java, gson)
        assertNull(preference.defaultValue)
    }

    @Test
    @Throws(Exception::class)
    fun testGetWithDefaultValue() {
        val player = gson.fromJson(JSON_LITE_PLAYER_1, AbsPlayer::class.java)
        val preference = PersistentGsonPreference("gson", player, keyValueStore,
                AbsPlayer::class.java, gson)
        assertNotNull(preference.get())
        assertEquals(player, preference.get())
    }

    @Test
    @Throws(Exception::class)
    fun testGetWithNullDefaultValue() {
        val preference = PersistentGsonPreference<AbsPlayer>("gson", null,
                keyValueStore, AbsPlayer::class.java, gson)
        assertNull(preference.get())
    }

    @Test
    @Throws(Exception::class)
    fun testSetWithDefaultValue() {
        val player1 = gson.fromJson(JSON_LITE_PLAYER_1, AbsPlayer::class.java)
        val preference = PersistentGsonPreference("gson", player1, keyValueStore,
                AbsPlayer::class.java, gson)

        val player2 = gson.fromJson(JSON_LITE_PLAYER_2, AbsPlayer::class.java)
        preference.set(player2)
        assertEquals(player2, preference.get())

        preference.set(null as AbsPlayer?)
        assertEquals(player1, preference.get())
    }

    @Test
    @Throws(Exception::class)
    fun testSetWithNullDefaultValue() {
        val preference = PersistentGsonPreference<AbsPlayer>("gson", null,
                keyValueStore, AbsPlayer::class.java, gson)

        val player = gson.fromJson(JSON_LITE_PLAYER_1, AbsPlayer::class.java)
        preference.set(player)
        assertEquals(player, preference.get())

        preference.set(null as AbsPlayer?)
        assertNull(preference.get())
    }

}
