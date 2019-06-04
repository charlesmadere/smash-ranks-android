package com.garpr.android.managers

import com.garpr.android.BaseTest
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.data.models.LitePlayer
import com.garpr.android.repositories.FavoritePlayersRepository
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
class FavoritePlayersRepositoryTest : BaseTest() {

    @Inject
    protected lateinit var favoritePlayersRepository: FavoritePlayersRepository

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
        var absPlayers = favoritePlayersRepository.absPlayers
        assertTrue(absPlayers.isNullOrEmpty())

        favoritePlayersRepository.addPlayer(PLAYER_1, regionManager.getRegion())
        favoritePlayersRepository.addPlayer(PLAYER_0, regionManager.getRegion())

        absPlayers = favoritePlayersRepository.absPlayers
        assertNotNull(absPlayers)
        assertEquals(2, absPlayers?.size)

        assertEquals(PLAYER_0, absPlayers?.get(0))
        assertEquals(PLAYER_1, absPlayers?.get(1))
    }

    @Test
    fun testAddListener() {
        var players: List<FavoritePlayer>? = null

        val listener = object : FavoritePlayersRepository.OnFavoritePlayersChangeListener {
            override fun onFavoritePlayersChange(favoritePlayersRepository: FavoritePlayersRepository) {
                players = favoritePlayersRepository.players
            }
        }

        favoritePlayersRepository.addListener(listener)
        assertNull(players)

        favoritePlayersRepository.addPlayer(PLAYER_0, regionManager.getRegion())
        assertNotNull(players)
        assertEquals(1, players?.size)
        assertEquals(PLAYER_0, players?.get(0))

        favoritePlayersRepository.removePlayer(PLAYER_0)
        assertTrue(players.isNullOrEmpty())
    }

    @Test
    fun testAddListenerTwice() {
        var count = 0

        val listener = object : FavoritePlayersRepository.OnFavoritePlayersChangeListener {
            override fun onFavoritePlayersChange(favoritePlayersRepository: FavoritePlayersRepository) {
                ++count
            }
        }

        favoritePlayersRepository.addListener(listener)
        favoritePlayersRepository.addListener(listener)
        favoritePlayersRepository.addPlayer(PLAYER_0, regionManager.getRegion())
        assertEquals(1, count)
    }

    @Test
    fun testAddPlayer() {
        favoritePlayersRepository.addPlayer(PLAYER_0, regionManager.getRegion())

        val players = favoritePlayersRepository.players
        assertNotNull(players)
        assertEquals(1, players?.size)
        assertEquals(PLAYER_0, players?.get(0))
    }

    @Test
    fun testAddPlayers() {
        favoritePlayersRepository.addPlayer(PLAYER_0, regionManager.getRegion())
        favoritePlayersRepository.addPlayer(PLAYER_1, regionManager.getRegion())

        val players = favoritePlayersRepository.players
        assertNotNull(players)
        assertEquals(2, players?.size)
        assertEquals(PLAYER_0, players?.get(0))
        assertEquals(PLAYER_1, players?.get(1))
    }

    @Test
    fun testClear() {
        favoritePlayersRepository.clear()
        assertTrue(favoritePlayersRepository.isEmpty)

        favoritePlayersRepository.addPlayer(PLAYER_0, regionManager.getRegion())
        assertFalse(favoritePlayersRepository.isEmpty)

        favoritePlayersRepository.clear()
        assertTrue(favoritePlayersRepository.isEmpty)

        favoritePlayersRepository.addPlayer(PLAYER_1, regionManager.getRegion())
        favoritePlayersRepository.addPlayer(PLAYER_0, regionManager.getRegion())
        favoritePlayersRepository.clear()
        assertTrue(favoritePlayersRepository.isEmpty)

    }

    @Test
    fun testContains() {
        assertFalse(PLAYER_0 in favoritePlayersRepository)
        assertFalse(PLAYER_0.id in favoritePlayersRepository)
        assertFalse(PLAYER_1 in favoritePlayersRepository)
        assertFalse(PLAYER_1.id in favoritePlayersRepository)

        favoritePlayersRepository.addPlayer(PLAYER_0, regionManager.getRegion())
        assertTrue(PLAYER_0 in favoritePlayersRepository)
        assertTrue(PLAYER_0.id in favoritePlayersRepository)
        assertFalse(PLAYER_1 in favoritePlayersRepository)
        assertFalse(PLAYER_1.id in favoritePlayersRepository)

        favoritePlayersRepository.removePlayer(PLAYER_1)
        assertTrue(PLAYER_0 in favoritePlayersRepository)
        assertTrue(PLAYER_0.id in favoritePlayersRepository)
        assertFalse(PLAYER_1 in favoritePlayersRepository)
        assertFalse(PLAYER_1.id in favoritePlayersRepository)

        favoritePlayersRepository.removePlayer(PLAYER_0)
        assertFalse(PLAYER_0 in favoritePlayersRepository)
        assertFalse(PLAYER_0.id in favoritePlayersRepository)
        assertFalse(PLAYER_1 in favoritePlayersRepository)
        assertFalse(PLAYER_1.id in favoritePlayersRepository)

        favoritePlayersRepository.addPlayer(PLAYER_1, regionManager.getRegion())
        assertFalse(PLAYER_0 in favoritePlayersRepository)
        assertFalse(PLAYER_0.id in favoritePlayersRepository)
        assertTrue(PLAYER_1 in favoritePlayersRepository)
        assertTrue(PLAYER_1.id in favoritePlayersRepository)

        favoritePlayersRepository.addPlayer(PLAYER_0, regionManager.getRegion())
        assertTrue(PLAYER_0 in favoritePlayersRepository)
        assertTrue(PLAYER_0.id in favoritePlayersRepository)
        assertTrue(PLAYER_1 in favoritePlayersRepository)
        assertTrue(PLAYER_1.id in favoritePlayersRepository)

        favoritePlayersRepository.clear()
        assertFalse(PLAYER_0 in favoritePlayersRepository)
        assertFalse(PLAYER_0.id in favoritePlayersRepository)
        assertFalse(PLAYER_1 in favoritePlayersRepository)
        assertFalse(PLAYER_1.id in favoritePlayersRepository)
    }

    @Test
    fun testIsEmpty() {
        assertTrue(favoritePlayersRepository.isEmpty)

        favoritePlayersRepository.addPlayer(PLAYER_0, regionManager.getRegion())
        assertFalse(favoritePlayersRepository.isEmpty)

        favoritePlayersRepository.removePlayer(PLAYER_1)
        assertFalse(favoritePlayersRepository.isEmpty)

        favoritePlayersRepository.removePlayer(PLAYER_1.id)
        assertFalse(favoritePlayersRepository.isEmpty)

        favoritePlayersRepository.removePlayer(PLAYER_0.id)
        assertTrue(favoritePlayersRepository.isEmpty)
    }

    @Test
    fun testPlayers() {
        var players = favoritePlayersRepository.players
        assertTrue(players.isNullOrEmpty())

        var absPlayers = favoritePlayersRepository.absPlayers
        assertTrue(absPlayers.isNullOrEmpty())

        favoritePlayersRepository.addPlayer(PLAYER_1, regionManager.getRegion())
        players = favoritePlayersRepository.players
        assertNotNull(players)
        assertEquals(1, players?.size)

        absPlayers = favoritePlayersRepository.absPlayers
        assertNotNull(absPlayers)
        assertEquals(1, absPlayers?.size)

        favoritePlayersRepository.addPlayer(PLAYER_0, regionManager.getRegion())
        players = favoritePlayersRepository.players
        assertNotNull(players)
        assertEquals(2, players?.size)

        absPlayers = favoritePlayersRepository.absPlayers
        assertNotNull(absPlayers)
        assertEquals(2, absPlayers?.size)

        favoritePlayersRepository.addPlayer(PLAYER_1, regionManager.getRegion())
        players = favoritePlayersRepository.players
        assertNotNull(players)
        assertEquals(2, players?.size)

        absPlayers = favoritePlayersRepository.absPlayers
        assertNotNull(absPlayers)
        assertEquals(2, absPlayers?.size)

        favoritePlayersRepository.removePlayer(PLAYER_0)
        players = favoritePlayersRepository.players
        assertNotNull(players)
        assertEquals(1, players?.size)

        absPlayers = favoritePlayersRepository.absPlayers
        assertNotNull(absPlayers)
        assertEquals(1, absPlayers?.size)

        favoritePlayersRepository.removePlayer(PLAYER_1)
        players = favoritePlayersRepository.players
        assertTrue(players.isNullOrEmpty())

        absPlayers = favoritePlayersRepository.absPlayers
        assertTrue(absPlayers.isNullOrEmpty())
    }

    @Test
    fun testRemoveListener() {
        var players: List<FavoritePlayer>? = null

        val listener = object : FavoritePlayersRepository.OnFavoritePlayersChangeListener {
            override fun onFavoritePlayersChange(favoritePlayersRepository: FavoritePlayersRepository) {
                players = favoritePlayersRepository.players
            }
        }

        favoritePlayersRepository.addListener(listener)
        assertNull(players)

        favoritePlayersRepository.addPlayer(PLAYER_0, regionManager.getRegion())
        assertNotNull(players)
        assertEquals(1, players?.size)
        assertEquals(PLAYER_0, players?.get(0))

        favoritePlayersRepository.removeListener(listener)
        favoritePlayersRepository.removePlayer(PLAYER_0)
        assertNotNull(players)
        assertEquals(1, players?.size)
        assertEquals(PLAYER_0, players?.get(0))
    }

    @Test
    fun testRemovePlayers() {
        favoritePlayersRepository.addPlayer(PLAYER_0, regionManager.getRegion())
        favoritePlayersRepository.addPlayer(PLAYER_1, regionManager.getRegion())

        favoritePlayersRepository.removePlayer(PLAYER_0)
        favoritePlayersRepository.removePlayer(PLAYER_1)
        var players = favoritePlayersRepository.players
        assertTrue(players.isNullOrEmpty())
        assertTrue(favoritePlayersRepository.isEmpty)

        favoritePlayersRepository.addPlayer(PLAYER_1, regionManager.getRegion())
        favoritePlayersRepository.addPlayer(PLAYER_0, regionManager.getRegion())
        favoritePlayersRepository.removePlayer(PLAYER_1.id)
        favoritePlayersRepository.removePlayer(PLAYER_0.id)
        players = favoritePlayersRepository.players
        assertTrue(players.isNullOrEmpty())
    }

    @Test
    fun testSize() {
        assertEquals(0, favoritePlayersRepository.size)

        favoritePlayersRepository.addPlayer(PLAYER_0, regionManager.getRegion())
        assertEquals(1, favoritePlayersRepository.size)

        favoritePlayersRepository.addPlayer(PLAYER_1, regionManager.getRegion())
        assertEquals(2, favoritePlayersRepository.size)

        favoritePlayersRepository.addPlayer(PLAYER_0, regionManager.getRegion())
        assertEquals(2, favoritePlayersRepository.size)

        favoritePlayersRepository.removePlayer(PLAYER_0)
        assertEquals(1, favoritePlayersRepository.size)

        favoritePlayersRepository.removePlayer(PLAYER_0)
        assertEquals(1, favoritePlayersRepository.size)

        favoritePlayersRepository.clear()
        assertEquals(0, favoritePlayersRepository.size)

        favoritePlayersRepository.removePlayer(PLAYER_1)
        assertEquals(0, favoritePlayersRepository.size)
    }

}
