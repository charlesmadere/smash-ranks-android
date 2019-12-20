package com.garpr.android.repositories

import com.garpr.android.data.models.NightMode

interface NightModeRepository {

    interface OnNightModeChangeListener {
        fun onNightModeChange(nightModeRepository: NightModeRepository)
    }

    var nightMode: NightMode

    fun addListener(listener: OnNightModeChangeListener)

    fun removeListener(listener: OnNightModeChangeListener?)

}
