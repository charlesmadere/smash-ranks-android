package com.garpr.android.repositories

import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.LitePlayer
import com.garpr.android.data.models.PlayersBundle
import com.garpr.android.data.models.Region
import com.garpr.android.networking.AbsServerApi
import com.garpr.android.test.BaseTest
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class PlayersRepositoryTest : BaseTest() {

    private lateinit var playersRepository: PlayersRepository
    private val serverApi = ServerApiOverride()

    @Before
    override fun setUp() {
        super.setUp()

        playersRepository = PlayersRepositoryImpl(serverApi)
    }

    @Test
    fun testGetPlayersWithEmptyPlayersBundle() {
        serverApi.playersBundle = EMPTY_PLAYERS_BUNDLE
        val bundle = playersRepository.getPlayers(NORCAL)
                .blockingGet()

        assertTrue(bundle.players.isNullOrEmpty())
    }

    @Test
    fun testGetPlayersWithPlayersBundle() {
        serverApi.playersBundle = PLAYERS_BUNDLE
        val bundle = playersRepository.getPlayers(NORCAL)
                .blockingGet()

        assertEquals(4, bundle.players?.size)
        assertEquals(BLARGH, bundle.players?.get(0))
        assertEquals(NMW, bundle.players?.get(1))
        assertEquals(SNAP, bundle.players?.get(2))
        assertEquals(UMARTH, bundle.players?.get(3))
    }

    companion object {
        private val BLARGH = LitePlayer(
                id = "58885df9d2994e3d56594112",
                name = "blargh"
        )

        private val NMW = LitePlayer(
                id = "583a4a15d2994e0577b05c8a",
                name = "NMW"
        )

        private val SNAP = LitePlayer(
                id = "59213f1ad2994e1d79144956",
                name = "Snap"
        )

        private val UMARTH = LitePlayer(
                id = "5877eb55d2994e15c7dea977",
                name = "Umarth"
        )

        private val EMPTY_PLAYERS_BUNDLE = PlayersBundle()

        private val PLAYERS_BUNDLE = PlayersBundle(
                players = listOf(UMARTH, BLARGH, SNAP, NMW)
        )

        private val NORCAL = Region(
                displayName = "Norcal",
                id = "norcal",
                endpoint = Endpoint.GAR_PR
        )
    }

    private class ServerApiOverride(
            internal var playersBundle: PlayersBundle? = null
    ) : AbsServerApi() {

        override fun getPlayers(region: Region): Single<PlayersBundle> {
            val playersBundle = this.playersBundle

            return if (playersBundle == null) {
                Single.error(NullPointerException())
            } else {
                Single.just(playersBundle)
            }
        }

    }

}
