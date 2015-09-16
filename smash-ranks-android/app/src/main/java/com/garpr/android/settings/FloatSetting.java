package com.garpr.android.settings;


public final class FloatSetting extends Setting<Float> {


    public FloatSetting(final String name, final String key) {
        super(name, key, 0f);
    }


    public FloatSetting(final String name, final String key, final Float defaultValue) {
        super(name, key, defaultValue);

        if (defaultValue == null) {
            throw new IllegalArgumentException("defaultValue can't be null");
        }
    }


    @Override
    public boolean exists() {
        return super.exists() && get() != Float.NaN;
    }


    @Override
    public Float get() {
        return readSharedPreferences().getFloat(mKey, mDefaultValue);
    }


    @Override
    public void set(final Float newValue, final boolean notifyListeners) {
        writeSharedPreferences().putFloat(mKey, mDefaultValue);
        super.set(newValue, notifyListeners);
    }


}
