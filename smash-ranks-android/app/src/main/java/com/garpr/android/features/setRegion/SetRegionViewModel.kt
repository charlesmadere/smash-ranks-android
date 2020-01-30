package com.garpr.android.features.setRegion

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.garpr.android.data.models.AbsRegion
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.Region
import com.garpr.android.data.models.RegionsBundle
import com.garpr.android.extensions.require
import com.garpr.android.features.common.viewModels.BaseViewModel
import com.garpr.android.misc.Schedulers
import com.garpr.android.misc.Timber
import com.garpr.android.repositories.RegionRepository
import com.garpr.android.repositories.RegionsRepository
import java.util.Collections
import com.garpr.android.data.models.Endpoint as GarPrEndpoint
import com.garpr.android.data.models.Region as GarPrRegion

class SetRegionViewModel(
        private val regionRepository: RegionRepository,
        private val regionsRepository: RegionsRepository,
        private val schedulers: Schedulers,
        private val timber: Timber
) : BaseViewModel() {

    val warnBeforeClose: Boolean
        get() = state.saveIconStatus == SaveIconStatus.ENABLED

    var selectedRegion: Region
        get() = state.selectedRegion ?: regionRepository.region
        set(value) {
            val saveIconStatus = if (state.list.isNullOrEmpty()) {
                SaveIconStatus.GONE
            } else if (value == regionRepository.region) {
                SaveIconStatus.DISABLED
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

    @WorkerThread
    private fun createList(bundle: RegionsBundle?): List<ListItem>? {
        val regions = bundle?.regions

        if (regions.isNullOrEmpty()) {
            return null
        }

        val filteredRegions = regions.filterIsInstance(Region::class.java)
                .filter { region -> region.isActive }

        if (filteredRegions.isNullOrEmpty()) {
            return null
        }

        Collections.sort(filteredRegions, AbsRegion.ENDPOINT_ORDER)

        val listItems = mutableListOf<ListItem>()
        val endpoints = mutableMapOf<Endpoint, Boolean>()

        Endpoint.values().forEach { endpoint ->
            endpoints.put(endpoint, false)
        }

        filteredRegions.forEach { region ->
            if (!endpoints.require(region.endpoint)) {
                endpoints[region.endpoint] = true
                listItems.add(ListItem.Endpoint(region.endpoint))
            }

            listItems.add(ListItem.Region(region))
        }

        endpoints.filter { entry -> !entry.value }
                .forEach { (endpoint, _) ->
                    listItems.add(ListItem.Endpoint(endpoint))
                    listItems.add(ListItem.EndpointError(endpoint))
                }

        return listItems
    }

    private fun errorState() {
        state = state.copy(
                hasError = true,
                isFetching = false,
                isRefreshEnabled = true,
                list = null,
                selectedRegion = null,
                saveIconStatus = SaveIconStatus.GONE
        )
    }

    fun fetchRegions() {
        state = state.copy(isFetching = true)

        disposables.add(regionsRepository.getRegions()
                .subscribeOn(schedulers.background)
                .observeOn(schedulers.background)
                .subscribe({ bundle ->
                    val list = createList(bundle)

                    if (list.isNullOrEmpty()) {
                        timber.e(TAG, "Error fetching regions")
                        errorState()
                    } else {
                        state = state.copy(
                                hasError = false,
                                isFetching = false,
                                isRefreshEnabled = false,
                                list = list,
                                selectedRegion = regionRepository.region,
                                saveIconStatus = SaveIconStatus.DISABLED
                        )
                    }
                }, {
                    timber.e(TAG, "Error fetching regions", it)
                    errorState()
                }))
    }

    fun saveSelectedRegion() {
        val region = selectedRegion

        require(region != regionRepository.region) {
            "region is the same as the user's current region!"
        }

        regionRepository.region = region
    }

    companion object {
        private const val TAG = "SetRegionViewModel"
    }

    sealed class ListItem {
        abstract val listId: Long

        class Endpoint(
                val endpoint: GarPrEndpoint
        ) : ListItem() {
            override val listId: Long = Long.MIN_VALUE + endpoint.ordinal.toLong()
        }

        class EndpointError(
                val endpoint: GarPrEndpoint
        ) : ListItem() {
            override val listId: Long = Long.MAX_VALUE - endpoint.ordinal.toLong()
        }

        class Region(
                val region: GarPrRegion
        ) : ListItem() {
            override val listId: Long = region.hashCode().toLong()
        }
    }

    enum class SaveIconStatus {
        DISABLED, ENABLED, GONE
    }

    data class State(
            val hasError: Boolean = false,
            val isFetching: Boolean = false,
            val isRefreshEnabled: Boolean = true,
            val list: List<ListItem>? = null,
            val selectedRegion: Region? = null,
            val saveIconStatus: SaveIconStatus = SaveIconStatus.GONE
    )

}
