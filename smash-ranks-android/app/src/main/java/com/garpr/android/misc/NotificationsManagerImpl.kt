package com.garpr.android.misc

import android.app.*
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import com.garpr.android.R
import com.garpr.android.activities.HomeActivity
import com.garpr.android.extensions.notificationManager
import com.garpr.android.extensions.notificationManagerCompat
import com.garpr.android.preferences.RankingsPollingPreferenceStore

class NotificationsManagerImpl(
        private val mApplication: Application,
        private val mRankingsPollingPreferenceStore: RankingsPollingPreferenceStore,
        private val mRegionManager: RegionManager
) : NotificationsManager {

    private val mImpl = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) Api26Impl() else BaseImpl()


    private open class BaseImpl {
        open fun createBuilder(context: Context): NotificationCompat.Builder {
            return NotificationCompat.Builder(context, RANKINGS_CHANNEL)
                    .setAutoCancel(true)
                    .setCategory(NotificationCompat.CATEGORY_SOCIAL)
                    .setContentTitle(context.getString(R.string.gar_pr))
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .setSmallIcon(R.drawable.notification)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private class Api26Impl : BaseImpl() {
        override fun createBuilder(context: Context): NotificationCompat.Builder {
            val notificationChannel = NotificationChannel(RANKINGS_CHANNEL,
                    context.getString(R.string.rankings_update_notifications),
                    NotificationManager.IMPORTANCE_LOW)
            notificationChannel.description = context.getString(R.string.rankings_update_description)
            notificationChannel.enableLights(true)
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            notificationChannel.setShowBadge(true)
            context.notificationManager.createNotificationChannel(notificationChannel)

            return super.createBuilder(context)
        }
    }

    companion object {
        private const val RANKINGS_CHANNEL = "rankings"
        private const val RANKINGS_ID = 1001
    }

    override fun cancelAll() {
        mApplication.notificationManagerCompat.cancelAll()
    }

    override fun rankingsUpdated() {
        val builder = mImpl.createBuilder(mApplication)

        builder.setContentIntent(PendingIntent.getActivity(mApplication, 0,
                HomeActivity.getLaunchIntent(mApplication), PendingIntent.FLAG_UPDATE_CURRENT))

        builder.setContentText(mApplication.getString(R.string.x_rankings_have_been_updated,
                mRegionManager.getRegion().displayName))

        if (mRankingsPollingPreferenceStore.vibrationEnabled.get() == true) {
            builder.setDefaults(NotificationCompat.DEFAULT_LIGHTS or NotificationCompat.DEFAULT_VIBRATE)
        } else {
            builder.setDefaults(NotificationCompat.DEFAULT_LIGHTS)
        }

        mRankingsPollingPreferenceStore.ringtone.get()?.let {
            builder.setSound(it)
        }

        mRankingsPollingPreferenceStore.rankingsDate.get()?.let {
            builder.setShowWhen(true)
                    .setWhen(it.date.time)
        }

        show(RANKINGS_ID, builder)
    }

    override fun show(id: Int, notificationBuilder: NotificationCompat.Builder) {
        show(id, notificationBuilder.build())
    }

    override fun show(id: Int, notification: Notification) {
        mApplication.notificationManagerCompat.notify(id, notification)
    }

}
