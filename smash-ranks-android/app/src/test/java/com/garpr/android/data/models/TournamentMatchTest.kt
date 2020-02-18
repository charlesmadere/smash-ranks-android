package com.garpr.android.data.models

import com.garpr.android.test.BaseTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import java.util.Calendar
import java.util.Collections
import java.util.Objects

class TournamentMatchTest : BaseTest() {

    @Test
    fun testChronologicalOrder() {
        var list = listOf(VS_CHARLEZARD, VS_GRANDMA_DAN, VS_DIMSUM)
        Collections.sort(list, TournamentMatch.CHRONOLOGICAL_ORDER)
        assertEquals(VS_CHARLEZARD, list[0])
        assertEquals(VS_GRANDMA_DAN, list[1])
        assertEquals(VS_DIMSUM, list[2])

        list = listOf(VS_DIMSUM, VS_CHARLEZARD, VS_GRANDMA_DAN)
        Collections.sort(list, TournamentMatch.CHRONOLOGICAL_ORDER)
        assertEquals(VS_CHARLEZARD, list[0])
        assertEquals(VS_GRANDMA_DAN, list[1])
        assertEquals(VS_DIMSUM, list[2])
    }

    @Test
    fun testEquals() {
        assertEquals(VS_CHARLEZARD, VS_CHARLEZARD)
        assertEquals(VS_DIMSUM, VS_DIMSUM)
        assertEquals(VS_GRANDMA_DAN, VS_GRANDMA_DAN)

        assertNotEquals(VS_CHARLEZARD, VS_DIMSUM)
        assertNotEquals(VS_DIMSUM, VS_GRANDMA_DAN)
        assertNotEquals(VS_GRANDMA_DAN, VS_CHARLEZARD)
    }

    @Test
    fun testHashCode() {
        assertEquals(Objects.hash(VS_CHARLEZARD.opponent, VS_CHARLEZARD.tournament,
                VS_CHARLEZARD.result), VS_CHARLEZARD.hashCode())
        assertEquals(Objects.hash(VS_DIMSUM.opponent, VS_DIMSUM.tournament,
                VS_DIMSUM.result), VS_DIMSUM.hashCode())
        assertEquals(Objects.hash(VS_GRANDMA_DAN.opponent, VS_GRANDMA_DAN.tournament,
                VS_GRANDMA_DAN.result), VS_GRANDMA_DAN.hashCode())
    }

    @Test
    fun testReverseChronologicalOrder() {
        var list = listOf(VS_CHARLEZARD, VS_GRANDMA_DAN, VS_DIMSUM)
        Collections.sort(list, TournamentMatch.REVERSE_CHRONOLOGICAL_ORDER)
        assertEquals(VS_DIMSUM, list[0])
        assertEquals(VS_GRANDMA_DAN, list[1])
        assertEquals(VS_CHARLEZARD, list[2])

        list = listOf(VS_DIMSUM, VS_CHARLEZARD, VS_GRANDMA_DAN)
        Collections.sort(list, TournamentMatch.REVERSE_CHRONOLOGICAL_ORDER)
        assertEquals(VS_DIMSUM, list[0])
        assertEquals(VS_GRANDMA_DAN, list[1])
        assertEquals(VS_CHARLEZARD, list[2])
    }

    companion object {
        private val CHARLEZARD: AbsPlayer = LitePlayer(
                id = "587a951dd2994e15c7dea9fe",
                name = "Charlezard"
        )

        private val DIMSUM: AbsPlayer = LitePlayer(
                id = "588999c5d2994e713ad63b35",
                name = "dimsum"
        )

        private val GRANDMA_DAN: AbsPlayer = LitePlayer(
                id = "588999c5d2994e713ad63a05",
                name = "grandma Dan"
        )

        private val FOUR_STOCK_FRIDAY_122: AbsTournament = LiteTournament(
                date = with(Calendar.getInstance()) {
                    clear()
                    set(Calendar.YEAR, 2018)
                    set(Calendar.MONTH, Calendar.OCTOBER)
                    set(Calendar.DAY_OF_MONTH, 26)
                    SimpleDate(time)
                },
                id = "5bda91acd2994e14bfeb9f14",
                name = "Four Stock Friday #122"
        )

        private val MELEE_AT_THE_MADE_124: AbsTournament = LiteTournament(
                date = with(Calendar.getInstance()) {
                    clear()
                    set(Calendar.YEAR, 2020)
                    set(Calendar.MONTH, Calendar.JANUARY)
                    set(Calendar.DAY_OF_MONTH, 10)
                    SimpleDate(time)
                },
                id = "5e1b94c0d2994e2e36ea0610",
                name = "Melee @ the Made #124"
        )

        private val NORCAL_ARCADIAN_6: AbsTournament = LiteTournament(
                date = with(Calendar.getInstance()) {
                    clear()
                    set(Calendar.YEAR, 2019)
                    set(Calendar.MONTH, Calendar.MAY)
                    set(Calendar.DAY_OF_MONTH, 11)
                    SimpleDate(time)
                },
                id = "5cdb30ebd2994e02dc3aae8e",
                name = "NorCal Arcadian #6"
        )

        private val VS_CHARLEZARD = TournamentMatch(
                opponent = CHARLEZARD,
                tournament = FOUR_STOCK_FRIDAY_122,
                result = MatchResult.WIN
        )

        private val VS_DIMSUM = TournamentMatch(
                opponent = DIMSUM,
                tournament = MELEE_AT_THE_MADE_124,
                result = MatchResult.WIN
        )

        private val VS_GRANDMA_DAN = TournamentMatch(
                opponent = GRANDMA_DAN,
                tournament = NORCAL_ARCADIAN_6,
                result = MatchResult.LOSE
        )
    }

}
