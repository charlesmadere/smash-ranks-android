package com.garpr.android.misc;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface RegionManager {

    void addListener(@NonNull final OnRegionChangeListener listener);

    @NonNull
    String getCurrentRegion();

    @NonNull
    String getCurrentRegion(@Nullable final Context context);

    void removeListener(@Nullable final OnRegionChangeListener listener);

    void setCurrentRegion(@NonNull final String region);


    interface OnRegionChangeListener {
        void onRegionChange(final RegionManager regionManager);
    }

    interface RegionHandle {
        @Nullable
        String getCurrentRegion();
    }

}
