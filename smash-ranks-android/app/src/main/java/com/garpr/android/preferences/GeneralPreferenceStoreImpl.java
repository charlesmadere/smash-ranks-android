package com.garpr.android.preferences;

import android.support.annotation.NonNull;

import com.garpr.android.models.AbsPlayer;
import com.garpr.android.models.NightMode;
import com.garpr.android.models.Region;
import com.garpr.android.preferences.persistent.PersistentGsonPreference;
import com.garpr.android.preferences.persistent.PersistentIntegerPreference;
import com.google.gson.Gson;

public class GeneralPreferenceStoreImpl implements GeneralPreferenceStore {

    private final Gson mGson;
    private final KeyValueStore mKeyValueStore;
    private final Region mDefaultRegion;

    private Preference<AbsPlayer> mIdentity;
    private Preference<Integer> mLastVersion;
    private Preference<NightMode> mNightMode;
    private Preference<Region> mCurrentRegion;


    public GeneralPreferenceStoreImpl(@NonNull final Gson gson,
            @NonNull final KeyValueStore keyValueStore, @NonNull final Region defaultRegion) {
        mGson = gson;
        mKeyValueStore = keyValueStore;
        mDefaultRegion = defaultRegion;
    }

    @Override
    public void clear() {
        mKeyValueStore.clear();
    }

    @Override
    public Preference<Region> getCurrentRegion() {
        if (mCurrentRegion == null) {
            mCurrentRegion = new PersistentGsonPreference<>("CURRENT_REGION", mDefaultRegion,
                    mKeyValueStore, Region.class, mGson);
        }

        return mCurrentRegion;
    }

    @Override
    public Preference<AbsPlayer> getIdentity() {
        if (mIdentity == null) {
            mIdentity = new PersistentGsonPreference<>("IDENTITY", null, mKeyValueStore,
                    AbsPlayer.class, mGson);
        }

        return mIdentity;
    }

    @Override
    public Preference<Integer> getLastVersion() {
        if (mLastVersion == null) {
            mLastVersion = new PersistentIntegerPreference("LAST_VERSION", null, mKeyValueStore);
        }

        return mLastVersion;
    }

    @Override
    public Preference<NightMode> getNightMode() {
        if (mNightMode == null) {
            mNightMode = new PersistentGsonPreference<>("NIGHT_MODE", NightMode.SYSTEM,
                    mKeyValueStore, NightMode.class, mGson);
        }

        return mNightMode;
    }

}
