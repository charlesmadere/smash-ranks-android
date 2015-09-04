package com.garpr.android.settings;


public final class StringSetting extends Setting<String> {


    public StringSetting(final String name, final String key) {
        super(name, key);
    }


    public StringSetting(final String name, final String key, final String defaultValue) {
        super(name, key, defaultValue);
    }


    @Override
    public String get() {
        return readSharedPreferences().getString(mKey, mDefaultValue);
    }


    @Override
    public void set(final String newValue, final boolean notifyListeners) {
        writeSharedPreferences().putString(mKey, newValue).apply();
        super.set(newValue, notifyListeners);
    }


}
