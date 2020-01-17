package com.garpr.android.features.favoritePlayers

import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.data.models.LitePlayer
import com.garpr.android.data.models.Region
import com.garpr.android.features.common.BaseViewModelTest
import com.garpr.android.misc.Schedulers
import com.garpr.android.repositories.FavoritePlayersRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.koin.test.inject

class AddOrRemovePlayerFromFavoritesViewModelTest : BaseViewModelTest() {

    private lateinit var viewModel: AddOrRemovePlayerFromFavoritesViewModel

    protected val favoritePlayersRepository: FavoritePlayersRepository by inject()
    protected val schedulers: Schedulers by inject()

    companion object {
        private val CHARLEZARD: AbsPlayer = LitePlayer(
                id = "587a951dd2994e15c7dea9fe",
                name = "Charlezard"
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

        viewModel = AddOrRemovePlayerFromFavoritesViewModel(favoritePlayersRepository, schedulers)
    }

    @Test
    fun testAddOrRemoveFromFavoritesDoesAdd() {
        var state: AddOrRemovePlayerFromFavoritesViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        var players: List<FavoritePlayer>? = null

        favoritePlayersRepository.playersObservable.subscribe {
            players = it
        }

        assertNotNull(state)
        assertEquals(false, state?.isFavorited)
        assertEquals(true, state?.isFetching)
        assertEquals(false, players?.contains(CHARLEZARD))

        val player = FavoritePlayer(
                id = CHARLEZARD.id,
                name = CHARLEZARD.name,
                region = NORCAL
        )

        viewModel.initialize(player)
        assertNotNull(state)
        assertEquals(false, state?.isFavorited)
        assertEquals(false, state?.isFetching)
        assertEquals(false, players?.contains(CHARLEZARD))

        viewModel.addOrRemoveFromFavorites()
        assertNotNull(state)
        assertEquals(true, state?.isFavorited)
        assertEquals(false, state?.isFetching)
        assertEquals(true, players?.contains(CHARLEZARD))
    }

    @Test
    fun testAddOrRemoveFromFavoritesDoesRemove() {
        favoritePlayersRepository.addPlayer(CHARLEZARD, NORCAL)

        var state: AddOrRemovePlayerFromFavoritesViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        var players: List<FavoritePlayer>? = null

        favoritePlayersRepository.playersObservable.subscribe {
            players = it
        }

        assertNotNull(state)
        assertEquals(false, state?.isFavorited)
        assertEquals(true, state?.isFetching)
        assertEquals(true, players?.contains(CHARLEZARD))

        val player = FavoritePlayer(
                id = CHARLEZARD.id,
                name = CHARLEZARD.name,
                region = NORCAL
        )

        viewModel.initialize(player)
        assertNotNull(state)
        assertEquals(true, state?.isFavorited)
        assertEquals(false, state?.isFetching)
        assertEquals(true, players?.contains(CHARLEZARD))

        viewModel.addOrRemoveFromFavorites()
        assertNotNull(state)
        assertEquals(false, state?.isFavorited)
        assertEquals(false, state?.isFetching)
        assertEquals(false, players?.contains(CHARLEZARD))
    }

    @Test
    fun testAddOrRemoveFromFavoritesWithoutInitialize() {
        var throwable: Throwable? = null

        try {
            viewModel.addOrRemoveFromFavorites()
        } catch (t: Throwable) {
            throwable = t
        }

        assertNotNull(throwable)
    }

    @Test
    fun testInitialState() {
        var state: AddOrRemovePlayerFromFavoritesViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        assertNotNull(state)
        assertEquals(false, state?.isFavorited)
        assertEquals(true, state?.isFetching)
    }

}
