package com.garpr.android.misc;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.garpr.android.models.Region;

public interface RegionManager {

    void addListener(@NonNull final OnRegionChangeListener listener);

    @NonNull
    Region getRegion();

    @NonNull
    Region getRegion(@Nullable final Context context);

    void removeListener(@Nullable final OnRegionChangeListener listener);

    void setRegion(@NonNull final Region region);


    interface OnRegionChangeListener {
        void onRegionChange(final RegionManager regionManager);
    }

    interface RegionHandle {
        @Nullable
        Region getCurrentRegion();
    }

}
