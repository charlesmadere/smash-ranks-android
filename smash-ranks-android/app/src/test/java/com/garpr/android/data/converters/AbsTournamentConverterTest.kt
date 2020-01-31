package com.garpr.android.data.converters

import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.AbsRegion
import com.garpr.android.data.models.AbsTournament
import com.garpr.android.data.models.FullTournament
import com.garpr.android.data.models.LitePlayer
import com.garpr.android.data.models.LiteRegion
import com.garpr.android.data.models.LiteTournament
import com.garpr.android.data.models.SimpleDate
import com.garpr.android.test.BaseTest
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.koin.test.inject
import java.util.Calendar

class AbsTournamentConverterTest : BaseTest() {

    protected val moshi: Moshi by inject()

    private lateinit var tournamentAdapter: JsonAdapter<AbsTournament>

    companion object {
        private val CHARLEZARD: AbsPlayer = LitePlayer(
                id = "587a951dd2994e15c7dea9fe",
                name = "Charlezard"
        )

        private val DARREL: AbsPlayer = LitePlayer(
                id = "587a951dd2994e15c7deaa00",
                name = "Darrel"
        )

        private val HMW: AbsPlayer = LitePlayer(
                id = "583a4a15d2994e0577b05c74",
                name = "homemadewaffles"
        )

        private val SPARK: AbsPlayer = LitePlayer(
                id = "5877eb55d2994e15c7dea97e",
                name = "Spark"
        )

        private val NORCAL: AbsRegion = LiteRegion(
                id = "norcal",
                displayName = "Norcal"
        )

        private val FOUR_STOCK_FRIDAY = FullTournament(
                date = SimpleDate(with(Calendar.getInstance()) {
                    clear()
                    set(Calendar.YEAR, 2018)
                    set(Calendar.MONTH, Calendar.JANUARY)
                    set(Calendar.DAY_OF_YEAR, 21)
                    time
                }),
                id = "5c1e7834d2994e505643f25c",
                name = "Four Stock Friday #128",
                regions = listOf(NORCAL.id),
                players = listOf(CHARLEZARD, DARREL, HMW, SPARK),
                url = "https://sjsumelee.challonge.com/FSF128Singles"
        )

        private val PHOENIX_UNDERGROUND = LiteTournament(
                date = SimpleDate(with(Calendar.getInstance()) {
                    clear()
                    set(Calendar.YEAR, 2019)
                    set(Calendar.MONTH, Calendar.JANUARY)
                    set(Calendar.DAY_OF_MONTH, 9)
                    time
                }),
                id = "5c384537d2994e505643f28d",
                name = "Phoenix Underground #82"
        )

        private val FOUR_STOCK_FRIDAY_JSON = "{\"date\":\"01/21/18\",\"id\":\"${FOUR_STOCK_FRIDAY.id}\",\"name\":\"${FOUR_STOCK_FRIDAY.name}\",\"regions\":[\"${NORCAL.id}\"],\"players\":[{\"id\":\"${HMW.id}\",\"name\":\"${HMW.name}\"},{\"id\":\"${SPARK.id}\",\"name\":\"${SPARK.name}\"},{\"id\":\"${DARREL.id}\",\"name\":\"${DARREL.name}\"},{\"id\":\"${CHARLEZARD.id}\",\"name\":\"${CHARLEZARD.name}\"}],\"url\":\"${FOUR_STOCK_FRIDAY.url}\",\"type\":\"challonge\"}"
        private val PHOENIX_UNDERGROUND_JSON = "{\"date\":\"01/09/19\",\"id\":\"${PHOENIX_UNDERGROUND.id}\",\"name\":\"${PHOENIX_UNDERGROUND.name}\",\"regions\":[\"${NORCAL.id}\"]}"
    }

    @Before
    override fun setUp() {
        super.setUp()

        tournamentAdapter = moshi.adapter(AbsTournament::class.java)
    }

    @Test
    fun testFromJsonWithFourStockFridayJson() {
        val tournament = tournamentAdapter.fromJson(FOUR_STOCK_FRIDAY_JSON)
        assertTrue(tournament is FullTournament)
        assertEquals(AbsTournament.Kind.FULL, tournament?.kind)
    }

    @Test
    fun testFromJsonWithPhoenixUndergroundJson() {
        val tournament = tournamentAdapter.fromJson(PHOENIX_UNDERGROUND_JSON)
        assertTrue(tournament is LiteTournament)
        assertEquals(AbsTournament.Kind.LITE, tournament?.kind)
    }

    @Test
    fun testToJsonWithFourStockFriday() {
        val json = tournamentAdapter.toJson(FOUR_STOCK_FRIDAY)
        assertFalse(json.isNullOrBlank())

        val tournament = tournamentAdapter.fromJson(json)
        assertEquals(FOUR_STOCK_FRIDAY, tournament)
    }

    @Test
    fun testToJsonWithPhoenixUnderground() {
        val json = tournamentAdapter.toJson(PHOENIX_UNDERGROUND)
        assertFalse(json.isNullOrBlank())

        val tournament = tournamentAdapter.fromJson(json)
        assertEquals(PHOENIX_UNDERGROUND, tournament)
    }

    @Test
    fun testToJsonWithNull() {
        val json = tournamentAdapter.toJson(null)
        assertTrue(json.isNullOrEmpty())
    }

}
