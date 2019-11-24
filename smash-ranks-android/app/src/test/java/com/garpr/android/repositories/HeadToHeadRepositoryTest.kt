package com.garpr.android.repositories

import com.garpr.android.BaseTest
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.AbsTournament
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.HeadToHead
import com.garpr.android.data.models.LitePlayer
import com.garpr.android.data.models.LiteTournament
import com.garpr.android.data.models.Match
import com.garpr.android.data.models.MatchResult
import com.garpr.android.data.models.Region
import com.garpr.android.data.models.SimpleDate
import com.garpr.android.misc.Schedulers
import com.garpr.android.networking.AbsServerApi
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.inject
import org.robolectric.RobolectricTestRunner
import java.util.Date

@RunWith(RobolectricTestRunner::class)
class HeadToHeadRepositoryTest : BaseTest() {

    private lateinit var headToHeadRepository: HeadToHeadRepository
    private val serverApi = ServerApiOverride()

    protected val schedulers: Schedulers by inject()

    companion object {
        private val NORCAL = Region(
                displayName = "Norcal",
                id = "norcal",
                endpoint = Endpoint.GAR_PR
        )

        private val AERIUS: AbsPlayer = LitePlayer(
                id = "597d28bed2994e34028b4cbe",
                name = "Aerius"
        )

        private val CHARLEZARD: AbsPlayer = LitePlayer(
                id = "587a951dd2994e15c7dea9fe",
                name = "Charlezard"
        )

        private val IMYT: AbsPlayer = LitePlayer(
                id = "5877eb55d2994e15c7dea98b",
                name = "Imyt"
        )

        private val SNAP: AbsPlayer = LitePlayer(
                id = "59213f1ad2994e1d79144956",
                name = "Snap"
        )

        private val SUPER_GATOR_GAMES: AbsTournament = LiteTournament(
                date = SimpleDate(Date(1554620400000L)),
                id = "5cac1fe7d2994e7c7950102e",
                name = "Super Gator Games"
        )

        private val MELEE_AT_THE_MADE_100: AbsTournament = LiteTournament(
                date = SimpleDate(Date(1558076400000L)),
                id = "5ce1f0d3d2994e12b795a1b1",
                name = "Melee @ the Made #100"
        )

        private val MATCH_0 = Match(
                result = MatchResult.LOSE,
                opponent = IMYT,
                tournament = SUPER_GATOR_GAMES
        )

        private val MATCH_1 = Match(
                result = MatchResult.LOSE,
                opponent = IMYT,
                tournament = MELEE_AT_THE_MADE_100
        )

        private val HEAD_TO_HEAD = HeadToHead(
                opponent = IMYT,
                player = CHARLEZARD,
                losses = 2,
                wins = 0,
                matches = listOf(MATCH_0, MATCH_1)
        )

        private val EMPTY_HEAD_TO_HEAD = HeadToHead(
                opponent = AERIUS,
                player = SNAP,
                losses = 0,
                wins = 0
        )
    }

    @Before
    override fun setUp() {
        super.setUp()

        headToHeadRepository = HeadToHeadRepositoryImpl(schedulers, serverApi)
    }

    @Test
    fun testGetHeadToHead() {
        val headToHead = headToHeadRepository.getHeadToHead(NORCAL, CHARLEZARD.id, IMYT.id)
                .blockingGet()

        assertNotNull(headToHead)
        assertEquals(IMYT, headToHead.opponent)
        assertEquals(CHARLEZARD, headToHead.player)
        assertEquals(2, headToHead.losses)
        assertEquals(0, headToHead.wins)
        assertEquals(2, headToHead.matches?.size)

        assertEquals(MATCH_1, headToHead.matches?.get(0))
        assertEquals(MATCH_0, headToHead.matches?.get(1))
    }

    @Test
    fun testGetHeadToHeadWithEmptyHeadToHead() {
        serverApi.headToHead = EMPTY_HEAD_TO_HEAD
        val headToHead = headToHeadRepository.getHeadToHead(NORCAL, CHARLEZARD.id, IMYT.id)
                .blockingGet()

        assertNotNull(headToHead)
        assertEquals(AERIUS, headToHead.opponent)
        assertEquals(SNAP, headToHead.player)
        assertEquals(0, headToHead.losses)
        assertEquals(0, headToHead.wins)
        assertTrue(headToHead.matches.isNullOrEmpty())
    }

    private class ServerApiOverride(
            internal var headToHead: HeadToHead? = HEAD_TO_HEAD
    ) : AbsServerApi() {
        override fun getHeadToHead(region: Region, playerId: String,
                opponentId: String): Single<HeadToHead> {
            val headToHead = this.headToHead

            return if (headToHead == null) {
                Single.error(NullPointerException())
            } else {
                Single.just(headToHead)
            }
        }
    }

}
