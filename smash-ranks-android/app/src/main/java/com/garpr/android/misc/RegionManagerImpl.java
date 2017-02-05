package com.garpr.android.misc;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.garpr.android.preferences.Preference;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.LinkedList;

public class RegionManagerImpl implements RegionManager {

    private final LinkedList<WeakReference<OnRegionChangeListener>> mListeners;
    private final Preference<String> mCurrentRegion;


    public RegionManagerImpl(@NonNull final Preference<String> currentRegion) {
        mListeners = new LinkedList<>();
        mCurrentRegion = currentRegion;
    }

    @Override
    public void addListener(@NonNull final OnRegionChangeListener listener) {
        synchronized (mListeners) {
            boolean addListener = true;
            final Iterator<WeakReference<OnRegionChangeListener>> iterator = mListeners.iterator();

            while (iterator.hasNext()) {
                final WeakReference<OnRegionChangeListener> reference = iterator.next();
                final OnRegionChangeListener item = reference.get();

                if (item == null) {
                    iterator.remove();
                } else if (item == listener) {
                    addListener = false;
                }
            }

            if (addListener) {
                mListeners.add(new WeakReference<>(listener));
            }
        }
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

    private void notifyListeners() {
        synchronized (mListeners) {
            final Iterator<WeakReference<OnRegionChangeListener>> iterator = mListeners.iterator();

            while (iterator.hasNext()) {
                final WeakReference<OnRegionChangeListener> reference = iterator.next();
                final OnRegionChangeListener item = reference.get();

                if (item == null) {
                    iterator.remove();
                } else {
                    item.onRegionChange(this);
                }
            }
        }
    }

    @Override
    public void removeListener(@Nullable final OnRegionChangeListener listener) {
        synchronized (mListeners) {
            final Iterator<WeakReference<OnRegionChangeListener>> iterator = mListeners.iterator();

            while (iterator.hasNext()) {
                final WeakReference<OnRegionChangeListener> reference = iterator.next();
                final OnRegionChangeListener item = reference.get();

                if (item == null || item == listener) {
                    iterator.remove();
                }
            }
        }
    }

    @Override
    public void setCurrentRegion(@NonNull final String region) {
        mCurrentRegion.set(region);
        notifyListeners();
    }

}
