package com.garpr.android.misc

import com.garpr.android.models.FullPlayer
import com.garpr.android.models.MatchResult
import com.garpr.android.models.MatchesBundle

interface PlayerToolbarManager {

    class Presentation {
        var isAddToFavoritesVisible: Boolean = false
            internal set

        var isAliasesVisible: Boolean = false
            internal set

        var isFilterVisible: Boolean = false
            internal set

        var isFilterAllVisible: Boolean = false
            internal set

        var isFilterLossesVisible: Boolean = false
            internal set

        var isFilterWinsVisible: Boolean = false
            internal set

        var isRemoveFromFavoritesVisible: Boolean = false
            internal set

        var isSetAsYourIdentityVisible: Boolean = false
            internal set

        var isShareVisible: Boolean = false
            internal set

        var isViewYourselfVsThisOpponentVisible: Boolean = false
            internal set
    }

    fun getPresentation(fullPlayer: FullPlayer?, matchesBundle: MatchesBundle?,
            matchResult: MatchResult?): Presentation

}
