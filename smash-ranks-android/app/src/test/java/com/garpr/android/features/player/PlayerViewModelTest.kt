package com.garpr.android.features.player

import com.garpr.android.BaseTest
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.FullPlayer
import com.garpr.android.data.models.LitePlayer
import com.garpr.android.data.models.MatchesBundle
import com.garpr.android.data.models.PlayerMatchesBundle
import com.garpr.android.data.models.Rating
import com.garpr.android.data.models.Region
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
                        wins = 2,
                        losses = 3,
                        matches = listOf(

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
