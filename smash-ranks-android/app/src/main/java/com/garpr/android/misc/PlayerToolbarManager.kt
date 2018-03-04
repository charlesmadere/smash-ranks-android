package com.garpr.android.misc

import com.garpr.android.models.MatchResult
import com.garpr.android.models.MatchesBundle

interface PlayerToolbarManager {

    data class Presentation(
            val isFilterVisible: Boolean = false,
            val isFilterAllVisible: Boolean = false,
            val isFilterLossesVisible: Boolean = false,
            val isFilterWinsVisible: Boolean = false
    )

    fun getPresentation(matchesBundle: MatchesBundle?, matchResult: MatchResult?): Presentation

}
