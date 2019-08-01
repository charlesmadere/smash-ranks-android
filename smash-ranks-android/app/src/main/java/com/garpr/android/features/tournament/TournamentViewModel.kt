package com.garpr.android.features.tournament

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.FullTournament
import com.garpr.android.data.models.Region
import com.garpr.android.features.common.viewModels.BaseViewModel
import com.garpr.android.misc.Searchable
import com.garpr.android.misc.ThreadUtils2
import com.garpr.android.misc.Timber
import com.garpr.android.repositories.TournamentsRepository
import javax.inject.Inject

class TournamentViewModel @Inject constructor(
        private val threadUtils: ThreadUtils2,
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

    private fun createMatchesList(tournament: FullTournament): List<MatchListItem>? {
        if (tournament.matches.isNullOrEmpty()) {
            return null
        }

        val list = mutableListOf<MatchListItem>()

        tournament.matches.forEach {
            list.add(MatchListItem.Match(it))
        }

        return list
    }

    private fun createPlayersList(tournament: FullTournament): List<PlayerListItem>? {
        if (tournament.players.isNullOrEmpty()) {
            return null
        }

        val list = mutableListOf<PlayerListItem>()

        tournament.players.forEach {
            list.add(PlayerListItem.Player(it))
        }

        return list
    }

    fun fetchTournament() {
        val region = this.region
        val tournamentId = this.tournamentId

        if (region == null || tournamentId == null) {
            throw IllegalStateException("initialize() hasn't been called!")
        }

        disposables.add(tournamentsRepository.getTournament(region, tournamentId)
                .subscribe({
                    val matches = createMatchesList(it)
                    val players = createPlayersList(it)

                    state = state.copy(
                            hasError = false,
                            isFetching = false,
                            isRefreshEnabled = false,
                            showMatchesEmpty = matches.isNullOrEmpty(),
                            showPlayersEmpty = players.isNullOrEmpty(),
                            showSearchIcon = true,
                            titleText = it.name,
                            tournament = it,
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
            return list
        }

        val trimmedQuery = query.trim()

        return list.filter {
            it is MatchListItem.Match &&
                    (it.match.winnerName.contains(trimmedQuery, true) ||
                            it.match.loserName.contains(trimmedQuery, true))
        }
    }

    @WorkerThread
    private fun searchPlayers(query: String?, list: List<PlayerListItem>?): List<PlayerListItem>? {
        if (query.isNullOrBlank() || list.isNullOrEmpty()) {
            return list
        }

        val trimmedQuery = query.trim()

        return list.filter {
            it is PlayerListItem.Player &&
                    it.player.name.contains(trimmedQuery, true)
        }
    }

    sealed class MatchListItem {
        abstract val listId: Long

        class Match(
                val match: FullTournament.Match
        ) : MatchListItem() {
            override val listId: Long = match.hashCode().toLong()
        }
    }

    sealed class PlayerListItem {
        abstract val listId: Long

        class Player(
                val player: AbsPlayer
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
