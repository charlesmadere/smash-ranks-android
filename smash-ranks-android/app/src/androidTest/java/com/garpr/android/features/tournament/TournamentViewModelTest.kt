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
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.koin.core.inject
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
        var matchesState: TournamentViewModel.MatchesState? = null
        var playersState: TournamentViewModel.PlayersState? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        viewModel.matchesStateLiveData.observeForever {
            matchesState = it
        }

        viewModel.playersStateLiveData.observeForever {
            playersState = it
        }

        tournamentsRepository.fullTournament = EMPTY_TOURNAMENT
        viewModel.initialize(NORCAL, EMPTY_TOURNAMENT.id)
        viewModel.fetchTournament()

        assertNotNull(state)
        assertEquals(false, state?.hasError)
        assertEquals(false, state?.isFetching)
        assertEquals(false, state?.isRefreshEnabled)
        assertEquals(false, state?.showSearchIcon)
        assertFalse(state?.subtitleText.isNullOrBlank())
        assertFalse(state?.titleText.isNullOrBlank())
        assertEquals(EMPTY_TOURNAMENT.id, state?.tournament?.id)

        assertNotNull(matchesState)
        assertEquals(true, matchesState?.isEmpty)
        assertTrue(matchesState?.list.isNullOrEmpty())
        assertNull(matchesState?.searchResults)

        assertNotNull(playersState)
        assertEquals(true, playersState?.isEmpty)
        assertTrue(playersState?.list.isNullOrEmpty())
        assertNull(playersState?.searchResults)
    }

    @Test
    fun testFetchTournamentWithMeleeAtTheMade119() {
        var state: TournamentViewModel.State? = null
        var matchesState: TournamentViewModel.MatchesState? = null
        var playersState: TournamentViewModel.PlayersState? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        viewModel.matchesStateLiveData.observeForever {
            matchesState = it
        }

        viewModel.playersStateLiveData.observeForever {
            playersState = it
        }

        tournamentsRepository.fullTournament = MELEE_AT_THE_MADE_119
        viewModel.initialize(NORCAL, MELEE_AT_THE_MADE_119.id)
        viewModel.fetchTournament()

        assertNotNull(state)
        assertEquals(false, state?.hasError)
        assertEquals(false, state?.isFetching)
        assertEquals(false, state?.isRefreshEnabled)
        assertEquals(true, state?.showSearchIcon)
        assertFalse(state?.subtitleText.isNullOrBlank())
        assertFalse(state?.titleText.isNullOrBlank())
        assertEquals(MELEE_AT_THE_MADE_119.id, state?.tournament?.id)

        assertNotNull(matchesState)
        assertEquals(false, matchesState?.isEmpty)
        assertEquals(7, matchesState?.list?.size)
        assertEquals(CHARLEZARD_VS_IMYT, (matchesState?.list?.get(0) as MatchListItem.Match).match)
        assertEquals(CHARLEZARD_VS_MIKKUZ, (matchesState?.list?.get(1) as MatchListItem.Match).match)
        assertEquals(IMYT_VS_MIKKUZ, (matchesState?.list?.get(2) as MatchListItem.Match).match)
        assertEquals(IMYT_VS_SNAP_0, (matchesState?.list?.get(3) as MatchListItem.Match).match)
        assertEquals(MIKKUZ_VS_SNAP, (matchesState?.list?.get(4) as MatchListItem.Match).match)
        assertEquals(IMYT_VS_SNAP_1, (matchesState?.list?.get(5) as MatchListItem.Match).match)
        assertEquals(IMYT_VS_SNAP_2, (matchesState?.list?.get(6) as MatchListItem.Match).match)
        assertNull(matchesState?.searchResults)

        assertNotNull(playersState)
        assertEquals(false, playersState?.isEmpty)
        assertEquals(4, playersState?.list?.size)
        assertEquals(CHARLEZARD, (playersState?.list?.get(0) as PlayerListItem.Player).player)
        assertEquals(IMYT, (playersState?.list?.get(1) as PlayerListItem.Player).player)
        assertEquals(MIKKUZ, (playersState?.list?.get(2) as PlayerListItem.Player).player)
        assertEquals(SNAP, (playersState?.list?.get(3) as PlayerListItem.Player).player)
        assertNull(playersState?.searchResults)
    }

    @Test
    fun testFetchTournamentWithoutCallingInitialize() {
        assertThrows(Throwable::class.java) {
            viewModel.fetchTournament()
        }
    }

    @Test
    fun testSearchMeleeAtTheMade119WithK() {
        var matchesState: TournamentViewModel.MatchesState? = null
        var playersState: TournamentViewModel.PlayersState? = null

        viewModel.matchesStateLiveData.observeForever {
            matchesState = it
        }

        viewModel.playersStateLiveData.observeForever {
            playersState = it
        }

        tournamentsRepository.fullTournament = MELEE_AT_THE_MADE_119
        viewModel.initialize(NORCAL, MELEE_AT_THE_MADE_119.id)
        viewModel.fetchTournament()
        viewModel.search("k")

        assertNotNull(matchesState)
        assertEquals(3, matchesState?.searchResults?.size)
        assertEquals(CHARLEZARD_VS_MIKKUZ, (matchesState?.searchResults?.get(0) as MatchListItem.Match).match)
        assertEquals(IMYT_VS_MIKKUZ, (matchesState?.searchResults?.get(1) as MatchListItem.Match).match)
        assertEquals(MIKKUZ_VS_SNAP, (matchesState?.searchResults?.get(2) as MatchListItem.Match).match)

        assertNotNull(playersState)
        assertEquals(1, playersState?.searchResults?.size)
        assertEquals(MIKKUZ, (playersState?.searchResults?.get(0) as PlayerListItem.Player).player)
    }

    @Test
    fun testSearchMeleeAtTheMade119WithBlankString() {
        var matchesState: TournamentViewModel.MatchesState? = null
        var playersState: TournamentViewModel.PlayersState? = null

        viewModel.matchesStateLiveData.observeForever {
            matchesState = it
        }

        viewModel.playersStateLiveData.observeForever {
            playersState = it
        }

        tournamentsRepository.fullTournament = MELEE_AT_THE_MADE_119
        viewModel.initialize(NORCAL, MELEE_AT_THE_MADE_119.id)
        viewModel.fetchTournament()
        viewModel.search(" ")

        assertNotNull(matchesState)
        assertEquals(7, matchesState?.list?.size)
        assertNull(matchesState?.searchResults)

        assertNotNull(playersState)
        assertEquals(4, playersState?.list?.size)
        assertNull(playersState?.searchResults)
    }

    @Test
    fun testSearchMeleeAtTheMade119WithEmptyString() {
        var matchesState: TournamentViewModel.MatchesState? = null
        var playersState: TournamentViewModel.PlayersState? = null

        viewModel.matchesStateLiveData.observeForever {
            matchesState = it
        }

        viewModel.playersStateLiveData.observeForever {
            playersState = it
        }

        tournamentsRepository.fullTournament = MELEE_AT_THE_MADE_119
        viewModel.initialize(NORCAL, MELEE_AT_THE_MADE_119.id)
        viewModel.fetchTournament()
        viewModel.search("")

        assertNotNull(matchesState)
        assertEquals(7, matchesState?.list?.size)
        assertNull(matchesState?.searchResults)

        assertNotNull(playersState)
        assertEquals(4, playersState?.list?.size)
        assertNull(playersState?.searchResults)
    }

    @Test
    fun testSearchMeleeAtTheMade119WithNullString() {
        var matchesState: TournamentViewModel.MatchesState? = null
        var playersState: TournamentViewModel.PlayersState? = null

        viewModel.matchesStateLiveData.observeForever {
            matchesState = it
        }

        viewModel.playersStateLiveData.observeForever {
            playersState = it
        }

        tournamentsRepository.fullTournament = MELEE_AT_THE_MADE_119
        viewModel.initialize(NORCAL, MELEE_AT_THE_MADE_119.id)
        viewModel.fetchTournament()
        viewModel.search(null)

        assertNotNull(matchesState)
        assertEquals(7, matchesState?.list?.size)
        assertNull(matchesState?.searchResults)

        assertNotNull(playersState)
        assertEquals(4, playersState?.list?.size)
        assertNull(playersState?.searchResults)
    }

    @Test
    fun testSearchMeleeAtTheMade119WithWadu() {
        var matchesState: TournamentViewModel.MatchesState? = null
        var playersState: TournamentViewModel.PlayersState? = null

        viewModel.matchesStateLiveData.observeForever {
            matchesState = it
        }

        viewModel.playersStateLiveData.observeForever {
            playersState = it
        }

        tournamentsRepository.fullTournament = MELEE_AT_THE_MADE_119
        viewModel.initialize(NORCAL, MELEE_AT_THE_MADE_119.id)
        viewModel.fetchTournament()
        viewModel.search("wadu")

        assertNotNull(matchesState)
        assertEquals(1, matchesState?.searchResults?.size)
        assertEquals(true, matchesState?.searchResults?.get(0) is MatchListItem.NoResults)
        assertEquals("wadu", (matchesState?.searchResults?.get(0) as MatchListItem.NoResults).query)

        assertNotNull(playersState)
        assertEquals(1, playersState?.searchResults?.size)
        assertEquals(true, playersState?.searchResults?.get(0) is PlayerListItem.NoResults)
        assertEquals("wadu", (playersState?.searchResults?.get(0) as PlayerListItem.NoResults).query)
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
