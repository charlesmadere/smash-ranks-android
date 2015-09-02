package com.garpr.android;


import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.garpr.android.misc.Console;
import com.garpr.android.misc.Constants;
import com.garpr.android.misc.CrashlyticsManager;
import com.garpr.android.misc.Heartbeat;
import com.garpr.android.misc.NetworkCache;
import com.garpr.android.misc.OkHttpStack;
import com.garpr.android.settings.Settings;

import io.fabric.sdk.android.Fabric;


public final class App extends Application {


    private static final String TAG = "App";

    private static Context sContext;
    private static RequestQueue sRequestQueue;




    public static void cancelNetworkRequests(final Heartbeat heartbeat) {
        sRequestQueue.cancelAll(heartbeat);
    }


    public static Context getContext() {
        return sContext;
    }


    private static PackageInfo getPackageInfo() {
        try {
            final String packageName = sContext.getPackageName();
            return sContext.getPackageManager().getPackageInfo(packageName, 0);
        } catch (final NameNotFoundException e) {
            // this should never happen
            throw new RuntimeException(e);
        }
    }


    public static RequestQueue getRequestQueue() {
        return sRequestQueue;
    }


    public static int getVersionCode() {
        return getPackageInfo().versionCode;
    }


    public static String getVersionName() {
        return getPackageInfo().versionName;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        Fabric.with(this, new Crashlytics());
        CrashlyticsManager.setBool(Constants.DEBUG, BuildConfig.DEBUG);
        sRequestQueue = Volley.newRequestQueue(sContext, new OkHttpStack());

        final int currentVersion = getVersionCode();
        final int lastVersion = Settings.LastVersion.get();

        if (currentVersion > lastVersion) {
            onUpgrade(lastVersion, currentVersion);
            Settings.LastVersion.set(currentVersion);
        }
    }


    private void onUpgrade(final int lastVersion, final int currentVersion) {
        Console.d(TAG, "Upgrading from " + lastVersion + " to " + currentVersion);

        if (lastVersion < 40) {
            // entirely new settings model and classes, all SharedPreferences must be cleared
            NetworkCache.clear();
            Settings.deleteAll();
        }

        if (lastVersion < 52) {
            // stored rankings date didn't necessarily reflect the user's region's date
            Settings.RankingsDate.delete();
        }
    }


    @Override
    public void onTrimMemory(final int level) {
        super.onTrimMemory(level);

        if (level >= TRIM_MEMORY_BACKGROUND) {
            Console.clearLogMessages();
            Console.d(TAG, "onTrimMemory(" + level + ')');
        }
    }


    @Override
    public String toString() {
        return TAG;
    }


}
