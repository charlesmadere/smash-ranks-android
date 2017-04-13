package com.garpr.android.misc;

import android.content.Context;
import android.content.ContextWrapper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.garpr.android.models.Region;
import com.garpr.android.preferences.Preference;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class RegionManagerImpl implements RegionManager {

    private static final String TAG = "RegionManagerImpl";

    private final List<WeakReference<OnRegionChangeListener>> mListeners;
    private final Preference<Region> mRegion;
    private final Timber mTimber;


    public RegionManagerImpl(@NonNull final Preference<Region> region,
            @NonNull final Timber timber) {
        mListeners = new LinkedList<>();
        mRegion = region;
        mTimber = timber;
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
    public Region getRegion() {
        final Region region = mRegion.get();

        if (region == null) {
            throw new IllegalStateException("region is null");
        }

        return region;
    }

    @NonNull
    @Override
    public Region getRegion(@Nullable final Context context) {
        if (context instanceof RegionHandle) {
            final Region region = ((RegionHandle) context).getCurrentRegion();

            if (region != null) {
                return region;
            }
        }

        if (context instanceof ContextWrapper) {
            return getRegion(((ContextWrapper) context).getBaseContext());
        }

        return getRegion();
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
    public void setRegion(@NonNull final Region region) {
        mTimber.d(TAG, "old region is \"" + getRegion() + "\", new region is \"" + region + "\"");
        mRegion.set(region);
        notifyListeners();
    }

}
