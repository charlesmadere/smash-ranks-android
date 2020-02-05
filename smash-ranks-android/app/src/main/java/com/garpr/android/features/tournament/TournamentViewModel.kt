package com.garpr.android.features.tournament

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.data.models.FullTournament
import com.garpr.android.data.models.Optional
import com.garpr.android.data.models.Region
import com.garpr.android.features.common.viewModels.BaseViewModel
import com.garpr.android.misc.Schedulers
import com.garpr.android.misc.Searchable
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.misc.Timber
import com.garpr.android.repositories.IdentityRepository
import com.garpr.android.repositories.TournamentsRepository
import io.reactivex.functions.BiFunction
import java.util.Collections

class TournamentViewModel(
        private val identityRepository: IdentityRepository,
        private val schedulers: Schedulers,
        private val threadUtils: ThreadUtils,
        private val timber: Timber,
        private val tournamentsRepository: TournamentsRepository
) : BaseViewModel(), Searchable {

    private var region: Region? = null
    private var tournamentId: String? = null

    private val _matchesStateLiveData = MutableLiveData<MatchesState>()
    val matchesStateLiveData: LiveData<MatchesState> = _matchesStateLiveData

    private var matchesState: MatchesState = MatchesState()
        set(value) {
            field = value
            _matchesStateLiveData.postValue(value)
        }

    private val _playersStateLiveData = MutableLiveData<PlayersState>()
    val playersStateLiveData: LiveData<PlayersState> = _playersStateLiveData

    private var playersState: PlayersState = PlayersState()
        set(value) {
            field = value
            _playersStateLiveData.postValue(value)
        }

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
    private fun createMatchesList(tournament: FullTournament?, identity: FavoritePlayer?): List<MatchListItem>? {
        val matches = tournament?.matches
        val sameRegion = region == identity?.region

        return if (matches.isNullOrEmpty()) {
            null
        } else {
            matches.map { match ->
                MatchListItem.Match(
                        winnerIsIdentity = sameRegion && identity != null && identity == match.winner,
                        loserIsIdentity = sameRegion && identity != null && identity == match.loser,
                        match = match
                )
            }
        }
    }

    @WorkerThread
    private fun createPlayersList(tournament: FullTournament?, identity: FavoritePlayer?): List<PlayerListItem>? {
        val players = tournament?.players

        return if (players.isNullOrEmpty()) {
            null
        } else {
            players.map { player ->
                PlayerListItem.Player(
                        player = player,
                        isIdentity = identity == player
                )
            }
        }
    }

    fun fetchTournament() {
        val region = this.region
        val tournamentId = this.tournamentId

        check(region != null && tournamentId != null) { "initialize() hasn't been called!" }

        state = state.copy(isFetching = true)

        disposables.add(tournamentsRepository.getTournament(region, tournamentId)
                .zipWith(identityRepository.identityObservable.take(1).singleOrError(),
                        BiFunction<FullTournament, Optional<FavoritePlayer>,
                                Pair<FullTournament, Optional<FavoritePlayer>>> { t1, t2 ->
                                    Pair(t1, t2)
                                })
                .subscribeOn(schedulers.background)
                .observeOn(schedulers.background)
                .subscribe({ (tournament, identity) ->
                    identityRepository.identityObservable.take(1)
                    val matches = createMatchesList(tournament, identity.orNull())
                    val players = createPlayersList(tournament, identity.orNull())

                    state = state.copy(
                            hasError = false,
                            isFetching = false,
                            isRefreshEnabled = false,
                            showSearchIcon = !matches.isNullOrEmpty() || !players.isNullOrEmpty(),
                            titleText = tournament.name,
                            tournament = tournament
                    )

                    matchesState = matchesState.copy(
                            isEmpty = matches.isNullOrEmpty(),
                            list = matches,
                            searchResults = null
                    )

                    playersState = playersState.copy(
                            isEmpty = players.isNullOrEmpty(),
                            list = players,
                            searchResults = null
                    )
                }, {
                    timber.e(TAG, "Error fetching tournament", it)

                    state = state.copy(
                            hasError = true,
                            isFetching = false,
                            isRefreshEnabled = true,
                            showSearchIcon = false,
                            titleText = null,
                            tournament = null
                    )

                    matchesState = matchesState.copy(
                            isEmpty = false,
                            list = null,
                            searchResults = null
                    )

                    playersState = playersState.copy(
                            isEmpty = false,
                            list = null,
                            searchResults = null
                    )
                }))
    }

    fun initialize(region: Region, tournamentId: String) {
        this.region = region
        this.tournamentId = tournamentId
        state = state.copy(subtitleText = region.displayName)
    }

    private fun initListeners() {
        disposables.add(identityRepository.identityObservable
                .subscribeOn(schedulers.background)
                .observeOn(schedulers.background)
                .subscribe { optional ->
                    refreshListItems(optional.orNull())
                })
    }

    @WorkerThread
    private fun refreshListItems(identity: FavoritePlayer?) {
        val matches = refreshMatchListItems(matchesState.list, identity)
        val matchesSearchResults = refreshMatchListItems(matchesState.searchResults, identity)

        matchesState = matchesState.copy(
                list = matches,
                searchResults = matchesSearchResults
        )

        val players = refreshPlayerListItems(playersState.list, identity)
        val playersSearchResults = refreshPlayerListItems(playersState.searchResults, identity)

        playersState = playersState.copy(
                list = players,
                searchResults = playersSearchResults
        )
    }

    @WorkerThread
    private fun refreshMatchListItems(list: List<MatchListItem>?, identity: FavoritePlayer?): List<MatchListItem>? {
        return if (list.isNullOrEmpty()) {
            list
        } else {
            val sameRegion = region == identity?.region

            list.map { listItem ->
                if (listItem is MatchListItem.Match) {
                    listItem.copy(
                            winnerIsIdentity = sameRegion && identity != null && identity == listItem.match.winner,
                            loserIsIdentity = sameRegion && identity != null && identity == listItem.match.loser
                    )
                } else {
                    listItem
                }
            }
        }
    }

    @WorkerThread
    private fun refreshPlayerListItems(list: List<PlayerListItem>?, identity: AbsPlayer?): List<PlayerListItem>? {
        return if (list.isNullOrEmpty()) {
            list
        } else {
            list.map { listItem ->
                if (listItem is PlayerListItem.Player) {
                    listItem.copy(isIdentity = listItem.player == identity)
                } else {
                    listItem
                }
            }
        }
    }

    override fun search(query: String?) {
        threadUtils.background.submit {
            val results = searchMatches(query, matchesState.list)
            matchesState = matchesState.copy(searchResults = results)
        }

        threadUtils.background.submit {
            val results = searchPlayers(query, playersState.list)
            playersState = playersState.copy(searchResults = results)
        }
    }

    @WorkerThread
    private fun searchMatches(query: String?, list: List<MatchListItem>?): List<MatchListItem>? {
        if (query.isNullOrBlank() || list.isNullOrEmpty()) {
            return null
        }

        val trimmedQuery = query.trim()

        val results = list.filterIsInstance(MatchListItem.Match::class.java)
                .filter { listItem ->
                    listItem.match.winner.name.contains(trimmedQuery, ignoreCase = true) ||
                            listItem.match.loser.name.contains(trimmedQuery, ignoreCase = true)
                }

        return if (results.isEmpty()) {
            Collections.singletonList(MatchListItem.NoResults(trimmedQuery))
        } else {
            results
        }
    }

    @WorkerThread
    private fun searchPlayers(query: String?, list: List<PlayerListItem>?): List<PlayerListItem>? {
        if (query.isNullOrBlank() || list.isNullOrEmpty()) {
            return null
        }

        val trimmedQuery = query.trim()

        val results =  list.filterIsInstance(PlayerListItem.Player::class.java)
                .filter { listItem ->
                    listItem.player.name.contains(trimmedQuery, ignoreCase = true)
                }

        return if (results.isEmpty()) {
            Collections.singletonList(PlayerListItem.NoResults(trimmedQuery))
        } else {
            results
        }
    }

    companion object {
        private const val TAG = "TournamentViewModel"
    }

    sealed class MatchListItem {
        abstract val listId: Long

        data class Match(
                val winnerIsIdentity: Boolean,
                val loserIsIdentity: Boolean,
                val match: FullTournament.Match
        ) : MatchListItem() {
            override val listId: Long = match.hashCode().toLong()
        }

        class NoResults(
                val query: String
        ) : MatchListItem() {
            override val listId: Long = Long.MIN_VALUE + 1L
        }
    }

    sealed class PlayerListItem {
        abstract val listId: Long

        class NoResults(
                val query: String
        ) : PlayerListItem() {
            override val listId: Long = Long.MIN_VALUE + 1L
        }

        data class Player(
                val player: AbsPlayer,
                val isIdentity: Boolean
        ) : PlayerListItem() {
            override val listId: Long = player.hashCode().toLong()
        }
    }

    data class MatchesState(
            val isEmpty: Boolean = false,
            val list: List<MatchListItem>? = null,
            val searchResults: List<MatchListItem>? = null
    )

    data class PlayersState(
            val isEmpty: Boolean = false,
            val list: List<PlayerListItem>? = null,
            val searchResults: List<PlayerListItem>? = null
    )

    data class State(
            val hasError: Boolean = false,
            val isFetching: Boolean = false,
            val isRefreshEnabled: Boolean = false,
            val showSearchIcon: Boolean = false,
            val subtitleText: CharSequence? = null,
            val titleText: CharSequence? = null,
            val tournament: FullTournament? = null
    )

}
