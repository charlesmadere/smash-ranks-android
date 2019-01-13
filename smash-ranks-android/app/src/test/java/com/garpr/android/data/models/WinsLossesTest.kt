package com.garpr.android.data.models

import com.garpr.android.BaseTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class WinsLossesTest : BaseTest() {

    private val player0: AbsPlayer = LitePlayer("1234", "Imyt")
    private val player1: AbsPlayer = LitePlayer("5678", "gaR")


    companion object {
        private const val DELTA: Float = 0.001f
    }

    @Test
    fun testWinLossPercentages() {
        var percentages = WinsLosses(player0, 1, player1, 2)
                .winLossPercentages
        assertEquals(1f / 3f, percentages[0], DELTA)
        assertEquals(2f / 3f, percentages[1], DELTA)

        percentages = WinsLosses(player1, 3, player0, 5)
                .winLossPercentages
        assertEquals(3f / 8f, percentages[0], DELTA)
        assertEquals(5f / 8f, percentages[1], DELTA)

        percentages = WinsLosses(player1, 10, player0, 1)
                .winLossPercentages
        assertEquals(10f / 11f, percentages[0], DELTA)
        assertEquals(1f / 11f, percentages[1], DELTA)

        percentages = WinsLosses(player1, 16, player0, 16)
                .winLossPercentages
        assertEquals(16f / 32f, percentages[0], DELTA)
        assertEquals(16f / 32f, percentages[1], DELTA)

        percentages = WinsLosses(player1, 1, player0, 1)
                .winLossPercentages
        assertEquals(1f / 2f, percentages[0], DELTA)
        assertEquals(1f / 2f, percentages[1], DELTA)
    }

    @Test
    fun testWinLossPercentagesEqualPlayerAndOpponentWins() {
        var percentages = WinsLosses(player1, 16, player0, 16)
                .winLossPercentages
        assertEquals(0.5f, percentages[0])
        assertEquals(0.5f, percentages[1])

        percentages = WinsLosses(player1, 1, player0, 1)
                .winLossPercentages
        assertEquals(0.5f, percentages[0])
        assertEquals(0.5f, percentages[1])

        percentages = WinsLosses(player1, 3, player0, 3)
                .winLossPercentages
        assertEquals(0.5f, percentages[0])
        assertEquals(0.5f, percentages[1])
    }

    @Test
    fun testWinLossPercentagesNoOpponentWins() {
        var percentages = WinsLosses(player0, 36, player1, 0)
                .winLossPercentages
        assertEquals(1f, percentages[0])
        assertEquals(0f, percentages[1])

        percentages = WinsLosses(player1, 3, player0, 0)
                .winLossPercentages
        assertEquals(1f, percentages[0])
        assertEquals(0f, percentages[1])
    }

    @Test
    fun testWinLossPercentagesNoPlayerWins() {
        var percentages = WinsLosses(player0, 0, player1, 21)
                .winLossPercentages
        assertEquals(0f, percentages[0])
        assertEquals(1f, percentages[1])

        percentages = WinsLosses(player1, 0, player0, 9)
                .winLossPercentages
        assertEquals(0f, percentages[0])
        assertEquals(1f, percentages[1])
    }

    @Test
    fun testWinLossPercentagesNoPlayerWinsNoOpponentWins() {
        var percentages = WinsLosses(player0, 0, player1, 0)
                .winLossPercentages
        assertEquals(0f, percentages[0])
        assertEquals(0f, percentages[1])

        percentages = WinsLosses(player1, 0, player0, 0)
                .winLossPercentages
        assertEquals(0f, percentages[0])
        assertEquals(0f, percentages[1])
    }

}
