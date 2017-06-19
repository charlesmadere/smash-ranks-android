package com.garpr.android.misc;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityManagerCompat;

public class DeviceUtilsImpl implements DeviceUtils {

    private final Application mApplication;


    public DeviceUtilsImpl(@NonNull final Application application) {
        mApplication = application;
    }

    @Override
    public boolean hasLowRam() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return true;
        } else {
            final ActivityManager activityManager = (ActivityManager) mApplication
                    .getSystemService(Context.ACTIVITY_SERVICE);
            return ActivityManagerCompat.isLowRamDevice(activityManager);
        }
    }

    @Override
    public boolean hasNetworkConnection() {
        final ConnectivityManager connectivityManager = (ConnectivityManager) mApplication
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

}
