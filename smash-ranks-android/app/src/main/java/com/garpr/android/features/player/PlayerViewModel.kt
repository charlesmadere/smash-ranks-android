package com.garpr.android.features.player

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.garpr.android.data.models.AbsTournament
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.data.models.FullPlayer
import com.garpr.android.data.models.PlayerMatchesBundle
import com.garpr.android.data.models.Region
import com.garpr.android.data.models.SmashCompetitor
import com.garpr.android.features.common.viewModels.BaseViewModel
import com.garpr.android.misc.Refreshable
import com.garpr.android.misc.Searchable
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.misc.Timber
import com.garpr.android.repositories.FavoritePlayersRepository
import com.garpr.android.repositories.IdentityRepository
import com.garpr.android.repositories.PlayerMatchesRepository
import com.garpr.android.sync.roster.SmashRosterStorage
import com.garpr.android.sync.roster.SmashRosterSyncManager
import com.garpr.android.data.models.Match as GarPrMatch

class PlayerViewModel(
        private val favoritePlayersRepository: FavoritePlayersRepository,
        private val identityRepository: IdentityRepository,
        private val playerMatchesRepository: PlayerMatchesRepository,
        private val smashRosterStorage: SmashRosterStorage,
        private val smashRosterSyncManager: SmashRosterSyncManager,
        private val threadUtils: ThreadUtils,
        private val timber: Timber
) : BaseViewModel(), FavoritePlayersRepository.OnFavoritePlayersChangeListener,
        IdentityRepository.OnIdentityChangeListener, Refreshable, Searchable {

    val identity: FavoritePlayer?
        get() = identityRepository.identity

    val player: FullPlayer?
        get() = state.playerMatchesBundle?.fullPlayer

    private val _stateLiveData = MutableLiveData<State>()
    val stateLiveData: LiveData<State> = _stateLiveData

    private var region: Region? = null

    private var state: State = State()
        set(value) {
            field = value
            _stateLiveData.postValue(value)
        }

    private var playerId: String? = null

    companion object {
        private const val TAG = "PlayerViewModel"
    }

    init {
        initListeners()
    }

    fun addOrRemoveFromFavorites() {
        val region = checkNotNull(this.region) { "initialize() hasn't been called!" }
        val player = checkNotNull(this.player) { "player hasn't been fetched!" }

        if (player in favoritePlayersRepository) {
            favoritePlayersRepository.removePlayer(player)
        } else {
            favoritePlayersRepository.addPlayer(player, region)
        }
    }

    @WorkerThread
    private fun createList(bundle: PlayerMatchesBundle?): List<ListItem>? {
        if (bundle == null) {
            return null
        }

        val list = mutableListOf<ListItem>()
        list.add(ListItem.Player)

        val matches = bundle.matchesBundle?.matches

        if (matches.isNullOrEmpty()) {
            list.add(ListItem.NoMatches)
            return list
        }

        var tournamentId: String? = null

        matches.forEach { match ->
            if (match.tournament.id != tournamentId) {
                tournamentId = match.tournament.id
                list.add(ListItem.Tournament(match.tournament))
            }

            list.add(ListItem.Match(match))
        }

        return list
    }

    fun fetchPlayer() {
        val region = this.region
        val playerId = this.playerId
        check(region != null && playerId != null) { "initialize() hasn't been called!" }

        state = state.copy(isFetching = true)

        disposables.add(playerMatchesRepository.getPlayerAndMatches(region, playerId)
                .subscribe({ bundle ->
                    val list = createList(bundle)
                    val showSearchIcon = list?.any { it is ListItem.Match } == true

                    state = state.copy(
                            hasError = false,
                            isFetching = false,
                            showSearchIcon = showSearchIcon,
                            list = list,
                            searchResults = null,
                            playerMatchesBundle = bundle
                    )

                    refresh()
                }, {
                    timber.e(TAG, "Error fetching player", it)

                    state = state.copy(
                            hasError = true,
                            isFetching = false,
                            showSearchIcon = false,
                            list = null,
                            searchResults = null,
                            playerMatchesBundle = null
                    )

                    refresh()
                }))
    }

    fun initialize(region: Region, playerId: String) {
        this.region = region
        this.playerId = playerId

        state = state.copy(subtitleText = region.displayName)

        refresh()
    }

    private fun initListeners() {
        favoritePlayersRepository.addListener(this)
        identityRepository.addListener(this)

        disposables.add(smashRosterSyncManager.observeSyncState
                .filter { state -> state == SmashRosterSyncManager.State.NOT_SYNCING }
                .subscribe({
                    refresh()
                }, {
                    timber.e(TAG, "Error listening for smash roster sync events", it)
                    refresh()
                }))
    }

    override fun onCleared() {
        favoritePlayersRepository.removeListener(this)
        identityRepository.removeListener(this)
        super.onCleared()
    }

    override fun onFavoritePlayersChange(favoritePlayersRepository: FavoritePlayersRepository) {
        refresh()
    }

    override fun onIdentityChange(identityRepository: IdentityRepository) {
        refresh()
    }

    override fun refresh() {
        threadUtils.background.submit {
            val region = this.region
            val playerId = this.playerId

            if (region == null || playerId == null) {
                // This scenario should not throw an exception since this is a possible state to
                // be in due to different threads and lifecycle callbacks and things calling
                // through to this method.
                return@submit
            }

            val player = this.player

            val isFavorited: Boolean = if (player == null) {
                false
            } else {
                player in favoritePlayersRepository
            }

            val smashCompetitor = smashRosterStorage.getSmashCompetitor(region, playerId)

            val titleText: CharSequence? = if (smashCompetitor?.tag?.isNotBlank() == true) {
                smashCompetitor.tag
            } else if (player?.name?.isNotBlank() == true) {
                player.name
            } else {
                null
            }

            state = state.copy(
                    isFavorited = isFavorited,
                    titleText = titleText,
                    smashCompetitor = smashCompetitor
            )
        }
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

        val results = mutableListOf<ListItem>()
        val trimmedQuery = query.trim()
        var addedNoMatches = false

        list.forEachIndexed { index, listItem ->
            if (listItem is ListItem.NoMatches) {
                if (!addedNoMatches) {
                    addedNoMatches = true
                    results.add(listItem)
                }
            } else if (listItem is ListItem.Tournament) {
                var addedTournament = false
                var j = index + 1

                while (j < list.size) {
                    val objectJ = list[j]

                    if (objectJ is ListItem.Match) {
                        if (objectJ.match.opponent.name.contains(trimmedQuery, true)) {
                            if (!addedTournament) {
                                addedTournament = true
                                results.add(listItem)
                            }

                            results.add(objectJ)
                        }

                        ++j
                    } else {
                        j = list.size
                    }
                }
            }
        }

        return results
    }

    sealed class ListItem {
        abstract val listId: Long

        class Match(
                val match: GarPrMatch
        ) : ListItem() {
            override val listId: Long = match.hashCode().toLong()
        }

        object NoMatches : ListItem() {
            override val listId: Long = Long.MIN_VALUE + 1L
        }

        object Player : ListItem() {
            override val listId: Long = Long.MIN_VALUE + 2L
        }

        class Tournament(
                val tournament: AbsTournament
        ) : ListItem() {
            override val listId: Long = tournament.hashCode().toLong()
        }
    }

    data class State(
            val hasError: Boolean = false,
            val isFavorited: Boolean = false,
            val isFetching: Boolean = false,
            val showSearchIcon: Boolean = false,
            val subtitleText: CharSequence? = null,
            val titleText: CharSequence? = null,
            val list: List<ListItem>? = null,
            val searchResults: List<ListItem>? = null,
            val playerMatchesBundle: PlayerMatchesBundle? = null,
            val smashCompetitor: SmashCompetitor? = null
    )

}
