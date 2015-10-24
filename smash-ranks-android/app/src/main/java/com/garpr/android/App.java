package com.garpr.android;


import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.android.volley.Cache;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.garpr.android.misc.Console;
import com.garpr.android.misc.Constants;
import com.garpr.android.misc.CrashlyticsManager;
import com.garpr.android.misc.Heartbeat;
import com.garpr.android.settings.Settings;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.fabric.sdk.android.Fabric;


public final class App extends Application {


    private static final String TAG = "App";

    private static App sInstance;
    private static ExecutorService sExecutorService;
    private static RequestQueue sRequestQueue;




    public static void cancelNetworkRequests(final Heartbeat heartbeat) {
        sRequestQueue.cancelAll(heartbeat);
    }


    public static void deleteNetworkCache() {
        getNetworkCache().clear();
    }


    public static App get() {
        return sInstance;
    }


    public static ExecutorService getExecutorService() {
        return sExecutorService;
    }


    public static Cache getNetworkCache() {
        return sRequestQueue.getCache();
    }


    private static PackageInfo getPackageInfo() {
        try {
            final String packageName = sInstance.getPackageName();
            return sInstance.getPackageManager().getPackageInfo(packageName, 0);
        } catch (final PackageManager.NameNotFoundException e) {
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
        sInstance = this;
        sExecutorService = Executors.newFixedThreadPool(3);
        sRequestQueue = Volley.newRequestQueue(this);

        Fabric.with(this, new Crashlytics());
        CrashlyticsManager.setBool(Constants.DEBUG, BuildConfig.DEBUG);

        final int currentVersion = getVersionCode();
        Console.d(TAG, "App created, current version is " + currentVersion);
        final int lastVersion = Settings.LastVersion.get();

        if (currentVersion > lastVersion) {
            onUpgrade(lastVersion);
            Settings.LastVersion.set(currentVersion);
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


    private void onUpgrade(final int lastVersion) {
        Console.d(TAG, "App upgrading from version " + lastVersion);

        if (lastVersion < 40) {
            // entirely new settings model and classes, all SharedPreferences must be cleared
            Settings.deleteAll();
        }

        if (lastVersion < 52) {
            // stored rankings date didn't necessarily reflect the user's region's date
            Settings.RankingsDate.delete();
        }

        if (lastVersion < 60) {
            // another big shift in how settings are stored
            Settings.deleteAll();
        }

        if (lastVersion < 93) {
            // we no longer use our own home-grown network cache solution
            Settings.edit("com.garpr.android.misc.NetworkCache").clear().apply();
        }
    }


    @Override
    public String toString() {
        return TAG;
    }


}
