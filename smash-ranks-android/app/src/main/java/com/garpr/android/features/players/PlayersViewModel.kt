package com.garpr.android.features.players

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.data.models.Optional
import com.garpr.android.data.models.PlayersBundle
import com.garpr.android.data.models.Region
import com.garpr.android.extensions.takeSingle
import com.garpr.android.features.common.viewModels.BaseViewModel
import com.garpr.android.misc.PlayerListBuilder
import com.garpr.android.misc.PlayerListBuilder.PlayerListItem
import com.garpr.android.misc.Schedulers
import com.garpr.android.misc.Searchable
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.misc.Timber
import com.garpr.android.repositories.IdentityRepository
import com.garpr.android.repositories.PlayersRepository
import io.reactivex.Single
import io.reactivex.functions.BiFunction

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

        disposables.add(Single.zip(playersRepository.getPlayers(region),
                identityRepository.identityObservable.takeSingle(),
                BiFunction<PlayersBundle, Optional<FavoritePlayer>,
                        Pair<PlayersBundle, Optional<FavoritePlayer>>> { t1, t2 ->
                            Pair(t1, t2)
                        })
                .subscribeOn(schedulers.background)
                .observeOn(schedulers.background)
                .subscribe({ (bundle, identity) ->
                    val list = playerListBuilder.create(bundle, identity.orNull())

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
                .subscribeOn(schedulers.background)
                .observeOn(schedulers.background)
                .subscribe { identity ->
                    refreshIdentity(identity.orNull())
                })
    }

    @WorkerThread
    private fun refreshIdentity(identity: AbsPlayer?) {
        val list = playerListBuilder.refresh(state.list, identity)
        val searchResults = playerListBuilder.refresh(state.searchResults, identity)

        state = state.copy(
                list = list,
                searchResults = searchResults
        )
    }

    override fun search(query: String?) {
        threadUtils.background.submit {
            val searchResults = playerListBuilder.search(state.list, query)
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
