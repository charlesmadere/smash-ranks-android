package com.garpr.android.misc;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

public class NotificationManagerImpl implements NotificationManager {

    private static final int NOTIFICATION_ID = 1;

    private final Application mApplication;


    public NotificationManagerImpl(final Application application) {
        mApplication = application;
    }

    @Override
    public void cancelAll() {
        NotificationManagerCompat.from(mApplication).cancelAll();
    }

    @Override
    public void show(@NonNull final NotificationCompat.Builder builder) {
        NotificationManagerCompat.from(mApplication).notify(NOTIFICATION_ID, builder.build());
    }

}
