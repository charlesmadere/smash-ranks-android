package com.garpr.android.managers

import com.garpr.android.BaseTest
import com.garpr.android.models.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class TournamentAdapterManagerTest : BaseTest() {

    @Inject
    protected lateinit var tournamentAdapterManager: TournamentAdapterManager


    companion object {
        private const val EMPTY = ""
        private const val HELLO_WORLD = "Hello, World!"
        private const val WHITESPACE = " "

        private val PLAYER_1: AbsPlayer = LitePlayer("1", "Imyt")
        private val PLAYER_2: AbsPlayer = FullPlayer("2", "Charlezard", null,
                null, null)
        private val PLAYER_3: AbsPlayer = RankedPlayer("3", "gaR", 1f, 1,
                null)

        private val MATCH_1 = FullTournament.Match(true, PLAYER_1.id, PLAYER_1.name,
                "1", PLAYER_3.id, PLAYER_3.name)
        private val MATCH_2 = FullTournament.Match(false, PLAYER_2.id, PLAYER_2.name,
                "2", PLAYER_1.id, PLAYER_1.name)

        private val FULL_TOURNAMENT_1 = FullTournament(null, SimpleDate(), "1",
                "Melee @ the Made")
        private val FULL_TOURNAMENT_2 = FullTournament(null, SimpleDate(), "2",
                "Norcal Arcadian #5", players = listOf(PLAYER_1, PLAYER_2, PLAYER_3))
        private val FULL_TOURNAMENT_3 = FullTournament(null, SimpleDate(), "3",
                "Genesis 3", matches = listOf(MATCH_1, MATCH_2))
        private val FULL_TOURNAMENT_4 = FullTournament(null, SimpleDate(), "4",
                "EVO 2017", players = FULL_TOURNAMENT_2.players,
                matches = FULL_TOURNAMENT_3.matches)
    }

    @Before
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)
    }

    @Test
    @Throws(Exception::class)
    fun testBuildMatchesListForFullTournament1() {
        val list = tournamentAdapterManager.buildMatchesList(FULL_TOURNAMENT_1)
        assertEquals(2, list.size)

        assertTrue(list[0] is FullTournament)
        assertTrue(list[1] is CharSequence)
    }

    @Test
    @Throws(Exception::class)
    fun testBuildMatchesListForFullTournament2() {
        val list = tournamentAdapterManager.buildMatchesList(FULL_TOURNAMENT_2)
        assertEquals(2, list.size)

        assertTrue(list[0] is FullTournament)
        assertTrue(list[1] is CharSequence)
    }

    @Test
    @Throws(Exception::class)
    fun testBuildMatchesListForFullTournament3() {
        val list = tournamentAdapterManager.buildMatchesList(FULL_TOURNAMENT_3)
        assertEquals(3, list.size)

        assertTrue(list[0] is FullTournament)
        assertTrue(list[1] is FullTournament.Match)
        assertTrue(list[2] is FullTournament.Match)
    }

    @Test
    @Throws(Exception::class)
    fun testBuildMatchesListForFullTournament4() {
        val list = tournamentAdapterManager.buildMatchesList(FULL_TOURNAMENT_4)
        assertEquals(3, list.size)

        assertTrue(list[0] is FullTournament)
        assertTrue(list[1] is FullTournament.Match)
        assertTrue(list[2] is FullTournament.Match)
    }

    @Test
    @Throws(Exception::class)
    fun testBuildSearchedMatchesList() {
        val list = tournamentAdapterManager.buildSearchedMatchesList(FULL_TOURNAMENT_1,
                listOf(MATCH_1, MATCH_2))
        assertEquals(3, list.size)

        assertTrue(list[0] is FullTournament)
        assertTrue(list[1] is FullTournament.Match)
        assertTrue(list[2] is FullTournament.Match)
    }

    @Test
    @Throws(Exception::class)
    fun testBuildSearchedMatchesListForEmptyMatches() {
        val list = tournamentAdapterManager.buildSearchedMatchesList(FULL_TOURNAMENT_2, listOf())
        assertEquals(1, list.size)

        assertTrue(list[0] is FullTournament)
    }

    @Test
    @Throws(Exception::class)
    fun testBuildSearchedMatchesListForNullMatches() {
        val list = tournamentAdapterManager.buildSearchedMatchesList(FULL_TOURNAMENT_3, null)
        assertEquals(1, list.size)

        assertTrue(list[0] is FullTournament)
    }

    @Test
    @Throws(Exception::class)
    fun testBuildPlayersListForFullTournament1() {
        val list = tournamentAdapterManager.buildPlayersList(FULL_TOURNAMENT_1)
        assertEquals(2, list.size)

        assertTrue(list[0] is FullTournament)
        assertTrue(list[1] is CharSequence)
    }

    @Test
    @Throws(Exception::class)
    fun testBuildPlayersListForFullTournament2() {
        val list = tournamentAdapterManager.buildPlayersList(FULL_TOURNAMENT_2)
        assertEquals(4, list.size)

        assertTrue(list[0] is FullTournament)
        assertTrue(list[1] is AbsPlayer)
        assertTrue(list[2] is AbsPlayer)
        assertTrue(list[3] is AbsPlayer)
    }

    @Test
    @Throws(Exception::class)
    fun testBuildPlayersListForFullTournament3() {
        val list = tournamentAdapterManager.buildPlayersList(FULL_TOURNAMENT_3)
        assertEquals(2, list.size)

        assertTrue(list[0] is FullTournament)
        assertTrue(list[1] is CharSequence)
    }

    @Test
    @Throws(Exception::class)
    fun testBuildPlayersListForFullTournament4() {
        val list = tournamentAdapterManager.buildPlayersList(FULL_TOURNAMENT_4)
        assertEquals(4, list.size)

        assertTrue(list[0] is FullTournament)
        assertTrue(list[1] is AbsPlayer)
        assertTrue(list[2] is AbsPlayer)
        assertTrue(list[3] is AbsPlayer)
    }

    @Test
    @Throws(Exception::class)
    fun testBuildSearchedPlayersList() {
        val list = tournamentAdapterManager.buildSearchedPlayersList(FULL_TOURNAMENT_1,
                listOf(PLAYER_1, PLAYER_2, PLAYER_3))
        assertEquals(4, list.size)

        assertTrue(list[0] is FullTournament)
        assertTrue(list[1] is AbsPlayer)
        assertTrue(list[2] is AbsPlayer)
        assertTrue(list[3] is AbsPlayer)
    }

    @Test
    @Throws(Exception::class)
    fun testBuildSearchedPlayersListForEmptyPlayers() {
        val list = tournamentAdapterManager.buildSearchedPlayersList(FULL_TOURNAMENT_1, listOf())
        assertEquals(1, list.size)

        assertTrue(list[0] is FullTournament)
    }

    @Test
    @Throws(Exception::class)
    fun testBuildSearchedPlayersListForNullPlayers() {
        val list = tournamentAdapterManager.buildSearchedPlayersList(FULL_TOURNAMENT_1, null)
        assertEquals(1, list.size)

        assertTrue(list[0] is FullTournament)
    }

    @Test
    @Throws(Exception::class)
    fun testGetItemIdForFullTournaments() {
        assertEquals(Long.MIN_VALUE,
                tournamentAdapterManager.getItemId(0, FULL_TOURNAMENT_1))
        assertEquals(Long.MIN_VALUE,
                tournamentAdapterManager.getItemId(0, FULL_TOURNAMENT_2))
        assertEquals(Long.MIN_VALUE,
                tournamentAdapterManager.getItemId(0, FULL_TOURNAMENT_3))
        assertEquals(Long.MIN_VALUE,
                tournamentAdapterManager.getItemId(0, FULL_TOURNAMENT_4))
    }

    @Test
    @Throws(Exception::class)
    fun testGetItemIdForMatches() {
        assertEquals(MATCH_1.hashCode().toLong(),
                tournamentAdapterManager.getItemId(3, MATCH_1))
        assertEquals(MATCH_2.hashCode().toLong(),
                tournamentAdapterManager.getItemId(4, MATCH_2))
    }

    @Test
    @Throws(Exception::class)
    fun testGetItemIdForPlayers() {
        assertEquals(PLAYER_1.hashCode().toLong(),
                tournamentAdapterManager.getItemId(3, PLAYER_1))
        assertEquals(PLAYER_2.hashCode().toLong(),
                tournamentAdapterManager.getItemId(4, PLAYER_2))
        assertEquals(PLAYER_3.hashCode().toLong(),
                tournamentAdapterManager.getItemId(5, PLAYER_3))
    }

    @Test
    @Throws(Exception::class)
    fun testGetItemIdForStrings() {
        assertEquals(EMPTY.hashCode().toLong(),
                tournamentAdapterManager.getItemId(2, EMPTY))
        assertEquals(HELLO_WORLD.hashCode().toLong(),
                tournamentAdapterManager.getItemId(2, HELLO_WORLD))
        assertEquals(WHITESPACE.hashCode().toLong(),
                tournamentAdapterManager.getItemId(2, WHITESPACE))
    }

    @Test
    @Throws(Exception::class)
    fun testGetItemViewTypeForFullTournament() {
        assertEquals(TournamentAdapterManager.ViewType.TOURNAMENT_INFO,
                tournamentAdapterManager.getItemViewType(0, FULL_TOURNAMENT_1))
        assertEquals(TournamentAdapterManager.ViewType.TOURNAMENT_INFO,
                tournamentAdapterManager.getItemViewType(0, FULL_TOURNAMENT_2))
        assertEquals(TournamentAdapterManager.ViewType.TOURNAMENT_INFO,
                tournamentAdapterManager.getItemViewType(0, FULL_TOURNAMENT_3))
        assertEquals(TournamentAdapterManager.ViewType.TOURNAMENT_INFO,
                tournamentAdapterManager.getItemViewType(0, FULL_TOURNAMENT_4))
    }

    @Test
    @Throws(Exception::class)
    fun testGetItemViewTypeForMatches() {
        assertEquals(TournamentAdapterManager.ViewType.MATCH,
                tournamentAdapterManager.getItemViewType(3, MATCH_1))
        assertEquals(TournamentAdapterManager.ViewType.MATCH,
                tournamentAdapterManager.getItemViewType(4, MATCH_2))
    }

    @Test
    @Throws(Exception::class)
    fun testGetItemViewTypeForPlayers() {
        assertEquals(TournamentAdapterManager.ViewType.PLAYER,
                tournamentAdapterManager.getItemViewType(3, PLAYER_1))
        assertEquals(TournamentAdapterManager.ViewType.PLAYER,
                tournamentAdapterManager.getItemViewType(4, PLAYER_2))
        assertEquals(TournamentAdapterManager.ViewType.PLAYER,
                tournamentAdapterManager.getItemViewType(5, PLAYER_3))
    }

    @Test
    @Throws(Exception::class)
    fun testGetItemViewTypeForString() {
        assertEquals(TournamentAdapterManager.ViewType.MESSAGE,
                tournamentAdapterManager.getItemViewType(2, EMPTY))
        assertEquals(TournamentAdapterManager.ViewType.MESSAGE,
                tournamentAdapterManager.getItemViewType(2, HELLO_WORLD))
        assertEquals(TournamentAdapterManager.ViewType.MESSAGE,
                tournamentAdapterManager.getItemViewType(2, WHITESPACE))
    }

}
