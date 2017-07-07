package com.garpr.android.misc

import android.content.Context

import com.garpr.android.models.Region

interface RegionManager {

    interface OnRegionChangeListener {
        fun onRegionChange(regionManager: RegionManager)
    }

    interface RegionHandle {
        val currentRegion: Region?
    }

    fun addListener(listener: OnRegionChangeListener)

    var region: Region

    fun getRegion(context: Context?): Region

    fun removeListener(listener: OnRegionChangeListener?)

}
