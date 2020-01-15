package com.garpr.android.features.headToHead

import com.garpr.android.BaseViewModelTest
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.AbsTournament
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.HeadToHead
import com.garpr.android.data.models.LitePlayer
import com.garpr.android.data.models.LiteTournament
import com.garpr.android.data.models.MatchResult
import com.garpr.android.data.models.Region
import com.garpr.android.data.models.SimpleDate
import com.garpr.android.data.models.TournamentMatch
import com.garpr.android.features.headToHead.HeadToHeadViewModel.ListItem
import com.garpr.android.misc.Schedulers
import com.garpr.android.misc.Timber
import com.garpr.android.repositories.HeadToHeadRepository
import com.garpr.android.repositories.IdentityRepository
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.koin.test.inject
import java.util.Date

class HeadToHeadViewModelTest : BaseViewModelTest() {

    private val headToHeadRepository = HeadToHeadRepositoryOverride()
    private lateinit var viewModel: HeadToHeadViewModel

    protected val identityRepository: IdentityRepository by inject()
    protected val schedulers: Schedulers by inject()
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
                        TournamentMatch(
                                result = MatchResult.LOSE,
                                opponent = MIKKUZ,
                                tournament = MELEE_AT_THE_MADE_100
                        ),
                        TournamentMatch(
                                result = MatchResult.LOSE,
                                opponent = MIKKUZ,
                                tournament = MELEE_AT_THE_MADE_100
                        ),
                        TournamentMatch(
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

    @Before
    override fun setUp() {
        super.setUp()

        viewModel = HeadToHeadViewModel(headToHeadRepository, identityRepository, schedulers,
                timber)
    }

    @Test
    fun testFetchHeadToHead() {
        var state: HeadToHeadViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        viewModel.fetchHeadToHead(NORCAL, SNAP.id, MIKKUZ.id)
        assertEquals(false, state?.hasError)
        assertEquals(false, state?.isFetching)
        assertEquals(6, state?.list?.size)

        assertTrue(state?.list?.get(0) is ListItem.WinsLosses)
        val winsLosses = state?.list?.get(0) as ListItem.WinsLosses
        assertEquals(SNAP, winsLosses.winsLosses.player)
        assertEquals(1, winsLosses.winsLosses.playerWins)
        assertEquals(MIKKUZ, winsLosses.winsLosses.opponent)
        assertEquals(2, winsLosses.winsLosses.opponentWins)

        assertTrue(state?.list?.get(1) is ListItem.Tournament)
        var tournament = state?.list?.get(1) as ListItem.Tournament
        assertEquals(MELEE_AT_THE_MADE_100, tournament.tournament)

        assertTrue(state?.list?.get(2) is ListItem.Match)
        var match = state?.list?.get(2) as ListItem.Match
        assertEquals(MatchResult.LOSE, match.match.result)
        assertEquals(SNAP, match.match.player)
        assertEquals(MIKKUZ, match.match.opponent)

        assertTrue(state?.list?.get(3) is ListItem.Match)
        match = state?.list?.get(3) as ListItem.Match
        assertEquals(MatchResult.LOSE, match.match.result)
        assertEquals(SNAP, match.match.player)
        assertEquals(MIKKUZ, match.match.opponent)

        assertTrue(state?.list?.get(4) is ListItem.Tournament)
        tournament = state?.list?.get(4) as ListItem.Tournament
        assertEquals(SUPER_GATOR_GAMES, tournament.tournament)

        assertTrue(state?.list?.get(5) is ListItem.Match)
        match = state?.list?.get(5) as ListItem.Match
        assertEquals(MatchResult.WIN, match.match.result)
        assertEquals(SNAP, match.match.player)
        assertEquals(MIKKUZ, match.match.opponent)
    }

    @Test
    fun testFetchHeadToHeadProperlyUpdatesIsFetching() {
        val isFetchingList = mutableListOf<Boolean>()

        viewModel.stateLiveData.observeForever {
            isFetchingList.add(it.isFetching)
        }

        viewModel.fetchHeadToHead(NORCAL, CHARLEZARD.id, IMYT.id)
        assertEquals(2, isFetchingList.size)
        assertTrue(isFetchingList[0])
        assertFalse(isFetchingList[1])
    }

    @Test
    fun testFetchHeadToHeadWithEmptyHeadToHead() {
        var state: HeadToHeadViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        headToHeadRepository.headToHead = EMPTY_HEAD_TO_HEAD
        viewModel.fetchHeadToHead(NORCAL, CHARLEZARD.id, IMYT.id)
        assertEquals(false, state?.hasError)
        assertEquals(false, state?.isFetching)
        assertEquals(2, state?.list?.size)

        assertTrue(state?.list?.get(0) is ListItem.WinsLosses)
        val winsLosses = state?.list?.get(0) as ListItem.WinsLosses
        assertEquals(CHARLEZARD, winsLosses.winsLosses.player)
        assertEquals(0, winsLosses.winsLosses.playerWins)
        assertEquals(IMYT, winsLosses.winsLosses.opponent)
        assertEquals(0, winsLosses.winsLosses.opponentWins)

        assertTrue(state?.list?.get(1) is ListItem.NoMatches)
    }

    @Test
    fun testFetchHeadToHeadWithNullHeadToHead() {
        var state: HeadToHeadViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        headToHeadRepository.headToHead = null
        viewModel.fetchHeadToHead(NORCAL, CHARLEZARD.id, IMYT.id)
        assertEquals(true, state?.hasError)
        assertEquals(false, state?.isFetching)
        assertEquals(true, state?.list.isNullOrEmpty())
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
