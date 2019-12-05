package com.garpr.android.repositories

import com.garpr.android.data.models.RegionsBundle
import io.reactivex.Single

interface RegionsRepository {

    fun getRegions(): Single<RegionsBundle>

}
