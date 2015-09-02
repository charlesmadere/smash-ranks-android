package com.garpr.android.settings;


import com.garpr.android.misc.Utils;

import org.json.JSONException;
import org.json.JSONObject;


public final class JSONSetting extends Setting<JSONObject> {


    private final StringSetting mStringSetting;




    JSONSetting(final String name, final String key) {
        super(name, key);
        mStringSetting = new StringSetting(name, key);
    }


    @Override
    public void delete() {
        mStringSetting.delete();
    }


    @Override
    public JSONObject get() {
        final String string = mStringSetting.get();

        if (Utils.validStrings(string)) {
            try {
                return new JSONObject(string);
            } catch (final JSONException e) {
                throw new RuntimeException(e);
            }
        } else {
            return null;
        }
    }


    @Override
    public void set(final JSONObject newValue) {
        final String newValueString;

        if (newValue == null) {
            newValueString = null;
        } else {
            newValueString = newValue.toString();
        }

        mStringSetting.set(newValueString);
    }


}
