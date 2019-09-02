package com.garpr.android.features.rankings

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.garpr.android.data.models.RankedPlayer
import com.garpr.android.data.models.RankingsBundle
import com.garpr.android.data.models.Region
import com.garpr.android.features.common.viewModels.BaseViewModel
import com.garpr.android.misc.Searchable
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.misc.Timber
import com.garpr.android.repositories.RankingsRepository
import javax.inject.Inject

class RankingsViewModel @Inject constructor(
        private val rankingsRepository: RankingsRepository,
        private val threadUtils: ThreadUtils,
        private val timber: Timber
) : BaseViewModel(), Searchable {

    private val _stateLiveData = MutableLiveData<State>()
    val stateLiveData: LiveData<State> = _stateLiveData

    private var state: State = State()
        set(value) {
            field = value
            _stateLiveData.postValue(value)
        }

    companion object {
        private const val TAG = "RankingsViewModel"
    }

    fun fetchRankings(region: Region) {
        state = state.copy(isFetching = true)

        disposables.add(rankingsRepository.getRankings(region)
                .subscribe({
                    state = state.copy(
                            isEmpty = it.rankings.isNullOrEmpty(),
                            isFetching = false,
                            hasError = false,
                            searchResults = null,
                            rankingsBundle = it
                    )
                }, {
                    timber.e(TAG, "Error fetching rankings", it)

                    state = state.copy(
                            isEmpty = false,
                            isFetching = false,
                            hasError = true,
                            searchResults = null,
                            rankingsBundle = null
                    )
                }))
    }

    override fun search(query: String?) {
        threadUtils.background.submit {
            val results = search(query, state.rankingsBundle?.rankings)
            state = state.copy(searchResults = results)
        }
    }

    @WorkerThread
    private fun search(query: String?, rankings: List<RankedPlayer>?): List<RankedPlayer>? {
        if (query.isNullOrBlank() || rankings.isNullOrEmpty()) {
            return null
        }

        val trimmedQuery = query.trim()

        return rankings.filter { player ->
            player.name.contains(trimmedQuery, true)
        }
    }

    data class State(
            val isEmpty: Boolean = false,
            val isFetching: Boolean = false,
            val hasError: Boolean = false,
            val searchResults: List<RankedPlayer>? = null,
            val rankingsBundle: RankingsBundle? = null
    )

}
