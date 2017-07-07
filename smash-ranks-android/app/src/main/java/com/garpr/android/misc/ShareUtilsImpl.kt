package com.garpr.android.misc

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v4.app.ShareCompat
import android.text.TextUtils
import android.widget.Toast
import com.garpr.android.R
import com.garpr.android.misc.Constants.PLAIN_TEXT
import com.garpr.android.models.AbsPlayer
import com.garpr.android.models.AbsTournament

class ShareUtilsImpl(
        private val mRegionManager: RegionManager,
        private val mTimber: Timber
) : ShareUtils {

    companion object {
        private const val TAG = "ShareUtilsImpl"
    }

    override fun openUrl(context: Context, url: String?) {
        if (url == null || TextUtils.isEmpty(url) || TextUtils.getTrimmedLength(url) == 0) {
            return
        }

        val uri = Uri.parse(url)

        try {
            val intent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            mTimber.e(TAG, "Unable to open browser to URI: \"" + uri + "\"", e)
            Toast.makeText(context, R.string.unable_to_open_link, Toast.LENGTH_LONG).show()
        }

    }

    override fun sharePlayer(activity: Activity, player: AbsPlayer) {
        val region = mRegionManager.getRegion(activity)

        ShareCompat.IntentBuilder.from(activity)
                .setChooserTitle(activity.getString(R.string.share_x, player.name))
                .setText(region.endpoint.getPlayerWebPath(region.id, player.id))
                .setType(PLAIN_TEXT)
                .startChooser()
    }

    override fun shareRankings(activity: Activity) {
        val region = mRegionManager.getRegion(activity)

        ShareCompat.IntentBuilder.from(activity)
                .setChooserTitle(activity.getString(R.string.share_rankings))
                .setText(region.endpoint.getRankingsWebPath(region.id))
                .setType(PLAIN_TEXT)
                .startChooser()
    }

    override fun shareTournament(activity: Activity,
            tournament: AbsTournament) {
        val region = mRegionManager.getRegion(activity)

        ShareCompat.IntentBuilder.from(activity)
                .setChooserTitle(activity.getString(R.string.share_x, tournament.name))
                .setText(region.endpoint.getTournamentWebPath(region.id, tournament.id))
                .setType(PLAIN_TEXT)
                .startChooser()
    }

    override fun shareTournaments(activity: Activity) {
        val region = mRegionManager.getRegion(activity)

        ShareCompat.IntentBuilder.from(activity)
                .setChooserTitle(activity.getString(R.string.share_tournaments))
                .setText(region.endpoint.getTournamentsWebPath(region.id))
                .setType(PLAIN_TEXT)
                .startChooser()
    }

}