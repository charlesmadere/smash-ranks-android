package com.garpr.android;


import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.garpr.android.misc.Console;
import com.garpr.android.misc.Constants;
import com.garpr.android.misc.CrashlyticsManager;
import com.garpr.android.misc.Heartbeat;
import com.garpr.android.misc.OkHttpStack;
import com.garpr.android.settings.Settings;
import com.squareup.okhttp.Cache;

import java.io.File;
import java.io.IOException;

import io.fabric.sdk.android.Fabric;


public final class App extends Application {


    private static final long NETWORK_CACHE_SIZE = 10l * 10l * 1024l; // 10 MiB
    private static final String TAG = "App";

    private static App sInstance;
    private static Cache sNetworkCache;
    private static RequestQueue sRequestQueue;




    public static void cancelNetworkRequests(final Heartbeat heartbeat) {
        sRequestQueue.cancelAll(heartbeat);
    }


    public static void deleteNetworkCache() {
        try {
            sNetworkCache.evictAll();
        } catch (final IOException e) {
            Console.e(TAG, "Error when attempting to delete the network cache", e);
        }
    }


    public static App get() {
        return sInstance;
    }


    public static Cache getNetworkCache() {
        return sNetworkCache;
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


    private void initializeVolley() {
        File cacheDir = getExternalCacheDir();
        String external = " external ";

        if (cacheDir == null) {
            cacheDir = getCacheDir();
            external = " local ";
        }

        cacheDir = new File(cacheDir.getPath() + File.separator + "NetworkCache");
        cacheDir.mkdirs();

        sNetworkCache = new Cache(cacheDir, NETWORK_CACHE_SIZE);

        try {
            sNetworkCache.initialize();
        } catch (final IOException e) {
            Console.e(TAG, "HTTP Cache initialization failure", e);
        }

        Console.d(TAG, "HTTP Cache is in the" + external + "cache dir at \"" +
                sNetworkCache.getDirectory().getPath() + '"');

        sRequestQueue = Volley.newRequestQueue(this, new OkHttpStack(sNetworkCache));
    }


    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        Fabric.with(this, new Crashlytics());
        CrashlyticsManager.setBool(Constants.DEBUG, BuildConfig.DEBUG);
        initializeVolley();

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
