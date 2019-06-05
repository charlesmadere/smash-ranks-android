package com.garpr.android.features.rankings

import com.garpr.android.features.common.viewModels.BaseViewModel
import com.garpr.android.networking.ServerApi
import com.garpr.android.repositories.RegionRepository
import javax.inject.Inject

class RankingsViewModel @Inject constructor(
        private val regionRepository: RegionRepository,
        private val serverApi: ServerApi
) : BaseViewModel() {

    init {

    }

}
