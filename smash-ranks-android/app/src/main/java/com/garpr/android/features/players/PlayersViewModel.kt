package com.garpr.android.features.players

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.PlayersBundle
import com.garpr.android.data.models.Region
import com.garpr.android.features.common.viewModels.BaseViewModel
import com.garpr.android.misc.Searchable
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.misc.Timber
import com.garpr.android.repositories.PlayersRepository
import javax.inject.Inject

class PlayersViewModel @Inject constructor(
        private val playersRepository: PlayersRepository,
        private val threadUtils: ThreadUtils,
        private val timber: Timber
) : BaseViewModel(), Searchable {

    private val _stateLiveData = MutableLiveData<State>()
    val stateLiveData: LiveData<State> = _stateLiveData

    private var state: State = State()
        set(value) {
            field = value
            _stateLiveData.postValue(value)
        }

    companion object {
        private const val TAG = "PlayersViewModel"
    }

    fun fetchPlayers(region: Region) {
        state = state.copy(isFetching = true)

        disposables.add(playersRepository.getPlayers(region)
                .subscribe({
                    val isEmpty = it.players.isNullOrEmpty()

                    state = state.copy(
                            isEmpty = isEmpty,
                            isFetching = false,
                            hasError = false,
                            showSearchIcon = !isEmpty,
                            searchResults = null,
                            playersBundle = it
                    )
                }, {
                    timber.e(TAG, "Error fetching players", it)

                    state = state.copy(
                            isEmpty = false,
                            isFetching = false,
                            hasError = true,
                            showSearchIcon = false,
                            searchResults = null,
                            playersBundle = null
                    )
                }))
    }

    override fun search(query: String?) {
        threadUtils.background.submit {
            val results = search(query, state.playersBundle?.players)
            state = state.copy(searchResults = results)
        }
    }

    @WorkerThread
    private fun search(query: String?, players: List<AbsPlayer>?): List<AbsPlayer>? {
        if (query.isNullOrBlank() || players.isNullOrEmpty()) {
            return players
        }

        val trimmedQuery = query.trim()

        return players.filter { player ->
            player.name.contains(trimmedQuery, true)
        }
    }

    data class State(
            val isEmpty: Boolean = false,
            val isFetching: Boolean = false,
            val hasError: Boolean = false,
            val showSearchIcon: Boolean = false,
            val searchResults: List<AbsPlayer>? = null,
            val playersBundle: PlayersBundle? = null
    )

}
