package com.garpr.android.calls;


import com.garpr.android.models.Region;
import com.garpr.android.settings.Settings;


public abstract class RegionBasedCall<T> extends Call<T> {


    private final Region mRegion;




    RegionBasedCall(final Response<T> response, final boolean ignoreCache)
            throws IllegalArgumentException {
        this(response, ignoreCache, Settings.Region.get());
    }


    RegionBasedCall(final Response<T> response, final boolean ignoreCache, final Region region) {
        super(response, ignoreCache);
        mRegion = region;
    }


    @Override
    String getUrl() {
        final String regionId = mRegion.getId();
        return super.getUrl() + regionId + '/';
    }


}
