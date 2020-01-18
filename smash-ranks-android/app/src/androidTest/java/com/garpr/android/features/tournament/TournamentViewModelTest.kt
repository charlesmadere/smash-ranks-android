package com.garpr.android.features.tournament

import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.FullTournament
import com.garpr.android.data.models.LitePlayer
import com.garpr.android.data.models.Region
import com.garpr.android.data.models.SimpleDate
import com.garpr.android.data.models.TournamentsBundle
import com.garpr.android.features.common.viewModels.BaseViewModelTest
import com.garpr.android.features.tournament.TournamentViewModel.MatchListItem
import com.garpr.android.features.tournament.TournamentViewModel.PlayerListItem
import com.garpr.android.misc.Schedulers
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.misc.Timber
import com.garpr.android.repositories.IdentityRepository
import com.garpr.android.repositories.TournamentsRepository
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.koin.test.inject
import java.util.Date

class TournamentViewModelTest : BaseViewModelTest() {

    private lateinit var viewModel: TournamentViewModel
    private val tournamentsRepository = TournamentsRepositoryOverride()

    protected val identityRepository: IdentityRepository by inject()
    protected val schedulers: Schedulers by inject()
    protected val threadUtils: ThreadUtils by inject()
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

        private val CHARLEZARD_VS_IMYT = FullTournament.Match(
                loser = CHARLEZARD,
                winner = IMYT,
                excluded = true,
                matchId = 0
        )

        private val CHARLEZARD_VS_MIKKUZ = FullTournament.Match(
                loser = CHARLEZARD,
                winner = MIKKUZ,
                excluded = true,
                matchId = 1
        )

        private val IMYT_VS_MIKKUZ = FullTournament.Match(
                loser = IMYT,
                winner = MIKKUZ,
                excluded = false,
                matchId = 2
        )

        private val IMYT_VS_SNAP_0 = FullTournament.Match(
                loser = SNAP,
                winner = IMYT,
                excluded = false,
                matchId = 3
        )

        private val IMYT_VS_SNAP_1 = FullTournament.Match(
                loser = IMYT,
                winner = SNAP,
                excluded = false,
                matchId = 5
        )

        private val IMYT_VS_SNAP_2 = FullTournament.Match(
                loser = SNAP,
                winner = IMYT,
                excluded = false,
                matchId = 6
        )

        private val MIKKUZ_VS_SNAP = FullTournament.Match(
                loser = MIKKUZ,
                winner = SNAP,
                excluded = false,
                matchId = 4
        )

        private val NORCAL = Region(
                displayName = "Norcal",
                id = "norcal",
                endpoint = Endpoint.GAR_PR
        )

        private val EMPTY_TOURNAMENT = FullTournament(
                date = SimpleDate(Date(1573200000000L)),
                id = "15F762B16BB7233F100CC40C",
                name = "Empty Tournament"
        )

        private val MELEE_AT_THE_MADE_119 = FullTournament(
                regions = listOf(NORCAL.id),
                date = SimpleDate(Date(1573804800000L)),
                id = "422DE0CE9D47B321DA63D918",
                name = "Melee @ the Made #119",
                players = listOf(CHARLEZARD, IMYT, MIKKUZ, SNAP),
                matches = listOf(CHARLEZARD_VS_IMYT, CHARLEZARD_VS_MIKKUZ, IMYT_VS_MIKKUZ,
                        IMYT_VS_SNAP_0, MIKKUZ_VS_SNAP, IMYT_VS_SNAP_1, IMYT_VS_SNAP_2)
        )
    }

    @Before
    override fun setUp() {
        super.setUp()

        viewModel = TournamentViewModel(identityRepository, schedulers, threadUtils, timber,
                tournamentsRepository)
    }

    @Test
    fun testFetchTournamentWithEmptyTournament() {
        var state: TournamentViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        tournamentsRepository.fullTournament = EMPTY_TOURNAMENT
        viewModel.initialize(NORCAL, EMPTY_TOURNAMENT.id)
        viewModel.fetchTournament()

        assertNotNull(state)
        assertEquals(false, state?.hasError)
        assertEquals(false, state?.isFetching)
        assertEquals(false, state?.isRefreshEnabled)
        assertEquals(true, state?.showMatchesEmpty)
        assertEquals(true, state?.showPlayersEmpty)
        assertEquals(false, state?.showSearchIcon)
        assertFalse(state?.subtitleText.isNullOrBlank())
        assertFalse(state?.titleText.isNullOrBlank())
        assertEquals(EMPTY_TOURNAMENT.id, state?.tournament?.id)
        assertTrue(state?.matches.isNullOrEmpty())
        assertTrue(state?.matchesSearchResults.isNullOrEmpty())
        assertTrue(state?.players.isNullOrEmpty())
        assertTrue(state?.playersSearchResults.isNullOrEmpty())
    }

    @Test
    fun testFetchTournamentWithMeleeAtTheMade119() {
        var state: TournamentViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        tournamentsRepository.fullTournament = MELEE_AT_THE_MADE_119
        viewModel.initialize(NORCAL, MELEE_AT_THE_MADE_119.id)
        viewModel.fetchTournament()

        assertNotNull(state)
        assertEquals(false, state?.hasError)
        assertEquals(false, state?.isFetching)
        assertEquals(false, state?.isRefreshEnabled)
        assertEquals(false, state?.showMatchesEmpty)
        assertEquals(false, state?.showPlayersEmpty)
        assertEquals(true, state?.showSearchIcon)
        assertFalse(state?.subtitleText.isNullOrBlank())
        assertFalse(state?.titleText.isNullOrBlank())
        assertEquals(MELEE_AT_THE_MADE_119.id, state?.tournament?.id)
        assertEquals(7, state?.matches?.size)
        assertEquals(CHARLEZARD_VS_IMYT, (state?.matches?.get(0) as MatchListItem.Match).match)
        assertEquals(CHARLEZARD_VS_MIKKUZ, (state?.matches?.get(1) as MatchListItem.Match).match)
        assertEquals(IMYT_VS_MIKKUZ, (state?.matches?.get(2) as MatchListItem.Match).match)
        assertEquals(IMYT_VS_SNAP_0, (state?.matches?.get(3) as MatchListItem.Match).match)
        assertEquals(MIKKUZ_VS_SNAP, (state?.matches?.get(4) as MatchListItem.Match).match)
        assertEquals(IMYT_VS_SNAP_1, (state?.matches?.get(5) as MatchListItem.Match).match)
        assertEquals(IMYT_VS_SNAP_2, (state?.matches?.get(6) as MatchListItem.Match).match)
        assertTrue(state?.matchesSearchResults.isNullOrEmpty())
        assertEquals(4, state?.players?.size)
        assertEquals(CHARLEZARD, (state?.players?.get(0) as PlayerListItem.Player).player)
        assertEquals(IMYT, (state?.players?.get(1) as PlayerListItem.Player).player)
        assertEquals(MIKKUZ, (state?.players?.get(2) as PlayerListItem.Player).player)
        assertEquals(SNAP, (state?.players?.get(3) as PlayerListItem.Player).player)
        assertTrue(state?.playersSearchResults.isNullOrEmpty())
    }

    @Test
    fun testFetchTournamentWithoutCallingInitialize() {
        var throwable: Throwable? = null

        try {
            viewModel.fetchTournament()
        } catch (t: Throwable) {
            throwable = t
        }

        assertNotNull(throwable)
    }

    @Test
    fun testSearchMeleeAtTheMade119() {
        var state: TournamentViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        tournamentsRepository.fullTournament = MELEE_AT_THE_MADE_119
        viewModel.initialize(NORCAL, MELEE_AT_THE_MADE_119.id)
        viewModel.fetchTournament()

        viewModel.search("k")
        assertEquals(3, state?.matchesSearchResults?.size)
        assertEquals(CHARLEZARD_VS_MIKKUZ, (state?.matchesSearchResults?.get(0) as MatchListItem.Match).match)
        assertEquals(IMYT_VS_MIKKUZ, (state?.matchesSearchResults?.get(1) as MatchListItem.Match).match)
        assertEquals(MIKKUZ_VS_SNAP, (state?.matchesSearchResults?.get(2) as MatchListItem.Match).match)
        assertEquals(1, state?.playersSearchResults?.size)
        assertEquals(MIKKUZ, (state?.playersSearchResults?.get(0) as PlayerListItem.Player).player)
    }

    @Test
    fun testSearchMeleeAtTheMade119WithBlankString() {
        var state: TournamentViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        tournamentsRepository.fullTournament = MELEE_AT_THE_MADE_119
        viewModel.initialize(NORCAL, MELEE_AT_THE_MADE_119.id)
        viewModel.fetchTournament()

        viewModel.search(" ")
        assertEquals(7, state?.matches?.size)
        assertNull(state?.matchesSearchResults)
        assertEquals(4, state?.players?.size)
        assertNull(state?.playersSearchResults)
    }

    @Test
    fun testSearchMeleeAtTheMade119WithEmptyString() {
        var state: TournamentViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        tournamentsRepository.fullTournament = MELEE_AT_THE_MADE_119
        viewModel.initialize(NORCAL, MELEE_AT_THE_MADE_119.id)
        viewModel.fetchTournament()

        viewModel.search("")
        assertEquals(7, state?.matches?.size)
        assertNull(state?.matchesSearchResults)
        assertEquals(4, state?.players?.size)
        assertNull(state?.playersSearchResults)
    }

    @Test
    fun testSearchMeleeAtTheMade119WithNullString() {
        var state: TournamentViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        tournamentsRepository.fullTournament = MELEE_AT_THE_MADE_119
        viewModel.initialize(NORCAL, MELEE_AT_THE_MADE_119.id)
        viewModel.fetchTournament()

        viewModel.search(null)
        assertEquals(7, state?.matches?.size)
        assertNull(state?.matchesSearchResults)
        assertEquals(4, state?.players?.size)
        assertNull(state?.playersSearchResults)
    }

    private class TournamentsRepositoryOverride(
            internal var fullTournament: FullTournament? = null
    ) : TournamentsRepository {
        override fun getTournament(region: Region, tournamentId: String): Single<FullTournament> {
            val fullTournament = this.fullTournament

            return if (fullTournament == null) {
                Single.error(NullPointerException())
            } else {
                return Single.just(fullTournament)
            }
        }

        override fun getTournaments(region: Region): Single<TournamentsBundle> {
            throw NotImplementedError()
        }
    }
    
}
