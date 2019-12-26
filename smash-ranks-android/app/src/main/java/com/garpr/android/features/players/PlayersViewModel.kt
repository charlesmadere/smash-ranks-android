package com.garpr.android.features.players

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.garpr.android.data.models.Region
import com.garpr.android.features.common.viewModels.BaseViewModel
import com.garpr.android.misc.PlayerListBuilder
import com.garpr.android.misc.PlayerListBuilder.PlayerListItem
import com.garpr.android.misc.Schedulers
import com.garpr.android.misc.Searchable
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.misc.Timber
import com.garpr.android.repositories.IdentityRepository
import com.garpr.android.repositories.PlayersRepository

class PlayersViewModel(
        private val identityRepository: IdentityRepository,
        private val playerListBuilder: PlayerListBuilder,
        private val playersRepository: PlayersRepository,
        private val schedulers: Schedulers,
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

    init {
        initListeners()
    }

    fun fetchPlayers(region: Region) {
        state = state.copy(isFetching = true)

        disposables.add(playersRepository.getPlayers(region)
                .observeOn(schedulers.background)
                .subscribe({ bundle ->
                    val list = playerListBuilder.create(bundle)

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
        disposables.add(identityRepository.identityObservable
                .subscribe {
                    refreshIdentity()
                })
    }

    @WorkerThread
    private fun refreshIdentity() {
        val list = playerListBuilder.refresh(state.list)
        val searchResults = playerListBuilder.refresh(state.searchResults)

        state = state.copy(
                list = list,
                searchResults = searchResults
        )
    }

    override fun search(query: String?) {
        threadUtils.background.submit {
            val searchResults = playerListBuilder.search(query, state.list)
            state = state.copy(searchResults = searchResults)
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
