package com.garpr.android.misc;


import android.content.Context;
import android.net.ConnectivityManager;
import android.support.v4.net.ConnectivityManagerCompat;

import com.garpr.android.App;
import com.garpr.android.BuildConfig;
import com.garpr.android.calls.Rankings;
import com.garpr.android.calls.Response;
import com.garpr.android.settings.Settings;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;
import com.google.android.gms.gcm.TaskParams;


public final class SyncManager extends GcmTaskService implements Heartbeat {


    private static final String TAG = "SyncManager";




    public static void cancel() {
        GcmNetworkManager.getInstance(App.getContext()).cancelAllTasks(SyncManager.class);
        Settings.Sync.IsScheduled.set(false);
    }


    public static void schedule() {
        if (!Utils.googlePlayServicesAreAvailable()) {
            Console.w(TAG, "Failed to schedule GcmNetworkTask because Google Play Services "
                    + " are unavailable");
            return;
        }

        final PeriodicTask.Builder builder = new PeriodicTask.Builder()
                .setPersisted(true)
                .setRequiresCharging(Settings.Sync.ChargingIsNecessary.get())
                .setRequiredNetwork(Task.NETWORK_STATE_CONNECTED)
                .setService(SyncManager.class)
                .setTag(TAG)
                .setUpdateCurrent(true);

        if (BuildConfig.DEBUG) {
            builder.setPeriod(60L * 60L);
        } else {
            builder.setPeriod(60L * 60L * 24L);
        }

        final Context context = App.getContext();
        final PeriodicTask task = builder.build();
        GcmNetworkManager.getInstance(context).schedule(task);
        Settings.Sync.IsScheduled.set(true);

        Console.d(TAG, "GcmNetworkTask has been scheduled " + printConfigurationString());
    }


    @Override
    public boolean isAlive() {
        return true;
    }


    @Override
    public int onRunTask(final TaskParams taskParams) {
        Console.d(TAG, "Running GcmNetworkTask " + printConfigurationString());

        if (Settings.Sync.WifiIsNecessary.get()) {
            final Context context = App.getContext();
            final ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (ConnectivityManagerCompat.isActiveNetworkMetered(cm)) {
                Console.d(TAG, "Rescheduling GcmNetworkTask, we're on a metered network");
                return GcmNetworkManager.RESULT_RESCHEDULE;
            }
        }

        Rankings.checkForUpdates(new Response<Rankings.Result>(TAG, this) {
            @Override
            public void error(final Exception e) {
                Console.e(TAG, "Error checking for rankings updates", e);
            }


            @Override
            public void success(final Rankings.Result result) {
                if (result == Rankings.Result.UPDATE_AVAILABLE) {
                    Console.d(TAG, "A new roster is available");
                    Notifications.showRankingsUpdated();
                } else {
                    Console.d(TAG, "No new roster available");
                }

                Settings.Sync.LastDate.set(System.currentTimeMillis());
            }
        });

        return GcmNetworkManager.RESULT_SUCCESS;
    }


    private static String printConfigurationString() {
        return "(charging necessary: " + Settings.Sync.ChargingIsNecessary.get() +
                ") (wifi necessary: " + Settings.Sync.WifiIsNecessary.get() + ')';
    }


    @Override
    public String toString() {
        return TAG;
    }


}
