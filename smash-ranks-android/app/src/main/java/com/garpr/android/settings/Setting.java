package com.garpr.android.settings;


import android.content.SharedPreferences;

import com.garpr.android.misc.Utils;


public abstract class Setting<T> {


    final T mDefaultValue;
    final String mKey;
    private final String mName;




    Setting(final String name, final String key) {
        this(name, key, null);
    }


    Setting(final String name, final String key, final T defaultValue) {
        if (!Utils.validStrings(name, key)) {
            throw new IllegalArgumentException("name and key can't be null / empty / whitespace");
        }

        mName = name;
        mKey = key;
        mDefaultValue = defaultValue;
    }


    public void delete() {
        writeSharedPreferences().remove(mKey).apply();
    }


    public boolean exists() {
        return readSharedPreferences().contains(mKey);
    }


    public abstract T get();


    final SharedPreferences readSharedPreferences() {
        return Settings.get(mName);
    }


    public abstract void set(final T newValue);


    final SharedPreferences.Editor writeSharedPreferences() {
        return Settings.edit(mName);
    }


}
