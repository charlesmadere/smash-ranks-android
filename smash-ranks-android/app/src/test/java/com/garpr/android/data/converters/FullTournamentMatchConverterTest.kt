package com.garpr.android.data.converters

import com.garpr.android.BaseKoinTest
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.FullTournament
import com.garpr.android.data.models.LitePlayer
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.koin.test.inject

class FullTournamentMatchConverterTest : BaseKoinTest() {

    protected val moshi: Moshi by inject()

    private lateinit var fullTournamentMatchAdapter: JsonAdapter<FullTournament.Match>

    companion object {
        private val BLARGH: AbsPlayer = LitePlayer(
                id = "58885df9d2994e3d56594112",
                name = "blargh257"
        )

        private val DARK_SILENCE: AbsPlayer = LitePlayer(
                id = "587a951dd2994e15c7dea9eb",
                name = "DarkSilence"
        )

        private val SNAP: AbsPlayer = LitePlayer(
                id = "59213f1ad2994e1d79144956",
                name = "Snap"
        )

        private val UNABLETABLE: AbsPlayer = LitePlayer(
                id = "58b132d5d2994e7265472773",
                name = "theunabletable"
        )

        private val BLARGH_VS_UNABLETABLE = FullTournament.Match(
                loser = BLARGH,
                winner = UNABLETABLE,
                excluded = true,
                matchId = 0
        )

        private val DARK_SILENCE_VS_SNAP = FullTournament.Match(
                loser = DARK_SILENCE,
                winner = SNAP,
                excluded = false,
                matchId = 1
        )

        private val BLARGH_VS_UNABLETABLE_JSON = "{\"excluded\":${BLARGH_VS_UNABLETABLE.excluded},\"loser_id\":\"${BLARGH.id}\",\"loser_name\":\"${BLARGH.name}\",\"match_id\":${BLARGH_VS_UNABLETABLE.matchId},\"winner_id\":\"${UNABLETABLE.id}\",\"winner_name\":\"${UNABLETABLE.name}\"}"
        private val DARK_SILENCE_VS_SNAP_JSON = "{\"excluded\":${DARK_SILENCE_VS_SNAP.excluded},\"loser_id\":\"${DARK_SILENCE.id}\",\"loser_name\":\"${DARK_SILENCE.name}\",\"match_id\":${DARK_SILENCE_VS_SNAP.matchId},\"winner_id\":\"${SNAP.id}\",\"winner_name\":\"${SNAP.name}\"}"
    }

    @Before
    override fun setUp() {
        super.setUp()

        fullTournamentMatchAdapter = moshi.adapter(FullTournament.Match::class.java)
    }

    @Test
    fun testFromJsonWithBlarghVsUnabletable() {
        val match = fullTournamentMatchAdapter.fromJson(BLARGH_VS_UNABLETABLE_JSON)
        assertNotNull(match)
        assertEquals(BLARGH, match?.loser)
        assertEquals(UNABLETABLE, match?.winner)
        assertEquals(true, match?.excluded)
        assertEquals(BLARGH_VS_UNABLETABLE.matchId, match?.matchId)
    }

    @Test
    fun testFromJsonWithDarkSilenceVsSnap() {
        val match = fullTournamentMatchAdapter.fromJson(DARK_SILENCE_VS_SNAP_JSON)
        assertNotNull(match)
        assertEquals(DARK_SILENCE, match?.loser)
        assertEquals(SNAP, match?.winner)
        assertEquals(false, match?.excluded)
        assertEquals(DARK_SILENCE_VS_SNAP.matchId, match?.matchId)
    }

    @Test
    fun testToJsonWithBlarghVsUnabletable() {
        val json = fullTournamentMatchAdapter.toJson(BLARGH_VS_UNABLETABLE)
        assertFalse(json.isNullOrBlank())

        val match = fullTournamentMatchAdapter.fromJson(json)
        assertEquals(BLARGH_VS_UNABLETABLE, match)
    }

    @Test
    fun testToJsonWithDarkSilenceVsSnap() {
        val json = fullTournamentMatchAdapter.toJson(DARK_SILENCE_VS_SNAP)
        assertFalse(json.isNullOrBlank())

        val match = fullTournamentMatchAdapter.fromJson(json)
        assertEquals(DARK_SILENCE_VS_SNAP, match)
    }

    @Test
    fun testToJsonWithNull() {
        val json = fullTournamentMatchAdapter.toJson(null)
        assertTrue(json.isNullOrEmpty())
    }

}
