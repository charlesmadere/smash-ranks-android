package com.garpr.android.features.headToHead

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.AbsTournament
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.data.models.HeadToHead
import com.garpr.android.data.models.HeadToHeadMatch
import com.garpr.android.data.models.Optional
import com.garpr.android.data.models.Region
import com.garpr.android.data.models.WinsLosses
import com.garpr.android.extensions.takeSingle
import com.garpr.android.features.common.viewModels.BaseViewModel
import com.garpr.android.misc.Schedulers
import com.garpr.android.misc.Timber
import com.garpr.android.repositories.HeadToHeadRepository
import com.garpr.android.repositories.IdentityRepository
import io.reactivex.Single
import io.reactivex.functions.BiFunction
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

    init {
        initListeners()
    }

    @WorkerThread
    private fun createList(headToHead: HeadToHead?, identity: AbsPlayer?): List<ListItem>? {
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
                    playerIsIdentity = headToHead.player == identity,
                    opponentIsIdentity = match.opponent == identity,
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

        disposables.add(Single.zip(headToHeadRepository.getHeadToHead(region, playerId, opponentId),
                identityRepository.identityObservable.takeSingle(),
                BiFunction<HeadToHead, Optional<FavoritePlayer>,
                        Pair<HeadToHead, Optional<FavoritePlayer>>> { t1, t2 ->
                            Pair(t1, t2)
                        })
                .subscribeOn(schedulers.background)
                .observeOn(schedulers.background)
                .subscribe({ (headToHead, identity) ->
                    val list = createList(headToHead, identity.orNull())

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

    private fun initListeners() {
        disposables.add(identityRepository.identityObservable
                .subscribeOn(schedulers.background)
                .observeOn(schedulers.background)
                .subscribe { identity ->
                    refreshListItems(identity.orNull())
                })
    }

    @WorkerThread
    private fun refreshListItems(identity: AbsPlayer?) {
        val list = state.list

        if (list.isNullOrEmpty()) {
            return
        }

        val newList = list.map { listItem ->
            if (listItem is ListItem.Match) {
                listItem.copy(
                        playerIsIdentity = listItem.match.player == identity,
                        opponentIsIdentity = listItem.match.opponent == identity
                )
            } else {
                listItem
            }
        }

        state = state.copy(list = newList)
    }

    sealed class ListItem {
        abstract val listId: Long

        data class Match(
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
