package com.garpr.android.managers

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.garpr.android.R
import com.garpr.android.data.models.SimpleDate
import com.garpr.android.extensions.notificationManager
import com.garpr.android.extensions.notificationManagerCompat
import com.garpr.android.features.home.HomeActivity
import com.garpr.android.misc.Timber
import com.garpr.android.repositories.RegionRepository
import com.garpr.android.sync.rankings.RankingsPollingManager
import android.net.Uri as AndroidUri

class NotificationsManagerImpl(
        private val context: Context,
        private val rankingsPollingManager: RankingsPollingManager,
        private val regionRepository: RegionRepository,
        private val timber: Timber
) : NotificationsManager {

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            initRankingsNotificationsChannel()
        }
    }

    override fun cancelRankingsUpdated() {
        timber.d(TAG, "canceling rankings notifications")
        context.notificationManagerCompat.cancel(RANKINGS_ID)
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun initRankingsNotificationsChannel() {
        val notificationChannel = NotificationChannel(
                RANKINGS_CHANNEL,
                context.getString(R.string.rankings_update_notifications_name),
                NotificationManager.IMPORTANCE_DEFAULT
        )

        notificationChannel.description = context.getString(R.string.rankings_update_notifications_description)
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = ContextCompat.getColor(context, R.color.blue)
        notificationChannel.lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
        notificationChannel.setShowBadge(true)

        context.notificationManager.createNotificationChannel(notificationChannel)
    }

    override fun showRankingsUpdated() {
        val builder = NotificationCompat.Builder(context, RANKINGS_CHANNEL)
                .setAutoCancel(true)
                .setCategory(NotificationCompat.CATEGORY_SOCIAL)
                .setContentTitle(context.getString(R.string.gar_pr))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_controller_white_24dp)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

        builder.setContentIntent(PendingIntent.getActivity(context, 0,
                HomeActivity.getLaunchIntent(context = context, restartActivityTask = true),
                PendingIntent.FLAG_UPDATE_CURRENT))

        val regionDisplayName = regionRepository.region.displayName
        builder.setContentText(context.getString(R.string.x_rankings_have_been_updated,
                regionDisplayName))

        if (rankingsPollingManager.isVibrationEnabled) {
            builder.setDefaults(NotificationCompat.DEFAULT_LIGHTS or NotificationCompat.DEFAULT_VIBRATE)
        } else {
            builder.setDefaults(NotificationCompat.DEFAULT_LIGHTS)
        }

        rankingsPollingManager.ringtone?.let { javaUri ->
            val string = javaUri.toString()
            builder.setSound(AndroidUri.parse(string))
        }

        timber.d(TAG, "Showing rankings updated notification! Debug info: " +
                "regionDisplayName = $regionDisplayName, " +
                "time = ${SimpleDate()}")

        context.notificationManagerCompat.notify(RANKINGS_ID, builder.build())
    }

    companion object {
        private const val RANKINGS_CHANNEL = "rankings"
        private const val RANKINGS_ID = 1001
        private const val TAG = "NotificationsManagerImpl"
    }

}
