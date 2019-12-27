package com.garpr.android.features.headToHead

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.garpr.android.data.models.AbsTournament
import com.garpr.android.data.models.HeadToHead
import com.garpr.android.data.models.HeadToHeadMatch
import com.garpr.android.data.models.Region
import com.garpr.android.data.models.WinsLosses
import com.garpr.android.features.common.viewModels.BaseViewModel
import com.garpr.android.misc.Schedulers
import com.garpr.android.misc.Timber
import com.garpr.android.repositories.HeadToHeadRepository
import com.garpr.android.repositories.IdentityRepository
import com.garpr.android.data.models.WinsLosses as GarPrWinsLosses

class HeadToHeadViewModel(
        private val headToHeadRepository: HeadToHeadRepository,
        private val identityRepository: IdentityRepository,
        private val schedulers: Schedulers,
        private val timber: Timber
) : BaseViewModel() {

    private val _stateLiveData = MutableLiveData<State>()
    val stateLiveData: LiveData<State> = _stateLiveData

    private var state: State = State()
        set(value) {
            field = value
            _stateLiveData.postValue(value)
        }

    companion object {
        private const val TAG = "HeadToHeadViewModel"
    }

    @WorkerThread
    private fun createList(headToHead: HeadToHead?): List<ListItem>? {
        if (headToHead == null) {
            return null
        }

        val list = mutableListOf<ListItem>()
        list.add(ListItem.WinsLosses(WinsLosses(
                player = headToHead.player,
                playerWins = headToHead.wins,
                opponent = headToHead.opponent,
                opponentWins = headToHead.losses
        )))

        if (headToHead.matches.isNullOrEmpty()) {
            list.add(ListItem.NoMatches)
            return list
        }

        var tournamentId: String? = null

        headToHead.matches.forEach { match ->
            if (match.tournament.id != tournamentId) {
                tournamentId = match.tournament.id
                list.add(ListItem.Tournament(match.tournament))
            }

            list.add(ListItem.Match(
                    playerIsIdentity = identityRepository.isPlayer(headToHead.player),
                    opponentIsIdentity = identityRepository.isPlayer(match.opponent),
                    match = HeadToHeadMatch(
                            result = match.result,
                            player = headToHead.player,
                            opponent = match.opponent
                    )
            ))
        }

        return list
    }

    fun fetchHeadToHead(region: Region, playerId: String, opponentId: String) {
        state = state.copy(isFetching = true)

        disposables.add(headToHeadRepository.getHeadToHead(region, playerId, opponentId)
                .subscribeOn(schedulers.background)
                .observeOn(schedulers.background)
                .subscribe({ headToHead ->
                    val list = createList(headToHead)

                    state = state.copy(
                            hasError = list.isNullOrEmpty(),
                            isFetching = false,
                            list = list
                    )
                }, {
                    timber.e(TAG, "Error fetching head to head", it)

                    state = state.copy(
                            hasError = true,
                            isFetching = false,
                            list = null
                    )
                }))
    }

    sealed class ListItem {
        abstract val listId: Long

        class Match(
                val playerIsIdentity: Boolean,
                val opponentIsIdentity: Boolean,
                val match: HeadToHeadMatch
        ) : ListItem() {
            override val listId: Long = match.hashCode().toLong()
        }

        object NoMatches : ListItem() {
            override val listId: Long = Long.MIN_VALUE + 1L
        }

        class Tournament(
                val tournament: AbsTournament
        ) : ListItem() {
            override val listId: Long = tournament.hashCode().toLong()
        }

        class WinsLosses(
                val winsLosses:  GarPrWinsLosses
        ) : ListItem() {
            override val listId: Long = Long.MIN_VALUE + 2L
        }
    }

    data class State(
            val hasError: Boolean = false,
            val isFetching: Boolean = false,
            val list: List<ListItem>? = null
    )

}
