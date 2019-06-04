package com.garpr.android.repositories

import android.content.Context
import com.garpr.android.data.models.Region

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
