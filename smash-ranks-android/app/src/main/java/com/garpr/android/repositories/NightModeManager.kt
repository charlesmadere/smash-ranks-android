package com.garpr.android.repositories

import android.content.Context
import com.garpr.android.data.models.NightMode

interface NightModeManager {

    interface OnNightModeChangeListener {
        fun onNightModeChange(nightModeManager: NightModeManager)
    }

    fun addListener(listener: OnNightModeChangeListener)

    fun getNightModeStrings(context: Context): Array<CharSequence>

    var nightMode: NightMode

    fun removeListener(listener: OnNightModeChangeListener?)

}
