package com.garpr.android.managers

import android.content.Context

interface HomeToolbarManager {

    data class Presentation(
            val isActivityRequirementsVisible: Boolean = false,
            val isViewYourselfVisible: Boolean = false
    )

    fun getPresentation(context: Context): Presentation

}
