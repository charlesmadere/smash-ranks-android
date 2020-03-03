package com.garpr.android.features.favoritePlayers

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.features.common.viewModels.BaseViewModel
import com.garpr.android.misc.Schedulers
import com.garpr.android.repositories.FavoritePlayersRepository

class AddOrRemovePlayerFromFavoritesViewModel(
        private val favoritePlayersRepository: FavoritePlayersRepository,
        private val schedulers: Schedulers
) : BaseViewModel() {

    private var player: FavoritePlayer? = null

    private var state: State = State()
        set(value) {
            field = value
            _stateLiveData.postValue(value)
        }

    private val _stateLiveData = MutableLiveData(state)
    val stateLiveData: LiveData<State> = _stateLiveData

    fun addOrRemoveFromFavorites() {
        val player = checkNotNull(this.player) { "initialize() was not called" }

        if (state.isFavorited) {
            favoritePlayersRepository.removePlayer(player, player.region)
        } else {
            favoritePlayersRepository.addPlayer(player, player.region)
        }
    }

    fun initialize(player: FavoritePlayer) {
        this.player = player
        initListeners()
    }

    private fun initListeners() {
        disposables.add(favoritePlayersRepository.playersObservable
                .subscribeOn(schedulers.background)
                .observeOn(schedulers.background)
                .subscribe { players ->
                    refreshIsFavorited(players)
                })
    }

    @WorkerThread
    private fun refreshIsFavorited(favoritePlayers: List<FavoritePlayer>?) {
        val player = checkNotNull(this.player) { "initialize() was not called" }

        val isFavorited = if (favoritePlayers.isNullOrEmpty()) {
            false
        } else {
            favoritePlayers.any { it == player }
        }

        state = state.copy(
                isFavorited = isFavorited,
                isFetching = false
        )
    }

    data class State(
            val isFavorited: Boolean = false,
            val isFetching: Boolean = true
    )

}
