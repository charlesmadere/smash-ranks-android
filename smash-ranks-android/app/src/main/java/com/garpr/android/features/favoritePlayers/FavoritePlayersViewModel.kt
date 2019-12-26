package com.garpr.android.features.favoritePlayers

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.garpr.android.features.common.viewModels.BaseViewModel
import com.garpr.android.misc.Refreshable
import com.garpr.android.misc.Schedulers
import com.garpr.android.misc.Searchable
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.repositories.FavoritePlayersRepository
import com.garpr.android.repositories.IdentityRepository
import com.garpr.android.data.models.FavoritePlayer as GarPrFavoritePlayer

class FavoritePlayersViewModel(
        private val favoritePlayersRepository: FavoritePlayersRepository,
        private val identityRepository: IdentityRepository,
        private val schedulers: Schedulers,
        private val threadUtils: ThreadUtils
) : BaseViewModel(), Refreshable, Searchable {

    private val _stateLiveData = MutableLiveData<State>()
    val stateLiveData: LiveData<State> = _stateLiveData

    private var state: State = State()
        set(value) {
            field = value
            _stateLiveData.postValue(value)
        }

    init {
        initListeners()
        refresh()
    }

    @WorkerThread
    private fun createList(players: List<GarPrFavoritePlayer>?): List<ListItem>? {
        return if (players.isNullOrEmpty()) {
            null
        } else {
            players.map { player ->
                ListItem.FavoritePlayer(
                        isIdentity = identityRepository.isPlayer(player),
                        player = player
                )
            }
        }
    }

    private fun initListeners() {
        disposables.add(favoritePlayersRepository.playersObservable
                .subscribeOn(schedulers.background)
                .observeOn(schedulers.background)
                .subscribe { refreshFavoritePlayers() })

        disposables.add(identityRepository.identityObservable
                .subscribeOn(schedulers.background)
                .observeOn(schedulers.background)
                .subscribe { refreshFavoritePlayers() })
    }

    override fun refresh() {
        threadUtils.background.submit {
            refreshFavoritePlayers()
        }
    }

    @WorkerThread
    private fun refreshFavoritePlayers() {
        state = state.copy(isFetching = true)

        val players = favoritePlayersRepository.players
        val list = createList(players)

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

        return list.filterIsInstance(ListItem.FavoritePlayer::class.java)
                .filter { it.player.name.contains(trimmedQuery, true) }
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
    }

}
