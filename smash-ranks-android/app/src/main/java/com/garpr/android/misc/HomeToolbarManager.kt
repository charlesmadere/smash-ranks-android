package com.garpr.android.misc

import android.content.Context

interface HomeToolbarManager {

    class Presentation(
            val isActivityRequirementsVisible: Boolean,
            val isViewYourselfVisible: Boolean
    )

    fun getPresentation(context: Context): Presentation

}
