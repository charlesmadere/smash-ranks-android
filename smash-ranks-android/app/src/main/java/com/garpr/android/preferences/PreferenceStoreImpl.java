package com.garpr.android.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceStoreImpl implements PreferenceStore {

    private final Context mContext;


    public PreferenceStoreImpl(final Context context) {
        mContext = context.getApplicationContext();
    }

    private void clear(final SharedPreferences sharedPreferences) {
        sharedPreferences.edit().clear().apply();
    }

    @Override
    public void clearAll() {
        clear(getDefaultSharedPreferences());
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public SharedPreferences getDefaultSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(mContext);
    }

}
