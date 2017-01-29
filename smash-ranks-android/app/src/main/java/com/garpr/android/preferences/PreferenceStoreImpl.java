package com.garpr.android.preferences;

import android.content.Context;

public class PreferenceStoreImpl implements PreferenceStore {

    private final Context mContext;


    public PreferenceStoreImpl(final Context context) {
        mContext = context.getApplicationContext();
    }

    @Override
    public void clearAll() {
        // TODO
    }

}
