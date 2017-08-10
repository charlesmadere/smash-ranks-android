package com.garpr.android.misc

import android.content.Context

interface HomeToolbarManager {

    class Presentation {
        var mIsActivityRequirementsVisible: Boolean = false
            internal set

        var mIsViewYourselfVisible: Boolean = false
            internal set
    }

    fun getPresentation(context: Context): Presentation

}
