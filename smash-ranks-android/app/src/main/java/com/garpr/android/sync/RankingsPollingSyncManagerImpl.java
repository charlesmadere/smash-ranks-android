package com.garpr.android.sync;

import android.support.annotation.NonNull;

import com.garpr.android.misc.GoogleApiWrapper;
import com.garpr.android.misc.Timber;
import com.garpr.android.models.PollFrequency;
import com.garpr.android.preferences.RankingsPollingPreferenceStore;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;

import java.util.concurrent.TimeUnit;

public class RankingsPollingSyncManagerImpl implements RankingsPollingSyncManager {

    private static final String TAG = "RankingsPollingSyncManagerImpl";

    private final GoogleApiWrapper mGoogleApiWrapper;
    private final RankingsPollingPreferenceStore mRankingsPollingPreferenceStore;
    private final Timber mTimber;


    public RankingsPollingSyncManagerImpl(@NonNull final GoogleApiWrapper googleApiWrapper,
            @NonNull final RankingsPollingPreferenceStore rankingsPollingPreferenceStore,
            @NonNull final Timber timber) {
        mGoogleApiWrapper = googleApiWrapper;
        mRankingsPollingPreferenceStore = rankingsPollingPreferenceStore;
        mTimber = timber;
    }

    private void disable() {
        final GcmNetworkManager gcmNetworkManager = mGoogleApiWrapper.getGcmNetworkManager();
        gcmNetworkManager.cancelAllTasks(RankingsPollingService.class);
        mTimber.d(TAG, "Sync has been disabled");
    }

    private void enable() {
        if (!mGoogleApiWrapper.isGooglePlayServicesAvailable()) {
            mTimber.w(TAG, "failed to schedule sync because Google Play Services are unavailable");
            return;
        }

        final PeriodicTask.Builder builder = new PeriodicTask.Builder()
                .setPersisted(true)
                .setService(RankingsPollingService.class)
                .setTag(TAG)
                .setUpdateCurrent(true);

        final PollFrequency pollFrequency = mRankingsPollingPreferenceStore.getPollFrequency().get();
        // noinspection ConstantConditions
        builder.setPeriod(TimeUnit.MILLISECONDS.toSeconds(pollFrequency.getTimeInMillis()));

        if (Boolean.TRUE.equals(mRankingsPollingPreferenceStore.getChargingRequired().get())) {
            builder.setRequiresCharging(true);
        } else {
            builder.setRequiresCharging(false);
        }

        if (Boolean.TRUE.equals(mRankingsPollingPreferenceStore.getWifiRequired().get())) {
            builder.setRequiredNetwork(Task.NETWORK_STATE_UNMETERED);
        } else {
            builder.setRequiredNetwork(Task.NETWORK_STATE_CONNECTED);
        }

        final PeriodicTask periodicTask = builder.build();
        final GcmNetworkManager gcmNetworkManager = mGoogleApiWrapper.getGcmNetworkManager();
        gcmNetworkManager.schedule(periodicTask);

        mTimber.d(TAG, "Sync has been enabled");
    }

    @Override
    public void enableOrDisable() {
        if (Boolean.TRUE.equals(mRankingsPollingPreferenceStore.getEnabled().get())) {
            enable();
        } else {
            disable();
        }
    }

}
