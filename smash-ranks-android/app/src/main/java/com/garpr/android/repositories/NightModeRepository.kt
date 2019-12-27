package com.garpr.android.repositories

import com.garpr.android.data.models.NightMode
import io.reactivex.Observable

interface NightModeRepository {

    var nightMode: NightMode

    val observable: Observable<NightMode>

}
