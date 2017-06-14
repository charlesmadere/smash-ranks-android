package com.garpr.android.preferences;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.garpr.android.models.PollFrequency;
import com.garpr.android.models.SimpleDate;
import com.garpr.android.preferences.persistent.PersistentBooleanPreference;
import com.garpr.android.preferences.persistent.PersistentGsonPreference;
import com.garpr.android.preferences.persistent.PersistentUriPreference;
import com.google.gson.Gson;

public class RankingsPollingPreferenceStoreImpl implements RankingsPollingPreferenceStore {

    private final Gson mGson;
    private final KeyValueStore mKeyValueStore;

    private Preference<Boolean> mChargingRequired;
    private Preference<Boolean> mEnabled;
    private Preference<Boolean> mVibrationEnabled;
    private Preference<Boolean> mWifiRequired;
    private Preference<PollFrequency> mPollFrequency;
    private Preference<SimpleDate> mLastPoll;
    private Preference<SimpleDate> mRankingsDate;
    private Preference<Uri> mRingtone;


    public RankingsPollingPreferenceStoreImpl(@NonNull final Gson gson,
            @NonNull final KeyValueStore keyValueStore) {
        mGson = gson;
        mKeyValueStore = keyValueStore;
    }

    @Override
    public void clear() {
        mKeyValueStore.clear();
    }

    @NonNull
    @Override
    public Preference<Boolean> getChargingRequired() {
        if (mChargingRequired == null) {
            mChargingRequired = new PersistentBooleanPreference("CHARGING_REQUIRED", Boolean.FALSE,
                    mKeyValueStore);
        }

        return mChargingRequired;
    }

    @NonNull
    @Override
    public Preference<Boolean> getEnabled() {
        if (mEnabled == null) {
            mEnabled = new PersistentBooleanPreference("ENABLED", Boolean.TRUE, mKeyValueStore);
        }

        return mEnabled;
    }

    @NonNull
    @Override
    public KeyValueStore getKeyValueStore() {
        return mKeyValueStore;
    }

    @NonNull
    @Override
    public Preference<SimpleDate> getLastPoll() {
        if (mLastPoll == null) {
            mLastPoll = new PersistentGsonPreference<>("LAST_POLL", null, mKeyValueStore,
                    SimpleDate.class, mGson);
        }

        return mLastPoll;
    }

    @NonNull
    @Override
    public Preference<PollFrequency> getPollFrequency() {
        if (mPollFrequency == null) {
            mPollFrequency = new PersistentGsonPreference<>("POLL_FREQUENCY", PollFrequency.DAILY,
                    mKeyValueStore, PollFrequency.class, mGson);
        }

        return mPollFrequency;
    }

    @NonNull
    @Override
    public Preference<SimpleDate> getRankingsDate() {
        if (mRankingsDate == null) {
            mRankingsDate = new PersistentGsonPreference<>("RANKINGS_DATE", null, mKeyValueStore,
                    SimpleDate.class, mGson);
        }

        return mRankingsDate;
    }

    @NonNull
    @Override
    public Preference<Uri> getRingtone() {
        if (mRingtone == null) {
            mRingtone = new PersistentUriPreference("RINGTONE", null, mKeyValueStore);
        }

        return mRingtone;
    }

    @NonNull
    @Override
    public Preference<Boolean> getVibrationEnabled() {
        if (mVibrationEnabled == null) {
            mVibrationEnabled = new PersistentBooleanPreference("VIBRATION_ENABLED", Boolean.FALSE,
                    mKeyValueStore);
        }

        return mVibrationEnabled;
    }

    @NonNull
    @Override
    public Preference<Boolean> getWifiRequired() {
        if (mWifiRequired == null) {
            mWifiRequired = new PersistentBooleanPreference("WIFI_REQUIRED", Boolean.TRUE,
                    mKeyValueStore);
        }

        return mWifiRequired;
    }

}
