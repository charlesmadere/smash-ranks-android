package com.garpr.android.features.home

import com.garpr.android.data.models.AbsRegion

interface HomeToolbarManager {

    data class Presentation(
            val isActivityRequirementsVisible: Boolean = false,
            val isViewYourselfVisible: Boolean = false
    )

    fun getPresentation(region: AbsRegion?): Presentation

}
