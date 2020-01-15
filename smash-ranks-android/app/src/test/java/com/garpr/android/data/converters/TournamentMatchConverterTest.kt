package com.garpr.android.data.converters

import com.garpr.android.BaseKoinTest
import com.garpr.android.data.models.LitePlayer
import com.garpr.android.data.models.LiteTournament
import com.garpr.android.data.models.MatchResult
import com.garpr.android.data.models.SimpleDate
import com.garpr.android.data.models.TournamentMatch
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.koin.test.inject

class TournamentMatchConverterTest : BaseKoinTest() {

    protected val moshi: Moshi by inject()

    private lateinit var tournamentMatchAdapter: JsonAdapter<TournamentMatch>

    companion object {
        private const val JSON_MATCH_0 = "{\"opponent_name\":\"Mao\",\"tournament_name\":\"Norcal Validated 1\",\"result\":\"win\",\"opponent_id\":\"588852e8d2994e3bbfa52dca\",\"tournament_id\":\"588850d5d2994e3bbfa52d67\",\"tournament_date\":\"01/14/17\"}"
        private const val JSON_MATCH_1 = "{\"opponent_name\":\"Arcadia\",\"tournament_name\":\"Get Smashed #108\",\"result\":\"excluded\",\"opponent_id\":\"5877eb55d2994e15c7dea981\",\"tournament_id\":\"58bfaed4d2994e057e91f71b\",\"tournament_date\":\"03/07/17\"}"
        private const val JSON_MATCH_2 = "{\"opponent_name\":\"Darrell\",\"tournament_name\":\"The Gator Games #3\",\"result\":\"lose\",\"opponent_id\":\"587a951dd2994e15c7deaa00\",\"tournament_id\":\"58a9139cd2994e756952ad94\",\"tournament_date\":\"02/18/17\"}"

        private val MATCH_0 = TournamentMatch(
                result = MatchResult.WIN,
                opponent = LitePlayer(
                        id = "583a4a15d2994e0577b05c74",
                        name = "homemadewaffles"
                ),
                tournament = LiteTournament(
                        date = SimpleDate(),
                        id = "597d2903d2994e34028b4cc4",
                        name = "GENESIS: RED"
                )
        )

        private val MATCH_1 = TournamentMatch(
                result = MatchResult.LOSE,
                opponent = LitePlayer(
                        id = "5888542ad2994e3bbfa52de4",
                        name = "ycz6"
                ),
                tournament = LiteTournament(
                        date = SimpleDate(),
                        id = "58ad3b1cd2994e756952adba",
                        name = "Get MADE at the Foundry"
                )
        )

        private val MATCH_2 = TournamentMatch(
                result = MatchResult.EXCLUDED,
                opponent = LitePlayer(
                        id = "53c64dba8ab65f6e6651f7bc",
                        name = "Hax"
                ),
                tournament = LiteTournament(
                        date = SimpleDate(),
                        id = "588827bad2994e0d53b14556",
                        name = "The Beat Down Ep.14"
                )
        )
    }

    @Before
    override fun setUp() {
        super.setUp()

        tournamentMatchAdapter = moshi.adapter(TournamentMatch::class.java)
    }

    @Test
    fun testFromJsonWithMatch0() {
        val match = tournamentMatchAdapter.fromJson(JSON_MATCH_0)
        assertNotNull(match)
    }

    @Test
    fun testFromJsonWithMatch1() {
        val match = tournamentMatchAdapter.fromJson(JSON_MATCH_1)
        assertNotNull(match)
    }

    @Test
    fun testFromJsonWithMatch2() {
        val match = tournamentMatchAdapter.fromJson(JSON_MATCH_2)
        assertNotNull(match)
    }

    @Test
    fun testToJsonWithNull() {
        val json = tournamentMatchAdapter.toJson(null)
        assertTrue(json.isNullOrEmpty())
    }

    @Test
    fun testToJsonWithMatch0() {
        val json = tournamentMatchAdapter.toJson(MATCH_0)
        assertFalse(json.isNullOrBlank())

        val match = tournamentMatchAdapter.fromJson(json)
        assertEquals(MATCH_0, match)
    }

    @Test
    fun testToJsonWithMatch1() {
        val json = tournamentMatchAdapter.toJson(MATCH_1)
        assertFalse(json.isNullOrBlank())

        val match = tournamentMatchAdapter.fromJson(json)
        assertEquals(MATCH_1, match)
    }

    @Test
    fun testToJsonWithMatch2() {
        val json = tournamentMatchAdapter.toJson(MATCH_2)
        assertFalse(json.isNullOrBlank())

        val match = tournamentMatchAdapter.fromJson(json)
        assertEquals(MATCH_2, match)
    }

}
