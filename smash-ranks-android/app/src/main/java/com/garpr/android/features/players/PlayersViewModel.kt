package com.garpr.android.features.players

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.garpr.android.data.models.Region
import com.garpr.android.features.common.viewModels.BaseViewModel
import com.garpr.android.misc.PlayerListBuilder
import com.garpr.android.misc.PlayerListBuilder.PlayerListItem
import com.garpr.android.misc.Searchable
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.misc.Timber
import com.garpr.android.repositories.IdentityRepository
import com.garpr.android.repositories.PlayersRepository

class PlayersViewModel(
        private val identityRepository: IdentityRepository,
        private val playerListBuilder: PlayerListBuilder,
        private val playersRepository: PlayersRepository,
        private val threadUtils: ThreadUtils,
        private val timber: Timber
) : BaseViewModel(), IdentityRepository.OnIdentityChangeListener, Searchable {

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

    init {
        initListeners()
    }

    fun fetchPlayers(region: Region) {
        state = state.copy(isFetching = true)

        disposables.add(playersRepository.getPlayers(region)
                .subscribe({
                    val list = playerListBuilder.create(it)

                    state = state.copy(
                            hasError = false,
                            isEmpty = list.isNullOrEmpty(),
                            isFetching = false,
                            showSearchIcon = !list.isNullOrEmpty(),
                            list = list,
                            searchResults = null
                    )
                }, {
                    timber.e(TAG, "Error fetching players", it)

                    state = state.copy(
                            hasError = true,
                            isEmpty = false,
                            isFetching = false,
                            showSearchIcon = false,
                            list = null,
                            searchResults = null
                    )
                }))
    }

    private fun initListeners() {
        identityRepository.addListener(this)
    }

    override fun onCleared() {
        identityRepository.removeListener(this)
        super.onCleared()
    }

    override fun onIdentityChange(identityRepository: IdentityRepository) {
        threadUtils.background.submit {
            state = state.copy(
                    list = playerListBuilder.refresh(state.list),
                    searchResults = playerListBuilder.refresh(state.searchResults)
            )
        }
    }

    override fun search(query: String?) {
        threadUtils.background.submit {
            val results = playerListBuilder.search(query, state.list)
            state = state.copy(searchResults = results)
        }
    }

    data class State(
            val hasError: Boolean = false,
            val isEmpty: Boolean = false,
            val isFetching: Boolean = false,
            val showSearchIcon: Boolean = false,
            val list: List<PlayerListItem>? = null,
            val searchResults: List<PlayerListItem>? = null
    )

}
