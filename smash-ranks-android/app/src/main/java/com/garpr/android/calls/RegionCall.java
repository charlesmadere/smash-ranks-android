package com.garpr.android.calls;


import com.garpr.android.misc.Response;
import com.garpr.android.models.Region;
import com.garpr.android.settings.Settings;


public abstract class RegionCall<T> extends Call<T> {


    private final Region mRegion;




    protected RegionCall(final Response<T> response, final boolean ignoreCache) throws
            IllegalArgumentException {
        this(response, ignoreCache, Settings.Region.get());
    }


    protected RegionCall(final Response<T> response, final boolean ignoreCache, final Region region) {
        super(response, ignoreCache);
        mRegion = region;
    }


    public final Region getRegion() {
        return mRegion;
    }


    @Override
    public String getUrl() {
        final String regionId = mRegion.getId();
        return super.getUrl() + regionId + '/';
    }


}
