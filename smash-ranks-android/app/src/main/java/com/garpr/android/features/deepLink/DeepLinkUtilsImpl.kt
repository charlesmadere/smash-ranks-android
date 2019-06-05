package com.garpr.android.features.deepLink

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.Region
import com.garpr.android.data.models.RegionsBundle
import com.garpr.android.features.home.HomeActivity
import com.garpr.android.features.home.HomeTab
import com.garpr.android.features.player.PlayerActivity
import com.garpr.android.features.players.PlayersActivity
import com.garpr.android.features.rankings.RankingsActivity
import com.garpr.android.features.tournament.TournamentActivity
import com.garpr.android.features.tournaments.TournamentsActivity
import com.garpr.android.misc.Timber
import com.garpr.android.repositories.RegionRepository

class DeepLinkUtilsImpl(
        private val regionRepository: RegionRepository,
        private val timber: Timber
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
        return if (intent == null) {
            timber.d(TAG, "Can't deep link, Intent is null")
            null
        } else {
            buildIntentStack(context, intent.data, region)
        }
    }

    override fun buildIntentStack(context: Context, uri: String?, region: Region): List<Intent>? {
        if (uri.isNullOrBlank()) {
            timber.d(TAG, "Can't deep link, uri is null / blank")
            return null
        }

        timber.d(TAG, "Attempting to deep link to \"$uri\"")

        val endpoint = getEndpoint(uri)

        if (endpoint == null) {
            timber.e(TAG, "Deep link path isn't for GAR PR")
            return null
        }

        val path = uri.substring(endpoint.getWebPath().length, uri.length)

        if (path.isBlank()) {
            timber.d(TAG, "Deep link path is null / blank")
            return null
        }

        val splits = path.split("/")

        if (splits.isEmpty()) {
            timber.d(TAG, "Deep link's path split is empty")
            return null
        }

        val regionId = splits[0]

        if (regionId.isBlank()) {
            timber.w(TAG, "Region ID is null / blank")
            return null
        }

        val sameRegion = regionId.equals(regionRepository.getRegion().id, ignoreCase = true)

        if (sameRegion && splits.size == 1) {
            return null
        }

        val intentStack = mutableListOf<Intent>()
        val page = splits[1]

        if (PLAYERS.equals(page, ignoreCase = true)) {
            buildPlayersIntentStack(context, intentStack, region, sameRegion, splits)
        } else if (RANKINGS.equals(page, ignoreCase = true)) {
            buildRankingsIntentStack(context, intentStack, region, sameRegion)
        } else if (TOURNAMENTS.equals(page, ignoreCase = true)) {
            buildTournamentsIntentStack(context, intentStack, region, sameRegion, splits)
        } else {
            timber.w(TAG, "Unknown page \"$page\"")
        }

        return intentStack
    }

    override fun buildIntentStack(context: Context, uri: Uri?, region: Region): List<Intent>? {
        return if (uri == null) {
            timber.d(TAG, "Can't deep link, Uri is null")
            null
        } else {
            buildIntentStack(context, uri.toString(), region)
        }
    }

    private fun buildPlayersIntentStack(context: Context, intentStack: MutableList<Intent>,
            region: Region, sameRegion: Boolean, splits: List<String>) {
        intentStack.add(HomeActivity.getLaunchIntent(context = context))

        if (sameRegion) {
            intentStack.add(PlayersActivity.getLaunchIntent(context))
        } else {
            intentStack.add(PlayersActivity.getLaunchIntent(context, region))
        }

        if (splits.size < 3) {
            return
        }

        val playerId = splits[2]

        if (playerId.isBlank()) {
            return
        }

        intentStack.add(PlayerActivity.getLaunchIntent(context, playerId,
                if (sameRegion) null else region))
    }

    private fun buildRankingsIntentStack(context: Context, intentStack: MutableList<Intent>,
            region: Region, sameRegion: Boolean) {
        if (sameRegion) {
            intentStack.add(HomeActivity.getLaunchIntent(context = context,
                initialPosition = HomeTab.RANKINGS))
        } else {
            intentStack.add(HomeActivity.getLaunchIntent(context = context))
            intentStack.add(RankingsActivity.getLaunchIntent(context, region))
        }
    }

    private fun buildTournamentsIntentStack(context: Context, intentStack: MutableList<Intent>,
            region: Region, sameRegion: Boolean, splits: List<String>) {
        if (sameRegion) {
            intentStack.add(HomeActivity.getLaunchIntent(context = context,
                initialPosition = HomeTab.TOURNAMENTS))
        } else {
            intentStack.add(HomeActivity.getLaunchIntent(context = context))
            intentStack.add(TournamentsActivity.getLaunchIntent(context, region))
        }

        if (splits.size < 3) {
            return
        }

        val tournamentId = splits[2]

        if (tournamentId.isBlank()) {
            return
        }

        intentStack.add(TournamentActivity.getLaunchIntent(context, tournamentId,
                if (sameRegion) null else region))
    }

    override fun getEndpoint(intent: Intent?): Endpoint? {
        return if (intent == null) {
            null
        } else {
            getEndpoint(intent.data)
        }
    }

    override fun getEndpoint(uri: String?): Endpoint? {
        val trimmedUri = uri?.trim() ?: return null

        return Endpoint.values()
                .firstOrNull { trimmedUri.startsWith(it.basePath) }
    }

    override fun getEndpoint(uri: Uri?): Endpoint? {
        return if (uri == null) {
            null
        } else {
            getEndpoint(uri.toString())
        }
    }

    override fun getRegion(intent: Intent?, regionsBundle: RegionsBundle?): Region? {
        return if (intent == null) {
            null
        } else {
            getRegion(intent.data, regionsBundle)
        }
    }

    override fun getRegion(uri: String?, regionsBundle: RegionsBundle?): Region? {
        val trimmedUri = uri?.trim()

        if (trimmedUri.isNullOrBlank()) {
            return null
        }

        val regions = regionsBundle?.regions

        if (regions.isNullOrEmpty()) {
            return null
        }

        return regions
                .asSequence()
                .filterIsInstance<Region>()
                .firstOrNull { trimmedUri.startsWith(it.endpoint.getWebPath(it.id)) }
    }

    override fun getRegion(uri: Uri?, regionsBundle: RegionsBundle?): Region? {
        return if (uri == null) {
            null
        } else {
            getRegion(uri.toString(), regionsBundle)
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
