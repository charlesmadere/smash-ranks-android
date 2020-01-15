package com.garpr.android.features.player

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.AbsTournament
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.data.models.FullPlayer
import com.garpr.android.data.models.Optional
import com.garpr.android.data.models.PlayerMatchesBundle
import com.garpr.android.data.models.Region
import com.garpr.android.data.models.SmashCompetitor
import com.garpr.android.data.models.TournamentMatch
import com.garpr.android.features.common.viewModels.BaseViewModel
import com.garpr.android.misc.Refreshable
import com.garpr.android.misc.Schedulers
import com.garpr.android.misc.Searchable
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.misc.Timber
import com.garpr.android.repositories.FavoritePlayersRepository
import com.garpr.android.repositories.IdentityRepository
import com.garpr.android.repositories.PlayerMatchesRepository
import com.garpr.android.sync.roster.SmashRosterStorage
import com.garpr.android.sync.roster.SmashRosterSyncManager
import io.reactivex.Single
import io.reactivex.functions.Function3

class PlayerViewModel(
        private val favoritePlayersRepository: FavoritePlayersRepository,
        private val identityRepository: IdentityRepository,
        private val playerMatchesRepository: PlayerMatchesRepository,
        private val schedulers: Schedulers,
        private val smashRosterStorage: SmashRosterStorage,
        private val smashRosterSyncManager: SmashRosterSyncManager,
        private val threadUtils: ThreadUtils,
        private val timber: Timber
) : BaseViewModel(), Refreshable, Searchable {

    val identity: AbsPlayer?
        get() = state.identity

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

        if (state.isFavorited) {
            favoritePlayersRepository.removePlayer(player, region)
        } else {
            favoritePlayersRepository.addPlayer(player, region)
        }
    }

    @WorkerThread
    private fun createList(bundle: PlayerMatchesBundle?, identity: AbsPlayer?): List<ListItem>? {
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

            list.add(ListItem.Match(
                    isIdentity = identity == match.opponent,
                    match = match
            ))
        }

        return list
    }

    fun fetchPlayer() {
        val region = this.region
        val playerId = this.playerId

        check(region != null && playerId != null) { "initialize() hasn't been called!" }

        state = state.copy(isFetching = true)

        disposables.add(Single.zip(playerMatchesRepository.getPlayerAndMatches(region, playerId),
                favoritePlayersRepository.playersObservable.take(1).singleOrError(),
                identityRepository.identityObservable.take(1).singleOrError(),
                Function3<PlayerMatchesBundle, List<FavoritePlayer>, Optional<FavoritePlayer>,
                        Triple<PlayerMatchesBundle, List<FavoritePlayer>, Optional<FavoritePlayer>>> { t1, t2, t3 ->
                            Triple(t1, t2, t3)
                        })
                .subscribeOn(schedulers.background)
                .observeOn(schedulers.background)
                .subscribe({ (bundle, favoritePlayers, identity) ->
                    val list = createList(bundle, identity.item)

                    val showSearchIcon = list?.any { listItem ->
                        listItem is ListItem.Match
                    } == true

                    val isFavorited = favoritePlayers.any { favoritePlayer ->
                        AbsPlayer.safeEquals(favoritePlayer, bundle.fullPlayer)
                    }

                    state = state.copy(
                            identity = identity.item,
                            hasError = false,
                            isFavorited = isFavorited,
                            isFetching = false,
                            showSearchIcon = showSearchIcon,
                            list = list,
                            searchResults = null,
                            playerMatchesBundle = bundle
                    )

                    refreshState()
                }, {
                    timber.e(TAG, "Error fetching player", it)

                    state = state.copy(
                            identity = null,
                            hasError = true,
                            isFavorited = false,
                            isFetching = false,
                            showSearchIcon = false,
                            list = null,
                            searchResults = null,
                            playerMatchesBundle = null
                    )

                    refreshState()
                }))
    }

    fun initialize(region: Region, playerId: String) {
        this.region = region
        this.playerId = playerId

        state = state.copy(subtitleText = region.displayName)

        refresh()
    }

    private fun initListeners() {
        disposables.add(favoritePlayersRepository.playersObservable
                .subscribeOn(schedulers.background)
                .observeOn(schedulers.background)
                .subscribe { favoritePlayers ->
                    refreshIsFavorited(favoritePlayers)
                })

        disposables.add(identityRepository.identityObservable
                .subscribeOn(schedulers.background)
                .observeOn(schedulers.background)
                .subscribe { optional ->
                    refreshListItems(optional.item)
                })

        disposables.add(smashRosterSyncManager.isSyncingObservable
                .subscribeOn(schedulers.background)
                .observeOn(schedulers.background)
                .filter { isSyncing -> !isSyncing }
                .subscribe {
                    refreshState()
                })
    }

    override fun refresh() {
        threadUtils.background.submit {
            refreshState()
        }
    }

    @WorkerThread
    private fun refreshIsFavorited(favoritePlayers: List<FavoritePlayer>) {
        val player = this.player

        val isFavorited: Boolean = if (player == null) {
            false
        } else {
            favoritePlayers.any { favoritePlayer ->
                AbsPlayer.safeEquals(player, favoritePlayer)
            }
        }

        state = state.copy(isFavorited = isFavorited)
    }

    @WorkerThread
    private fun refreshListItems(identity: AbsPlayer?) {
        val list = refreshListItems(identity, state.list)
        val searchResults = refreshListItems(identity, state.searchResults)

        state = state.copy(
                identity = identity,
                list = list,
                searchResults = searchResults
        )
    }

    @WorkerThread
    private fun refreshListItems(identity: AbsPlayer?, list: List<ListItem>?): List<ListItem>? {
        return if (list.isNullOrEmpty()) {
            list
        } else {
            list.map { listItem ->
                if (listItem is ListItem.Match) {
                    listItem.copy(isIdentity = listItem.match.opponent == identity)
                } else {
                    listItem
                }
            }
        }
    }

    @WorkerThread
    private fun refreshState() {
        val region = this.region
        val playerId = this.playerId

        if (region == null || playerId == null) {
            // This scenario should not throw an exception since this is a possible state to
            // be in due to different threads and lifecycle callbacks and things calling
            // through to this method.
            return
        }

        val player = this.player

        val smashCompetitor = smashRosterStorage.getSmashCompetitor(region, playerId)

        val titleText: CharSequence? = if (smashCompetitor?.tag?.isNotBlank() == true) {
            smashCompetitor.tag
        } else if (player?.name?.isNotBlank() == true) {
            player.name
        } else {
            null
        }

        state = state.copy(
                titleText = titleText,
                smashCompetitor = smashCompetitor
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

        data class Match(
                val isIdentity: Boolean,
                val match: TournamentMatch
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
            val identity: AbsPlayer? = null,
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
