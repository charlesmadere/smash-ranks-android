package com.garpr.android.models

import com.garpr.android.BaseTest
import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.Collections
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class AbsPlayerTest : BaseTest() {

    private lateinit var favoritePlayer1: AbsPlayer
    private lateinit var favoritePlayer2: AbsPlayer
    private lateinit var fullPlayer1: AbsPlayer
    private lateinit var litePlayer1: AbsPlayer
    private lateinit var litePlayer2: AbsPlayer
    private lateinit var rankedPlayer1: AbsPlayer
    private lateinit var rankedPlayer2: AbsPlayer


    @Inject
    protected lateinit var gson: Gson


    companion object {
        private const val JSON_FAVORITE_PLAYER_1 = "{\"region\":{\"endpoint\":\"gar_pr\",\"display_name\":\"Norcal\",\"id\":\"norcal\"},\"id\":\"583a4a15d2994e0577b05c74\",\"name\":\"mikkuz\"}"
        private const val JSON_FAVORITE_PLAYER_2 = "{\"region\":{\"endpoint\":\"not_gar_pr\",\"display_name\":\"Georgia\",\"id\":\"georgia\"},\"id\":\"583a4a15d2994e0577b05c86\",\"name\":\"druggedfox\"}"
        private const val JSON_FULL_PLAYER_1 = "{\"ratings\":{\"googlemtv\":{\"mu\":37.05546025182014,\"sigma\":2.0824461049194727},\"norcal\":{\"mu\":37.02140742867105,\"sigma\":2.3075802611877165}},\"name\":\"gaR\",\"regions\":[\"norcal\",\"googlemtv\"],\"merge_children\":[\"58523b44d2994e15c7dea945\"],\"id\":\"58523b44d2994e15c7dea945\",\"merged\":false,\"merge_parent\":null}"
        private const val JSON_LITE_PLAYER_1 = "{\"id\":\"583a4a15d2994e0577b05c74\",\"name\":\"homemadewaffles\"}"
        private const val JSON_LITE_PLAYER_2 = "{\"id\":\"5877eb55d2994e15c7dea97e\",\"name\":\"Spark\"}"
        private const val JSON_RANKED_PLAYER_1 = "{\"rating\":36.81988474543549,\"name\":\"ycz6\",\"rank\":13,\"previous_rank\":16,\"id\":\"5888542ad2994e3bbfa52de4\"}"
        private const val JSON_RANKED_PLAYER_2 = "{\"id\":\"53c64dba8ab65f6e6651f7bc\",\"name\":\"Hax\",\"rank\":2,\"rating\":37.975921649503086}"
    }

    @Before
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)

        favoritePlayer1 = gson.fromJson(JSON_FAVORITE_PLAYER_1, AbsPlayer::class.java)
        favoritePlayer2 = gson.fromJson(JSON_FAVORITE_PLAYER_2, AbsPlayer::class.java)
        fullPlayer1 = gson.fromJson(JSON_FULL_PLAYER_1, AbsPlayer::class.java)
        litePlayer1 = gson.fromJson(JSON_LITE_PLAYER_1, AbsPlayer::class.java)
        litePlayer2 = gson.fromJson(JSON_LITE_PLAYER_2, AbsPlayer::class.java)
        rankedPlayer1 = gson.fromJson(JSON_RANKED_PLAYER_1, AbsPlayer::class.java)
        rankedPlayer2 = gson.fromJson(JSON_RANKED_PLAYER_2, AbsPlayer::class.java)
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
    fun testEquals() {
        assertEquals(favoritePlayer1, favoritePlayer1)
        assertEquals(favoritePlayer2, favoritePlayer2)
        assertNotEquals(favoritePlayer1, favoritePlayer2)

        assertEquals(fullPlayer1, fullPlayer1)
        assertNotEquals(fullPlayer1, litePlayer1)
        assertNotEquals(fullPlayer1, rankedPlayer1)

        assertEquals(litePlayer1, litePlayer1)
        assertEquals(litePlayer2, litePlayer2)
        assertNotEquals(litePlayer1, litePlayer2)

        assertEquals(rankedPlayer1, rankedPlayer1)
        assertEquals(rankedPlayer2, rankedPlayer2)
        assertNotEquals(rankedPlayer1, rankedPlayer2)
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
        assertTrue(fullPlayer.aliases.isNullOrEmpty())
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
    fun testFromJsonRankedPlayer1() {
        assertEquals("ycz6", rankedPlayer1.name)
        assertEquals("5888542ad2994e3bbfa52de4", rankedPlayer1.id)

        assertTrue(rankedPlayer1 is RankedPlayer)
        assertEquals(AbsPlayer.Kind.RANKED, rankedPlayer1.kind)

        val rankedPlayer = rankedPlayer1 as RankedPlayer
        assertEquals(13, rankedPlayer.rank)
        assertNotNull(rankedPlayer.rating)
        assertEquals(16, rankedPlayer.previousRank)
    }

    @Test
    @Throws(Exception::class)
    fun testFromJsonRankedPlayer2() {
        assertEquals("Hax", rankedPlayer2.name)
        assertEquals("53c64dba8ab65f6e6651f7bc", rankedPlayer2.id)

        assertTrue(rankedPlayer2 is RankedPlayer)
        assertEquals(AbsPlayer.Kind.RANKED, rankedPlayer2.kind)

        val rankedPlayer = rankedPlayer2 as RankedPlayer
        assertEquals(2, rankedPlayer.rank)
        assertNotNull(rankedPlayer.rating)
        assertNull(rankedPlayer.previousRank)
    }

    @Test
    @Throws(Exception::class)
    fun testHashCode() {
        assertEquals("583a4a15d2994e0577b05c74".hashCode(), favoritePlayer1.hashCode())
        assertEquals("583a4a15d2994e0577b05c86".hashCode(), favoritePlayer2.hashCode())
        assertEquals("58523b44d2994e15c7dea945".hashCode(), fullPlayer1.hashCode())
        assertEquals("583a4a15d2994e0577b05c74".hashCode(), litePlayer1.hashCode())
        assertEquals("5877eb55d2994e15c7dea97e".hashCode(), litePlayer2.hashCode())
        assertEquals("5888542ad2994e3bbfa52de4".hashCode(), rankedPlayer1.hashCode())
        assertEquals("53c64dba8ab65f6e6651f7bc".hashCode(), rankedPlayer2.hashCode())
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
