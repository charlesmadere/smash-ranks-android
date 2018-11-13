package com.garpr.android.managers

import com.garpr.android.BaseTest
import com.garpr.android.models.AbsPlayer
import com.garpr.android.models.FavoritePlayer
import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class FavoritePlayersManagerTest : BaseTest() {

    private lateinit var player1: AbsPlayer
    private lateinit var player2: AbsPlayer

    @Inject
    protected lateinit var favoritePlayersManager: FavoritePlayersManager

    @Inject
    protected lateinit var gson: Gson

    @Inject
    protected lateinit var regionManager: RegionManager


    companion object {
        private const val JSON_PLAYER_1 = "{\"id\":\"583a4a15d2994e0577b05c74\",\"name\":\"homemadewaffles\"}"
        private const val JSON_PLAYER_2 = "{\"id\":\"5877eb55d2994e15c7dea97e\",\"name\":\"Spark\"}"
    }

    @Before
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)

        player1 = gson.fromJson(JSON_PLAYER_1, AbsPlayer::class.java)
        player2 = gson.fromJson(JSON_PLAYER_2, AbsPlayer::class.java)
    }

    @Test
    @Throws(Exception::class)
    fun testAbsPlayers() {
        var absPlayers = favoritePlayersManager.absPlayers
        assertTrue(absPlayers.isNullOrEmpty())

        favoritePlayersManager.addPlayer(player2, regionManager.getRegion())
        favoritePlayersManager.addPlayer(player1, regionManager.getRegion())

        absPlayers = favoritePlayersManager.absPlayers
        assertNotNull(absPlayers)
        assertEquals(2, absPlayers?.size)

        assertEquals(player1, absPlayers?.get(0))
        assertEquals(player2, absPlayers?.get(1))
    }

    @Test
    @Throws(Exception::class)
    fun testAddListener() {
        var players: List<FavoritePlayer>? = null

        val listener = object : FavoritePlayersManager.OnFavoritePlayersChangeListener {
            override fun onFavoritePlayersChange(favoritePlayersManager: FavoritePlayersManager) {
                players = favoritePlayersManager.players
            }
        }

        favoritePlayersManager.addListener(listener)
        assertNull(players)

        favoritePlayersManager.addPlayer(player1, regionManager.getRegion())
        assertNotNull(players)
        assertEquals(1, players?.size)
        assertEquals(player1, players?.get(0))

        favoritePlayersManager.removePlayer(player1)
        assertTrue(players.isNullOrEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun testAddListenerTwice() {
        var count = 0

        val listener = object : FavoritePlayersManager.OnFavoritePlayersChangeListener {
            override fun onFavoritePlayersChange(favoritePlayersManager: FavoritePlayersManager) {
                ++count
            }
        }

        favoritePlayersManager.addListener(listener)
        favoritePlayersManager.addListener(listener)
        favoritePlayersManager.addPlayer(player1, regionManager.getRegion())
        assertEquals(1, count)
    }

    @Test
    @Throws(Exception::class)
    fun testAddPlayer() {
        favoritePlayersManager.addPlayer(player1, regionManager.getRegion())

        val players = favoritePlayersManager.players
        assertNotNull(players)
        assertEquals(1, players?.size)
        assertEquals(player1, players?.get(0))
    }

    @Test
    @Throws(Exception::class)
    fun testAddPlayers() {
        favoritePlayersManager.addPlayer(player1, regionManager.getRegion())
        favoritePlayersManager.addPlayer(player2, regionManager.getRegion())

        val players = favoritePlayersManager.players
        assertNotNull(players)
        assertEquals(2, players?.size)
        assertEquals(player1, players?.get(0))
        assertEquals(player2, players?.get(1))
    }

    @Test
    @Throws(Exception::class)
    fun testClear() {
        favoritePlayersManager.clear()
        assertTrue(favoritePlayersManager.isEmpty)

        favoritePlayersManager.addPlayer(player1, regionManager.getRegion())
        assertFalse(favoritePlayersManager.isEmpty)

        favoritePlayersManager.clear()
        assertTrue(favoritePlayersManager.isEmpty)

        favoritePlayersManager.addPlayer(player2, regionManager.getRegion())
        favoritePlayersManager.addPlayer(player1, regionManager.getRegion())
        favoritePlayersManager.clear()
        assertTrue(favoritePlayersManager.isEmpty)

    }

    @Test
    @Throws(Exception::class)
    fun testContains() {
        assertFalse(player1 in favoritePlayersManager)
        assertFalse(player1.id in favoritePlayersManager)
        assertFalse(player2 in favoritePlayersManager)
        assertFalse(player2.id in favoritePlayersManager)

        favoritePlayersManager.addPlayer(player1, regionManager.getRegion())
        assertTrue(player1 in favoritePlayersManager)
        assertTrue(player1.id in favoritePlayersManager)
        assertFalse(player2 in favoritePlayersManager)
        assertFalse(player2.id in favoritePlayersManager)

        favoritePlayersManager.removePlayer(player2)
        assertTrue(player1 in favoritePlayersManager)
        assertTrue(player1.id in favoritePlayersManager)
        assertFalse(player2 in favoritePlayersManager)
        assertFalse(player2.id in favoritePlayersManager)

        favoritePlayersManager.removePlayer(player1)
        assertFalse(player1 in favoritePlayersManager)
        assertFalse(player1.id in favoritePlayersManager)
        assertFalse(player2 in favoritePlayersManager)
        assertFalse(player2.id in favoritePlayersManager)

        favoritePlayersManager.addPlayer(player2, regionManager.getRegion())
        assertFalse(player1 in favoritePlayersManager)
        assertFalse(player1.id in favoritePlayersManager)
        assertTrue(player2 in favoritePlayersManager)
        assertTrue(player2.id in favoritePlayersManager)

        favoritePlayersManager.addPlayer(player1, regionManager.getRegion())
        assertTrue(player1 in favoritePlayersManager)
        assertTrue(player1.id in favoritePlayersManager)
        assertTrue(player2 in favoritePlayersManager)
        assertTrue(player2.id in favoritePlayersManager)

        favoritePlayersManager.clear()
        assertFalse(player1 in favoritePlayersManager)
        assertFalse(player1.id in favoritePlayersManager)
        assertFalse(player2 in favoritePlayersManager)
        assertFalse(player2.id in favoritePlayersManager)
    }

    @Test
    @Throws(Exception::class)
    fun testIsEmpty() {
        assertTrue(favoritePlayersManager.isEmpty)

        favoritePlayersManager.addPlayer(player1, regionManager.getRegion())
        assertFalse(favoritePlayersManager.isEmpty)

        favoritePlayersManager.removePlayer(player2)
        assertFalse(favoritePlayersManager.isEmpty)

        favoritePlayersManager.removePlayer(player2.id)
        assertFalse(favoritePlayersManager.isEmpty)

        favoritePlayersManager.removePlayer(player1.id)
        assertTrue(favoritePlayersManager.isEmpty)
    }

    @Test
    @Throws(Exception::class)
    fun testPlayers() {
        var players = favoritePlayersManager.players
        assertTrue(players == null || players.isEmpty())

        var absPlayers = favoritePlayersManager.absPlayers
        assertTrue(absPlayers == null || absPlayers.isEmpty())

        favoritePlayersManager.addPlayer(player2, regionManager.getRegion())
        players = favoritePlayersManager.players
        assertNotNull(players)
        assertEquals(1, players?.size)

        absPlayers = favoritePlayersManager.absPlayers
        assertNotNull(absPlayers)
        assertEquals(1, absPlayers?.size)

        favoritePlayersManager.addPlayer(player1, regionManager.getRegion())
        players = favoritePlayersManager.players
        assertNotNull(players)
        assertEquals(2, players?.size)

        absPlayers = favoritePlayersManager.absPlayers
        assertNotNull(absPlayers)
        assertEquals(2, absPlayers?.size)

        favoritePlayersManager.addPlayer(player2, regionManager.getRegion())
        players = favoritePlayersManager.players
        assertNotNull(players)
        assertEquals(2, players?.size)

        absPlayers = favoritePlayersManager.absPlayers
        assertNotNull(absPlayers)
        assertEquals(2, absPlayers?.size)

        favoritePlayersManager.removePlayer(player1)
        players = favoritePlayersManager.players
        assertNotNull(players)
        assertEquals(1, players?.size)

        absPlayers = favoritePlayersManager.absPlayers
        assertNotNull(absPlayers)
        assertEquals(1, absPlayers?.size)

        favoritePlayersManager.removePlayer(player2)
        players = favoritePlayersManager.players
        assertTrue(players == null || players.isEmpty())

        absPlayers = favoritePlayersManager.absPlayers
        assertTrue(absPlayers == null || absPlayers.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun testRemoveListener() {
        var players: List<FavoritePlayer>? = null

        val listener = object : FavoritePlayersManager.OnFavoritePlayersChangeListener {
            override fun onFavoritePlayersChange(favoritePlayersManager: FavoritePlayersManager) {
                players = favoritePlayersManager.players
            }
        }

        favoritePlayersManager.addListener(listener)
        assertNull(players)

        favoritePlayersManager.addPlayer(player1, regionManager.getRegion())
        assertNotNull(players)
        assertEquals(1, players?.size)
        assertEquals(player1, players?.get(0))

        favoritePlayersManager.removeListener(listener)
        favoritePlayersManager.removePlayer(player1)
        assertNotNull(players)
        assertEquals(1, players?.size)
        assertEquals(player1, players?.get(0))
    }

    @Test
    @Throws(Exception::class)
    fun testRemovePlayers() {
        favoritePlayersManager.addPlayer(player1, regionManager.getRegion())
        favoritePlayersManager.addPlayer(player2, regionManager.getRegion())

        favoritePlayersManager.removePlayer(player1)
        favoritePlayersManager.removePlayer(player2)
        var players = favoritePlayersManager.players
        assertTrue(players == null || players.isEmpty())
        assertTrue(favoritePlayersManager.isEmpty)

        favoritePlayersManager.addPlayer(player2, regionManager.getRegion())
        favoritePlayersManager.addPlayer(player1, regionManager.getRegion())
        favoritePlayersManager.removePlayer(player2.id)
        favoritePlayersManager.removePlayer(player1.id)
        players = favoritePlayersManager.players
        assertTrue(players == null || players.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun testSize() {
        assertEquals(0, favoritePlayersManager.size)

        favoritePlayersManager.addPlayer(player1, regionManager.getRegion())
        assertEquals(1, favoritePlayersManager.size)

        favoritePlayersManager.addPlayer(player2, regionManager.getRegion())
        assertEquals(2, favoritePlayersManager.size)

        favoritePlayersManager.addPlayer(player1, regionManager.getRegion())
        assertEquals(2, favoritePlayersManager.size)

        favoritePlayersManager.removePlayer(player1)
        assertEquals(1, favoritePlayersManager.size)

        favoritePlayersManager.removePlayer(player1)
        assertEquals(1, favoritePlayersManager.size)

        favoritePlayersManager.clear()
        assertEquals(0, favoritePlayersManager.size)

        favoritePlayersManager.removePlayer(player2)
        assertEquals(0, favoritePlayersManager.size)
    }

}
