package com.garpr.android.features.notifications

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.garpr.android.R
import com.garpr.android.data.models.SimpleDate
import com.garpr.android.extensions.notificationManager
import com.garpr.android.extensions.notificationManagerCompat
import com.garpr.android.features.home.HomeActivity
import com.garpr.android.misc.Timber
import com.garpr.android.preferences.RankingsPollingPreferenceStore
import com.garpr.android.repositories.RegionManager

class NotificationsManagerImpl(
        private val application: Application,
        private val rankingsPollingPreferenceStore: RankingsPollingPreferenceStore,
        private val regionManager: RegionManager,
        private val timber: Timber
) : NotificationsManager {

    private val impl: Impl = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        Api26Impl()
    } else {
        BaseImpl()
    }


    companion object {
        private const val RANKINGS_CHANNEL = "rankings"
        private const val RANKINGS_ID: Int = 1001
        private const val TAG = "NotificationsManagerImpl"
    }

    override fun cancelAll() {
        timber.d(TAG, "canceling all notifications")
        application.notificationManagerCompat.cancelAll()
    }

    override fun rankingsUpdated() {
        val builder = impl.createBuilder(application)

        builder.setContentIntent(PendingIntent.getActivity(application, 0,
                HomeActivity.getLaunchIntent(context = application, restartActivityTask = true),
                PendingIntent.FLAG_UPDATE_CURRENT))

        val regionDisplayName = regionManager.getRegion().displayName
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

        show(RANKINGS_ID, builder)
    }

    override fun show(id: Int, notificationBuilder: NotificationCompat.Builder) {
        show(id, notificationBuilder.build())
    }

    override fun show(id: Int, notification: Notification) {
        application.notificationManagerCompat.notify(id, notification)
    }

    private interface Impl {
        fun createBuilder(context: Context): NotificationCompat.Builder
    }

    private open class BaseImpl : Impl {
        override fun createBuilder(context: Context): NotificationCompat.Builder {
            return NotificationCompat.Builder(context, RANKINGS_CHANNEL)
                    .setAutoCancel(true)
                    .setCategory(NotificationCompat.CATEGORY_SOCIAL)
                    .setContentTitle(context.getString(R.string.gar_pr))
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .setSmallIcon(R.drawable.ic_controller_white_24dp)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private class Api26Impl : BaseImpl() {
        override fun createBuilder(context: Context): NotificationCompat.Builder {
            val notificationChannel = NotificationChannel(
                    RANKINGS_CHANNEL,
                    context.getString(R.string.rankings_update_notifications),
                    NotificationManager.IMPORTANCE_LOW)
            notificationChannel.description = context.getString(R.string.rankings_update_description)
            notificationChannel.enableLights(true)
            notificationChannel.lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
            notificationChannel.setShowBadge(true)
            context.notificationManager.createNotificationChannel(notificationChannel)

            return super.createBuilder(context)
        }
    }

}
