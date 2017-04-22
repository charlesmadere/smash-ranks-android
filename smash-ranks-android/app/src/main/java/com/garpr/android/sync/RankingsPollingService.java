package com.garpr.android.sync;

import android.app.PendingIntent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.activities.HomeActivity;
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

    private void showRankingsUpdated() {
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setCategory(NotificationCompat.CATEGORY_SOCIAL)
                .setContentTitle(getString(R.string.gar_pr))
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setSmallIcon(R.drawable.notification)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        builder.setContentIntent(PendingIntent.getActivity(this, 0,
                HomeActivity.getLaunchIntent(this), PendingIntent.FLAG_UPDATE_CURRENT));

        builder.setContentText(getString(R.string.x_rankings_have_been_updated,
                        mRegionManager.getRegion()));

        if (Boolean.TRUE.equals(mRankingsPollingPreferenceStore.getVibrationEnabled().get())) {
            builder.setDefaults(NotificationCompat.DEFAULT_LIGHTS |
                    NotificationCompat.DEFAULT_VIBRATE);
        } else {
            builder.setDefaults(NotificationCompat.DEFAULT_LIGHTS);
        }

        final Drawable largeIconDrawable = ContextCompat.getDrawable(this, R.mipmap.launcher);
        Bitmap largeIconBitmap = null;

        if (largeIconDrawable instanceof BitmapDrawable) {
            largeIconBitmap = ((BitmapDrawable) largeIconDrawable).getBitmap();
        }

        if (largeIconBitmap == null) {
            largeIconBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.launcher);
        }

        builder.setLargeIcon(largeIconBitmap);

        final Uri ringtoneUri = mRankingsPollingPreferenceStore.getRingtone().get();

        if (ringtoneUri != null) {
            builder.setSound(ringtoneUri);
        }

        mNotificationManager.show(builder);
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
            showRankingsUpdated();
        }
    }

}
