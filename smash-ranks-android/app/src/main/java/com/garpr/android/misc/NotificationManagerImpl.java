package com.garpr.android.misc;

import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;

import com.garpr.android.R;
import com.garpr.android.activities.HomeActivity;
import com.garpr.android.preferences.RankingsPollingPreferenceStore;

public class NotificationManagerImpl implements NotificationManager {

    private static final int NOTIFICATION_ID = 1;

    private final Application mApplication;
    private final RankingsPollingPreferenceStore mRankingsPollingPreferenceStore;
    private final RegionManager mRegionManager;


    public NotificationManagerImpl(@NonNull final Application application,
            @NonNull final RankingsPollingPreferenceStore rankingsPollingPreferenceStore,
            @NonNull final RegionManager regionManager) {
        mApplication = application;
        mRankingsPollingPreferenceStore = rankingsPollingPreferenceStore;
        mRegionManager = regionManager;
    }

    @Override
    public void cancelAll() {
        NotificationManagerCompat.from(mApplication).cancelAll();
    }

    @Override
    public void show(@NonNull final NotificationCompat.Builder builder) {
        NotificationManagerCompat.from(mApplication).notify(NOTIFICATION_ID, builder.build());
    }

    @Override
    public void showRankingsUpdated(@NonNull final Context context) {
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setAutoCancel(true)
                .setCategory(NotificationCompat.CATEGORY_SOCIAL)
                .setContentTitle(context.getString(R.string.gar_pr))
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setSmallIcon(R.drawable.notification)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        builder.setContentIntent(PendingIntent.getActivity(context, 0,
                HomeActivity.Companion.getLaunchIntent(context), PendingIntent.FLAG_UPDATE_CURRENT));

        builder.setContentText(context.getString(R.string.x_rankings_have_been_updated,
                mRegionManager.getRegion()));

        if (Boolean.TRUE.equals(mRankingsPollingPreferenceStore.getVibrationEnabled().get())) {
            builder.setDefaults(NotificationCompat.DEFAULT_LIGHTS |
                    NotificationCompat.DEFAULT_VIBRATE);
        } else {
            builder.setDefaults(NotificationCompat.DEFAULT_LIGHTS);
        }

        final Drawable largeIconDrawable = ContextCompat.getDrawable(context, R.mipmap.launcher);
        Bitmap largeIconBitmap = null;

        if (largeIconDrawable instanceof BitmapDrawable) {
            largeIconBitmap = ((BitmapDrawable) largeIconDrawable).getBitmap();
        }

        if (largeIconBitmap == null) {
            largeIconBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.launcher);
        }

        builder.setLargeIcon(largeIconBitmap);

        final Uri ringtoneUri = mRankingsPollingPreferenceStore.getRingtone().get();

        if (ringtoneUri != null) {
            builder.setSound(ringtoneUri);
        }

        show(builder);
    }

}
