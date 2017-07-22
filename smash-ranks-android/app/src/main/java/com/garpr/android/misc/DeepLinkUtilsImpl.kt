package com.garpr.android.misc

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import com.garpr.android.activities.*
import com.garpr.android.models.Endpoint
import com.garpr.android.models.Region
import com.garpr.android.models.RegionsBundle
import java.util.*

class DeepLinkUtilsImpl(
        private val mRegionManager: RegionManager,
        private val mTimber: Timber
) : DeepLinkUtils {

    companion object {
        private const val TAG = "DeepLinkUtilsImpl"

        private const val PLAYERS = "players"
        private const val RANKINGS = "rankings"
        private const val TOURNAMENTS = "tournaments"

        // Players
        // https://www.garpr.com/#/norcal/players
        // https://www.notgarpr.com/#/newjersey/players

        // Rankings
        // https://www.garpr.com/#/norcal/rankings
        // https://www.notgarpr.com/#/nyc/rankings

        // apollo iii
        // https://www.notgarpr.com/#/nyc/tournaments/58c72c801d41c8259fa1f8bf

        // Norcal Validated 2
        // https://www.garpr.com/#/norcal/tournaments/58a00514d2994e4d0f2e25a6

        // rubicon 12
        // https://www.notgarpr.com/#/chicago/tournaments/579839b0e592573cf1845f46

        // SFAT
        // https://www.garpr.com/#/norcal/players/588852e8d2994e3bbfa52d88

        // Swedish Delight
        // https://www.notgarpr.com/#/nyc/players/545b240b8ab65f7a95f74940
    }

    override fun buildIntentStack(context: Context, intent: Intent?, region: Region): List<Intent>? {
        if (intent == null) {
            mTimber.d(TAG, "Can't deep link, Intent is null")
            return null
        } else {
            return buildIntentStack(context, intent.data, region)
        }
    }

    override fun buildIntentStack(context: Context, uri: String?, region: Region): List<Intent>? {
        if (uri == null || TextUtils.isEmpty(uri) || TextUtils.getTrimmedLength(uri) == 0) {
            mTimber.d(TAG, "Can't deep link, uri is null / empty / whitespace")
            return null
        }

        mTimber.d(TAG, "Attempting to deep link to \"" + uri + "\"")

        val endpoint = getEndpoint(uri)

        if (endpoint == null) {
            mTimber.e(TAG, "Deep link path isn't for GAR PR")
            return null
        }

        val path = uri.substring(endpoint.webPath.length, uri.length)

        if (TextUtils.isEmpty(path) || TextUtils.getTrimmedLength(path) == 0) {
            mTimber.d(TAG, "Deep link path is null / empty / whitespace")
            return null
        }

        val splits = path.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        if (splits.isEmpty()) {
            mTimber.d(TAG, "Deep link's path split is empty")
            return null
        }

        val regionId = splits[0]

        if (TextUtils.isEmpty(regionId) || TextUtils.getTrimmedLength(regionId) == 0) {
            mTimber.w(TAG, "Region ID is null / empty / whitespace")
            return null
        }

        val sameRegion = regionId.equals(mRegionManager.region.id, ignoreCase = true)

        if (sameRegion && splits.size == 1) {
            return null
        }

        val intentStack = ArrayList<Intent>()
        val page = splits[1]

        if (PLAYERS.equals(page, ignoreCase = true)) {
            buildPlayersIntentStack(context, intentStack, region, sameRegion, splits)
        } else if (RANKINGS.equals(page, ignoreCase = true)) {
            buildRankingsIntentStack(context, intentStack, region, sameRegion)
        } else if (TOURNAMENTS.equals(page, ignoreCase = true)) {
            buildTournamentsIntentStack(context, intentStack, region, sameRegion, splits)
        } else {
            mTimber.w(TAG, "Unknown page \"" + page + "\"")
        }

        return intentStack
    }

    override fun buildIntentStack(context: Context, uri: Uri?, region: Region): List<Intent>? {
        if (uri == null) {
            mTimber.d(TAG, "Can't deep link, Uri is null")
            return null
        } else {
            return buildIntentStack(context, uri.toString(), region)
        }
    }

    private fun buildPlayersIntentStack(context: Context, intentStack: MutableList<Intent>,
            region: Region, sameRegion: Boolean, splits: Array<String>) {
        intentStack.add(HomeActivity.getLaunchIntent(context))

        if (sameRegion) {
            intentStack.add(PlayersActivity.getLaunchIntent(context))
        } else {
            intentStack.add(PlayersActivity.getLaunchIntent(context, region))
        }

        if (splits.size < 3) {
            return
        }

        val playerId = splits[2]

        if (TextUtils.isEmpty(playerId) || TextUtils.getTrimmedLength(playerId) == 0) {
            return
        }

        intentStack.add(PlayerActivity.getLaunchIntent(context, playerId, null,
                if (sameRegion) null else region))
    }

    private fun buildRankingsIntentStack(context: Context, intentStack: MutableList<Intent>,
            region: Region, sameRegion: Boolean) {
        if (sameRegion) {
            intentStack.add(HomeActivity.getLaunchIntent(context, HomeActivity.POSITION_RANKINGS))
        } else {
            intentStack.add(HomeActivity.getLaunchIntent(context))
            intentStack.add(RankingsActivity.getLaunchIntent(context, region))
        }
    }

    private fun buildTournamentsIntentStack(context: Context, intentStack: MutableList<Intent>,
            region: Region, sameRegion: Boolean, splits: Array<String>) {
        if (sameRegion) {
            intentStack.add(HomeActivity.getLaunchIntent(context, HomeActivity.POSITION_TOURNAMENTS))
        } else {
            intentStack.add(HomeActivity.getLaunchIntent(context))
            intentStack.add(TournamentsActivity.getLaunchIntent(context, region))
        }

        if (splits.size < 3) {
            return
        }

        val tournamentId = splits[2]

        if (TextUtils.isEmpty(tournamentId) || TextUtils.getTrimmedLength(tournamentId) == 0) {
            return
        }

        intentStack.add(TournamentActivity.getLaunchIntent(context, tournamentId, null, null,
                if (sameRegion) null else region))
    }

    override fun getEndpoint(intent: Intent?): Endpoint? {
        if (intent == null) {
            return null
        } else {
            return getEndpoint(intent.data)
        }
    }

    override fun getEndpoint(uri: String?): Endpoint? {
        if (uri == null || TextUtils.isEmpty(uri) || TextUtils.getTrimmedLength(uri) == 0) {
            return null
        }

        for (endpoint in Endpoint.values()) {
            if (uri.startsWith(endpoint.basePath)) {
                return endpoint
            }
        }

        return null
    }

    override fun getEndpoint(uri: Uri?): Endpoint? {
        if (uri == null) {
            return null
        } else {
            return getEndpoint(uri.toString())
        }
    }

    override fun getRegion(intent: Intent?, regionsBundle: RegionsBundle?): Region? {
        if (intent == null) {
            return null
        } else {
            return getRegion(intent.data, regionsBundle)
        }
    }

    override fun getRegion(uri: String?, regionsBundle: RegionsBundle?): Region? {
        val _uri = uri?.trim()

        if (_uri == null || TextUtils.isEmpty(_uri)) {
            return null
        }

        val regions = regionsBundle?.regions

        if (regions == null || regions.isEmpty()) {
            return null
        }

        for (region in regions) {
            if (_uri.startsWith(region.endpoint.getWebPath(region.id))) {
                return region
            }
        }

        return null
    }

    override fun getRegion(uri: Uri?, regionsBundle: RegionsBundle?): Region? {
        if (uri == null) {
            return null
        } else {
            return getRegion(uri.toString(), regionsBundle)
        }
    }

    override fun isValidUri(intent: Intent?): Boolean {
        return isValidUri(intent?.data)
    }

    override fun isValidUri(uri: String?): Boolean {
        return getEndpoint(uri) != null
    }

    override fun isValidUri(uri: Uri?): Boolean {
        return isValidUri(uri?.toString())
    }

}
