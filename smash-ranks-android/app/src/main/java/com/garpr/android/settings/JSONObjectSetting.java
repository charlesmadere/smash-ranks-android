package com.garpr.android.settings;


import com.garpr.android.misc.Utils;

import org.json.JSONException;
import org.json.JSONObject;


public final class JSONObjectSetting extends Setting<JSONObject> {


    private final StringSetting mStringSetting;




    public JSONObjectSetting(final String name, final String key) {
        super(name, key);
        mStringSetting = new StringSetting(name, key);
    }


    @Override
    public boolean exists() {
        return super.exists() && mStringSetting.exists();
    }


    @Override
    public JSONObject get() {
        final String string = mStringSetting.get();
        final JSONObject jsonObject;

        if (Utils.validStrings(string)) {
            try {
                jsonObject = new JSONObject(string);
            } catch (final JSONException e) {
                throw new RuntimeException(e);
            }
        } else {
            jsonObject = null;
        }

        return jsonObject;
    }


    @Override
    public void set(final JSONObject newValue, final boolean notifyListeners) {
        final String newValueString;

        if (newValue == null) {
            newValueString = null;
        } else {
            newValueString = newValue.toString();
        }

        mStringSetting.set(newValueString, notifyListeners);
        super.set(newValue, notifyListeners);
    }


}
