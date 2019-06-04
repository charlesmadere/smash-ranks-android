package com.garpr.android.features.player

import com.garpr.android.data.models.AbsRegion
import com.garpr.android.data.models.FullPlayer

interface PlayerProfileManager {

    data class Presentation(
            val isAddToFavoritesVisible: Boolean = false,
            val isViewYourselfVsThisOpponentVisible: Boolean = false,
            val aliases: CharSequence? = null,
            val mains: CharSequence? = null,
            val name: CharSequence? = null,
            val rating: CharSequence? = null,
            val tag: CharSequence = "",
            val unadjustedRating: CharSequence? = null,
            val avatar: String? = null,
            val otherWebsite: String? = null,
            val twitch: String? = null,
            val twitter: String? = null,
            val youTube: String? = null
    )

    fun getPresentation(player: FullPlayer, region: AbsRegion): Presentation

}
