package com.garpr.android.misc;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.garpr.android.preferences.Preference;

import java.lang.ref.WeakReference;
import java.util.LinkedList;

public class RegionManagerImpl implements RegionManager {

    private final LinkedList<WeakReference<OnRegionChangeListener>> mOnRegionChangeListeners;
    private final Preference<String> mCurrentRegion;


    public RegionManagerImpl(final Preference<String> currentRegion) {
        mOnRegionChangeListeners = new LinkedList<>();
        mCurrentRegion = currentRegion;
    }

    @Override
    public void addListener(@NonNull final OnRegionChangeListener l) {

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

    @Override
    public void removeListener(@Nullable final OnRegionChangeListener l) {

    }

    @Override
    public void setCurrentRegion(@NonNull final String region) {
        mCurrentRegion.set(region);
    }

}
