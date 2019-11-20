package com.garpr.android.features.rankings

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.PreviousRank
import com.garpr.android.data.models.RankingsBundle
import com.garpr.android.data.models.Region
import com.garpr.android.extensions.truncate
import com.garpr.android.features.common.viewModels.BaseViewModel
import com.garpr.android.misc.Searchable
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.misc.Timber
import com.garpr.android.repositories.IdentityRepository
import com.garpr.android.repositories.RankingsRepository
import java.text.NumberFormat

class RankingsViewModel(
        private val identityRepository: IdentityRepository,
        private val rankingsRepository: RankingsRepository,
        private val threadUtils: ThreadUtils,
        private val timber: Timber
) : BaseViewModel(), IdentityRepository.OnIdentityChangeListener, Searchable {

    private val _stateLiveData = MutableLiveData<State>()
    val stateLiveData: LiveData<State> = _stateLiveData

    private var state: State = State()
        set(value) {
            field = value
            _stateLiveData.postValue(value)
        }

    companion object {
        private const val TAG = "RankingsViewModel"
        private val NUMBER_FORMAT = NumberFormat.getInstance()
    }

    init {
        identityRepository.addListener(this)
    }

    @WorkerThread
    private fun createList(bundle: RankingsBundle?): List<ListItem>? {
        val rankings = bundle?.rankings

        if (rankings.isNullOrEmpty()) {
            return null
        }

        val previousRankSupported = rankings.any { it.previousRank != null }

        return bundle.rankings.map { player ->
            val previousRank = if (previousRankSupported) {
                checkNotNull(player.previousRank) {
                    "This is impossible here if RankedPlayerConverter does its job correctly."
                }

                if (player.rank == player.previousRank || player.previousRank == Int.MIN_VALUE) {
                    PreviousRank.INVISIBLE
                } else if (player.rank < player.previousRank) {
                    PreviousRank.INCREASE
                } else {
                    PreviousRank.DECREASE
                }
            } else {
                PreviousRank.GONE
            }

            ListItem.Player(
                    player = player,
                    isIdentity = identityRepository.isPlayer(player),
                    previousRank = previousRank,
                    rank = NUMBER_FORMAT.format(player.rank),
                    rating = player.rating.truncate()
            )
        }
    }

    fun fetchRankings(region: Region) {
        state = state.copy(isFetching = true)

        disposables.add(rankingsRepository.getRankings(region)
                .subscribe({ bundle ->
                    val list = createList(bundle)

                    state = state.copy(
                            hasError = false,
                            isEmpty = list.isNullOrEmpty(),
                            isFetching = false,
                            list = list,
                            searchResults = null,
                            rankingsBundle = bundle
                    )
                }, {
                    timber.e(TAG, "Error fetching rankings", it)

                    state = state.copy(
                            hasError = true,
                            isEmpty = false,
                            isFetching = false,
                            list = null,
                            searchResults = null,
                            rankingsBundle = null
                    )
                }))
    }

    override fun onCleared() {
        identityRepository.removeListener(this)
        super.onCleared()
    }

    override fun onIdentityChange(identityRepository: IdentityRepository) {
        threadUtils.background.submit {
            val list = createList(state.rankingsBundle)
            state = state.copy(list = list)
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

        val trimmedQuery = query.trim()

        return list.filterIsInstance(ListItem.Player::class.java)
                .filter { it.player.name.contains(trimmedQuery, true) }
    }

    sealed class ListItem {
        abstract val listId: Long

        class Player(
                val player: AbsPlayer,
                val isIdentity: Boolean,
                val previousRank: PreviousRank,
                val rank: String,
                val rating: String
        ) : ListItem() {
            override val listId: Long = player.hashCode().toLong()
        }
    }

    data class State(
            val hasError: Boolean = false,
            val isEmpty: Boolean = false,
            val isFetching: Boolean = false,
            val list: List<ListItem>? = null,
            val searchResults: List<ListItem>? = null,
            val rankingsBundle: RankingsBundle? = null
    )

}
