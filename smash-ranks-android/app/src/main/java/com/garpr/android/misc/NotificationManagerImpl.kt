package com.garpr.android.misc

import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.content.ContextCompat
import com.garpr.android.R
import com.garpr.android.activities.HomeActivity
import com.garpr.android.preferences.RankingsPollingPreferenceStore

class NotificationManagerImpl(
        private val mApplication: Application,
        private val mRankingsPollingPreferenceStore: RankingsPollingPreferenceStore,
        private val mRegionManager: RegionManager
) : NotificationManager {

    companion object {
        private const val NOTIFICATION_ID = 1
    }

    override fun cancelAll() {
        NotificationManagerCompat.from(mApplication).cancelAll()
    }

    override fun show(builder: NotificationCompat.Builder) {
        NotificationManagerCompat.from(mApplication).notify(NOTIFICATION_ID, builder.build())
    }

    override fun showRankingsUpdated(context: Context) {
        val builder = NotificationCompat.Builder(context)
                .setAutoCancel(true)
                .setCategory(NotificationCompat.CATEGORY_SOCIAL)
                .setContentTitle(context.getString(R.string.gar_pr))
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setSmallIcon(R.drawable.notification)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

        builder.setContentIntent(PendingIntent.getActivity(context, 0,
                HomeActivity.getLaunchIntent(context), PendingIntent.FLAG_UPDATE_CURRENT))

        builder.setContentText(context.getString(R.string.x_rankings_have_been_updated,
                mRegionManager.region))

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

        show(builder)
    }

}
