package com.garpr.android.sync;

import android.support.annotation.Nullable;

import com.garpr.android.App;
import com.garpr.android.misc.DeviceUtils;
import com.garpr.android.misc.NotificationManager;
import com.garpr.android.misc.RegionManager;
import com.garpr.android.misc.Timber;
import com.garpr.android.models.RankingsBundle;
import com.garpr.android.models.SimpleDate;
import com.garpr.android.networking.ApiListener;
import com.garpr.android.networking.ServerApi;
import com.garpr.android.preferences.Preference;
import com.garpr.android.preferences.RankingsPollingPreferenceStore;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;

import javax.inject.Inject;

public class RankingsPollingService extends GcmTaskService implements ApiListener<RankingsBundle> {

    private static final String TAG = "RankingsPollingService";

    private SimpleDate mOldRankingsDate;

    @Inject
    DeviceUtils mDeviceUtils;

    @Inject
    NotificationManager mNotificationManager;

    @Inject
    RankingsPollingPreferenceStore mRankingsPollingPreferenceStore;

    @Inject
    RankingsPollingSyncManager mRankingsPollingSyncManager;

    @Inject
    RegionManager mRegionManager;

    @Inject
    ServerApi mServerApi;

    @Inject
    Timber mTimber;


    public RankingsPollingService() {
        App.get().getAppComponent().inject(this);
    }

    @Override
    public void failure(final int errorCode) {
        mTimber.e(TAG, "canceling any notifications, failure fetching rankings");
        mNotificationManager.cancelAll();
    }

    @Override
    public boolean isAlive() {
        return true;
    }

    @Override
    public void onInitializeTasks() {
        super.onInitializeTasks();
        mRankingsPollingSyncManager.enableOrDisable();
    }

    @Override
    public int onRunTask(final TaskParams taskParams) {
        mOldRankingsDate = mRankingsPollingPreferenceStore.getRankingsDate().get();

        if (mOldRankingsDate == null) {
            mTimber.d(TAG, "canceling sync, the user has no rankings date");
            return GcmNetworkManager.RESULT_SUCCESS;
        }

        if (!mDeviceUtils.hasNetworkConnection()) {
            mTimber.d(TAG, "rescheduling sync, the device does not have a network connection");
            return GcmNetworkManager.RESULT_RESCHEDULE;
        }

        final Preference<SimpleDate> lastPollPref = mRankingsPollingPreferenceStore.getLastPoll();
        final SimpleDate lastPoll = lastPollPref.get();
        final SimpleDate currentPoll = new SimpleDate();
        lastPollPref.set(currentPoll);
        mTimber.d(TAG, "syncing now... last poll: " + lastPoll + " current poll: " + currentPoll);

        mServerApi.getRankings(mRegionManager.getRegion(), this);
        return GcmNetworkManager.RESULT_SUCCESS;
    }

    @Override
    public void success(@Nullable final RankingsBundle rankingsBundle) {
        if (rankingsBundle == null) {
            mTimber.d(TAG, "canceling any notifications, received a null rankings bundle");
            mNotificationManager.cancelAll();
            return;
        }

        final SimpleDate newRankingsDate = mRankingsPollingPreferenceStore.getRankingsDate().get();

        if (newRankingsDate == null) {
            // should be impossible...
            mTimber.e(TAG, "new rankings date is null! canceling any notifications");
            mNotificationManager.cancelAll();
        } else if (newRankingsDate.happenedAfter(mOldRankingsDate)) {
            mNotificationManager.showRankingsUpdated(this);
        }
    }

}
