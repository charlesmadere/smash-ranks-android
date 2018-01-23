package com.garpr.android.misc

import com.garpr.android.models.FullPlayer
import com.garpr.android.models.MatchResult
import com.garpr.android.models.MatchesBundle

interface PlayerToolbarManager {

    data class Presentation(
            val isAddToFavoritesVisible: Boolean = false,
            val isAliasesVisible: Boolean = false,
            val isFilterVisible: Boolean = false,
            val isFilterAllVisible: Boolean = false,
            val isFilterLossesVisible: Boolean = false,
            val isFilterWinsVisible: Boolean = false,
            val isRemoveFromFavoritesVisible: Boolean = false,
            val isSetAsYourIdentityVisible: Boolean = false,
            val isShareVisible: Boolean = false,
            val isViewYourselfVsThisOpponentVisible: Boolean = false
    )

    fun getPresentation(fullPlayer: FullPlayer?, matchesBundle: MatchesBundle?,
            matchResult: MatchResult?): Presentation

}
