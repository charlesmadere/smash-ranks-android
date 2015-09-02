package com.garpr.android.settings;


public final class BooleanSetting extends Setting<Boolean> {


    BooleanSetting(final String name, final String key, final Boolean defaultValue) {
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
    public void set(final Boolean newValue) {
        writeSharedPreferences().putBoolean(mKey, newValue).apply();
    }


    public Boolean toggle() {
        set(!get());
        return get();
    }


}
