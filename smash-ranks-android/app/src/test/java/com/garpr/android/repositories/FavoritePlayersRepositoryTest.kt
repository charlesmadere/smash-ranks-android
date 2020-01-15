package com.garpr.android.repositories

import com.garpr.android.BaseKoinTest
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.data.models.LitePlayer
import com.garpr.android.data.models.Region
import com.garpr.android.koin.FAVORITE_PLAYERS_KEY_VALUE_STORE
import com.garpr.android.preferences.KeyValueStoreProvider
import com.squareup.moshi.Moshi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.koin.test.inject

class FavoritePlayersRepositoryTest : BaseKoinTest() {

    protected val favoritePlayersRepository: FavoritePlayersRepository by inject()
    protected val keyValueStoreProvider: KeyValueStoreProvider by inject()
    protected val moshi: Moshi by inject()

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
    fun testMigrate() {
        var players: List<FavoritePlayer>? = null

        favoritePlayersRepository.playersObservable.subscribe {
            players = it
        }

        val keyValueStore = keyValueStoreProvider.getKeyValueStore(FAVORITE_PLAYERS_KEY_VALUE_STORE)
        val jsonAdapter = moshi.adapter(FavoritePlayer::class.java)

        keyValueStore.setString(HMW.id, jsonAdapter.toJson(FavoritePlayer(
                id = HMW.id,
                name = HMW.name,
                region = NORCAL
        )))

        keyValueStore.setString(SPARK.id, jsonAdapter.toJson(FavoritePlayer(
                id = SPARK.id,
                name = SPARK.name,
                region = NORCAL
        )))

        assertEquals(true, players?.isEmpty())

        favoritePlayersRepository.migrate()
        assertEquals(2, players?.size)
        assertEquals(HMW, players?.get(0))
        assertEquals(SPARK, players?.get(1))
        assertTrue(keyValueStore.all.isNullOrEmpty())
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

        favoritePlayersRepository.removePlayer(SPARK, NORCAL)
        assertEquals(1, players?.size)

        favoritePlayersRepository.removePlayer(HMW, NORCAL)
        assertEquals(true, players?.isEmpty())
    }

    @Test
    fun testRemovePlayer() {
        var players: List<FavoritePlayer>? = null

        favoritePlayersRepository.playersObservable.subscribe {
            players = it
        }

        favoritePlayersRepository.addPlayer(HMW, NORCAL)
        favoritePlayersRepository.removePlayer(HMW, NORCAL)
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

        favoritePlayersRepository.removePlayer(HMW, NORCAL)
        assertEquals(1, players?.size)
        assertEquals(SPARK, players?.get(0))

        favoritePlayersRepository.removePlayer(SPARK, NORCAL)
        assertEquals(true, players?.isEmpty())
    }

    @Test
    fun testRemovePlayersWithDuplicate() {
        var players: List<FavoritePlayer>? = null

        favoritePlayersRepository.playersObservable.subscribe {
            players = it
        }

        favoritePlayersRepository.addPlayer(SPARK, NORCAL)
        favoritePlayersRepository.removePlayer(SPARK, NORCAL)
        favoritePlayersRepository.removePlayer(SPARK, NORCAL)
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

        favoritePlayersRepository.removePlayer(HMW, NORCAL)
        assertEquals(1, size)

        favoritePlayersRepository.removePlayer(SPARK, NORCAL)
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

        favoritePlayersRepository.removePlayer(HMW, NORCAL)
        assertEquals(1, size)

        favoritePlayersRepository.removePlayer(SPARK, NORCAL)
        assertEquals(0, size)

        favoritePlayersRepository.removePlayer(HMW, NORCAL)
        assertEquals(0, size)
    }

}
