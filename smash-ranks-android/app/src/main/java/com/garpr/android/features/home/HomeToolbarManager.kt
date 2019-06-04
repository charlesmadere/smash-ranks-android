package com.garpr.android.features.home

import com.garpr.android.data.models.RankingCriteria

interface HomeToolbarManager {

    data class Presentation(
            val isActivityRequirementsVisible: Boolean = false,
            val isViewYourselfVisible: Boolean = false
    )

    fun getPresentation(rankingCriteria: RankingCriteria?): Presentation

}
