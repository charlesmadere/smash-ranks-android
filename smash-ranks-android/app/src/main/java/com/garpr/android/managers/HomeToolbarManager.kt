package com.garpr.android.managers

import com.garpr.android.models.RankingCriteria

interface HomeToolbarManager {

    data class Presentation(
            val isActivityRequirementsVisible: Boolean = false,
            val isViewYourselfVisible: Boolean = false
    )

    fun getPresentation(rankingCriteria: RankingCriteria?): Presentation

}
