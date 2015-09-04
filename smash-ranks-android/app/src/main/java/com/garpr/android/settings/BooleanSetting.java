package com.garpr.android.settings;


public final class BooleanSetting extends Setting<Boolean> {


    public BooleanSetting(final String name, final String key) {
        super(name, key, Boolean.FALSE);
    }


    public BooleanSetting(final String name, final String key, final Boolean defaultValue) {
        super(name, key, defaultValue);

        if (defaultValue == null) {
            throw new IllegalArgumentException("defaultValue can't be null");
        }
    }


    @Override
    public Boolean get() {
        return readSharedPreferences().getBoolean(mKey, mDefaultValue);
    }


    @Override
    public void set(final Boolean newValue, final boolean notifyListeners) {
        writeSharedPreferences().putBoolean(mKey, newValue).apply();
        super.set(newValue, notifyListeners);
    }


    public Boolean toggle() {
        set(!get());
        return get();
    }


}
