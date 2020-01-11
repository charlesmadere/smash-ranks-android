package com.garpr.android.features.player

import com.garpr.android.data.models.AbsRegion
import com.garpr.android.data.models.FullPlayer
import com.garpr.android.data.models.SmashCompetitor

interface PlayerProfileManager {

    data class Presentation(
            val isAddToFavoritesVisible: Boolean = false,
            val isCompareVisible: Boolean = false,
            val aliases: CharSequence? = null,
            val mains: CharSequence? = null,
            val name: CharSequence? = null,
            val rating: CharSequence? = null,
            val tag: CharSequence = "",
            val unadjustedRating: CharSequence? = null,
            val avatar: String? = null,
            val twitch: String? = null,
            val twitter: String? = null,
            val youTube: String? = null
    )

    fun getPresentation(region: AbsRegion, isFavorited: Boolean, isIdentity: Boolean,
            player: FullPlayer, smashCompetitor: SmashCompetitor? = null): Presentation

}
