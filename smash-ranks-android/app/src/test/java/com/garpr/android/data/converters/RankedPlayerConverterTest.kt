package com.garpr.android.data.converters

import com.garpr.android.data.models.RankedPlayer
import com.garpr.android.test.BaseTest
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.koin.test.inject

class RankedPlayerConverterTest : BaseTest() {

    protected val moshi: Moshi by inject()

    private lateinit var rankedPlayerAdapter: JsonAdapter<RankedPlayer>

    companion object {
        private const val JSON_PLAYER_0 = "{\"rating\":40.54488107578405,\"name\":\"Kevbot\",\"rank\":10,\"previous_rank\":8,\"id\":\"588852e8d2994e3bbfa52dbd\"}"
        private const val JSON_PLAYER_1 = "{\"rating\":44.516087199067016,\"name\":\"SFAT\",\"rank\":2,\"previous_rank\":null,\"id\":\"588852e8d2994e3bbfa52d88\"}"
        private const val JSON_PLAYER_2 = "{\"rating\":38.007338332983444,\"name\":\"R2DLiu\",\"rank\":17,\"id\":\"588999c4d2994e713ad637f4\"}"

        private val RANKED_PLAYER_0 = RankedPlayer(
                id = "588852e8d2994e3bbfa52dbd",
                name = "Kevbot",
                rating = 40.54488107578405f,
                rank = 10,
                previousRank = 8
        )

        private val RANKED_PLAYER_1 = RankedPlayer(
                id = "588852e8d2994e3bbfa52d88",
                name = "SFAT",
                rating = 44.516087199067016f,
                rank = 2
        )
    }

    @Before
    override fun setUp() {
        super.setUp()

        rankedPlayerAdapter = moshi.adapter(RankedPlayer::class.java)
    }

    @Test
    fun testFromJsonWithPlayer0() {
        val player = rankedPlayerAdapter.fromJson(JSON_PLAYER_0)
        assertNotNull(player)
        assertNotNull(player?.previousRank)
        assertNotEquals(Int.MIN_VALUE, player?.previousRank)
    }

    @Test
    fun testFromJsonWithPlayer1() {
        val player = rankedPlayerAdapter.fromJson(JSON_PLAYER_1)
        assertNotNull(player)
        assertEquals(Int.MIN_VALUE, player?.previousRank)
    }

    @Test
    fun testFromJsonWithPlayer2() {
        val player = rankedPlayerAdapter.fromJson(JSON_PLAYER_2)
        assertNotNull(player)
        assertNull(player?.previousRank)
    }

    @Test
    fun testToJsonWithNull() {
        val json = rankedPlayerAdapter.toJson(null)
        assertTrue(json.isNullOrEmpty())
    }

    @Test
    fun testToJsonWithPlayer0() {
        val json = rankedPlayerAdapter.toJson(RANKED_PLAYER_0)
        assertFalse(json.isNullOrBlank())

        val player = rankedPlayerAdapter.fromJson(json)
        assertEquals(RANKED_PLAYER_0, player)
    }

    @Test
    fun testToJsonWithPlayer1() {
        val json = rankedPlayerAdapter.toJson(RANKED_PLAYER_1)
        assertFalse(json.isNullOrBlank())

        val player = rankedPlayerAdapter.fromJson(json)
        assertEquals(RANKED_PLAYER_1, player)
    }

}
