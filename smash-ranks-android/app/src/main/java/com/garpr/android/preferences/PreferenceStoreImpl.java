package com.garpr.android.preferences;

import android.content.Context;

import com.garpr.android.models.NightMode;

public class PreferenceStoreImpl implements PreferenceStore {

    private final Context mContext;

    private Preference<NightMode> mNightMode;


    public PreferenceStoreImpl(final Context context) {
        mContext = context.getApplicationContext();
    }

    @Override
    public void clearAll() {
        mNightMode.delete();
    }

    @Override
    public Preference<NightMode> getNightMode() {
        if (mNightMode == null) {
            // TODO
        }

        return mNightMode;
    }

}
