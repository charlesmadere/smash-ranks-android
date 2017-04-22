package com.garpr.android.misc;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

public interface NotificationManager {

    void cancelAll();

    void show(@NonNull final NotificationCompat.Builder builder);

    void showRankingsUpdated(@NonNull final Context context);

}
