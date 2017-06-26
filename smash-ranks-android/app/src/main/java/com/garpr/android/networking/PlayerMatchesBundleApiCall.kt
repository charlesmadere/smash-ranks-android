package com.garpr.android.networking

import com.garpr.android.misc.Constants
import com.garpr.android.misc.Heartbeat
import com.garpr.android.models.FullPlayer
import com.garpr.android.models.MatchesBundle
import com.garpr.android.models.PlayerMatchesBundle
import com.garpr.android.models.Region

class PlayerMatchesBundleApiCall(
        private val listener: ApiListener<PlayerMatchesBundle>,
        private val region: Region,
        private val serverApi: ServerApi,
        private val playerId: String
) : Heartbeat {

    private var fullPlayer: FullPlayer? = null
    private var fullPlayerFound = false
    private var fullPlayerErrorCode: Int? = null
    private var matchesBundle: MatchesBundle? = null
    private var matchesBundleFound = false
    private var matchesBundleErrorCode: Int? = null


    fun fetch() {
        serverApi.getMatches(region, playerId, matchesBundleListener)
        serverApi.getPlayer(region, playerId, fullPlayerListener)
    }

    override fun isAlive(): Boolean {
        return listener.isAlive
    }

    @Synchronized
    private fun proceed() {
        if (!fullPlayerFound || !matchesBundleFound) {
            return
        }

        if (fullPlayerErrorCode == Constants.ERROR_CODE_BAD_REQUEST ||
                matchesBundleErrorCode == Constants.ERROR_CODE_BAD_REQUEST) {
            listener.failure(Constants.ERROR_CODE_BAD_REQUEST)
            return
        }

        val fullPlayer = fullPlayer

        if (fullPlayer == null) {
            listener.failure(Constants.ERROR_CODE_UNKNOWN)
        } else {
            val playerMatchesBundle = PlayerMatchesBundle()
            playerMatchesBundle.fullPlayer = fullPlayer
            playerMatchesBundle.matchesBundle = matchesBundle
            listener.success(playerMatchesBundle)
        }
    }

    private val fullPlayerListener = object : ApiListener<FullPlayer> {
        override fun failure(errorCode: Int) {
            fullPlayerErrorCode = errorCode
            fullPlayer = null
            fullPlayerFound = true
            proceed()
        }

        override fun isAlive(): Boolean {
            return this@PlayerMatchesBundleApiCall.isAlive
        }

        override fun success(`object`: FullPlayer?) {
            fullPlayer = `object`
            fullPlayerFound = true
            proceed()
        }
    }

    private val matchesBundleListener = object : ApiListener<MatchesBundle> {
        override fun failure(errorCode: Int) {
            matchesBundleErrorCode = errorCode
            matchesBundle = null
            matchesBundleFound = true
            proceed()
        }

        override fun isAlive(): Boolean {
            return this@PlayerMatchesBundleApiCall.isAlive
        }

        override fun success(`object`: MatchesBundle?) {
            matchesBundle = `object`
            matchesBundleFound = true
            proceed()
        }
    }

}
