package com.garpr.android.features.favoritePlayers

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.features.common.viewModels.BaseViewModel
import com.garpr.android.misc.Searchable
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.repositories.FavoritePlayersRepository

class FavoritePlayersViewModel(
        private val favoritePlayersRepository: FavoritePlayersRepository,
        private val threadUtils: ThreadUtils
) : BaseViewModel(), FavoritePlayersRepository.OnFavoritePlayersChangeListener, Searchable {

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

    fun fetchFavoritePlayers() {
        threadUtils.background.submit {
            state = state.copy(
                    favoritePlayers = favoritePlayersRepository.players,
                    searchResults = null
            )
        }
    }

    override fun onCleared() {
        favoritePlayersRepository.removeListener(this)
        super.onCleared()
    }

    override fun onFavoritePlayersChange(favoritePlayersRepository: FavoritePlayersRepository) {
        fetchFavoritePlayers()
    }

    override fun search(query: String?) {
        threadUtils.background.submit {
            val results = search(query, state.favoritePlayers)
            state = state.copy(searchResults = results)
        }
    }

    @WorkerThread
    private fun search(query: String?, favoritePlayers: List<FavoritePlayer>?): List<FavoritePlayer>? {
        if (query.isNullOrBlank() || favoritePlayers.isNullOrEmpty()) {
            return favoritePlayers
        }

        val trimmedQuery = query.trim()

        return favoritePlayers
                .filter { it.name.contains(trimmedQuery, true) }
    }

    data class State(
            val favoritePlayers: List<FavoritePlayer>? = null,
            val searchResults: List<FavoritePlayer>? = null
    )

}
