package com.garpr.android.features.player

import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.AbsTournament
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.FullPlayer
import com.garpr.android.data.models.LitePlayer
import com.garpr.android.data.models.LiteTournament
import com.garpr.android.data.models.MatchResult
import com.garpr.android.data.models.MatchesBundle
import com.garpr.android.data.models.PlayerMatchesBundle
import com.garpr.android.data.models.Rating
import com.garpr.android.data.models.Region
import com.garpr.android.data.models.SimpleDate
import com.garpr.android.data.models.TournamentMatch
import com.garpr.android.features.common.BaseViewModelTest
import com.garpr.android.features.player.PlayerViewModel.ListItem
import com.garpr.android.misc.Schedulers
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.misc.Timber
import com.garpr.android.repositories.FavoritePlayersRepository
import com.garpr.android.repositories.IdentityRepository
import com.garpr.android.repositories.PlayerMatchesRepository
import com.garpr.android.sync.roster.SmashRosterStorage
import com.garpr.android.sync.roster.SmashRosterSyncManager
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

class PlayerViewModelTest : BaseViewModelTest() {

    private val playerMatchesRepository = PlayerMatchesRepositoryOverride()
    private lateinit var viewModel: PlayerViewModel

    protected val favoritePlayersRepository: FavoritePlayersRepository by inject()
    protected val identityRepository: IdentityRepository by inject()
    protected val schedulers: Schedulers by inject()
    protected val smashRosterStorage: SmashRosterStorage by inject()
    protected val smashRosterSyncManager: SmashRosterSyncManager by inject()
    protected val threadUtils: ThreadUtils by inject()
    protected val timber: Timber by inject()

    companion object {
        private const val CHARLEZARD_ID = "587a951dd2994e15c7dea9fe"

        private val ABS_PLAYER_CHARLEZARD: AbsPlayer = LitePlayer(
                id = CHARLEZARD_ID,
                name = "Charlezard"
        )

        private val ABS_PLAYER_JOEJOE: AbsPlayer = LitePlayer(
                id = "588999c5d2994e713ad63c7b",
                name = "joejoe"
        )

        private val ABS_PLAYER_IMYT: AbsPlayer = LitePlayer(
                id = "5877eb55d2994e15c7dea98b",
                name = "Imyt"
        )

        private val ABS_PLAYER_PIMP_JONG_ILLEST: AbsPlayer = LitePlayer(
                id = "588999c5d2994e713ad63c6f",
                name = "Pimp Jong Illest"
        )

        private val TOURNAMENT_MADE_112: AbsTournament = LiteTournament(
                date = SimpleDate(Date(1567148400000L)),
                id = "5d6a1784d2994e1a9c3dd665",
                name = "Melee @ the Made #112"
        )

        private val TOURNAMENT_PEOPLES_TUESDAYS_14: AbsTournament = LiteTournament(
                date = SimpleDate(Date(1565679600000L)),
                id = "5d53bd4ed2994e22acc95a92",
                name = "The People's Tuesdays #14"
        )

        private val TOURNAMENT_PEOPLES_TUESDAYS_16: AbsTournament = LiteTournament(
                date = SimpleDate(Date(1566889200000L)),
                id = "5d661e33d2994e1a9c3dd64f",
                name = "The People's Tuesdays #16"
        )

        private val FULL_PLAYER_CHARLEZARD = FullPlayer(
                id = CHARLEZARD_ID,
                name = ABS_PLAYER_CHARLEZARD.name,
                aliases = listOf(
                        "Charles"
                ),
                regions = listOf(
                        "norcal"
                ),
                ratings = mapOf(
                        "norcal" to Rating(
                                mu = 27.421812f,
                                sigma = 0.874929f
                        )
                )
        )

        private val EMPTY_PLAYER_MATCHES_BUNDLE = PlayerMatchesBundle(
                fullPlayer = FULL_PLAYER_CHARLEZARD
        )

        private val PLAYER_MATCHES_BUNDLE = PlayerMatchesBundle(
                fullPlayer = FULL_PLAYER_CHARLEZARD,
                matchesBundle = MatchesBundle(
                        player = ABS_PLAYER_CHARLEZARD,
                        wins = 1,
                        losses = 4,
                        matches = listOf(
                                TournamentMatch(
                                        result = MatchResult.LOSE,
                                        opponent = ABS_PLAYER_IMYT,
                                        tournament = TOURNAMENT_MADE_112
                                ),
                                TournamentMatch(
                                        result = MatchResult.WIN,
                                        opponent = ABS_PLAYER_PIMP_JONG_ILLEST,
                                        tournament = TOURNAMENT_MADE_112
                                ),
                                TournamentMatch(
                                        result = MatchResult.LOSE,
                                        opponent = ABS_PLAYER_JOEJOE,
                                        tournament = TOURNAMENT_MADE_112
                                ),
                                TournamentMatch(
                                        result = MatchResult.EXCLUDED,
                                        opponent = ABS_PLAYER_IMYT,
                                        tournament = TOURNAMENT_PEOPLES_TUESDAYS_16
                                ),
                                TournamentMatch(
                                        result = MatchResult.EXCLUDED,
                                        opponent = ABS_PLAYER_PIMP_JONG_ILLEST,
                                        tournament = TOURNAMENT_PEOPLES_TUESDAYS_16
                                ),
                                TournamentMatch(
                                        result = MatchResult.LOSE,
                                        opponent = ABS_PLAYER_JOEJOE,
                                        tournament = TOURNAMENT_PEOPLES_TUESDAYS_14
                                ),
                                TournamentMatch(
                                        result = MatchResult.LOSE,
                                        opponent = ABS_PLAYER_IMYT,
                                        tournament = TOURNAMENT_PEOPLES_TUESDAYS_14
                                )
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

        viewModel = PlayerViewModel(favoritePlayersRepository, identityRepository,
                playerMatchesRepository, schedulers, smashRosterStorage, smashRosterSyncManager,
                threadUtils, timber)
    }

    @Test
    fun testAddOrRemoveFromFavorites() {
        viewModel.initialize(NORCAL, CHARLEZARD_ID)

        var state: PlayerViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        viewModel.fetchPlayer()
        assertEquals(false, state?.isFavorited)

        viewModel.addOrRemoveFromFavorites()
        assertEquals(true, state?.isFavorited)

        viewModel.addOrRemoveFromFavorites()
        assertEquals(false, state?.isFavorited)

        favoritePlayersRepository.addPlayer(ABS_PLAYER_CHARLEZARD, NORCAL)
        assertEquals(true, state?.isFavorited)

        favoritePlayersRepository.removePlayer(ABS_PLAYER_CHARLEZARD, NORCAL)
        assertEquals(false, state?.isFavorited)
    }

    @Test
    fun testFetchPlayer() {
        viewModel.initialize(NORCAL, CHARLEZARD_ID)

        var state: PlayerViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        viewModel.fetchPlayer()
        assertEquals(false, state?.hasError)
        assertEquals(false, state?.isFavorited)
        assertEquals(false, state?.isFetching)
        assertEquals(true, state?.showSearchIcon)
        assertFalse(state?.subtitleText.isNullOrBlank())
        assertFalse(state?.titleText.isNullOrBlank())
        assertEquals(11, state?.list?.size)
        assertNull(state?.searchResults)
        assertEquals(PLAYER_MATCHES_BUNDLE, state?.playerMatchesBundle)
        assertNull(state?.smashCompetitor)

        assertTrue(state?.list?.get(0) is ListItem.Player)

        assertTrue(state?.list?.get(1) is ListItem.Tournament)
        var tournament = state?.list?.get(1) as ListItem.Tournament
        assertEquals(TOURNAMENT_MADE_112, tournament.tournament)

        assertTrue(state?.list?.get(2) is ListItem.Match)
        var match = state?.list?.get(2) as ListItem.Match
        assertEquals(ABS_PLAYER_IMYT, match.match.opponent)
        assertEquals(TOURNAMENT_MADE_112, match.match.tournament)

        assertTrue(state?.list?.get(3) is ListItem.Match)
        match = state?.list?.get(3) as ListItem.Match
        assertEquals(ABS_PLAYER_PIMP_JONG_ILLEST, match.match.opponent)
        assertEquals(TOURNAMENT_MADE_112, match.match.tournament)

        assertTrue(state?.list?.get(4) is ListItem.Match)
        match = state?.list?.get(4) as ListItem.Match
        assertEquals(ABS_PLAYER_JOEJOE, match.match.opponent)
        assertEquals(TOURNAMENT_MADE_112, match.match.tournament)

        assertTrue(state?.list?.get(5) is ListItem.Tournament)
        tournament = state?.list?.get(5) as ListItem.Tournament
        assertEquals(TOURNAMENT_PEOPLES_TUESDAYS_16, tournament.tournament)

        assertTrue(state?.list?.get(6) is ListItem.Match)
        match = state?.list?.get(6) as ListItem.Match
        assertEquals(ABS_PLAYER_IMYT, match.match.opponent)
        assertEquals(TOURNAMENT_PEOPLES_TUESDAYS_16, match.match.tournament)

        assertTrue(state?.list?.get(7) is ListItem.Match)
        match = state?.list?.get(7) as ListItem.Match
        assertEquals(ABS_PLAYER_PIMP_JONG_ILLEST, match.match.opponent)
        assertEquals(TOURNAMENT_PEOPLES_TUESDAYS_16, match.match.tournament)

        assertTrue(state?.list?.get(8) is ListItem.Tournament)
        tournament = state?.list?.get(8) as ListItem.Tournament
        assertEquals(TOURNAMENT_PEOPLES_TUESDAYS_14, tournament.tournament)

        assertTrue(state?.list?.get(9) is ListItem.Match)
        match = state?.list?.get(9) as ListItem.Match
        assertEquals(ABS_PLAYER_JOEJOE, match.match.opponent)
        assertEquals(TOURNAMENT_PEOPLES_TUESDAYS_14, match.match.tournament)

        assertTrue(state?.list?.get(10) is ListItem.Match)
        match = state?.list?.get(10) as ListItem.Match
        assertEquals(ABS_PLAYER_IMYT, match.match.opponent)
        assertEquals(TOURNAMENT_PEOPLES_TUESDAYS_14, match.match.tournament)
    }

    @Test
    fun testFetchPlayerWithEmptyBundle() {
        playerMatchesRepository.playerMatchesBundle = EMPTY_PLAYER_MATCHES_BUNDLE
        viewModel.initialize(NORCAL, CHARLEZARD_ID)

        var state: PlayerViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        viewModel.fetchPlayer()
        assertEquals(false, state?.hasError)
        assertEquals(false, state?.isFavorited)
        assertEquals(false, state?.isFetching)
        assertEquals(false, state?.showSearchIcon)
        assertFalse(state?.subtitleText.isNullOrBlank())
        assertFalse(state?.titleText.isNullOrBlank())
        assertEquals(2, state?.list?.size)
        assertNull(state?.searchResults)
        assertEquals(EMPTY_PLAYER_MATCHES_BUNDLE, state?.playerMatchesBundle)
        assertNull(state?.smashCompetitor)

        assertTrue(state?.list?.get(0) is ListItem.Player)
        assertTrue(state?.list?.get(1) is ListItem.NoMatches)
    }

    @Test
    fun testFetchPlayerWithNullBundle() {
        playerMatchesRepository.playerMatchesBundle = null
        viewModel.initialize(NORCAL, CHARLEZARD_ID)

        var state: PlayerViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        viewModel.fetchPlayer()
        assertEquals(true, state?.hasError)
        assertEquals(false, state?.isFavorited)
        assertEquals(false, state?.isFetching)
        assertEquals(false, state?.showSearchIcon)
        assertFalse(state?.subtitleText.isNullOrBlank())
        assertTrue(state?.titleText.isNullOrBlank())
        assertNull(state?.list)
        assertNull(state?.searchResults)
        assertNull(state?.playerMatchesBundle)
        assertNull(state?.smashCompetitor)
    }

    @Test
    fun testFetchPlayerWithoutInitializeDoesThrowException() {
        var throwable: Throwable? = null

        try {
            viewModel.fetchPlayer()
        } catch (t: Throwable) {
            throwable = t
        }

        assertNotNull(throwable)
    }

    @Test
    fun testInitialize() {
        var state: PlayerViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        viewModel.initialize(NORCAL, CHARLEZARD_ID)
        assertEquals(false, state?.isFavorited)
        assertFalse(state?.subtitleText.isNullOrBlank())
        assertTrue(state?.titleText.isNullOrBlank())
        assertNull(state?.smashCompetitor)
    }

    @Test
    fun testSearchWithBlankString() {
        viewModel.initialize(NORCAL, CHARLEZARD_ID)

        var state: PlayerViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        viewModel.fetchPlayer()
        viewModel.search(" ")

        assertNull(state?.searchResults)
    }

    @Test
    fun testSearchWithEmptyString() {
        viewModel.initialize(NORCAL, CHARLEZARD_ID)

        var state: PlayerViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        viewModel.fetchPlayer()
        viewModel.search("")

        assertNull(state?.searchResults)
    }

    @Test
    fun testSearchWithImyt() {
        viewModel.initialize(NORCAL, CHARLEZARD_ID)

        var state: PlayerViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        viewModel.fetchPlayer()
        viewModel.search("imyt")

        assertNotNull(state?.searchResults)
        assertEquals(6, state?.searchResults?.size)

        var tournament = state?.searchResults?.get(0) as ListItem.Tournament
        assertEquals(TOURNAMENT_MADE_112, tournament.tournament)

        var match = state?.searchResults?.get(1) as ListItem.Match
        assertEquals(ABS_PLAYER_IMYT, match.match.opponent)
        assertEquals(TOURNAMENT_MADE_112, match.match.tournament)

        tournament = state?.searchResults?.get(2) as ListItem.Tournament
        assertEquals(TOURNAMENT_PEOPLES_TUESDAYS_16, tournament.tournament)

        match = state?.searchResults?.get(3) as ListItem.Match
        assertEquals(ABS_PLAYER_IMYT, match.match.opponent)
        assertEquals(TOURNAMENT_PEOPLES_TUESDAYS_16, match.match.tournament)

        tournament = state?.searchResults?.get(4) as ListItem.Tournament
        assertEquals(TOURNAMENT_PEOPLES_TUESDAYS_14, tournament.tournament)

        match = state?.searchResults?.get(5) as ListItem.Match
        assertEquals(ABS_PLAYER_IMYT, match.match.opponent)
        assertEquals(TOURNAMENT_PEOPLES_TUESDAYS_14, tournament.tournament)
    }

    @Test
    fun testSearchWithJoe() {
        viewModel.initialize(NORCAL, CHARLEZARD_ID)

        var state: PlayerViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        viewModel.fetchPlayer()
        viewModel.search("Joe")

        assertNotNull(state?.searchResults)
        assertEquals(4, state?.searchResults?.size)

        var tournament = state?.searchResults?.get(0) as ListItem.Tournament
        assertEquals(TOURNAMENT_MADE_112, tournament.tournament)

        var match = state?.searchResults?.get(1) as ListItem.Match
        assertEquals(ABS_PLAYER_JOEJOE, match.match.opponent)
        assertEquals(TOURNAMENT_MADE_112, match.match.tournament)

        tournament = state?.searchResults?.get(2) as ListItem.Tournament
        assertEquals(TOURNAMENT_PEOPLES_TUESDAYS_14, tournament.tournament)

        match = state?.searchResults?.get(3) as ListItem.Match
        assertEquals(ABS_PLAYER_JOEJOE, match.match.opponent)
        assertEquals(TOURNAMENT_PEOPLES_TUESDAYS_14, match.match.tournament)
    }

    @Test
    fun testSearchWithNullString() {
        viewModel.initialize(NORCAL, CHARLEZARD_ID)

        var state: PlayerViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        viewModel.fetchPlayer()
        viewModel.search(null)

        assertNull(state?.searchResults)
    }

    private class PlayerMatchesRepositoryOverride(
            internal var playerMatchesBundle: PlayerMatchesBundle? = PLAYER_MATCHES_BUNDLE
    ) : PlayerMatchesRepository {

        override fun getPlayerAndMatches(region: Region, playerId: String): Single<PlayerMatchesBundle> {
            val playerMatchesBundle = this.playerMatchesBundle

            return if (playerMatchesBundle == null) {
                Single.error(NullPointerException())
            } else {
                Single.just(playerMatchesBundle)
            }
        }

    }

}
