package com.garpr.android.models

import com.garpr.android.BaseTest
import com.garpr.android.BuildConfig
import com.google.gson.Gson
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.*
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class AbsPlayerTest : BaseTest() {

    private lateinit var mFavoritePlayer1: AbsPlayer
    private lateinit var mFullPlayer1: AbsPlayer
    private lateinit var mLitePlayer1: AbsPlayer
    private lateinit var mLitePlayer2: AbsPlayer

    @Inject
    lateinit protected var mGson: Gson


    companion object {
        private const val JSON_FAVORITE_PLAYER_1 = "{\"region\":{\"endpoint\":\"gar_pr\",\"display_name\":\"Norcal\",\"id\":\"norcal\"},\"id\":\"583a4a15d2994e0577b05c74\",\"name\":\"mikkuz\"}"
        private const val JSON_FULL_PLAYER_1 = "{\"ratings\":{\"googlemtv\":{\"mu\":37.05546025182014,\"sigma\":2.0824461049194727},\"norcal\":{\"mu\":37.02140742867105,\"sigma\":2.3075802611877165}},\"name\":\"gaR\",\"regions\":[\"norcal\",\"googlemtv\"],\"merge_children\":[\"58523b44d2994e15c7dea945\"],\"id\":\"58523b44d2994e15c7dea945\",\"merged\":false,\"merge_parent\":null}"
        private const val JSON_LITE_PLAYER_1 = "{\"id\":\"583a4a15d2994e0577b05c74\",\"name\":\"homemadewaffles\"}"
        private const val JSON_LITE_PLAYER_2 = "{\"id\":\"5877eb55d2994e15c7dea97e\",\"name\":\"Spark\"}"
    }

    @Before
    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)

        mFavoritePlayer1 = mGson.fromJson(JSON_FAVORITE_PLAYER_1, AbsPlayer::class.java)
        mFullPlayer1 = mGson.fromJson(JSON_FULL_PLAYER_1, AbsPlayer::class.java)
        mLitePlayer1 = mGson.fromJson(JSON_LITE_PLAYER_1, AbsPlayer::class.java)
        mLitePlayer2 = mGson.fromJson(JSON_LITE_PLAYER_2, AbsPlayer::class.java)
    }

    @Test
    @Throws(Exception::class)
    fun testComparatorAlphabeticalOrder() {
        val list = mutableListOf(mFullPlayer1, mLitePlayer1, mLitePlayer2, mFavoritePlayer1)
        Collections.sort(list, AbsPlayer.ALPHABETICAL_ORDER)

        assertEquals(mFullPlayer1, list[0])
        assertEquals(mLitePlayer1, list[1])
        assertEquals(mFavoritePlayer1, list[2])
        assertEquals(mLitePlayer2, list[3])
    }

    @Test
    @Throws(Exception::class)
    fun testFromJsonFavoritePlayer() {
        val player = mGson.fromJson(JSON_FAVORITE_PLAYER_1, AbsPlayer::class.java)
        assertNotNull(player)

        assertEquals("homemadewaffles", player.name)
        assertEquals("583a4a15d2994e0577b05c74", player.id)

        assertTrue(player is FavoritePlayer)
        assertEquals(player.kind, AbsPlayer.Kind.FAVORITE)

        val favoritePlayer = player as FavoritePlayer
        val region = favoritePlayer.region
        assertNotNull(region)
        assertEquals("norcal", region.id)

        val endpoint = region.endpoint
        assertNotNull(endpoint)
        assertEquals(endpoint, Endpoint.GAR_PR)
    }

    @Test
    @Throws(Exception::class)
    fun testFromJsonFullPlayer() {
        val player = mGson.fromJson(JSON_FULL_PLAYER_1, AbsPlayer::class.java)
        assertNotNull(player)

        assertEquals("gaR", player.name)
        assertEquals("58523b44d2994e15c7dea945", player.id)

        assertTrue(player is FullPlayer)
        assertEquals(AbsPlayer.Kind.FULL, player.kind)

        val fullPlayer = player as FullPlayer
        assertTrue(fullPlayer.aliases?.isEmpty() == true)
        assertTrue(fullPlayer.ratings?.isNotEmpty() == true)
        assertTrue(fullPlayer.regions?.isNotEmpty() == true)
    }

    @Test
    @Throws(Exception::class)
    fun testFromJsonLitePlayer() {
        val player = mGson.fromJson(JSON_LITE_PLAYER_1, AbsPlayer::class.java)
        assertNotNull(player)

        assertEquals(player.name, "homemadewaffles")
        assertEquals(player.id, "583a4a15d2994e0577b05c74")

        assertTrue(player is LitePlayer)
        assertEquals(player.kind, AbsPlayer.Kind.LITE)
    }

    @Test
    @Throws(Exception::class)
    fun testFromNull() {
        val player = mGson.fromJson(null as String?, AbsPlayer::class.java)
        assertNull(player)
    }

    @Test
    @Throws(Exception::class)
    fun testToJsonAndBackWithFavoritePlayer() {
        val before = mGson.fromJson(JSON_FAVORITE_PLAYER_1, AbsPlayer::class.java)
        val json = mGson.toJson(before, AbsPlayer::class.java)
        val after = mGson.fromJson(json, AbsPlayer::class.java)

        assertEquals(before, after)
        assertEquals(before.kind, after.kind)
    }

    @Test
    @Throws(Exception::class)
    fun testToJsonAndBackWithFullPlayer() {
        val before = mGson.fromJson(JSON_FULL_PLAYER_1, AbsPlayer::class.java)
        val json = mGson.toJson(before, AbsPlayer::class.java)
        val after = mGson.fromJson(json, AbsPlayer::class.java)

        assertEquals(before, after)
        assertEquals(before.kind, after.kind)
    }

    @Test
    @Throws(Exception::class)
    fun testToJsonAndBackWithLitePlayer() {
        val before = mGson.fromJson(JSON_LITE_PLAYER_1, AbsPlayer::class.java)
        val json = mGson.toJson(before, AbsPlayer::class.java)
        val after = mGson.fromJson(json, AbsPlayer::class.java)

        assertEquals(before, after)
        assertEquals(before.kind, after.kind)
    }

}
