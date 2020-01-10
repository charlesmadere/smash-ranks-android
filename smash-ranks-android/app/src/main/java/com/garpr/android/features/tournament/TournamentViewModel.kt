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

class TournamentViewModel(
        private val identityRepository: IdentityRepository,
        private val schedulers: Schedulers,
        private val threadUtils: ThreadUtils,
        private val timber: Timber,
        private val tournamentsRepository: TournamentsRepository
) : BaseViewModel(), Searchable {

    private var region: Region? = null
    private var tournamentId: String? = null

    private val _stateLiveData = MutableLiveData<State>()
    val stateLiveData: LiveData<State> = _stateLiveData

    private var state: State = State()
        set(value) {
            field = value
            _stateLiveData.postValue(value)
        }

    companion object {
        private const val TAG = "TournamentViewModel"
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
                    val matches = createMatchesList(tournament, identity.item)
                    val players = createPlayersList(tournament, identity.item)

                    state = state.copy(
                            hasError = false,
                            isFetching = false,
                            isRefreshEnabled = false,
                            showMatchesEmpty = matches.isNullOrEmpty(),
                            showPlayersEmpty = players.isNullOrEmpty(),
                            showSearchIcon = !matches.isNullOrEmpty() || !players.isNullOrEmpty(),
                            titleText = tournament.name,
                            tournament = tournament,
                            matches = matches,
                            matchesSearchResults = null,
                            players = players,
                            playersSearchResults = null
                    )
                }, {
                    timber.e(TAG, "Error fetching tournament", it)

                    state = state.copy(
                            hasError = true,
                            isFetching = false,
                            isRefreshEnabled = true,
                            showMatchesEmpty = false,
                            showPlayersEmpty = false,
                            showSearchIcon = false,
                            titleText = null,
                            tournament = null,
                            matches = null,
                            matchesSearchResults = null,
                            players = null,
                            playersSearchResults = null
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
                    refreshListItems(optional.item)
                })
    }

    @WorkerThread
    private fun refreshListItems(identity: FavoritePlayer?) {
        val matches = refreshMatchListItems(identity, state.matches)
        val matchesSearchResults = refreshMatchListItems(identity, state.matchesSearchResults)
        val players = refreshPlayerListItems(identity, state.players)
        val playersSearchResults = refreshPlayerListItems(identity, state.playersSearchResults)

        state = state.copy(
                matches = matches,
                matchesSearchResults = matchesSearchResults,
                players = players,
                playersSearchResults = playersSearchResults
        )
    }

    @WorkerThread
    private fun refreshMatchListItems(identity: FavoritePlayer?, list: List<MatchListItem>?): List<MatchListItem>? {
        return if (list.isNullOrEmpty()) {
            list
        } else {
            val newList = mutableListOf<MatchListItem>()
            val sameRegion = region == identity?.region

            list.mapTo(newList) { listItem ->
                if (listItem is MatchListItem.Match) {
                    listItem.copy(
                            winnerIsIdentity = sameRegion && identity != null && identity == listItem.match.winner,
                            loserIsIdentity = sameRegion && identity != null && identity == listItem.match.loser
                    )
                } else {
                    listItem
                }
            }

            newList
        }
    }

    @WorkerThread
    private fun refreshPlayerListItems(identity: FavoritePlayer?, list: List<PlayerListItem>?): List<PlayerListItem>? {
        return if (list.isNullOrEmpty()) {
            list
        } else {
            val newList = mutableListOf<PlayerListItem>()

            list.mapTo(newList) { listItem ->
                if (listItem is PlayerListItem.Player) {
                    listItem.copy(isIdentity = listItem.player == identity)
                } else {
                    listItem
                }
            }

            newList
        }
    }

    override fun search(query: String?) {
        threadUtils.background.submit {
            val results = searchMatches(query, state.matches)
            state = state.copy(matchesSearchResults = results)
        }

        threadUtils.background.submit {
            val results = searchPlayers(query, state.players)
            state = state.copy(playersSearchResults = results)
        }
    }

    @WorkerThread
    private fun searchMatches(query: String?, list: List<MatchListItem>?): List<MatchListItem>? {
        if (query.isNullOrBlank() || list.isNullOrEmpty()) {
            return null
        }

        val trimmedQuery = query.trim()

        return list.filterIsInstance(MatchListItem.Match::class.java)
                .filter { listItem ->
                    listItem.match.winner.name.contains(trimmedQuery, ignoreCase = true) ||
                            listItem.match.loser.name.contains(trimmedQuery, ignoreCase = true)
                }
    }

    @WorkerThread
    private fun searchPlayers(query: String?, list: List<PlayerListItem>?): List<PlayerListItem>? {
        if (query.isNullOrBlank() || list.isNullOrEmpty()) {
            return null
        }

        val trimmedQuery = query.trim()

        return list.filterIsInstance(PlayerListItem.Player::class.java)
                .filter { listItem ->
                    listItem.player.name.contains(trimmedQuery, ignoreCase = true)
                }
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
    }

    sealed class PlayerListItem {
        abstract val listId: Long

        data class Player(
                val player: AbsPlayer,
                val isIdentity: Boolean
        ) : PlayerListItem() {
            override val listId: Long = player.hashCode().toLong()
        }
    }

    data class State(
            val hasError: Boolean = false,
            val isFetching: Boolean = false,
            val isRefreshEnabled: Boolean = false,
            val showMatchesEmpty: Boolean = false,
            val showPlayersEmpty: Boolean = false,
            val showSearchIcon: Boolean = false,
            val subtitleText: CharSequence? = null,
            val titleText: CharSequence? = null,
            val tournament: FullTournament? = null,
            val matches: List<MatchListItem>? = null,
            val matchesSearchResults: List<MatchListItem>? = null,
            val players: List<PlayerListItem>? = null,
            val playersSearchResults: List<PlayerListItem>? = null
    )

}
