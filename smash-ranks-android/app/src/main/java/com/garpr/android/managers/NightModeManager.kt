package com.garpr.android.managers

import com.garpr.android.data.models.NightMode

interface NightModeManager {

    interface OnNightModeChangeListener {
        fun onNightModeChange(nightModeManager: NightModeManager)
    }

    fun addListener(listener: OnNightModeChangeListener)

    var nightMode: NightMode

    fun removeListener(listener: OnNightModeChangeListener?)

}
