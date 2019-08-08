package com.garpr.android.features.setRegion

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.garpr.android.data.models.AbsRegion
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.Region
import com.garpr.android.data.models.RegionsBundle
import com.garpr.android.features.common.viewModels.BaseViewModel
import com.garpr.android.misc.Timber
import com.garpr.android.repositories.RegionRepository
import com.garpr.android.repositories.RegionsRepository
import java.util.Collections
import javax.inject.Inject

class SetRegionViewModel @Inject constructor(
        private val regionRepository: RegionRepository,
        private val regionsRepository: RegionsRepository,
        private val timber: Timber
) : BaseViewModel() {

    val warnBeforeClose: Boolean
        get() = state.saveIconStatus == SaveIconStatus.ENABLED

    var selectedRegion: Region
        get() = state.selectedRegion ?: regionRepository.getRegion()
        set(value) {
            val saveIconStatus = if (state.list.isNullOrEmpty()) {
                SaveIconStatus.GONE
            } else if (value == regionRepository.getRegion()) {
                SaveIconStatus.VISIBLE
            } else {
                SaveIconStatus.ENABLED
            }

            state = state.copy(
                    selectedRegion = value,
                    saveIconStatus = saveIconStatus
            )
        }

    private val _stateLiveData = MutableLiveData<State>()
    val stateLiveData: LiveData<State> = _stateLiveData

    private var state: State = State()
        set(value) {
            field = value
            _stateLiveData.postValue(value)
        }

    companion object {
        private const val TAG = "SetRegionViewModel"
    }

    @WorkerThread
    private fun createList(bundle: RegionsBundle?): List<ListItem>? {
        val regions = bundle?.regions

        if (regions.isNullOrEmpty()) {
            return null
        }

        val regionsCopy = mutableListOf<Region>()
        regionsCopy.addAll(regions
                .filterIsInstance(Region::class.java)
                .filter { it.isActive })

        if (regionsCopy.isNullOrEmpty()) {
            return null
        }

        Collections.sort(regionsCopy, AbsRegion.ENDPOINT_ORDER)

        val list = mutableListOf<ListItem>()
        var endpoint: Endpoint? = null

        regionsCopy.forEach { region ->
            if (region.endpoint != endpoint) {
                endpoint = region.endpoint
                list.add(ListItem.Endpoint(region.endpoint))
            }

            list.add(ListItem.Region(region))
        }

        return list
    }

    private fun errorState() {
        state = state.copy(
                isFetching = false,
                isRefreshEnabled = true,
                hasError = true,
                list = null,
                selectedRegion = null,
                saveIconStatus = SaveIconStatus.GONE
        )
    }

    fun fetchRegions() {
        state = state.copy(isFetching = true)

        disposables.add(regionsRepository.getRegions()
                .subscribe({
                    val list = createList(it)

                    if (list.isNullOrEmpty()) {
                        timber.e(TAG, "Error fetching regions")
                        errorState()
                    } else {
                        state = state.copy(
                                isFetching = false,
                                isRefreshEnabled = false,
                                hasError = false,
                                list = list,
                                selectedRegion = regionRepository.getRegion(),
                                saveIconStatus = SaveIconStatus.VISIBLE
                        )
                    }
                }, {
                    timber.e(TAG, "Error fetching regions", it)
                    errorState()
                }))
    }

    fun saveSelectedRegion() {
        val region = selectedRegion

        if (region == regionRepository.getRegion()) {
            throw IllegalStateException("region is the same as the user's current region!")
        }

        regionRepository.setRegion(region)
    }

    sealed class ListItem {
        abstract val listId: Long

        class Endpoint(
                val endpoint: com.garpr.android.data.models.Endpoint
        ) : ListItem() {
            override val listId: Long = Long.MIN_VALUE + endpoint.ordinal.toLong()
        }

        class Region(
                val region: com.garpr.android.data.models.Region
        ) : ListItem() {
            override val listId: Long = region.hashCode().toLong()
        }
    }

    data class State(
            val isFetching: Boolean = false,
            val isRefreshEnabled: Boolean = true,
            val hasError: Boolean = false,
            val list: List<ListItem>? = null,
            val selectedRegion: Region? = null,
            val saveIconStatus: SaveIconStatus = SaveIconStatus.GONE
    )

    enum class SaveIconStatus {
        GONE, VISIBLE, ENABLED
    }

}