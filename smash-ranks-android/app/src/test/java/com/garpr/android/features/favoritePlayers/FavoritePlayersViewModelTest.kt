package com.garpr.android.features.favoritePlayers

import com.garpr.android.BaseTest
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.LitePlayer
import com.garpr.android.data.models.Region
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.repositories.FavoritePlayersRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.inject
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class FavoritePlayersViewModelTest : BaseTest() {

    private lateinit var viewModel: FavoritePlayersViewModel

    protected val favoritePlayersRepository: FavoritePlayersRepository by inject()
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

        viewModel = FavoritePlayersViewModel(favoritePlayersRepository, threadUtils)
    }

    @Test
    fun testAddFavoritePlayerCausesUpdate() {
        var state: FavoritePlayersViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        favoritePlayersRepository.addPlayer(MIKKUZ, NORCAL)
        assertEquals(1, state?.favoritePlayers?.size)
        assertEquals(true, state?.favoritePlayers?.contains(MIKKUZ))
    }

    @Test
    fun testAddAndRemoveFavoritePlayersCausesUpdates() {
        var state: FavoritePlayersViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        favoritePlayersRepository.addPlayer(IMYT, NORCAL)
        assertEquals(1, state?.favoritePlayers?.size)
        assertEquals(true, state?.favoritePlayers?.contains(IMYT))

        favoritePlayersRepository.addPlayer(SNAP, NORCAL)
        assertEquals(2, state?.favoritePlayers?.size)
        assertEquals(true, state?.favoritePlayers?.contains(IMYT))
        assertEquals(true, state?.favoritePlayers?.contains(SNAP))

        favoritePlayersRepository.removePlayer(IMYT)
        assertEquals(1, state?.favoritePlayers?.size)
        assertEquals(true, state?.favoritePlayers?.contains(SNAP))
        assertEquals(false, state?.favoritePlayers?.contains(IMYT))
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
        assertEquals(false, state?.searchResults?.contains(CHARLEZARD))
        assertEquals(true, state?.searchResults?.contains(IMYT))
        assertEquals(true, state?.searchResults?.contains(MIKKUZ))
        assertEquals(false, state?.searchResults?.contains(SNAP))
        assertEquals(true, state?.searchResults?.contains(TWO_SAINT))
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
