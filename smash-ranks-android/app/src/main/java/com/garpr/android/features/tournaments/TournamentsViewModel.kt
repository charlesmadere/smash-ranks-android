package com.garpr.android.features.tournaments

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.garpr.android.data.models.AbsTournament
import com.garpr.android.data.models.Region
import com.garpr.android.data.models.TournamentsBundle
import com.garpr.android.features.common.viewModels.BaseViewModel
import com.garpr.android.misc.Schedulers
import com.garpr.android.misc.Searchable
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.misc.Timber
import com.garpr.android.repositories.TournamentsRepository

class TournamentsViewModel(
        private val schedulers: Schedulers,
        private val threadUtils: ThreadUtils,
        private val timber: Timber,
        private val tournamentsRepository: TournamentsRepository
) : BaseViewModel(), Searchable {

    private val _stateLiveData = MutableLiveData<State>()
    val stateLiveData: LiveData<State> = _stateLiveData

    private var state: State = State()
        set(value) {
            field = value
            _stateLiveData.postValue(value)
        }

    companion object {
        private const val TAG = "TournamentsViewModel"
    }

    fun fetchTournaments(region: Region) {
        state = state.copy(isFetching = true)

        disposables.add(tournamentsRepository.getTournaments(region)
                .observeOn(schedulers.background)
                .subscribe({ bundle ->
                    state = state.copy(
                            isEmpty = bundle.tournaments.isNullOrEmpty(),
                            isFetching = false,
                            hasError = false,
                            searchResults = null,
                            tournamentsBundle = bundle
                    )
                }, {
                    timber.e(TAG, "Error fetching tournaments", it)

                    state = state.copy(
                            isEmpty = false,
                            isFetching = false,
                            hasError = true,
                            searchResults = null,
                            tournamentsBundle = null
                    )
                }))
    }

    override fun search(query: String?) {
        threadUtils.background.submit {
            val results = search(query, state.tournamentsBundle?.tournaments)
            state = state.copy(searchResults = results)
        }
    }

    @WorkerThread
    private fun search(query: String?, tournaments: List<AbsTournament>?): List<AbsTournament>? {
        if (query.isNullOrBlank() || tournaments.isNullOrEmpty()) {
            return null
        }

        val trimmedQuery = query.trim()

        return tournaments.filter { tournament ->
            tournament.name.contains(trimmedQuery, true)
        }
    }

    data class State(
            val hasError: Boolean = false,
            val isEmpty: Boolean = false,
            val isFetching: Boolean = false,
            val searchResults: List<AbsTournament>? = null,
            val tournamentsBundle: TournamentsBundle? = null
    )

}
