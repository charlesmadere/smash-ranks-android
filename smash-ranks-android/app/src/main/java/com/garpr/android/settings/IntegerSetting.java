package com.garpr.android.settings;


public final class IntegerSetting extends Setting<Integer> {


    public IntegerSetting(final String name, final String key) {
        super(name, key, 0);
    }


    public IntegerSetting(final String name, final String key, final Integer defaultValue) {
        super(name, key, defaultValue);

        if (defaultValue == null) {
            throw new IllegalArgumentException("defaultValue can't be null");
        }
    }


    @Override
    public Integer get() {
        return readSharedPreferences().getInt(mKey, mDefaultValue);
    }


    @Override
    public void set(final Integer newValue, final boolean notifyListeners) {
        writeSharedPreferences().putInt(mKey, newValue).apply();
        super.set(newValue, notifyListeners);
    }


}
