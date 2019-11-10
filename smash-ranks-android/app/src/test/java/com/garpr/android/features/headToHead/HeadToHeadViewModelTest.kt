package com.garpr.android.features.headToHead

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
import com.garpr.android.misc.Timber
import com.garpr.android.repositories.HeadToHeadRepository
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.inject
import org.robolectric.RobolectricTestRunner
import java.util.Date

@RunWith(RobolectricTestRunner::class)
class HeadToHeadViewModelTest : BaseTest() {

    private lateinit var viewModel: HeadToHeadViewModel

    private val headToHeadRepository = HeadToHeadRepositoryOverride()
    protected val timber: Timber by inject()

    companion object {
        private val CHARLEZARD: AbsPlayer = LitePlayer(
                id = "587a951dd2994e15c7dea9fe",
                name = "Charlezard"
        )

        private val IMYT: AbsPlayer = LitePlayer(
                id = "5877eb55d2994e15c7dea98b",
                name = "Imyt"
        )

        private val MIKKUZ: AbsPlayer = LitePlayer(
                id = "583a4a15d2994e0577b05c74",
                name = "mikkuz"
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

        private val EMPTY_HEAD_TO_HEAD = HeadToHead(
                opponent = IMYT,
                player = CHARLEZARD,
                losses = 0,
                wins = 0
        )

        private val HEAD_TO_HEAD = HeadToHead(
                opponent = MIKKUZ,
                player = SNAP,
                losses = 2,
                wins = 1,
                matches = listOf(
                        Match(
                                result = MatchResult.LOSE,
                                opponent = MIKKUZ,
                                tournament = MELEE_AT_THE_MADE_100
                        ),
                        Match(
                                result = MatchResult.LOSE,
                                opponent = MIKKUZ,
                                tournament = MELEE_AT_THE_MADE_100
                        ),
                        Match(
                                result = MatchResult.WIN,
                                opponent = MIKKUZ,
                                tournament = SUPER_GATOR_GAMES
                        )
                )
        )

        private val NORCAL = Region(
                displayName = "Norcal",
                id = "norcal",
                endpoint = Endpoint.GAR_PR
        )
    }

    override fun setUp() {
        super.setUp()

        viewModel = HeadToHeadViewModel(headToHeadRepository, timber)
    }

    @Test
    fun testFetchHeadToHead() {
        var state: HeadToHeadViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        viewModel.fetchHeadToHead(NORCAL, CHARLEZARD.id, IMYT.id)
        assertEquals(false, state?.hasError)
        assertEquals(false, state?.isFetching)
        assertEquals(2, state?.list?.size)

        val winsLosses = state?.list?.get(0) as? HeadToHeadViewModel.ListItem.WinsLosses
        assertNotNull(winsLosses)
        assertEquals(CHARLEZARD, winsLosses?.winsLosses?.player)
        assertEquals(0, winsLosses?.winsLosses?.playerWins)
        assertEquals(IMYT, winsLosses?.winsLosses?.opponent)
        assertEquals(0, winsLosses?.winsLosses?.opponentWins)

        assertTrue(state?.list?.get(1) is HeadToHeadViewModel.ListItem.NoMatches)
    }

    @Test
    fun testFetchHeadToHeadWithEmptyHeadToHead() {
        var state: HeadToHeadViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        headToHeadRepository.headToHead = EMPTY_HEAD_TO_HEAD
        viewModel.fetchHeadToHead(NORCAL, SNAP.id, MIKKUZ.id)
        assertEquals(false, state?.hasError)
        assertEquals(false, state?.isFetching)
        assertEquals(4, state?.list?.size)

        val winsLosses = state?.list?.get(0) as? HeadToHeadViewModel.ListItem.WinsLosses
        assertNotNull(winsLosses)
        assertEquals(SNAP, winsLosses?.winsLosses?.player)
        assertEquals(1, winsLosses?.winsLosses?.playerWins)
        assertEquals(MIKKUZ, winsLosses?.winsLosses?.opponent)
        assertEquals(2, winsLosses?.winsLosses?.opponentWins)

        var tournament = state?.list?.get(1) as? HeadToHeadViewModel.ListItem.Tournament
        assertNotNull(tournament)
        assertEquals(MELEE_AT_THE_MADE_100, tournament)

        var match = state?.list?.get(2) as? HeadToHeadViewModel.ListItem.Match
        assertNotNull(match)
        assertEquals(MatchResult.LOSE, match?.match?.result)
        assertEquals(SNAP, match?.match?.player)
        assertEquals(MIKKUZ, match?.match?.opponent)

        match = state?.list?.get(3) as? HeadToHeadViewModel.ListItem.Match
        assertNotNull(match)
        assertEquals(MatchResult.LOSE, match?.match?.result)
        assertEquals(SNAP, match?.match?.player)
        assertEquals(MIKKUZ, match?.match?.opponent)

        tournament = state?.list?.get(4) as? HeadToHeadViewModel.ListItem.Tournament
        assertNotNull(tournament)
        assertEquals(SUPER_GATOR_GAMES, tournament)

        match = state?.list?.get(5) as? HeadToHeadViewModel.ListItem.Match
        assertNotNull(match)
        assertEquals(MatchResult.WIN, match?.match?.result)
        assertEquals(SNAP, match?.match?.player)
        assertEquals(MIKKUZ, match?.match?.opponent)
    }

    private class HeadToHeadRepositoryOverride(
            internal var headToHead: HeadToHead? = HEAD_TO_HEAD
    ) : HeadToHeadRepository {

        override fun getHeadToHead(region: Region, playerId: String, opponentId: String): Single<HeadToHead> {
            val headToHead = this.headToHead

            return if (headToHead == null) {
                Single.error(NullPointerException())
            } else {
                Single.just(headToHead)
            }
        }

    }

}
