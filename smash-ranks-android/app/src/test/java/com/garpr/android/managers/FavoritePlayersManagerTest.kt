package com.garpr.android.managers

import com.garpr.android.BaseTest
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.data.models.LitePlayer
import com.garpr.android.repositories.FavoritePlayersManager
import com.garpr.android.repositories.RegionManager
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

    @Inject
    protected lateinit var favoritePlayersManager: FavoritePlayersManager

    @Inject
    protected lateinit var regionManager: RegionManager


    companion object {
        private val PLAYER_0: AbsPlayer = LitePlayer(
                id = "583a4a15d2994e0577b05c74",
                name = "homemadewaffles"
        )

        private val PLAYER_1: AbsPlayer = LitePlayer(
                id = "5877eb55d2994e15c7dea97e",
                name = "Spark"
        )
    }

    @Before
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)
    }

    @Test
    fun testAbsPlayers() {
        var absPlayers = favoritePlayersManager.absPlayers
        assertTrue(absPlayers.isNullOrEmpty())

        favoritePlayersManager.addPlayer(PLAYER_1, regionManager.getRegion())
        favoritePlayersManager.addPlayer(PLAYER_0, regionManager.getRegion())

        absPlayers = favoritePlayersManager.absPlayers
        assertNotNull(absPlayers)
        assertEquals(2, absPlayers?.size)

        assertEquals(PLAYER_0, absPlayers?.get(0))
        assertEquals(PLAYER_1, absPlayers?.get(1))
    }

    @Test
    fun testAddListener() {
        var players: List<FavoritePlayer>? = null

        val listener = object : FavoritePlayersManager.OnFavoritePlayersChangeListener {
            override fun onFavoritePlayersChange(favoritePlayersManager: FavoritePlayersManager) {
                players = favoritePlayersManager.players
            }
        }

        favoritePlayersManager.addListener(listener)
        assertNull(players)

        favoritePlayersManager.addPlayer(PLAYER_0, regionManager.getRegion())
        assertNotNull(players)
        assertEquals(1, players?.size)
        assertEquals(PLAYER_0, players?.get(0))

        favoritePlayersManager.removePlayer(PLAYER_0)
        assertTrue(players.isNullOrEmpty())
    }

    @Test
    fun testAddListenerTwice() {
        var count = 0

        val listener = object : FavoritePlayersManager.OnFavoritePlayersChangeListener {
            override fun onFavoritePlayersChange(favoritePlayersManager: FavoritePlayersManager) {
                ++count
            }
        }

        favoritePlayersManager.addListener(listener)
        favoritePlayersManager.addListener(listener)
        favoritePlayersManager.addPlayer(PLAYER_0, regionManager.getRegion())
        assertEquals(1, count)
    }

    @Test
    fun testAddPlayer() {
        favoritePlayersManager.addPlayer(PLAYER_0, regionManager.getRegion())

        val players = favoritePlayersManager.players
        assertNotNull(players)
        assertEquals(1, players?.size)
        assertEquals(PLAYER_0, players?.get(0))
    }

    @Test
    fun testAddPlayers() {
        favoritePlayersManager.addPlayer(PLAYER_0, regionManager.getRegion())
        favoritePlayersManager.addPlayer(PLAYER_1, regionManager.getRegion())

        val players = favoritePlayersManager.players
        assertNotNull(players)
        assertEquals(2, players?.size)
        assertEquals(PLAYER_0, players?.get(0))
        assertEquals(PLAYER_1, players?.get(1))
    }

    @Test
    fun testClear() {
        favoritePlayersManager.clear()
        assertTrue(favoritePlayersManager.isEmpty)

        favoritePlayersManager.addPlayer(PLAYER_0, regionManager.getRegion())
        assertFalse(favoritePlayersManager.isEmpty)

        favoritePlayersManager.clear()
        assertTrue(favoritePlayersManager.isEmpty)

        favoritePlayersManager.addPlayer(PLAYER_1, regionManager.getRegion())
        favoritePlayersManager.addPlayer(PLAYER_0, regionManager.getRegion())
        favoritePlayersManager.clear()
        assertTrue(favoritePlayersManager.isEmpty)

    }

    @Test
    fun testContains() {
        assertFalse(PLAYER_0 in favoritePlayersManager)
        assertFalse(PLAYER_0.id in favoritePlayersManager)
        assertFalse(PLAYER_1 in favoritePlayersManager)
        assertFalse(PLAYER_1.id in favoritePlayersManager)

        favoritePlayersManager.addPlayer(PLAYER_0, regionManager.getRegion())
        assertTrue(PLAYER_0 in favoritePlayersManager)
        assertTrue(PLAYER_0.id in favoritePlayersManager)
        assertFalse(PLAYER_1 in favoritePlayersManager)
        assertFalse(PLAYER_1.id in favoritePlayersManager)

        favoritePlayersManager.removePlayer(PLAYER_1)
        assertTrue(PLAYER_0 in favoritePlayersManager)
        assertTrue(PLAYER_0.id in favoritePlayersManager)
        assertFalse(PLAYER_1 in favoritePlayersManager)
        assertFalse(PLAYER_1.id in favoritePlayersManager)

        favoritePlayersManager.removePlayer(PLAYER_0)
        assertFalse(PLAYER_0 in favoritePlayersManager)
        assertFalse(PLAYER_0.id in favoritePlayersManager)
        assertFalse(PLAYER_1 in favoritePlayersManager)
        assertFalse(PLAYER_1.id in favoritePlayersManager)

        favoritePlayersManager.addPlayer(PLAYER_1, regionManager.getRegion())
        assertFalse(PLAYER_0 in favoritePlayersManager)
        assertFalse(PLAYER_0.id in favoritePlayersManager)
        assertTrue(PLAYER_1 in favoritePlayersManager)
        assertTrue(PLAYER_1.id in favoritePlayersManager)

        favoritePlayersManager.addPlayer(PLAYER_0, regionManager.getRegion())
        assertTrue(PLAYER_0 in favoritePlayersManager)
        assertTrue(PLAYER_0.id in favoritePlayersManager)
        assertTrue(PLAYER_1 in favoritePlayersManager)
        assertTrue(PLAYER_1.id in favoritePlayersManager)

        favoritePlayersManager.clear()
        assertFalse(PLAYER_0 in favoritePlayersManager)
        assertFalse(PLAYER_0.id in favoritePlayersManager)
        assertFalse(PLAYER_1 in favoritePlayersManager)
        assertFalse(PLAYER_1.id in favoritePlayersManager)
    }

    @Test
    fun testIsEmpty() {
        assertTrue(favoritePlayersManager.isEmpty)

        favoritePlayersManager.addPlayer(PLAYER_0, regionManager.getRegion())
        assertFalse(favoritePlayersManager.isEmpty)

        favoritePlayersManager.removePlayer(PLAYER_1)
        assertFalse(favoritePlayersManager.isEmpty)

        favoritePlayersManager.removePlayer(PLAYER_1.id)
        assertFalse(favoritePlayersManager.isEmpty)

        favoritePlayersManager.removePlayer(PLAYER_0.id)
        assertTrue(favoritePlayersManager.isEmpty)
    }

    @Test
    fun testPlayers() {
        var players = favoritePlayersManager.players
        assertTrue(players.isNullOrEmpty())

        var absPlayers = favoritePlayersManager.absPlayers
        assertTrue(absPlayers.isNullOrEmpty())

        favoritePlayersManager.addPlayer(PLAYER_1, regionManager.getRegion())
        players = favoritePlayersManager.players
        assertNotNull(players)
        assertEquals(1, players?.size)

        absPlayers = favoritePlayersManager.absPlayers
        assertNotNull(absPlayers)
        assertEquals(1, absPlayers?.size)

        favoritePlayersManager.addPlayer(PLAYER_0, regionManager.getRegion())
        players = favoritePlayersManager.players
        assertNotNull(players)
        assertEquals(2, players?.size)

        absPlayers = favoritePlayersManager.absPlayers
        assertNotNull(absPlayers)
        assertEquals(2, absPlayers?.size)

        favoritePlayersManager.addPlayer(PLAYER_1, regionManager.getRegion())
        players = favoritePlayersManager.players
        assertNotNull(players)
        assertEquals(2, players?.size)

        absPlayers = favoritePlayersManager.absPlayers
        assertNotNull(absPlayers)
        assertEquals(2, absPlayers?.size)

        favoritePlayersManager.removePlayer(PLAYER_0)
        players = favoritePlayersManager.players
        assertNotNull(players)
        assertEquals(1, players?.size)

        absPlayers = favoritePlayersManager.absPlayers
        assertNotNull(absPlayers)
        assertEquals(1, absPlayers?.size)

        favoritePlayersManager.removePlayer(PLAYER_1)
        players = favoritePlayersManager.players
        assertTrue(players.isNullOrEmpty())

        absPlayers = favoritePlayersManager.absPlayers
        assertTrue(absPlayers.isNullOrEmpty())
    }

    @Test
    fun testRemoveListener() {
        var players: List<FavoritePlayer>? = null

        val listener = object : FavoritePlayersManager.OnFavoritePlayersChangeListener {
            override fun onFavoritePlayersChange(favoritePlayersManager: FavoritePlayersManager) {
                players = favoritePlayersManager.players
            }
        }

        favoritePlayersManager.addListener(listener)
        assertNull(players)

        favoritePlayersManager.addPlayer(PLAYER_0, regionManager.getRegion())
        assertNotNull(players)
        assertEquals(1, players?.size)
        assertEquals(PLAYER_0, players?.get(0))

        favoritePlayersManager.removeListener(listener)
        favoritePlayersManager.removePlayer(PLAYER_0)
        assertNotNull(players)
        assertEquals(1, players?.size)
        assertEquals(PLAYER_0, players?.get(0))
    }

    @Test
    fun testRemovePlayers() {
        favoritePlayersManager.addPlayer(PLAYER_0, regionManager.getRegion())
        favoritePlayersManager.addPlayer(PLAYER_1, regionManager.getRegion())

        favoritePlayersManager.removePlayer(PLAYER_0)
        favoritePlayersManager.removePlayer(PLAYER_1)
        var players = favoritePlayersManager.players
        assertTrue(players.isNullOrEmpty())
        assertTrue(favoritePlayersManager.isEmpty)

        favoritePlayersManager.addPlayer(PLAYER_1, regionManager.getRegion())
        favoritePlayersManager.addPlayer(PLAYER_0, regionManager.getRegion())
        favoritePlayersManager.removePlayer(PLAYER_1.id)
        favoritePlayersManager.removePlayer(PLAYER_0.id)
        players = favoritePlayersManager.players
        assertTrue(players.isNullOrEmpty())
    }

    @Test
    fun testSize() {
        assertEquals(0, favoritePlayersManager.size)

        favoritePlayersManager.addPlayer(PLAYER_0, regionManager.getRegion())
        assertEquals(1, favoritePlayersManager.size)

        favoritePlayersManager.addPlayer(PLAYER_1, regionManager.getRegion())
        assertEquals(2, favoritePlayersManager.size)

        favoritePlayersManager.addPlayer(PLAYER_0, regionManager.getRegion())
        assertEquals(2, favoritePlayersManager.size)

        favoritePlayersManager.removePlayer(PLAYER_0)
        assertEquals(1, favoritePlayersManager.size)

        favoritePlayersManager.removePlayer(PLAYER_0)
        assertEquals(1, favoritePlayersManager.size)

        favoritePlayersManager.clear()
        assertEquals(0, favoritePlayersManager.size)

        favoritePlayersManager.removePlayer(PLAYER_1)
        assertEquals(0, favoritePlayersManager.size)
    }

}
