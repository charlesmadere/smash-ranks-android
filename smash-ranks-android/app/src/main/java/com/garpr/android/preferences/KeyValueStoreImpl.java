package com.garpr.android.preferences;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class KeyValueStoreImpl implements KeyValueStore {

    private final Application mApplication;
    private final String mName;


    public KeyValueStoreImpl(@NonNull final Application application, @NonNull final String name) {
        mApplication = application;
        mName = name;
    }

    @Override
    public void clear() {
        getSharedPreferences()
                .edit()
                .clear()
                .apply();
    }

    @Override
    public boolean contains(@NonNull final String key) {
        return getSharedPreferences().contains(key);
    }

    @Override
    public boolean getBoolean(@NonNull final String key, final boolean fallbackValue) {
        return getSharedPreferences().getBoolean(key, fallbackValue);
    }

    @Override
    public float getFloat(@NonNull final String key, final float fallbackValue) {
        return getSharedPreferences().getFloat(key, fallbackValue);
    }

    @Override
    public int getInteger(@NonNull final String key, final int fallbackValue) {
        return getSharedPreferences().getInt(key, fallbackValue);
    }

    @Override
    public long getLong(@NonNull final String key, final long fallbackValue) {
        return getSharedPreferences().getLong(key, fallbackValue);
    }

    @Override
    public String getString(@NonNull final String key, @Nullable final String fallbackValue) {
        return getSharedPreferences().getString(key, fallbackValue);
    }

    private SharedPreferences getSharedPreferences() {
        return mApplication.getSharedPreferences(mName, Context.MODE_PRIVATE);
    }

    @Override
    public void remove(@NonNull final String key) {
        getSharedPreferences()
                .edit()
                .remove(key)
                .apply();
    }

    @Override
    public void setBoolean(@NonNull final String key, final boolean value) {
        getSharedPreferences()
                .edit()
                .putBoolean(key, value)
                .apply();
    }

    @Override
    public void setFloat(@NonNull final String key, final float value) {
        getSharedPreferences()
                .edit()
                .putFloat(key, value)
                .apply();
    }

    @Override
    public void setInteger(@NonNull final String key, final int value) {
        getSharedPreferences()
                .edit()
                .putInt(key, value)
                .apply();
    }

    @Override
    public void setLong(@NonNull final String key, final long value) {
        getSharedPreferences()
                .edit()
                .putLong(key, value)
                .apply();
    }

    @Override
    public void setString(@NonNull final String key, @NonNull final String value) {
        getSharedPreferences()
                .edit()
                .putString(key, value)
                .apply();
    }

}
