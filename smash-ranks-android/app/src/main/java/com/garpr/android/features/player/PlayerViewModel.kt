package com.garpr.android.features.player

import androidx.annotation.AnyThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.AbsRegion
import com.garpr.android.data.models.AbsTournament
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.data.models.FullPlayer
import com.garpr.android.data.models.Optional
import com.garpr.android.data.models.PlayerMatchesBundle
import com.garpr.android.data.models.Region
import com.garpr.android.data.models.SmashCompetitor
import com.garpr.android.data.models.TournamentMatch
import com.garpr.android.extensions.takeSingle
import com.garpr.android.features.common.viewModels.BaseViewModel
import com.garpr.android.misc.Schedulers
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.misc.Timber
import com.garpr.android.repositories.FavoritePlayersRepository
import com.garpr.android.repositories.IdentityRepository
import com.garpr.android.repositories.PlayerMatchesRepository
import com.garpr.android.sync.roster.SmashRosterStorage
import com.garpr.android.sync.roster.SmashRosterSyncManager
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.Function3
import java.util.Collections

class PlayerViewModel(
        private val favoritePlayersRepository: FavoritePlayersRepository,
        private val identityRepository: IdentityRepository,
        private val playerMatchesRepository: PlayerMatchesRepository,
        private val schedulers: Schedulers,
        private val smashRosterStorage: SmashRosterStorage,
        private val smashRosterSyncManager: SmashRosterSyncManager,
        private val threadUtils: ThreadUtils,
        private val timber: Timber
) : BaseViewModel() {

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

    var searchQuery: String? = null
        set(value) {
            field = value
            search(value)
        }

    init {
        initListeners()
    }

    @AnyThread
    fun addOrRemoveFromFavorites() {
        val region = checkNotNull(this.region) { "initialize() hasn't been called!" }

        val listItem = state.list
                ?.filterIsInstance(ListItem.Player::class.java)
                ?.first()
                ?: throw IllegalStateException("There is no ListItem.Player in the list!")

        if (listItem.isFavorited) {
            favoritePlayersRepository.removePlayer(listItem.player, region)
        } else {
            favoritePlayersRepository.addPlayer(listItem.player, region)
        }
    }

    @WorkerThread
    private fun createList(
            bundle: PlayerMatchesBundle?,
            favoritePlayers: List<FavoritePlayer>,
            identity: AbsPlayer?,
            smashCompetitor: SmashCompetitor?
    ): List<ListItem>? {
        val region = checkNotNull(this.region) { "initialize() hasn't been called!" }

        if (bundle == null) {
            return null
        }

        val list = mutableListOf<ListItem>()

        list.add(ListItem.Player(
                identity = identity,
                region = region,
                isFavorited = favoritePlayers.any { favoritePlayer ->
                    AbsPlayer.safeEquals(favoritePlayer, bundle.fullPlayer)
                },
                player = bundle.fullPlayer,
                smashCompetitor = smashCompetitor
        ))

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

        disposables.add(Single.zip(
                playerMatchesRepository.getPlayerAndMatches(region, playerId),
                favoritePlayersRepository.playersObservable.takeSingle(),
                identityRepository.identityObservable.takeSingle(),
                Function3<PlayerMatchesBundle, List<FavoritePlayer>, Optional<FavoritePlayer>,
                        Triple<PlayerMatchesBundle, List<FavoritePlayer>, Optional<FavoritePlayer>>> { t1, t2, t3 ->
                            Triple(t1, t2, t3)
                        })
                .subscribeOn(schedulers.background)
                .observeOn(schedulers.background)
                .subscribe({ (bundle, favoritePlayers, identity) ->
                    val smashCompetitor = smashRosterStorage.getSmashCompetitor(region, playerId)

                    val list = createList(
                            bundle = bundle,
                            favoritePlayers = favoritePlayers,
                            identity = identity.orNull(),
                            smashCompetitor = smashCompetitor
                    )

                    val showSearchIcon: Boolean = list?.any { listItem ->
                        listItem is ListItem.Match
                    } == true

                    val titleText: CharSequence = if (smashCompetitor?.tag?.isNotBlank() == true) {
                        smashCompetitor.tag
                    } else {
                        bundle.fullPlayer.name
                    }

                    state = state.copy(
                            identity = identity.orNull(),
                            hasError = false,
                            isFetching = false,
                            showSearchIcon = showSearchIcon,
                            titleText = titleText,
                            list = list,
                            searchResults = null,
                            playerMatchesBundle = bundle
                    )
                }, {
                    timber.e(TAG, "Error fetching player", it)

                    state = state.copy(
                            identity = null,
                            hasError = true,
                            isFetching = false,
                            showSearchIcon = false,
                            titleText = null,
                            list = null,
                            searchResults = null,
                            playerMatchesBundle = null
                    )
                }))
    }

    fun initialize(region: Region, playerId: String) {
        this.region = region
        this.playerId = playerId
        state = state.copy(subtitleText = region.displayName)
    }

    private fun initListeners() {
        disposables.add(Observable.combineLatest(
                favoritePlayersRepository.playersObservable,
                identityRepository.identityObservable,
                smashRosterSyncManager.isSyncingObservable
                        .filter { isSyncing -> !isSyncing },
                Function3<List<FavoritePlayer>, Optional<FavoritePlayer>, Boolean,
                        Pair<List<FavoritePlayer>, Optional<FavoritePlayer>>> { t1, t2, t3 ->
                            Pair(t1, t2)
                        })
                .subscribeOn(schedulers.background)
                .observeOn(schedulers.background)
                .subscribe { (favorites, identityOptional) ->
                    refreshListItems(
                            favorites = favorites,
                            identity = identityOptional.orNull()
                    )
                })
    }

    @WorkerThread
    private fun refreshListItems(favorites: List<FavoritePlayer>, identity: FavoritePlayer?) {
        val list = refreshListItems(state.list, favorites, identity)
        val searchResults = search(searchQuery, list)

        state = state.copy(
                identity = identity,
                list = list,
                searchResults = searchResults
        )
    }

    @WorkerThread
    private fun refreshListItems(list: List<ListItem>?, favorites: List<FavoritePlayer>,
            identity: FavoritePlayer?): List<ListItem>? {
        return if (list.isNullOrEmpty()) {
            list
        } else {
            list.map { listItem ->
                if (listItem is ListItem.Match) {
                    listItem.copy(isIdentity = listItem.match.opponent == identity)
                } else if (listItem is ListItem.Player) {
                    val region = checkNotNull(this.region) { "initialize() hasn't been called!" }

                    val player = checkNotNull(state.playerMatchesBundle?.fullPlayer) {
                        "This should be impossible, player data has not yet been fetched."
                    }

                    listItem.copy(
                            identity = identity,
                            isFavorited = favorites.any { favoritePlayer ->
                                AbsPlayer.safeEquals(favoritePlayer, player)
                            },
                            smashCompetitor = smashRosterStorage.getSmashCompetitor(region, player.id)
                    )
                } else {
                    listItem
                }
            }
        }
    }

    @AnyThread
    private fun search(query: String?) {
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
                        if (objectJ.match.opponent.name.contains(trimmedQuery, ignoreCase = true)) {
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

        return if (results.isEmpty()) {
            Collections.singletonList(ListItem.NoResults(trimmedQuery))
        } else {
            results
        }
    }

    companion object {
        private const val TAG = "PlayerViewModel"
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

        class NoResults(
                val query: String
        ) : ListItem() {
            override val listId: Long = Long.MIN_VALUE + 2L
        }

        data class Player(
                val identity: AbsPlayer?,
                val region: AbsRegion,
                val isFavorited: Boolean,
                val player: FullPlayer,
                val smashCompetitor: SmashCompetitor?
        ) : ListItem() {
            override val listId: Long = Long.MIN_VALUE + 3L
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
            val isFetching: Boolean = false,
            val showSearchIcon: Boolean = false,
            val subtitleText: CharSequence? = null,
            val titleText: CharSequence? = null,
            val list: List<ListItem>? = null,
            val searchResults: List<ListItem>? = null,
            val playerMatchesBundle: PlayerMatchesBundle? = null
    )

}
