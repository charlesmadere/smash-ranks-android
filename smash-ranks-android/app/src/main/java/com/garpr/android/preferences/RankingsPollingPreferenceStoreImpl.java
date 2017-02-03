package com.garpr.android.preferences;

import android.support.annotation.NonNull;

import com.garpr.android.models.PollFrequency;
import com.garpr.android.preferences.persistent.PersistentBooleanPreference;
import com.garpr.android.preferences.persistent.PersistentGsonPreference;
import com.garpr.android.preferences.persistent.PersistentLongPreference;
import com.google.gson.Gson;

public class RankingsPollingPreferenceStoreImpl implements RankingsPollingPreferenceStore {

    private final Gson mGson;
    private final KeyValueStore mKeyValueStore;

    private Preference<Boolean> mChargingRequired;
    private Preference<Boolean> mEnabled;
    private Preference<Boolean> mWifiRequired;
    private Preference<Long> mLastPoll;
    private Preference<PollFrequency> mPollFrequency;


    public RankingsPollingPreferenceStoreImpl(@NonNull final Gson gson,
            @NonNull final KeyValueStore keyValueStore) {
        mGson = gson;
        mKeyValueStore = keyValueStore;
    }

    @Override
    public void clear() {
        mKeyValueStore.clear();
    }

    @Override
    public Preference<Boolean> getChargingRequired() {
        if (mChargingRequired == null) {
            mChargingRequired = new PersistentBooleanPreference("CHARGING_REQUIRED", Boolean.FALSE,
                    mKeyValueStore);
        }

        return mChargingRequired;
    }

    @Override
    public Preference<Boolean> getEnabled() {
        if (mEnabled == null) {
            mEnabled = new PersistentBooleanPreference("ENABLED", Boolean.TRUE, mKeyValueStore);
        }

        return mEnabled;
    }

    @Override
    public Preference<Long> getLastPoll() {
        if (mLastPoll == null) {
            mLastPoll = new PersistentLongPreference("LAST_POLL", null, mKeyValueStore);
        }

        return mLastPoll;
    }

    @Override
    public Preference<PollFrequency> getPollFrequency() {
        if (mPollFrequency == null) {
            mPollFrequency = new PersistentGsonPreference<>("POLL_FREQUENCY", PollFrequency.DAILY,
                    mKeyValueStore, PollFrequency.class, mGson);
        }

        return mPollFrequency;
    }

    @Override
    public Preference<Boolean> getWifiRequired() {
        if (mWifiRequired == null) {
            mWifiRequired = new PersistentBooleanPreference("WIFI_REQUIRED", Boolean.TRUE,
                    mKeyValueStore);
        }

        return mWifiRequired;
    }

}
