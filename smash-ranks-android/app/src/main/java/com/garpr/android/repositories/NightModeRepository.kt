package com.garpr.android.repositories

import android.content.Context
import com.garpr.android.data.models.NightMode

interface NightModeRepository {

    interface OnNightModeChangeListener {
        fun onNightModeChange(nightModeRepository: NightModeRepository)
    }

    fun addListener(listener: OnNightModeChangeListener)

    fun getNightModeStrings(context: Context): Array<CharSequence>

    var nightMode: NightMode

    fun removeListener(listener: OnNightModeChangeListener?)

}
