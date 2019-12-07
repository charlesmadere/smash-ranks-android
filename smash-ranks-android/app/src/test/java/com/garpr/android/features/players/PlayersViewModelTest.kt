package com.garpr.android.features.players

import com.garpr.android.BaseTest
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.FullPlayer
import com.garpr.android.data.models.LitePlayer
import com.garpr.android.data.models.PlayersBundle
import com.garpr.android.data.models.Region
import com.garpr.android.misc.PlayerListBuilder
import com.garpr.android.misc.PlayerListBuilder.PlayerListItem
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.misc.Timber
import com.garpr.android.repositories.IdentityRepository
import com.garpr.android.repositories.PlayersRepository
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.inject
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class PlayersViewModelTest : BaseTest() {

    private val playersRepository = PlayersRepositoryOverride()
    private lateinit var viewModel: PlayersViewModel

    protected val identityRepository: IdentityRepository by inject()
    protected val playerListBuilder: PlayerListBuilder by inject()
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

        private val EMPTY_PLAYERS_BUNDLE = PlayersBundle()

        private val PLAYERS_BUNDLE = PlayersBundle(
                players = listOf(CHARLEZARD, IMYT, MIKKUZ, SNAP)
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

        viewModel = PlayersViewModel(identityRepository, playerListBuilder, playersRepository,
                threadUtils, timber)
    }

    @Test
    fun testFetchPlayers() {
        var state: PlayersViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        viewModel.fetchPlayers(NORCAL)
        assertEquals(false, state?.hasError)
        assertEquals(false, state?.isEmpty)
        assertEquals(false, state?.isFetching)
        assertEquals(true, state?.showSearchIcon)
        assertNotNull(state?.list)
        assertNull(state?.searchResults)

        // TODO
    }

    @Test
    fun testFetchPlayersWithEmptyPlayersBundle() {
        playersRepository.playersBundle = EMPTY_PLAYERS_BUNDLE
        var state: PlayersViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        viewModel.fetchPlayers(NORCAL)
        assertEquals(false, state?.hasError)
        assertEquals(true, state?.isEmpty)
        assertEquals(false, state?.isFetching)
        assertEquals(false, state?.showSearchIcon)
        assertNull(state?.list)
        assertNull(state?.searchResults)
    }

    @Test
    fun testSearch() {
        var state: PlayersViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        viewModel.fetchPlayers(NORCAL)
        viewModel.search("a")

        assertNotNull(state?.searchResults)
        assertEquals(4, state?.searchResults?.size)

        assertTrue(state?.searchResults?.get(0) is PlayerListItem.Divider.Letter)
        var letter = state?.searchResults?.get(0) as PlayerListItem.Divider.Letter
        assertEquals("C", letter.letter)

        assertTrue(state?.searchResults?.get(1) is PlayerListItem.Player)
        var player = state?.searchResults?.get(1) as PlayerListItem.Player
        assertEquals(CHARLEZARD, player.player)

        assertTrue(state?.searchResults?.get(2) is PlayerListItem.Divider.Letter)
        letter = state?.searchResults?.get(2) as PlayerListItem.Divider.Letter
        assertEquals("S", letter.letter)

        assertTrue(state?.searchResults?.get(3) is PlayerListItem.Player)
        player = state?.searchResults?.get(3) as PlayerListItem.Player
        assertEquals(SNAP, player.player)
    }

    @Test
    fun testSearchWithBlankString() {
        var state: PlayersViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        viewModel.fetchPlayers(NORCAL)
        viewModel.search(" ")

        assertNull(state?.searchResults)
    }

    @Test
    fun testSearchWithEmptyString() {
        var state: PlayersViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        viewModel.fetchPlayers(NORCAL)
        viewModel.search("")

        assertNull(state?.searchResults)
    }

    @Test
    fun testSearchWithNullString() {
        var state: PlayersViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        viewModel.fetchPlayers(NORCAL)
        viewModel.search(null)

        assertNull(state?.searchResults)
    }

    private class PlayersRepositoryOverride(
            internal var playersBundle: PlayersBundle? = PLAYERS_BUNDLE
    ) : PlayersRepository {

        override fun getPlayer(region: Region, playerId: String): Single<FullPlayer> {
            throw NotImplementedError()
        }

        override fun getPlayers(region: Region): Single<PlayersBundle> {
            val bundle = playersBundle

            return if (bundle == null) {
                Single.error(NullPointerException())
            } else {
                Single.just(bundle)
            }
        }

    }

}
