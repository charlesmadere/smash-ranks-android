package com.garpr.android.managers

import com.garpr.android.models.SmashCompetitor

class SmashRosterAvatarUrlHelperImpl(
    private val baseUrl: String
) : SmashRosterAvatarUrlHelper {

    override fun getAvatarUrl(avatarPath: String?): String? {
        return if (avatarPath?.isNotBlank() == true) {
            baseUrl + avatarPath
        } else {
            null
        }
    }

    override fun getLargeAvatarUrl(smashCompetitor: SmashCompetitor?): String? {
        return getAvatarUrl(smashCompetitor?.avatar?.large)
    }

    override fun getMediumAvatarUrl(smashCompetitor: SmashCompetitor?): String? {
        return getAvatarUrl(smashCompetitor?.avatar?.medium)
    }

    override fun getOriginalAvatarUrl(smashCompetitor: SmashCompetitor?): String? {
        return getAvatarUrl(smashCompetitor?.avatar?.original)
    }

    override fun getSmallAvatarUrl(smashCompetitor: SmashCompetitor?): String? {
        return getAvatarUrl(smashCompetitor?.avatar?.small)
    }

}
