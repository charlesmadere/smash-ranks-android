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
import java.util.Collections

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

    @WorkerThread
    private fun createList(bundle: TournamentsBundle?): List<ListItem>? {
        val tournaments = bundle?.tournaments

        return if (tournaments.isNullOrEmpty()) {
            null
        } else {
            tournaments.map { tournament ->
                ListItem.Tournament(
                        tournament = tournament
                )
            }
        }
    }

    fun fetchTournaments(region: Region) {
        state = state.copy(isFetching = true)

        disposables.add(tournamentsRepository.getTournaments(region)
                .subscribeOn(schedulers.background)
                .observeOn(schedulers.background)
                .subscribe({ bundle ->
                    val list = createList(bundle)

                    state = state.copy(
                            hasError = false,
                            isEmpty = list.isNullOrEmpty(),
                            isFetching = false,
                            list = list,
                            searchResults = null,
                            tournamentsBundle = bundle
                    )
                }, {
                    timber.e(TAG, "Error fetching tournaments", it)

                    state = state.copy(
                            hasError = true,
                            isEmpty = false,
                            isFetching = false,
                            list = null,
                            searchResults = null,
                            tournamentsBundle = null
                    )
                }))
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

        val results = list
                .filterIsInstance(ListItem.Tournament::class.java)
                .filter { listItem ->
                    listItem.tournament.name.contains(trimmedQuery, ignoreCase = true)
                }

        return if (results.isEmpty()) {
            Collections.singletonList(ListItem.NoResults(trimmedQuery))
        } else {
            results
        }
    }

    companion object {
        private const val TAG = "TournamentsViewModel"
    }

    sealed class ListItem {
        abstract val listId: Long

        class NoResults(
                val query: String
        ) : ListItem() {
            override val listId: Long = Long.MIN_VALUE + 1L
        }

        class Tournament(
                val tournament: AbsTournament
        ) : ListItem() {
            override val listId: Long = tournament.hashCode().toLong()
        }
    }

    data class State(
            val hasError: Boolean = false,
            val isEmpty: Boolean = false,
            val isFetching: Boolean = false,
            val list: List<ListItem>? = null,
            val searchResults: List<ListItem>? = null,
            val tournamentsBundle: TournamentsBundle? = null
    )

}
