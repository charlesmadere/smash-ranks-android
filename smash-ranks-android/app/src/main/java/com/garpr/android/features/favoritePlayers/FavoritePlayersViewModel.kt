package com.garpr.android.features.favoritePlayers

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.data.models.Optional
import com.garpr.android.features.common.viewModels.BaseViewModel
import com.garpr.android.misc.Schedulers
import com.garpr.android.misc.Searchable
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.repositories.FavoritePlayersRepository
import com.garpr.android.repositories.IdentityRepository
import io.reactivex.functions.BiFunction
import java.util.Collections

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
    private fun createList(players: List<FavoritePlayer>?, identity: AbsPlayer?): List<ListItem>? {
        return if (players.isNullOrEmpty()) {
            null
        } else {
            players.map { player ->
                ListItem.Player(
                        isIdentity = player == identity,
                        player = player
                )
            }
        }
    }

    private fun initListeners() {
        disposables.add(favoritePlayersRepository.playersObservable
                .withLatestFrom(identityRepository.identityObservable,
                        BiFunction<List<FavoritePlayer>, Optional<FavoritePlayer>,
                                Pair<List<FavoritePlayer>, Optional<FavoritePlayer>>> { t1, t2 ->
                                    Pair(t1, t2)
                                })
                .subscribeOn(schedulers.background)
                .observeOn(schedulers.background)
                .subscribe { (players, identity) ->
                    refreshFavoritePlayers(players, identity.orNull())
                })
    }

    @WorkerThread
    private fun refreshFavoritePlayers(players: List<FavoritePlayer>, identity: AbsPlayer?) {
        val list = createList(players, identity)

        state = state.copy(
                isEmpty = list.isNullOrEmpty(),
                isFetching = false,
                list = list,
                searchResults = null
        )
    }

    override fun search(query: String?) {
        threadUtils.background.submit {
            val results = search(query, state.list)
            state = state.copy(searchResults = results)
        }
    }

    @WorkerThread
    private fun search(query: String?, list: List<ListItem>?): List<ListItem>? {
        if (query.isNullOrBlank() || list.isNullOrEmpty()) {
            return null
        }

        val trimmedQuery = query.trim()

        val results = list
                .filterIsInstance(ListItem.Player::class.java)
                .filter { listItem ->
                    listItem.player.name.contains(trimmedQuery, ignoreCase = true)
                }

        return if (results.isEmpty()) {
            Collections.singletonList(ListItem.NoResults(trimmedQuery))
        } else {
            results
        }
    }

    sealed class ListItem {
        abstract val listId: Long

        class Player(
                val isIdentity: Boolean,
                val player: FavoritePlayer
        ) : ListItem() {
            override val listId: Long = player.hashCode().toLong()
        }

        class NoResults(
                val query: String
        ) : ListItem() {
            override val listId: Long = Long.MIN_VALUE + 1L
        }
    }

    data class State(
            val isEmpty: Boolean = false,
            val isFetching: Boolean = true,
            val list: List<ListItem>? = null,
            val searchResults: List<ListItem>? = null
    )

}
