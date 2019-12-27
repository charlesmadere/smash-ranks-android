package com.garpr.android.features.home.shareRegion

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.garpr.android.features.common.viewModels.BaseViewModel
import com.garpr.android.misc.Schedulers
import com.garpr.android.repositories.RegionRepository

class ShareRegionViewModel(
        private val regionRepository: RegionRepository,
        private val schedulers: Schedulers
) : BaseViewModel() {

    private val _unitLiveData = MutableLiveData<Unit>()
    val unitLiveData: LiveData<Unit> = _unitLiveData

    init {
        initListeners()
    }

    private fun initListeners() {
        disposables.add(regionRepository.observable
                .subscribeOn(schedulers.background)
                .observeOn(schedulers.background)
                .subscribe {
                    _unitLiveData.postValue(Unit)
                })
    }

}
