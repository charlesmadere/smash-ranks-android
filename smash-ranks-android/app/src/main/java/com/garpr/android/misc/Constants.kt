package com.garpr.android.misc

import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.Region

object Constants {

    // Defaults
    val DefaultRegion = Region(displayName = "Norcal", id = "norcal", endpoint = Endpoint.GAR_PR)

    // GAR PR and Not GAR PR Paths
    const val GAR_PR_API_PORT = 3001
    const val GAR_PR_BASE_PATH = "https://www.garpr.com"
    const val NOT_GAR_PR_API_PORT = 3001
    const val NOT_GAR_PR_BASE_PATH = "https://www.notgarpr.com"

    // Miscellaneous
    const val ERROR_CODE_BAD_REQUEST = 400
    const val ERROR_CODE_UNKNOWN = Integer.MAX_VALUE
    const val OTHER = "other"
    const val PLAIN_TEXT = "text/plain"
    const val TWITCH = "twitch"
    const val TWITTER = "twitter"
    const val YOUTUBE = "youtube"

    // Smash Roster Paths
    const val SMASH_ROSTER_BASE_PATH = "https://s3-us-west-1.amazonaws.com/smash-ranks-android-players/"
    const val SMASH_ROSTER_FORM_URL = "https://goo.gl/forms/UrNp2PVF1iQdPfFD3"

    // URLs
    const val CHARLES_TWITTER_URL = "https://twitter.com/charlesmadere"
    const val GAR_TWITTER_URL = "https://twitter.com/garsh0p"
    const val GITHUB_URL = "https://github.com/charlesmadere/smash-ranks-android"
    const val TSUAII_TWITTER_URL = "https://twitter.com/tsuaii"

}
