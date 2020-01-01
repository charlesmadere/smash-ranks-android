package com.garpr.android.features.favoritePlayers

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.garpr.android.data.models.Optional
import com.garpr.android.features.common.viewModels.BaseViewModel
import com.garpr.android.misc.Schedulers
import com.garpr.android.misc.Searchable
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.repositories.FavoritePlayersRepository
import com.garpr.android.repositories.IdentityRepository
import io.reactivex.functions.BiFunction
import com.garpr.android.data.models.FavoritePlayer as GarPrFavoritePlayer

class FavoritePlayersViewModel(
        private val favoritePlayersRepository: FavoritePlayersRepository,
        private val identityRepository: IdentityRepository,
        private val schedulers: Schedulers,
        private val threadUtils: ThreadUtils
) : BaseViewModel(), Searchable {

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

    @WorkerThread
    private fun createListItems(players: List<GarPrFavoritePlayer>?,
            identity: GarPrFavoritePlayer?): List<ListItem>? {
        return if (players.isNullOrEmpty()) {
            null
        } else {
            players.map { player ->
                ListItem.FavoritePlayer(
                        isIdentity = player == identity,
                        player = player
                )
            }
        }
    }

    private fun initListeners() {
        disposables.add(favoritePlayersRepository.playersObservable
                .withLatestFrom(identityRepository.identityObservable,
                        BiFunction<List<GarPrFavoritePlayer>, Optional<GarPrFavoritePlayer>,
                                Pair<List<GarPrFavoritePlayer>, Optional<GarPrFavoritePlayer>>> { t1, t2 ->
                                    Pair(t1, t2)
                                })
                .subscribeOn(schedulers.background)
                .observeOn(schedulers.background)
                .subscribe { (players, identity) ->
                    refreshFavoritePlayers(players, identity.item)
                })
    }

    @WorkerThread
    private fun refreshFavoritePlayers(players: List<GarPrFavoritePlayer>,
            identity: GarPrFavoritePlayer?) {
        val list = createListItems(players, identity)

        state = state.copy(
                isEmpty = list.isNullOrEmpty(),
                isFetching = false,
                list = list,
                searchResults = null
        )
    }

    override fun search(query: String?) {
        threadUtils.background.submit {
            val searchResults = search(query, state.list)
            state = state.copy(searchResults = searchResults)
        }
    }

    @WorkerThread
    private fun search(query: String?, list: List<ListItem>?): List<ListItem>? {
        if (query.isNullOrBlank() || list.isNullOrEmpty()) {
            return null
        }

        val trimmedQuery = query.trim()

        val results = list.filterIsInstance(ListItem.FavoritePlayer::class.java)
                .filter { it.player.name.contains(trimmedQuery, true) }

        return if (results.isEmpty()) {
            listOf(ListItem.NoResults(trimmedQuery))
        } else {
            results
        }
    }

    data class State(
            val isEmpty: Boolean = false,
            val isFetching: Boolean = true,
            val list: List<ListItem>? = null,
            val searchResults: List<ListItem>? = null
    )

    sealed class ListItem {
        abstract val listId: Long

        class FavoritePlayer(
                val isIdentity: Boolean,
                val player: GarPrFavoritePlayer
        ) : ListItem() {
            override val listId: Long = player.hashCode().toLong()
        }

        class NoResults(
                val query: String
        ) : ListItem() {
            override val listId: Long = Long.MAX_VALUE - 1L
        }
    }

}
