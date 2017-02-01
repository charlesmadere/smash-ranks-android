package com.garpr.android.misc;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.ActivityManagerCompat;

public class DeviceUtilsImpl implements DeviceUtils {

    private final Application mApplication;


    public DeviceUtilsImpl(final Application application) {
        mApplication = application;
    }

    @Override
    public boolean isLowRam() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return true;
        } else {
            final ActivityManager activityManager = (ActivityManager) mApplication
                    .getSystemService(Context.ACTIVITY_SERVICE);
            return ActivityManagerCompat.isLowRamDevice(activityManager);
        }
    }

}
