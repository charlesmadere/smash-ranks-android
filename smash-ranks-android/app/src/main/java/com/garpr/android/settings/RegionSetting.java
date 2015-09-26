package com.garpr.android.settings;


import com.garpr.android.models.Region;

import org.json.JSONException;
import org.json.JSONObject;


public final class RegionSetting extends Setting<Region> {


    private final JSONObjectSetting mJSONObjectSetting;




    public RegionSetting(final String name, final String key) {
        super(name, key);
        mJSONObjectSetting = new JSONObjectSetting(name, key);
    }


    @Override
    public boolean exists() {
        return super.exists() && mJSONObjectSetting.exists();
    }


    @Override
    public Region get() {
        final JSONObject json = mJSONObjectSetting.get();

        try {
            final Region region;

            if (json == null) {
                region = null;
            } else {
                region = new Region(json);
            }

            return region;
        } catch (final JSONException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void set(final Region newValue, final boolean notifyListeners) {
        mJSONObjectSetting.set(newValue.toJSON(), notifyListeners);
        super.set(newValue, notifyListeners);
    }


}
