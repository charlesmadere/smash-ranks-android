package com.garpr.android.repositories

import android.content.Context
import com.garpr.android.data.models.Region
import io.reactivex.Observable

interface RegionRepository {

    interface RegionHandle {
        val currentRegion: Region?
    }

    val observable: Observable<Region>

    fun getRegion(context: Context? = null): Region

    fun setRegion(region: Region)

}
