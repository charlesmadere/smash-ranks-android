package com.garpr.android.features.player

import com.garpr.android.BaseTest
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.AbsTournament
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.FullPlayer
import com.garpr.android.data.models.LitePlayer
import com.garpr.android.data.models.LiteTournament
import com.garpr.android.data.models.Match
import com.garpr.android.data.models.MatchResult
import com.garpr.android.data.models.MatchesBundle
import com.garpr.android.data.models.PlayerMatchesBundle
import com.garpr.android.data.models.Rating
import com.garpr.android.data.models.Region
import com.garpr.android.data.models.SimpleDate
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.misc.Timber
import com.garpr.android.repositories.FavoritePlayersRepository
import com.garpr.android.repositories.IdentityRepository
import com.garpr.android.repositories.PlayerMatchesRepository
import com.garpr.android.sync.roster.SmashRosterStorage
import com.garpr.android.sync.roster.SmashRosterSyncManager
import io.reactivex.Single
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.Date
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class PlayerViewModelTest : BaseTest() {

    private val playerMatchesRepository = PlayerMatchesRepositoryOverride()
    private lateinit var viewModel: PlayerViewModel

    @Inject
    protected lateinit var favoritePlayersRepository: FavoritePlayersRepository

    @Inject
    protected lateinit var identityRepository: IdentityRepository

    @Inject
    protected lateinit var smashRosterStorage: SmashRosterStorage

    @Inject
    protected lateinit var smashRosterSyncManager: SmashRosterSyncManager

    @Inject
    protected lateinit var threadUtils: ThreadUtils

    @Inject
    protected lateinit var timber: Timber

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
                date = SimpleDate(Date(1567148400000)),
                id = "5d6a1784d2994e1a9c3dd665",
                name = "Melee @ the Made #112"
        )

        private val TOURNAMENT_PEOPLES_TUESDAYS_14: AbsTournament = LiteTournament(
                date = SimpleDate(Date(1565679600000)),
                id = "5d53bd4ed2994e22acc95a92",
                name = "The People's Tuesdays #14"
        )

        private val TOURNAMENT_PEOPLES_TUESDAYS_16: AbsTournament = LiteTournament(
                date = SimpleDate(Date(1566889200000)),
                id = "5d661e33d2994e1a9c3dd64f",
                name = "The People's Tuesdays #16"
        )

        private val FULL_PLAYER_CHARLEZARD = FullPlayer(
                id = ABS_PLAYER_CHARLEZARD.id,
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
                fullPlayer = FULL_PLAYER_CHARLEZARD,
                matchesBundle = null
        )

        private val PLAYER_MATCHES_BUNDLE = PlayerMatchesBundle(
                fullPlayer = FULL_PLAYER_CHARLEZARD,
                matchesBundle = MatchesBundle(
                        player = ABS_PLAYER_CHARLEZARD,
                        wins = 1,
                        losses = 4,
                        matches = listOf(
                                Match(
                                        result = MatchResult.LOSE,
                                        opponent = ABS_PLAYER_IMYT,
                                        tournament = TOURNAMENT_MADE_112
                                ),
                                Match(
                                        result = MatchResult.WIN,
                                        opponent = ABS_PLAYER_PIMP_JONG_ILLEST,
                                        tournament = TOURNAMENT_MADE_112
                                ),
                                Match(
                                        result = MatchResult.LOSE,
                                        opponent = ABS_PLAYER_JOEJOE,
                                        tournament = TOURNAMENT_MADE_112
                                ),
                                Match(
                                        result = MatchResult.EXCLUDED,
                                        opponent = ABS_PLAYER_IMYT,
                                        tournament = TOURNAMENT_PEOPLES_TUESDAYS_16
                                ),
                                Match(
                                        result = MatchResult.EXCLUDED,
                                        opponent = ABS_PLAYER_PIMP_JONG_ILLEST,
                                        tournament = TOURNAMENT_PEOPLES_TUESDAYS_16
                                ),
                                Match(
                                        result = MatchResult.LOSE,
                                        opponent = ABS_PLAYER_JOEJOE,
                                        tournament = TOURNAMENT_PEOPLES_TUESDAYS_14
                                ),
                                Match(
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
        testAppComponent.inject(this)

        viewModel = PlayerViewModel(favoritePlayersRepository, identityRepository,
                playerMatchesRepository, smashRosterStorage, smashRosterSyncManager,
                threadUtils, timber)
    }

    @Test
    fun testFetchPlayerWithInitializingDoesNotThrowException() {
        viewModel.initialize(NORCAL, CHARLEZARD_ID)

        var state: PlayerViewModel.State? = null
        var throwable: Throwable? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        try {
            viewModel.fetchPlayer()
        } catch (t: Throwable) {
            throwable = t
        }

        assertNotNull(state)
        assertNull(throwable)
    }

    @Test
    fun testFetchPlayerWithoutInitializingThrowsException() {
        var state: PlayerViewModel.State? = null
        var throwable: Throwable? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        try {
            viewModel.fetchPlayer()
        } catch (t: Throwable) {
            throwable = t
        }

        assertTrue(state?.list.isNullOrEmpty())
        assertNotNull(throwable)
    }

    @Test
    fun testSearchWithEmptyString() {
        viewModel.initialize(NORCAL, CHARLEZARD_ID)

        viewModel.stateLiveData.observeForever {

        }

        viewModel.search("")
    }

    @Test
    fun testSearchWithNullString() {
        viewModel.initialize(NORCAL, CHARLEZARD_ID)

        viewModel.search(null)
    }

    private class PlayerMatchesRepositoryOverride(
            internal var playerMatchesBundle: PlayerMatchesBundle? = PLAYER_MATCHES_BUNDLE
    ) : PlayerMatchesRepository {

        override fun getPlayerAndMatches(region: Region, playerId: String): Single<PlayerMatchesBundle> {
            val bundle = playerMatchesBundle

            return if (bundle == null) {
                Single.error(NullPointerException("playerMatchesBundle is null"))
            } else {
                Single.just(bundle)
            }
        }

    }

}
