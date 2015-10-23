package com.garpr.android.misc;


import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.activities.RankingsActivity;
import com.garpr.android.models.Region;
import com.garpr.android.settings.Settings;


/**
 * Much of this class's code was taken from the Android documentation:
 * https://developer.android.com/training/notify-user/build-notification.html
 */
public final class NotificationManager {


    private static final int NOTIFICATION_ID = 1;




    public static void clear() {
        final Context context = App.get();
        NotificationManagerCompat.from(context).cancelAll();
    }


    public static void showRankingsUpdated() {
        final Context context = App.get();
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setAutoCancel(true)
                .setCategory(NotificationCompat.CATEGORY_SOCIAL)
                .setContentTitle(context.getString(R.string.gar_pr))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSmallIcon(R.drawable.notification)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        final Region region = Settings.User.Region.get();
        builder.setContentText(context.getString(R.string.x_rankings_have_been_updated,
                region.getName()));

        final Intent intent = new RankingsActivity.IntentBuilder(context)
                .isFromRankingsUpdateNotification()
                .getIntent();

        final PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build());
    }


}