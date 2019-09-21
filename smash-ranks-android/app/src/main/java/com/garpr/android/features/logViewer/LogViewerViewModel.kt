package com.garpr.android.features.logViewer

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.garpr.android.features.common.viewModels.BaseViewModel
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.misc.Timber

class LogViewerViewModel(
        private val threadUtils: ThreadUtils,
        private val timber: Timber
) : BaseViewModel() {

    private val _stateLiveData = MutableLiveData<State>()
    val stateLiveData: LiveData<State> = _stateLiveData

    private var state: State = State()
        set(value) {
            field = value
            _stateLiveData.postValue(value)
        }

    fun clearEntries() {
        state = state.copy(isFetching = true)

        threadUtils.background.submit {
            timber.clearEntries()
            fetchEntriesAndUpdateState()
        }
    }

    fun fetchEntries() {
        state = state.copy(isFetching = true)

        threadUtils.background.submit {
            fetchEntriesAndUpdateState()
        }
    }

    @WorkerThread
    private fun fetchEntriesAndUpdateState() {
        val entries = timber.entries

        state = state.copy(
                isEmpty = entries.isNullOrEmpty(),
                isFetching = false,
                entries = entries
        )
    }

    data class State(
            val isEmpty: Boolean = false,
            val isFetching: Boolean = false,
            val entries: List<Timber.Entry>? = null
    )

}
