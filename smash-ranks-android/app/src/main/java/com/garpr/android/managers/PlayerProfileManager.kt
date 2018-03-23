package com.garpr.android.managers

import com.garpr.android.models.AbsRegion
import com.garpr.android.models.FullPlayer

interface PlayerProfileManager {

    data class Presentation(
            val isAddToFavoritesVisible: Boolean = false,
            val isViewYourselfVsThisOpponentVisible: Boolean = false,
            val aliases: CharSequence? = null,
            val rating: CharSequence? = null,
            val unadjustedRating: CharSequence? = null
    )

    fun getPresentation(player: FullPlayer, region: AbsRegion): Presentation

}
