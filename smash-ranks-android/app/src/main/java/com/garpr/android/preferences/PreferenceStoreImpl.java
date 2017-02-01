package com.garpr.android.preferences;

import android.content.Context;

import com.garpr.android.models.NightMode;

public class PreferenceStoreImpl implements PreferenceStore {

    private final Context mContext;

    private Preference<NightMode> mNightMode;
    private Preference<String> mCurrentRegion;


    public PreferenceStoreImpl(final Context context) {
        mContext = context.getApplicationContext();
    }

    @Override
    public void clearAll() {
        mNightMode.delete();
    }

    @Override
    public Preference<String> getCurrentRegion() {
        if (mCurrentRegion == null) {
            // TODO
        }

        return mCurrentRegion;
    }

    @Override
    public Preference<NightMode> getNightMode() {
        if (mNightMode == null) {
            // TODO
        }

        return mNightMode;
    }

}
