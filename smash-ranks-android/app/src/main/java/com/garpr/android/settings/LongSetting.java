package com.garpr.android.settings;


public final class LongSetting extends Setting<Long> {


    LongSetting(final String name, final String key, final Long defaultValue) {
        super(name, key, defaultValue);

        if (defaultValue == null) {
            throw new IllegalArgumentException("defaultValue can't be null");
        }
    }


    @Override
    public Long get() {
        return readSharedPreferences().getLong(mKey, mDefaultValue);
    }


    @Override
    public void set(final Long newValue) {
        writeSharedPreferences().putLong(mKey, newValue).apply();
    }


}
