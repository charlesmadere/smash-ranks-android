package com.garpr.android.settings;


import com.garpr.android.models.Region;

import org.json.JSONException;
import org.json.JSONObject;


public final class RegionSetting extends Setting<Region> {


    private final JSONSetting mJSONSetting;




    RegionSetting(final String name, final String key) {
        super(name, key);
        mJSONSetting = new JSONSetting(name, key);
    }


    @Override
    public void delete() {
        mJSONSetting.delete();
    }


    @Override
    public Region get() {
        final JSONObject json = mJSONSetting.get();

        try {
            return new Region(json);
        } catch (final JSONException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void set(final Region newValue, final boolean notifyListeners) {
        mJSONSetting.set(newValue.toJSON());
        super.set(newValue, notifyListeners);
    }


}
