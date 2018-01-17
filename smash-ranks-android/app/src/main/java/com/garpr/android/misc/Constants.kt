package com.garpr.android.misc

import com.garpr.android.models.Endpoint
import com.garpr.android.models.Region

object Constants {

    // Defaults
    // TODO maybe this should be retrieved from the server via a splash screen...
    val DefaultRegion = Region(true, 45, 2,
            1000, "Norcal", "norcal", Endpoint.GAR_PR)

    // GAR PR Paths
    const val GAR_PR_API_PORT = 3001
    const val GAR_PR_BASE_PATH = "https://www.garpr.com"

    // Miscellaneous
    const val ERROR_CODE_BAD_REQUEST = 400
    const val ERROR_CODE_UNKNOWN = Integer.MAX_VALUE
    const val CHARLES_TWITTER_URL = "https://twitter.com/charlesmadere"
    const val GAR_TWITTER_URL = "https://twitter.com/garsh0p"
    const val GITHUB_URL = "https://github.com/charlesmadere/smash-ranks-android"
    const val PLAIN_TEXT = "text/plain"

    // NOT GAR PR Paths
    const val NOT_GAR_PR_API_PORT = 3001
    const val NOT_GAR_PR_BASE_PATH = "https://www.notgarpr.com"

    // Smash Roster Paths
    const val SMASH_ROSTER_BASE_PATH = "https://raw.githubusercontent.com/charlesmadere/smash-ranks-android-players/"

}
