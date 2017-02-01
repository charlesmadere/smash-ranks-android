package com.garpr.android.preferences;

import android.app.Application;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.garpr.android.models.NightMode;
import com.garpr.android.preferences.persistent.PersistentGsonPreference;
import com.garpr.android.preferences.persistent.PersistentStringPreference;
import com.google.gson.Gson;

public class PreferenceStoreImpl implements PreferenceStore {

    private final Application mApplication;
    private final Gson mGson;
    private final KeyValueStore mKeyValueStore;
    private final String mDefaultRegion;
    private final String mName;

    private Preference<NightMode> mNightMode;
    private Preference<String> mCurrentRegion;


    public PreferenceStoreImpl(@NonNull final Application application, @NonNull final Gson gson,
            @NonNull final KeyValueStore keyValueStore, @NonNull final String defaultRegion) {
        mApplication = application;
        mGson = gson;
        mKeyValueStore = keyValueStore;
        mDefaultRegion = defaultRegion;
        mName = application.getPackageName() + ".Preferences";
    }

    @Override
    public void clearAll() {
        PreferenceManager.getDefaultSharedPreferences(mApplication);
        mKeyValueStore.clear(mName);
    }

    @Override
    public Preference<String> getCurrentRegion() {
        if (mCurrentRegion == null) {
            mCurrentRegion = new PersistentStringPreference(mName, "CURRENT_REGION",
                    mDefaultRegion, mKeyValueStore);
        }

        return mCurrentRegion;
    }

    @Override
    public Preference<NightMode> getNightMode() {
        if (mNightMode == null) {
            mNightMode = new PersistentGsonPreference<>(mName, "NIGHT_MODE", NightMode.SYSTEM,
                    NightMode.class, mKeyValueStore, mGson);
        }

        return mNightMode;
    }

}
