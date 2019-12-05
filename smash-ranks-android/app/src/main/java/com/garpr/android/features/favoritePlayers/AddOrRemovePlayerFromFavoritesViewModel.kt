package com.garpr.android.features.favoritePlayers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.features.common.viewModels.BaseViewModel
import com.garpr.android.misc.Refreshable
import com.garpr.android.repositories.FavoritePlayersRepository

class AddOrRemovePlayerFromFavoritesViewModel(
        private val favoritePlayersRepository: FavoritePlayersRepository
) : BaseViewModel(), FavoritePlayersRepository.OnFavoritePlayersChangeListener, Refreshable {

    private var player: FavoritePlayer? = null

    private val _stateLiveData = MutableLiveData<State>()
    val stateLiveData: LiveData<State> = _stateLiveData

    private var state: State = State()
        set(value) {
            field = value
            _stateLiveData.postValue(value)
        }

    init {
        favoritePlayersRepository.addListener(this)
    }

    fun addToFavorites() {
        val player = checkNotNull(this.player) { "initialize() was not called" }
        favoritePlayersRepository.addPlayer(player, player.region)
    }

    fun initialize(player: FavoritePlayer) {
        this.player = player
        refresh()
    }

    override fun onCleared() {
        favoritePlayersRepository.removeListener(this)
        super.onCleared()
    }

    override fun onFavoritePlayersChange(favoritePlayersRepository: FavoritePlayersRepository) {
        refresh()
    }

    override fun refresh() {
        val player = this.player

        val isAlreadyFavorited = if (player == null) {
            false
        } else {
            player in favoritePlayersRepository
        }

        state = state.copy(isAlreadyFavorited = isAlreadyFavorited)
    }

    fun removeFromFavorites() {
        val player = checkNotNull(this.player) { "initialize() was not called" }
        favoritePlayersRepository.removePlayer(player)
    }

    data class State(
            val isAlreadyFavorited: Boolean = false
    )

}
