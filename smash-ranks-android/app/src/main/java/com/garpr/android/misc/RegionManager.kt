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

    fun getRegion(context: Context? = null): Region

    fun removeListener(listener: OnRegionChangeListener?)

    fun setRegion(region: Region)

}
