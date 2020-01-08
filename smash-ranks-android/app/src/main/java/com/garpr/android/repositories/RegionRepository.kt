package com.garpr.android.repositories

import com.garpr.android.data.models.Region
import io.reactivex.Observable

interface RegionRepository {

    var region: Region

    val observable: Observable<Region>

}
