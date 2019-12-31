package com.garpr.android.repositories

import com.garpr.android.BaseTest
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.data.models.LitePlayer
import com.garpr.android.data.models.Region
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.inject
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class FavoritePlayersRepositoryTest : BaseTest() {

    protected val favoritePlayersRepository: FavoritePlayersRepository by inject()

    companion object {
        private val HMW: AbsPlayer = LitePlayer(
                id = "583a4a15d2994e0577b05c74",
                name = "homemadewaffles"
        )

        private val SPARK: AbsPlayer = LitePlayer(
                id = "5877eb55d2994e15c7dea97e",
                name = "Spark"
        )

        private val NORCAL = Region(
                displayName = "Norcal",
                id = "norcal",
                endpoint = Endpoint.GAR_PR
        )
    }

    @Test
    fun testAddPlayer() {
        var players: List<FavoritePlayer>? = null

        favoritePlayersRepository.playersObservable.subscribe {
            players = it
        }

        favoritePlayersRepository.addPlayer(HMW, NORCAL)
        assertEquals(1, players?.size)
        assertEquals(HMW, players?.get(0))
    }

    @Test
    fun testAddPlayers() {
        var players: List<FavoritePlayer>? = null

        favoritePlayersRepository.playersObservable.subscribe {
            players = it
        }

        favoritePlayersRepository.addPlayer(SPARK, NORCAL)
        assertEquals(1, players?.size)
        assertEquals(SPARK, players?.get(0))

        favoritePlayersRepository.addPlayer(HMW, NORCAL)
        assertEquals(2, players?.size)
        assertEquals(HMW, players?.get(0))
        assertEquals(SPARK, players?.get(1))
    }

    @Test
    fun testAddPlayersWithDuplicate() {
        var players: List<FavoritePlayer>? = null

        favoritePlayersRepository.playersObservable.subscribe {
            players = it
        }

        favoritePlayersRepository.addPlayer(SPARK, NORCAL)
        favoritePlayersRepository.addPlayer(SPARK, NORCAL)
        assertEquals(1, players?.size)
        assertEquals(SPARK, players?.get(0))
    }

    @Test
    fun testClear() {
        var size: Int? = null

        favoritePlayersRepository.sizeObservable.subscribe {
            size = it
        }

        favoritePlayersRepository.clear()
        assertEquals(0, size)

        favoritePlayersRepository.addPlayer(HMW, NORCAL)
        favoritePlayersRepository.addPlayer(SPARK, NORCAL)
        favoritePlayersRepository.clear()
        assertEquals(0, size)
    }

    @Test
    fun testContains() {
        assertFalse(HMW in favoritePlayersRepository)
        assertFalse(SPARK in favoritePlayersRepository)

        favoritePlayersRepository.addPlayer(HMW, NORCAL)
        assertTrue(HMW in favoritePlayersRepository)
        assertFalse(SPARK in favoritePlayersRepository)

        favoritePlayersRepository.removePlayer(SPARK)
        assertTrue(HMW in favoritePlayersRepository)
        assertFalse(SPARK in favoritePlayersRepository)

        favoritePlayersRepository.removePlayer(HMW)
        assertFalse(HMW in favoritePlayersRepository)
        assertFalse(SPARK in favoritePlayersRepository)

        favoritePlayersRepository.addPlayer(SPARK, NORCAL)
        assertFalse(HMW in favoritePlayersRepository)
        assertTrue(SPARK in favoritePlayersRepository)

        favoritePlayersRepository.addPlayer(HMW, NORCAL)
        assertTrue(HMW in favoritePlayersRepository)
        assertTrue(SPARK in favoritePlayersRepository)

        favoritePlayersRepository.clear()
        assertFalse(HMW in favoritePlayersRepository)
        assertFalse(SPARK in favoritePlayersRepository)
    }

    @Test
    fun testPlayersObservable() {
        var players: List<FavoritePlayer>? = null

        favoritePlayersRepository.playersObservable.subscribe {
            players = it
        }

        assertEquals(true, players?.isEmpty())

        favoritePlayersRepository.addPlayer(HMW, NORCAL)
        assertEquals(1, players?.size)

        favoritePlayersRepository.addPlayer(SPARK, NORCAL)
        assertEquals(2, players?.size)

        favoritePlayersRepository.removePlayer(SPARK)
        assertEquals(1, players?.size)

        favoritePlayersRepository.removePlayer(HMW)
        assertEquals(true, players?.isEmpty())
    }

    @Test
    fun testRemovePlayer() {
        var players: List<FavoritePlayer>? = null

        favoritePlayersRepository.playersObservable.subscribe {
            players = it
        }

        favoritePlayersRepository.addPlayer(HMW, NORCAL)
        favoritePlayersRepository.removePlayer(HMW)
        assertEquals(true, players?.isEmpty())
    }

    @Test
    fun testRemovePlayers() {
        var players: List<FavoritePlayer>? = null

        favoritePlayersRepository.playersObservable.subscribe {
            players = it
        }

        favoritePlayersRepository.addPlayer(HMW, NORCAL)
        favoritePlayersRepository.addPlayer(SPARK, NORCAL)
        assertEquals(2, players?.size)
        assertEquals(HMW, players?.get(0))
        assertEquals(SPARK, players?.get(1))

        favoritePlayersRepository.removePlayer(HMW)
        assertEquals(1, players?.size)
        assertEquals(SPARK, players?.get(0))

        favoritePlayersRepository.removePlayer(SPARK)
        assertEquals(true, players?.isEmpty())
    }

    @Test
    fun testRemovePlayersWithDuplicate() {
        var players: List<FavoritePlayer>? = null

        favoritePlayersRepository.playersObservable.subscribe {
            players = it
        }

        favoritePlayersRepository.addPlayer(SPARK, NORCAL)
        favoritePlayersRepository.removePlayer(SPARK)
        favoritePlayersRepository.removePlayer(SPARK)
        assertEquals(true, players?.isEmpty())
    }

    @Test
    fun testSizeObservable() {
        var size: Int? = null

        favoritePlayersRepository.sizeObservable.subscribe {
            size = it
        }

        assertEquals(0, size)

        favoritePlayersRepository.addPlayer(HMW, NORCAL)
        assertEquals(1, size)

        favoritePlayersRepository.addPlayer(SPARK, NORCAL)
        assertEquals(2, size)

        favoritePlayersRepository.removePlayer(HMW)
        assertEquals(1, size)

        favoritePlayersRepository.removePlayer(SPARK)
        assertEquals(0, size)
    }

    @Test
    fun testSizeObservableWithDuplicates() {
        var size: Int? = null

        favoritePlayersRepository.sizeObservable.subscribe {
            size = it
        }

        assertEquals(0, size)

        favoritePlayersRepository.addPlayer(HMW, NORCAL)
        assertEquals(1, size)

        favoritePlayersRepository.addPlayer(SPARK, NORCAL)
        assertEquals(2, size)

        favoritePlayersRepository.addPlayer(HMW, NORCAL)
        assertEquals(2, size)

        favoritePlayersRepository.removePlayer(HMW)
        assertEquals(1, size)

        favoritePlayersRepository.removePlayer(SPARK)
        assertEquals(0, size)

        favoritePlayersRepository.removePlayer(HMW)
        assertEquals(0, size)
    }

}
