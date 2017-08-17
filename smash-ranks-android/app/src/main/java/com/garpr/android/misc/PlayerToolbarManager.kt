package com.garpr.android.misc

import android.content.res.Resources
import com.garpr.android.models.FullPlayer
import com.garpr.android.models.Match
import com.garpr.android.models.MatchesBundle

interface PlayerToolbarManager {

    class Presentation {
        var mIsAddToFavoritesVisible: Boolean = false
            internal set

        var mIsAliasesVisible: Boolean = false
            internal set

        var mIsFilterVisible: Boolean = false
            internal set

        var mIsFilterAllVisible: Boolean = false
            internal set

        var mIsFilterLossesVisible: Boolean = false
            internal set

        var mIsFilterWinsVisible: Boolean = false
            internal set

        var mIsRemoveFromFavoritesVisible: Boolean = false
            internal set

        var mIsSetAsYourIdentityVisible: Boolean = false
            internal set

        var mIsShareVisible: Boolean = false
            internal set

        var mIsViewYourselfVsThisOpponentVisible: Boolean = false
            internal set

        var mSetAsYourIdentityTitle: CharSequence? = null
            internal set

        var mViewYourselfVsThisOpponentTitle: CharSequence? = null
            internal set
    }

    fun getPresentation(resources: Resources, fullPlayer: FullPlayer?,
            matchesBundle: MatchesBundle?, matchResult: Match.Result?): Presentation

}
