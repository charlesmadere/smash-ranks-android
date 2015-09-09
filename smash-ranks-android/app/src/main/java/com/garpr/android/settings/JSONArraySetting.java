package com.garpr.android.settings;


import com.garpr.android.misc.Utils;

import org.json.JSONArray;
import org.json.JSONException;


public final class JSONArraySetting extends Setting<JSONArray> {


    private final StringSetting mStringSetting;




    public JSONArraySetting(final String name, final String key) {
        super(name, key);
        mStringSetting = new StringSetting(name, key);
    }


    @Override
    public boolean exists() {
        if (super.exists()) {
            final JSONArray json = get();
            return json != null && json.length() >= 1;
        } else {
            return false;
        }
    }


    @Override
    public JSONArray get() {
        final String string = mStringSetting.get();
        final JSONArray jsonArray;

        if (Utils.validStrings(string)) {
            try {
                jsonArray = new JSONArray(string);
            } catch (final JSONException e) {
                throw new RuntimeException(e);
            }
        } else {
            jsonArray = null;
        }

        return jsonArray;
    }


    @Override
    public void set(final JSONArray newValue, final boolean notifyListeners) {
        final String newValueString;

        if (newValue == null) {
            newValueString = null;
        } else {
            newValueString = newValue.toString();
        }

        mStringSetting.set(newValueString);
        super.set(newValue, notifyListeners);
    }


}
