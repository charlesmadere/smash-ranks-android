package com.garpr.android.misc

import android.content.Context

interface HomeToolbarManager {

    data class Presentation(
            val isActivityRequirementsVisible: Boolean = false,
            val isViewYourselfVisible: Boolean = false
    )

    fun getPresentation(context: Context): Presentation

}
