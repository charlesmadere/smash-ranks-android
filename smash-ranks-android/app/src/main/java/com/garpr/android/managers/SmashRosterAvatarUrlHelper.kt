package com.garpr.android.managers

import com.garpr.android.models.SmashCompetitor

interface SmashRosterAvatarUrlHelper {

    fun getAvatarUrl(avatarPath: String?): String?

    fun getLargeAvatarUrl(smashCompetitor: SmashCompetitor?): String?

    fun getMediumAvatarUrl(smashCompetitor: SmashCompetitor?): String?

    fun getOriginalAvatarUrl(smashCompetitor: SmashCompetitor?): String?

    fun getSmallAvatarUrl(smashCompetitor: SmashCompetitor?): String?

}
