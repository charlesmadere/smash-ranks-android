package com.garpr.android.data.models

import com.garpr.android.test.BaseTest
import org.junit.Assert.assertEquals
import org.junit.Test

class WinsLossesTest : BaseTest() {

    companion object {
        private val PLAYER_0: AbsPlayer = LitePlayer(
                id = "1234",
                name = "Imyt"
        )

        private val PLAYER_1: AbsPlayer = LitePlayer(
                id = "5678",
                name = "gaR"
        )

        private const val DELTA: Float = 0.001f
    }

    @Test
    fun testWinLossPercentages() {
        var percentages = WinsLosses(PLAYER_0, 1, PLAYER_1, 2)
                .winLossPercentages
        assertEquals(1f / 3f, percentages[0], DELTA)
        assertEquals(2f / 3f, percentages[1], DELTA)

        percentages = WinsLosses(PLAYER_1, 3, PLAYER_0, 5)
                .winLossPercentages
        assertEquals(3f / 8f, percentages[0], DELTA)
        assertEquals(5f / 8f, percentages[1], DELTA)

        percentages = WinsLosses(PLAYER_1, 10, PLAYER_0, 1)
                .winLossPercentages
        assertEquals(10f / 11f, percentages[0], DELTA)
        assertEquals(1f / 11f, percentages[1], DELTA)

        percentages = WinsLosses(PLAYER_1, 16, PLAYER_0, 16)
                .winLossPercentages
        assertEquals(16f / 32f, percentages[0], DELTA)
        assertEquals(16f / 32f, percentages[1], DELTA)

        percentages = WinsLosses(PLAYER_1, 1, PLAYER_0, 1)
                .winLossPercentages
        assertEquals(1f / 2f, percentages[0], DELTA)
        assertEquals(1f / 2f, percentages[1], DELTA)
    }

    @Test
    fun testWinLossPercentagesEqualPlayerAndOpponentWins() {
        var percentages = WinsLosses(PLAYER_1, 16, PLAYER_0, 16)
                .winLossPercentages
        assertEquals(0.5f, percentages[0])
        assertEquals(0.5f, percentages[1])

        percentages = WinsLosses(PLAYER_1, 1, PLAYER_0, 1)
                .winLossPercentages
        assertEquals(0.5f, percentages[0])
        assertEquals(0.5f, percentages[1])

        percentages = WinsLosses(PLAYER_1, 3, PLAYER_0, 3)
                .winLossPercentages
        assertEquals(0.5f, percentages[0])
        assertEquals(0.5f, percentages[1])
    }

    @Test
    fun testWinLossPercentagesNoOpponentWins() {
        var percentages = WinsLosses(PLAYER_0, 36, PLAYER_1, 0)
                .winLossPercentages
        assertEquals(1f, percentages[0])
        assertEquals(0f, percentages[1])

        percentages = WinsLosses(PLAYER_1, 3, PLAYER_0, 0)
                .winLossPercentages
        assertEquals(1f, percentages[0])
        assertEquals(0f, percentages[1])
    }

    @Test
    fun testWinLossPercentagesNoPlayerWins() {
        var percentages = WinsLosses(PLAYER_0, 0, PLAYER_1, 21)
                .winLossPercentages
        assertEquals(0f, percentages[0])
        assertEquals(1f, percentages[1])

        percentages = WinsLosses(PLAYER_1, 0, PLAYER_0, 9)
                .winLossPercentages
        assertEquals(0f, percentages[0])
        assertEquals(1f, percentages[1])
    }

    @Test
    fun testWinLossPercentagesNoPlayerWinsNoOpponentWins() {
        var percentages = WinsLosses(PLAYER_0, 0, PLAYER_1, 0)
                .winLossPercentages
        assertEquals(0f, percentages[0])
        assertEquals(0f, percentages[1])

        percentages = WinsLosses(PLAYER_1, 0, PLAYER_0, 0)
                .winLossPercentages
        assertEquals(0f, percentages[0])
        assertEquals(0f, percentages[1])
    }

}
