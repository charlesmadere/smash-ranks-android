package com.garpr.android.features.favoritePlayers

import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.LitePlayer
import com.garpr.android.data.models.Region
import com.garpr.android.features.common.viewModels.BaseViewModelTest
import com.garpr.android.features.favoritePlayers.FavoritePlayersViewModel.ListItem
import com.garpr.android.misc.Schedulers
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.repositories.FavoritePlayersRepository
import com.garpr.android.repositories.IdentityRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.koin.test.inject

class FavoritePlayersViewModelTest : BaseViewModelTest() {

    private lateinit var viewModel: FavoritePlayersViewModel

    protected val favoritePlayersRepository: FavoritePlayersRepository by inject()
    protected val identityRepository: IdentityRepository by inject()
    protected val schedulers: Schedulers by inject()
    protected val threadUtils: ThreadUtils by inject()

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

        private val TWO_SAINT: AbsPlayer = LitePlayer(
                id = "56d093064d7521611048599e",
                name = "2Saint"
        )

        private val NEW_JERSEY = Region(
                displayName = "New Jersey",
                id = "norcal",
                endpoint = Endpoint.NOT_GAR_PR
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

        viewModel = FavoritePlayersViewModel(favoritePlayersRepository, identityRepository,
                schedulers, threadUtils)
    }

    @Test
    fun testAddFavoritePlayerCausesUpdate() {
        var state: FavoritePlayersViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        favoritePlayersRepository.addPlayer(MIKKUZ, NORCAL)
        assertEquals(1, state?.list?.size)

        val player = state?.list?.get(0) as ListItem.Player
        assertFalse(player.isIdentity)
        assertEquals(MIKKUZ, player.player)
    }

    @Test
    fun testAddAndRemoveFavoritePlayersCausesUpdates() {
        var state: FavoritePlayersViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        favoritePlayersRepository.addPlayer(IMYT, NORCAL)
        assertEquals(1, state?.list?.size)

        var player = state?.list?.get(0) as ListItem.Player
        assertFalse(player.isIdentity)
        assertEquals(IMYT, player.player)

        favoritePlayersRepository.addPlayer(SNAP, NORCAL)
        assertEquals(2, state?.list?.size)

        player = state?.list?.get(0) as ListItem.Player
        assertEquals(IMYT, player.player)

        player = state?.list?.get(1) as ListItem.Player
        assertEquals(SNAP, player.player)

        favoritePlayersRepository.removePlayer(IMYT, NORCAL)
        assertEquals(1, state?.list?.size)

        player = state?.list?.get(0) as ListItem.Player
        assertEquals(SNAP, player.player)
    }

    @Test
    fun testInitialState() {
        var state: FavoritePlayersViewModel.State? = null
        val isFetchingStates = mutableListOf<Boolean>()

        viewModel.stateLiveData.observeForever {
            state = it
            isFetchingStates.add(it.isFetching)
        }

        assertEquals(true, state?.isEmpty)
        assertEquals(false, state?.isFetching)
        assertTrue(state?.list.isNullOrEmpty())
        assertNull(state?.searchResults)
    }

    @Test
    fun testSearch() {
        favoritePlayersRepository.addPlayer(CHARLEZARD, NORCAL)
        favoritePlayersRepository.addPlayer(IMYT, NORCAL)
        favoritePlayersRepository.addPlayer(MIKKUZ, NORCAL)
        favoritePlayersRepository.addPlayer(SNAP, NORCAL)
        favoritePlayersRepository.addPlayer(TWO_SAINT, NEW_JERSEY)

        var state: FavoritePlayersViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        viewModel.search("i")
        assertEquals(3, state?.searchResults?.size)

        var player = state?.searchResults?.get(0) as ListItem.Player
        assertEquals(IMYT, player.player)

        player = state?.searchResults?.get(1) as ListItem.Player
        assertEquals(MIKKUZ, player.player)

        player = state?.searchResults?.get(2) as ListItem.Player
        assertEquals(TWO_SAINT, player.player)

        viewModel.search("Wadu")
        assertEquals(1, state?.searchResults?.size)

        val noResults = state?.searchResults?.get(0) as ListItem.NoResults
        assertEquals("Wadu", noResults.query)
    }

    @Test
    fun testSearchWithBlankStringAndNoFavorites() {
        var state: FavoritePlayersViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        viewModel.search(" ")
        assertNull(state?.searchResults)
    }

    @Test
    fun testSearchWithEmptyStringAndNoFavorites() {
        var state: FavoritePlayersViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        viewModel.search("")
        assertNull(state?.searchResults)
    }

    @Test
    fun testSearchWithNullStringAndNoFavorites() {
        var state: FavoritePlayersViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        viewModel.search(null)
        assertNull(state?.searchResults)
    }

}
