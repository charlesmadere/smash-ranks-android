package com.garpr.android.data.models

import com.garpr.android.test.BaseTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import java.util.Calendar
import java.util.Collections

class MatchTest : BaseTest() {

    companion object {
        private val MATCH_0 = TournamentMatch(
                result = MatchResult.WIN,
                opponent = LitePlayer(
                        id = "588852e8d2994e3bbfa52dca",
                        name = "Mao"
                ),
                tournament = LiteTournament(
                        date = SimpleDate(
                                with(Calendar.getInstance()) {
                                    clear()
                                    set(Calendar.YEAR, 2017)
                                    set(Calendar.MONTH, Calendar.JANUARY)
                                    set(Calendar.DAY_OF_MONTH, 14)
                                    time
                                }
                        ),
                        id = "588850d5d2994e3bbfa52d67",
                        name = "Norcal Validated 1"
                )
        )

        private val MATCH_1 = TournamentMatch(
                result = MatchResult.EXCLUDED,
                opponent = LitePlayer(
                        id = "5877eb55d2994e15c7dea981",
                        name = "Arcadia"
                ),
                tournament = LiteTournament(
                        date = SimpleDate(
                                with(Calendar.getInstance()) {
                                    clear()
                                    set(Calendar.YEAR, 2017)
                                    set(Calendar.MONTH, Calendar.MARCH)
                                    set(Calendar.DAY_OF_MONTH, 7)
                                    time
                                }
                        ),
                        id = "58bfaed4d2994e057e91f71b",
                        name = "Get Smashed #108"
                )
        )

        private val MATCH_2 = TournamentMatch(
                result = MatchResult.EXCLUDED,
                opponent = LitePlayer(
                        id = "587a951dd2994e15c7deaa00",
                        name = "Darrell"
                ),
                tournament = LiteTournament(
                        date = SimpleDate(
                                with(Calendar.getInstance()) {
                                    clear()
                                    set(Calendar.YEAR, 2017)
                                    set(Calendar.MONTH, Calendar.FEBRUARY)
                                    set(Calendar.DAY_OF_MONTH, 18)
                                    time
                                }
                        ),
                        id = "58a9139cd2994e756952ad94",
                        name = "The Gator Games #3"
                )
        )
    }

    @Test
    fun testChronologicalOrder() {
        val list = listOf(MATCH_0, MATCH_1, MATCH_2)
        Collections.sort(list, TournamentMatch.CHRONOLOGICAL_ORDER)

        assertEquals(MATCH_0, list[0])
        assertEquals(MATCH_2, list[1])
        assertEquals(MATCH_1, list[2])
    }

    @Test
    fun testEquals() {
        assertEquals(MATCH_0, MATCH_0)
        assertEquals(MATCH_1, MATCH_1)
        assertEquals(MATCH_2, MATCH_2)
    }

    @Test
    fun testHashCode() {
        assertEquals(MATCH_0.hashCode(), MATCH_0.hashCode())
        assertEquals(MATCH_1.hashCode(), MATCH_1.hashCode())
        assertEquals(MATCH_2.hashCode(), MATCH_2.hashCode())

        assertNotEquals(MATCH_0.hashCode(), MATCH_1.hashCode())
        assertNotEquals(MATCH_0.hashCode(), MATCH_2.hashCode())
        assertNotEquals(MATCH_1.hashCode(), MATCH_2.hashCode())
    }

    @Test
    fun testReverseChronologicalOrder() {
        val list = listOf(MATCH_0, MATCH_1, MATCH_2)
        Collections.sort(list, TournamentMatch.REVERSE_CHRONOLOGICAL_ORDER)

        assertEquals(MATCH_1, list[0])
        assertEquals(MATCH_2, list[1])
        assertEquals(MATCH_0, list[2])
    }

}
