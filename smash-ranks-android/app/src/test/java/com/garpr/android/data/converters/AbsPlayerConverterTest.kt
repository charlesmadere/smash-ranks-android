package com.garpr.android.data.converters

import com.garpr.android.BaseTest
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.data.models.FullPlayer
import com.garpr.android.data.models.LitePlayer
import com.garpr.android.data.models.RankedPlayer
import com.garpr.android.data.models.Rating
import com.garpr.android.data.models.Region
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.koin.test.inject

class AbsPlayerConverterTest : BaseTest() {

    protected val moshi: Moshi by inject()

    private lateinit var playerAdapter: JsonAdapter<AbsPlayer>

    companion object {
        private const val JSON_FAVORITE_PLAYER = "{\"region\":{\"endpoint\":\"gar_pr\",\"display_name\":\"Norcal\",\"id\":\"norcal\"},\"id\":\"583a4a15d2994e0577b05c74\",\"name\":\"mikkuz\"}"
        private const val JSON_FULL_PLAYER = "{\"ratings\":{\"norcal\":{\"mu\":44.434429274706105,\"sigma\":0.9786849598499319}},\"name\":\"Laudandus\",\"regions\":[\"norcal\"],\"merge_parent\":null,\"merge_children\":[\"588852e7d2994e3bbfa52d6e\"],\"id\":\"588852e7d2994e3bbfa52d6e\",\"merged\":false,\"aliases\":[\"laudandus\"]}"
        private const val JSON_LITE_PLAYER = "{\"name\":\"Bobo\",\"id\":\"588850b3d2994e3bbfa52d5d\"}"
        private const val JSON_RANKED_PLAYER = "{\"rating\":44.535857570040676,\"name\":\"CLG. | PewPewU\",\"rank\":1,\"previous_rank\":null,\"id\":\"588852e8d2994e3bbfa52da7\"}"

        private val FAVORITE_PLAYER: AbsPlayer = FavoritePlayer(
                id = "583a4a15d2994e0577b05c74",
                name = "mikkuz",
                region = Region(
                        displayName = "Norcal",
                        id = "norcal",
                        endpoint = Endpoint.GAR_PR
                )
        )

        private val FULL_PLAYER: AbsPlayer = FullPlayer(
                id = "5877eb55d2994e15c7dea97e",
                name = "Spark",
                aliases = listOf("Sparky"),
                regions = listOf("norcal", "googlemtv"),
                ratings = mapOf(
                        "norcal" to Rating(1f, 2f)
                )
        )

        private val LITE_PLAYER: AbsPlayer = LitePlayer(
                id = "58523b44d2994e15c7dea945",
                name = "gaR"
        )

        private val RANKED_PLAYER: AbsPlayer = RankedPlayer(
                id = "583a4a15d2994e0577b05c74",
                name = "homemadewaffles",
                rating = 1f,
                rank = 8,
                previousRank = 7
        )
    }

    @Before
    override fun setUp() {
        super.setUp()

        playerAdapter = moshi.adapter(AbsPlayer::class.java)
    }

    @Test
    fun testFromJsonWithFavoritePlayer() {
        val player = playerAdapter.fromJson(JSON_FAVORITE_PLAYER)
        assertTrue(player is FavoritePlayer)
        assertEquals(AbsPlayer.Kind.FAVORITE, player?.kind)
    }

    @Test
    fun testFromJsonWithFullPlayer() {
        val player = playerAdapter.fromJson(JSON_FULL_PLAYER)
        assertTrue(player is FullPlayer)
        assertEquals(AbsPlayer.Kind.FULL, player?.kind)
    }

    @Test
    fun testFromJsonWithLitePlayer() {
        val player = playerAdapter.fromJson(JSON_LITE_PLAYER)
        assertTrue(player is LitePlayer)
        assertEquals(AbsPlayer.Kind.LITE, player?.kind)
    }

    @Test
    fun testFromJsonWithRankedPlayer() {
        val player = playerAdapter.fromJson(JSON_RANKED_PLAYER)
        assertTrue(player is RankedPlayer)
        assertEquals(AbsPlayer.Kind.RANKED, player?.kind)
    }

    @Test
    fun testToJsonWithFavoritePlayer() {
        val json = playerAdapter.toJson(FAVORITE_PLAYER)
        assertFalse(json.isNullOrBlank())

        val player = playerAdapter.fromJson(json)
        assertEquals(FAVORITE_PLAYER, player)
    }

    @Test
    fun testToJsonWithFullPlayer() {
        val json = playerAdapter.toJson(FULL_PLAYER)
        assertFalse(json.isNullOrBlank())

        val player = playerAdapter.fromJson(json)
        assertEquals(FULL_PLAYER, player)
    }

    @Test
    fun testToJsonWithLitePlayer() {
        val json = playerAdapter.toJson(LITE_PLAYER)
        assertFalse(json.isNullOrBlank())

        val player = playerAdapter.fromJson(json)
        assertEquals(LITE_PLAYER, player)
    }

    @Test
    fun testToJsonWithNull() {
        val json = playerAdapter.toJson(null)
        assertTrue(json.isNullOrEmpty())
    }

    @Test
    fun testToJsonWithRankedPlayer() {
        val json = playerAdapter.toJson(RANKED_PLAYER)
        assertFalse(json.isNullOrBlank())

        val player = playerAdapter.fromJson(json)
        assertEquals(RANKED_PLAYER, player)
    }

}
