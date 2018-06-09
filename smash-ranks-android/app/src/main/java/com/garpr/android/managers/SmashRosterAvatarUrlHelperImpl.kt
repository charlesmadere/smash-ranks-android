package com.garpr.android.managers

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

}
