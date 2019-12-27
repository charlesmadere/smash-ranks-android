package com.garpr.android.features.favoritePlayers

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.features.common.viewModels.BaseViewModel
import com.garpr.android.misc.Refreshable
import com.garpr.android.misc.Schedulers
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.repositories.FavoritePlayersRepository

class AddOrRemovePlayerFromFavoritesViewModel(
        private val favoritePlayersRepository: FavoritePlayersRepository,
        private val schedulers: Schedulers,
        private val threadUtils: ThreadUtils
) : BaseViewModel(), Refreshable {

    private var player: FavoritePlayer? = null

    private val _stateLiveData = MutableLiveData<State>()
    val stateLiveData: LiveData<State> = _stateLiveData

    private var state: State = State()
        set(value) {
            field = value
            _stateLiveData.postValue(value)
        }

    init {
        initListeners()
    }

    fun addToFavorites() {
        val player = checkNotNull(this.player) { "initialize() was not called" }
        favoritePlayersRepository.addPlayer(player, player.region)
    }

    fun initialize(player: FavoritePlayer) {
        this.player = player
        refresh()
    }

    private fun initListeners() {
        disposables.add(favoritePlayersRepository.playersObservable
                .observeOn(schedulers.background)
                .subscribe { players ->
                    refreshIsAlreadyFavorited(players)
                })
    }

    override fun refresh() {
        threadUtils.background.submit {
            refreshIsAlreadyFavorited(favoritePlayersRepository.players)
        }
    }

    @WorkerThread
    private fun refreshIsAlreadyFavorited(players: List<FavoritePlayer>?) {
        val player = this.player

        val isAlreadyFavorited = if (player == null || players.isNullOrEmpty()) {
            false
        } else {
            players.any { favoritePlayer ->
                favoritePlayer.id.equals(player.id, ignoreCase = true)
            }
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
