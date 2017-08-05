package com.garpr.android.misc

import android.app.*
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.content.ContextCompat
import com.garpr.android.R
import com.garpr.android.activities.HomeActivity
import com.garpr.android.extensions.notificationManager
import com.garpr.android.preferences.RankingsPollingPreferenceStore

class NotificationsManagerImpl(
        private val mApplication: Application,
        private val mRankingsPollingPreferenceStore: RankingsPollingPreferenceStore,
        private val mRegionManager: RegionManager
) : NotificationsManager {

    private val mImpl: BaseImpl


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

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mImpl = Api26Impl()
        } else {
            mImpl = BaseImpl()
        }
    }

    override fun cancelAll() {
        NotificationManagerCompat.from(mApplication).cancelAll()
    }

    override fun rankingsUpdated(context: Context) {
        val builder = mImpl.createBuilder(context)

        builder.setContentIntent(PendingIntent.getActivity(context, 0,
                HomeActivity.getLaunchIntent(context), PendingIntent.FLAG_UPDATE_CURRENT))

        builder.setContentText(context.getString(R.string.x_rankings_have_been_updated,
                mRegionManager.getRegion()))

        if (mRankingsPollingPreferenceStore.vibrationEnabled.get() == true) {
            builder.setDefaults(NotificationCompat.DEFAULT_LIGHTS or NotificationCompat.DEFAULT_VIBRATE)
        } else {
            builder.setDefaults(NotificationCompat.DEFAULT_LIGHTS)
        }

        val largeIconDrawable = ContextCompat.getDrawable(context, R.mipmap.launcher)
        var largeIconBitmap: Bitmap? = null

        if (largeIconDrawable is BitmapDrawable) {
            largeIconBitmap = largeIconDrawable.bitmap
        }

        if (largeIconBitmap == null) {
            largeIconBitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.launcher)
        }

        builder.setLargeIcon(largeIconBitmap)

        mRankingsPollingPreferenceStore.ringtone.get()?.let {
            builder.setSound(it)
        }

        show(RANKINGS_ID, builder)
    }

    override fun show(id: Int, notificationBuilder: NotificationCompat.Builder) {
        show(id, notificationBuilder.build())
    }

    override fun show(id: Int, notification: Notification) {
        NotificationManagerCompat.from(mApplication).notify(id, notification)
    }

}
