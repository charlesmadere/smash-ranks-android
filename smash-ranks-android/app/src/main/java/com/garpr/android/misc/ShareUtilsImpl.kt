package com.garpr.android.misc

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.ShareCompat
import com.garpr.android.R
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.AbsTournament
import com.garpr.android.misc.Constants.PLAIN_TEXT
import android.net.Uri as AndroidUri

class ShareUtilsImpl(
        private val regionHandleUtils: RegionHandleUtils,
        private val timber: Timber
) : ShareUtils {

    override fun openUrl(context: Context, url: String?) {
        if (url.isNullOrBlank()) {
            return
        }

        val androidUri = AndroidUri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, androidUri)

        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            timber.e(TAG, "Unable to open browser to URI: $androidUri", e)
            Toast.makeText(context, R.string.unable_to_open_link, Toast.LENGTH_LONG).show()
        }
    }

    override fun sharePlayer(activity: Activity, player: AbsPlayer) {
        val region = regionHandleUtils.getRegion(activity)

        ShareCompat.IntentBuilder.from(activity)
                .setChooserTitle(activity.getString(R.string.share_x, player.name))
                .setText(region.endpoint.getPlayerWebPath(region.id, player.id))
                .setType(PLAIN_TEXT)
                .startChooser()
    }

    override fun shareRankings(activity: Activity) {
        val region = regionHandleUtils.getRegion(activity)

        ShareCompat.IntentBuilder.from(activity)
                .setChooserTitle(activity.getString(R.string.share_rankings))
                .setText(region.endpoint.getRankingsWebPath(region.id))
                .setType(PLAIN_TEXT)
                .startChooser()
    }

    override fun shareTournament(activity: Activity, tournament: AbsTournament) {
        val region = regionHandleUtils.getRegion(activity)

        ShareCompat.IntentBuilder.from(activity)
                .setChooserTitle(activity.getString(R.string.share_x, tournament.name))
                .setText(region.endpoint.getTournamentWebPath(region.id, tournament.id))
                .setType(PLAIN_TEXT)
                .startChooser()
    }

    override fun shareTournaments(activity: Activity) {
        val region = regionHandleUtils.getRegion(activity)

        ShareCompat.IntentBuilder.from(activity)
                .setChooserTitle(activity.getString(R.string.share_tournaments))
                .setText(region.endpoint.getTournamentsWebPath(region.id))
                .setType(PLAIN_TEXT)
                .startChooser()
    }

    companion object {
        private const val TAG = "ShareUtilsImpl"
    }

}
