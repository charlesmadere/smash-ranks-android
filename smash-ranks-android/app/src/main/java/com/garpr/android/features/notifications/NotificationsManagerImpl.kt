package com.garpr.android.features.notifications

import android.annotation.TargetApi
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.garpr.android.R
import com.garpr.android.data.models.SimpleDate
import com.garpr.android.extensions.notificationManager
import com.garpr.android.extensions.notificationManagerCompat
import com.garpr.android.features.home.HomeActivity
import com.garpr.android.misc.Timber
import com.garpr.android.preferences.RankingsPollingPreferenceStore
import com.garpr.android.repositories.RegionRepository

class NotificationsManagerImpl(
        private val application: Application,
        private val rankingsPollingPreferenceStore: RankingsPollingPreferenceStore,
        private val regionRepository: RegionRepository,
        private val timber: Timber
) : NotificationsManager {

    companion object {
        private const val RANKINGS_CHANNEL = "rankings"
        private const val RANKINGS_ID = 1001
        private const val TAG = "NotificationsManagerImpl"
    }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            initRankingsNotificationsChannel()
        }
    }

    override fun cancelRankingsUpdated() {
        timber.d(TAG, "canceling rankings notifications")
        application.notificationManagerCompat.cancel(RANKINGS_ID)
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun initRankingsNotificationsChannel() {
        val notificationChannel = NotificationChannel(
                RANKINGS_CHANNEL,
                application.getString(R.string.rankings_update_notifications_name),
                NotificationManager.IMPORTANCE_DEFAULT)

        notificationChannel.description = application.getString(R.string.rankings_update_notifications_description)
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = ContextCompat.getColor(application, R.color.blue)
        notificationChannel.lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
        notificationChannel.setShowBadge(true)

        application.notificationManager.createNotificationChannel(notificationChannel)
    }

    override fun showRankingsUpdated() {
        val builder = NotificationCompat.Builder(application, RANKINGS_CHANNEL)
                .setAutoCancel(true)
                .setCategory(NotificationCompat.CATEGORY_SOCIAL)
                .setContentTitle(application.getString(R.string.gar_pr))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_controller_white_24dp)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

        builder.setContentIntent(PendingIntent.getActivity(application, 0,
                HomeActivity.getLaunchIntent(context = application, restartActivityTask = true),
                PendingIntent.FLAG_UPDATE_CURRENT))

        val regionDisplayName = regionRepository.getRegion().displayName
        builder.setContentText(application.getString(R.string.x_rankings_have_been_updated,
                regionDisplayName))

        if (rankingsPollingPreferenceStore.vibrationEnabled.get() == true) {
            builder.setDefaults(NotificationCompat.DEFAULT_LIGHTS or NotificationCompat.DEFAULT_VIBRATE)
        } else {
            builder.setDefaults(NotificationCompat.DEFAULT_LIGHTS)
        }

        rankingsPollingPreferenceStore.ringtone.get()?.let {
            builder.setSound(it)
        }

        timber.d(TAG, "Showing rankings updated notification! Debug info: " +
                "regionDisplayName = $regionDisplayName, " +
                "time = ${SimpleDate()}")

        application.notificationManagerCompat.notify(RANKINGS_ID, builder.build())
    }

}
