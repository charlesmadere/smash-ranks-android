package com.garpr.android.preferences;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class KeyValueStoreImpl implements KeyValueStore {

    private final Application mApplication;


    public KeyValueStoreImpl(@NonNull final Application application) {
        mApplication = application;
    }

    @Override
    public void clear(@NonNull final String name) {
        getSharedPreferences(name)
                .edit()
                .clear()
                .apply();
    }

    @Override
    public boolean contains(@NonNull final String name, @NonNull final String key) {
        return getSharedPreferences(name).contains(key);
    }

    @Override
    public boolean getBoolean(@NonNull final String name, @NonNull final String key,
            final boolean fallbackValue) {
        return getSharedPreferences(name).getBoolean(key, fallbackValue);
    }

    @Override
    public float getFloat(@NonNull final String name, @NonNull final String key,
            final float fallbackValue) {
        return getSharedPreferences(name).getFloat(key, fallbackValue);
    }

    @Override
    public int getInteger(@NonNull final String name, @NonNull final String key,
            final int fallbackValue) {
        return getSharedPreferences(name).getInt(key, fallbackValue);
    }

    @Override
    public long getLong(@NonNull final String name, @NonNull final String key,
            final long fallbackValue) {
        return getSharedPreferences(name).getLong(key, fallbackValue);
    }

    @Override
    public String getString(@NonNull final String name, @NonNull final String key,
            @Nullable final String fallbackValue) {
        return getSharedPreferences(name).getString(key, fallbackValue);
    }

    private SharedPreferences getSharedPreferences(@NonNull final String name) {
        return mApplication.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    @Override
    public void remove(@NonNull final String name, @NonNull final String key) {
        getSharedPreferences(name)
                .edit()
                .remove(key)
                .apply();
    }

    @Override
    public void setBoolean(@NonNull final String name, @NonNull final String key,
            final boolean value) {
        getSharedPreferences(name)
                .edit()
                .putBoolean(key, value)
                .apply();
    }

    @Override
    public void setFloat(@NonNull final String name, @NonNull final String key, final float value) {
        getSharedPreferences(name)
                .edit()
                .putFloat(key, value)
                .apply();
    }

    @Override
    public void setInteger(@NonNull final String name, @NonNull final String key, final int value) {
        getSharedPreferences(name)
                .edit()
                .putInt(key, value)
                .apply();
    }

    @Override
    public void setLong(@NonNull final String name, @NonNull final String key, final long value) {
        getSharedPreferences(name)
                .edit()
                .putLong(key, value)
                .apply();
    }

    @Override
    public void setString(@NonNull final String name, @NonNull final String key,
            @NonNull final String value) {
        getSharedPreferences(name)
                .edit()
                .putString(key, value)
                .apply();
    }

}
