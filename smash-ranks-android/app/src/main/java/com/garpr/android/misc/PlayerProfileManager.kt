package com.garpr.android.misc

import com.garpr.android.models.AbsRegion
import com.garpr.android.models.FullPlayer

interface PlayerProfileManager {

    data class Presentation(
            val isAddToFavoritesVisible: Boolean = false,
            val isRatingVisible: Boolean = false,
            val isRemoveFromFavoritesVisible: Boolean = false,
            val isViewYourselfVsThisOpponentVisible: Boolean = false,
            val aliases: CharSequence? = null
    )

    fun getPresentation(player: FullPlayer, region: AbsRegion): Presentation

}
