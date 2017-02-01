package com.garpr.android.misc;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.garpr.android.preferences.StringPreference;

public class RegionManagerImpl implements RegionManager {

    private final StringPreference mCurrentRegion;


    public RegionManagerImpl(final StringPreference currentRegion) {
        mCurrentRegion = currentRegion;
    }

    @NonNull
    @Override
    public String getCurrentRegion() {
        final String currentRegion = mCurrentRegion.get();

        if (TextUtils.isEmpty(currentRegion)) {
            throw new IllegalStateException("current region is empty!");
        }

        return currentRegion;
    }

}
