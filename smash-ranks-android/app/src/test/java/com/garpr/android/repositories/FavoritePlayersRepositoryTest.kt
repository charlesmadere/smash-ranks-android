package com.garpr.android.repositories

import com.garpr.android.BaseTest
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.data.models.LitePlayer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.inject
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class FavoritePlayersRepositoryTest : BaseTest() {

    protected val favoritePlayersRepository: FavoritePlayersRepository by inject()
    protected val regionRepository: RegionRepository by inject()

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

    @Test
    fun testAddPlayer() {
        favoritePlayersRepository.addPlayer(PLAYER_0, regionRepository.getRegion())

        val players = favoritePlayersRepository.players
        assertNotNull(players)
        assertEquals(1, players?.size)
        assertEquals(PLAYER_0, players?.get(0))
    }

    @Test
    fun testAddPlayers() {
        favoritePlayersRepository.addPlayer(PLAYER_0, regionRepository.getRegion())
        favoritePlayersRepository.addPlayer(PLAYER_1, regionRepository.getRegion())

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

        favoritePlayersRepository.addPlayer(PLAYER_0, regionRepository.getRegion())
        assertFalse(favoritePlayersRepository.isEmpty)

        favoritePlayersRepository.clear()
        assertTrue(favoritePlayersRepository.isEmpty)

        favoritePlayersRepository.addPlayer(PLAYER_1, regionRepository.getRegion())
        favoritePlayersRepository.addPlayer(PLAYER_0, regionRepository.getRegion())
        favoritePlayersRepository.clear()
        assertTrue(favoritePlayersRepository.isEmpty)

    }

    @Test
    fun testContains() {
        assertFalse(PLAYER_0 in favoritePlayersRepository)
        assertFalse(PLAYER_0.id in favoritePlayersRepository)
        assertFalse(PLAYER_1 in favoritePlayersRepository)
        assertFalse(PLAYER_1.id in favoritePlayersRepository)

        favoritePlayersRepository.addPlayer(PLAYER_0, regionRepository.getRegion())
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

        favoritePlayersRepository.addPlayer(PLAYER_1, regionRepository.getRegion())
        assertFalse(PLAYER_0 in favoritePlayersRepository)
        assertFalse(PLAYER_0.id in favoritePlayersRepository)
        assertTrue(PLAYER_1 in favoritePlayersRepository)
        assertTrue(PLAYER_1.id in favoritePlayersRepository)

        favoritePlayersRepository.addPlayer(PLAYER_0, regionRepository.getRegion())
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

        favoritePlayersRepository.addPlayer(PLAYER_0, regionRepository.getRegion())
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

        favoritePlayersRepository.addPlayer(PLAYER_1, regionRepository.getRegion())
        players = favoritePlayersRepository.players
        assertNotNull(players)
        assertEquals(1, players?.size)

        favoritePlayersRepository.addPlayer(PLAYER_0, regionRepository.getRegion())
        players = favoritePlayersRepository.players
        assertNotNull(players)
        assertEquals(2, players?.size)

        favoritePlayersRepository.addPlayer(PLAYER_1, regionRepository.getRegion())
        players = favoritePlayersRepository.players
        assertNotNull(players)
        assertEquals(2, players?.size)

        favoritePlayersRepository.removePlayer(PLAYER_0)
        players = favoritePlayersRepository.players
        assertNotNull(players)
        assertEquals(1, players?.size)

        favoritePlayersRepository.removePlayer(PLAYER_1)
        players = favoritePlayersRepository.players
        assertTrue(players.isNullOrEmpty())
    }

    @Test
    fun testPlayersObservable() {
        var value: List<FavoritePlayer>? = null

        favoritePlayersRepository.playersObservable.subscribe {
            value = it
        }

        assertNotNull(value)
        assertTrue(value.isNullOrEmpty())

        favoritePlayersRepository.addPlayer(PLAYER_0, regionRepository.getRegion())
        assertNotNull(value)
        assertEquals(1, value?.size)

        favoritePlayersRepository.addPlayer(PLAYER_1, regionRepository.getRegion())
        assertNotNull(value)
        assertEquals(2, value?.size)

        favoritePlayersRepository.removePlayer(PLAYER_1)
        assertNotNull(value)
        assertEquals(1, value?.size)

        favoritePlayersRepository.removePlayer(PLAYER_0)
        assertNotNull(value)
        assertTrue(value.isNullOrEmpty())
    }

    @Test
    fun testRemovePlayers() {
        favoritePlayersRepository.addPlayer(PLAYER_0, regionRepository.getRegion())
        favoritePlayersRepository.addPlayer(PLAYER_1, regionRepository.getRegion())

        favoritePlayersRepository.removePlayer(PLAYER_0)
        favoritePlayersRepository.removePlayer(PLAYER_1)
        var players = favoritePlayersRepository.players
        assertTrue(players.isNullOrEmpty())
        assertTrue(favoritePlayersRepository.isEmpty)

        favoritePlayersRepository.addPlayer(PLAYER_1, regionRepository.getRegion())
        favoritePlayersRepository.addPlayer(PLAYER_0, regionRepository.getRegion())
        favoritePlayersRepository.removePlayer(PLAYER_1.id)
        favoritePlayersRepository.removePlayer(PLAYER_0.id)
        players = favoritePlayersRepository.players
        assertTrue(players.isNullOrEmpty())
    }

    @Test
    fun testSize() {
        assertEquals(0, favoritePlayersRepository.size)

        favoritePlayersRepository.addPlayer(PLAYER_0, regionRepository.getRegion())
        assertEquals(1, favoritePlayersRepository.size)

        favoritePlayersRepository.addPlayer(PLAYER_1, regionRepository.getRegion())
        assertEquals(2, favoritePlayersRepository.size)

        favoritePlayersRepository.addPlayer(PLAYER_0, regionRepository.getRegion())
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
