package com.garpr.android.models

import com.garpr.android.BaseTest
import com.google.gson.Gson
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.*
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class AbsPlayerTest : BaseTest() {

    private lateinit var favoritePlayer1: AbsPlayer
    private lateinit var favoritePlayer2: AbsPlayer
    private lateinit var fullPlayer1: AbsPlayer
    private lateinit var litePlayer1: AbsPlayer
    private lateinit var litePlayer2: AbsPlayer

    @Inject
    lateinit protected var gson: Gson


    companion object {
        private const val JSON_FAVORITE_PLAYER_1 = "{\"region\":{\"endpoint\":\"gar_pr\",\"display_name\":\"Norcal\",\"id\":\"norcal\"},\"id\":\"583a4a15d2994e0577b05c74\",\"name\":\"mikkuz\"}"
        private const val JSON_FAVORITE_PLAYER_2 = "{\"region\":{\"endpoint\":\"not_gar_pr\",\"display_name\":\"Georgia\",\"id\":\"georgia\"},\"id\":\"583a4a15d2994e0577b05c86\",\"name\":\"druggedfox\"}"
        private const val JSON_FULL_PLAYER_1 = "{\"ratings\":{\"googlemtv\":{\"mu\":37.05546025182014,\"sigma\":2.0824461049194727},\"norcal\":{\"mu\":37.02140742867105,\"sigma\":2.3075802611877165}},\"name\":\"gaR\",\"regions\":[\"norcal\",\"googlemtv\"],\"merge_children\":[\"58523b44d2994e15c7dea945\"],\"id\":\"58523b44d2994e15c7dea945\",\"merged\":false,\"merge_parent\":null}"
        private const val JSON_LITE_PLAYER_1 = "{\"id\":\"583a4a15d2994e0577b05c74\",\"name\":\"homemadewaffles\"}"
        private const val JSON_LITE_PLAYER_2 = "{\"id\":\"5877eb55d2994e15c7dea97e\",\"name\":\"Spark\"}"
    }

    @Before
    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)

        favoritePlayer1 = gson.fromJson(JSON_FAVORITE_PLAYER_1, AbsPlayer::class.java)
        favoritePlayer2 = gson.fromJson(JSON_FAVORITE_PLAYER_2, AbsPlayer::class.java)
        fullPlayer1 = gson.fromJson(JSON_FULL_PLAYER_1, AbsPlayer::class.java)
        litePlayer1 = gson.fromJson(JSON_LITE_PLAYER_1, AbsPlayer::class.java)
        litePlayer2 = gson.fromJson(JSON_LITE_PLAYER_2, AbsPlayer::class.java)
    }

    @Test
    @Throws(Exception::class)
    fun testComparatorAlphabeticalOrder() {
        val list = listOf(fullPlayer1, litePlayer2, litePlayer1, favoritePlayer2, favoritePlayer1)
        Collections.sort(list, AbsPlayer.ALPHABETICAL_ORDER)

        assertEquals(favoritePlayer2, list[0])
        assertEquals(fullPlayer1, list[1])
        assertEquals(litePlayer1, list[2])
        assertEquals(favoritePlayer1, list[3])
        assertEquals(litePlayer2, list[4])
    }

    @Test
    @Throws(Exception::class)
    fun testFromJsonFavoritePlayer1() {
        assertEquals("mikkuz", favoritePlayer1.name)
        assertEquals("583a4a15d2994e0577b05c74", favoritePlayer1.id)

        assertTrue(favoritePlayer1 is FavoritePlayer)
        assertEquals(AbsPlayer.Kind.FAVORITE, favoritePlayer1.kind)

        val favoritePlayer = favoritePlayer1 as FavoritePlayer
        assertEquals("norcal", favoritePlayer.region.id)
        assertEquals(Endpoint.GAR_PR, favoritePlayer.region.endpoint)
    }

    @Test
    @Throws(Exception::class)
    fun testFromJsonFavoritePlayer2() {
        assertEquals("druggedfox", favoritePlayer2.name)
        assertEquals("583a4a15d2994e0577b05c86", favoritePlayer2.id)

        assertTrue(favoritePlayer2 is FavoritePlayer)
        assertEquals(AbsPlayer.Kind.FAVORITE, favoritePlayer2.kind)

        val favoritePlayer = favoritePlayer2 as FavoritePlayer
        assertEquals("georgia", favoritePlayer.region.id)
        assertEquals(Endpoint.NOT_GAR_PR, favoritePlayer.region.endpoint)
    }

    @Test
    @Throws(Exception::class)
    fun testFromJsonFullPlayer1() {
        assertEquals("gaR", fullPlayer1.name)
        assertEquals("58523b44d2994e15c7dea945", fullPlayer1.id)

        assertTrue(fullPlayer1 is FullPlayer)
        assertEquals(AbsPlayer.Kind.FULL, fullPlayer1.kind)

        val fullPlayer = fullPlayer1 as FullPlayer
        assertTrue(fullPlayer.aliases == null || fullPlayer.aliases?.isEmpty() == true)
        assertTrue(fullPlayer.ratings?.isNotEmpty() == true)
        assertTrue(fullPlayer.regions?.isNotEmpty() == true)
    }

    @Test
    @Throws(Exception::class)
    fun testFromJsonLitePlayer1() {
        assertEquals("homemadewaffles", litePlayer1.name)
        assertEquals("583a4a15d2994e0577b05c74", litePlayer1.id)

        assertTrue(litePlayer1 is LitePlayer)
        assertEquals(AbsPlayer.Kind.LITE, litePlayer1.kind)
    }

    @Test
    @Throws(Exception::class)
    fun testFromJsonLitePlayer2() {
        assertEquals("Spark", litePlayer2.name)
        assertEquals("5877eb55d2994e15c7dea97e", litePlayer2.id)

        assertTrue(litePlayer2 is LitePlayer)
        assertEquals(AbsPlayer.Kind.LITE, litePlayer2.kind)
    }

    @Test
    @Throws(Exception::class)
    fun testFromJsonNull() {
        val player = gson.fromJson(null as String?, AbsPlayer::class.java)
        assertNull(player)
    }

    @Test
    @Throws(Exception::class)
    fun testToJsonAndBackWithFavoritePlayer() {
        val json = gson.toJson(favoritePlayer1, AbsPlayer::class.java)
        val after = gson.fromJson(json, AbsPlayer::class.java)

        assertEquals(favoritePlayer1, after)
        assertEquals(favoritePlayer1.kind, after.kind)
    }

    @Test
    @Throws(Exception::class)
    fun testToJsonAndBackWithFullPlayer() {
        val json = gson.toJson(fullPlayer1, AbsPlayer::class.java)
        val after = gson.fromJson(json, AbsPlayer::class.java)

        assertEquals(fullPlayer1, after)
        assertEquals(fullPlayer1.kind, after.kind)
    }

    @Test
    @Throws(Exception::class)
    fun testToJsonAndBackWithLitePlayer1() {
        val json = gson.toJson(litePlayer1, AbsPlayer::class.java)
        val after = gson.fromJson(json, AbsPlayer::class.java)

        assertEquals(litePlayer1, after)
        assertEquals(litePlayer1.kind, after.kind)
    }

    @Test
    @Throws(Exception::class)
    fun testToJsonAndBackWithLitePlayer2() {
        val json = gson.toJson(litePlayer2, AbsPlayer::class.java)
        val after = gson.fromJson(json, AbsPlayer::class.java)

        assertEquals(litePlayer2, after)
        assertEquals(litePlayer2.kind, after.kind)
    }

}
