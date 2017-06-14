package com.garpr.android.preferences;

import android.support.annotation.NonNull;

import com.garpr.android.models.FavoritePlayer;
import com.garpr.android.models.NightMode;
import com.garpr.android.models.Region;
import com.garpr.android.preferences.persistent.PersistentGsonPreference;
import com.garpr.android.preferences.persistent.PersistentIntegerPreference;
import com.google.gson.Gson;

public class GeneralPreferenceStoreImpl implements GeneralPreferenceStore {

    private final Gson mGson;
    private final KeyValueStore mKeyValueStore;
    private final Region mDefaultRegion;

    private Preference<FavoritePlayer> mIdentity;
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

    @NonNull
    @Override
    public Preference<Region> getCurrentRegion() {
        if (mCurrentRegion == null) {
            mCurrentRegion = new PersistentGsonPreference<>("CURRENT_REGION", mDefaultRegion,
                    mKeyValueStore, Region.class, mGson);
        }

        return mCurrentRegion;
    }

    @NonNull
    @Override
    public Preference<FavoritePlayer> getIdentity() {
        if (mIdentity == null) {
            mIdentity = new PersistentGsonPreference<>("IDENTITY", null, mKeyValueStore,
                    FavoritePlayer.class, mGson);
        }

        return mIdentity;
    }

    @NonNull
    @Override
    public KeyValueStore getKeyValueStore() {
        return mKeyValueStore;
    }

    @NonNull
    @Override
    public Preference<Integer> getLastVersion() {
        if (mLastVersion == null) {
            mLastVersion = new PersistentIntegerPreference("LAST_VERSION", null, mKeyValueStore);
        }

        return mLastVersion;
    }

    @NonNull
    @Override
    public Preference<NightMode> getNightMode() {
        if (mNightMode == null) {
            mNightMode = new PersistentGsonPreference<>("NIGHT_MODE", NightMode.SYSTEM,
                    mKeyValueStore, NightMode.class, mGson);
        }

        return mNightMode;
    }

}
