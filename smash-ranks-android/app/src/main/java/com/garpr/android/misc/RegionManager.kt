package com.garpr.android.misc

import android.content.Context
import com.garpr.android.models.AbsRegion

import com.garpr.android.models.LiteRegion

interface RegionManager {

    interface OnRegionChangeListener {
        fun onRegionChange(regionManager: RegionManager)
    }

    interface RegionHandle {
        val currentRegion: AbsRegion?
    }

    fun addListener(listener: OnRegionChangeListener)

    fun getRegion(context: Context?): AbsRegion

    var region: LiteRegion

    fun removeListener(listener: OnRegionChangeListener?)

}
