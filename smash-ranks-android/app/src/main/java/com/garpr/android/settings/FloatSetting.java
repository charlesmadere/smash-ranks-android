package com.garpr.android.settings;


public final class FloatSetting extends Setting<Float> {


    FloatSetting(final String name, final String key, final Float defaultValue) {
        super(name, key, defaultValue);

        if (defaultValue == null) {
            throw new IllegalArgumentException("defaultValue can't be null");
        }
    }


    @Override
    public Float get() {
        return readSharedPreferences().getFloat(mKey, mDefaultValue);
    }


    @Override
    public void set(final Float newValue) {
        writeSharedPreferences().putFloat(mKey, mDefaultValue);
    }


}
