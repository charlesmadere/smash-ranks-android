package com.garpr.android.preferences.persistent;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.garpr.android.preferences.KeyValueStore;
import com.garpr.android.preferences.Preference;
import com.google.gson.Gson;

import java.lang.reflect.Type;

public class PersistentGsonPreference<T> extends BasePersistentPreference<T> {

    private final Gson mGson;
    private final Preference<String> mBackingPreference;
    private final Type mType;


    public PersistentGsonPreference(@NonNull final String name, @NonNull final String key,
            @Nullable final T defaultValue, final Type type,
            @NonNull final KeyValueStore keyValueStore, @NonNull final Gson gson) {
        super(name, key, defaultValue, keyValueStore);
        mGson = gson;
        mBackingPreference = new PersistentStringPreference(name, key, null, keyValueStore);
        mType = type;
    }

    @Override
    public boolean exists() {
        return mBackingPreference.exists() || getDefaultValue() != null;
    }

    @Nullable
    @Override
    public T get() {
        if (exists()) {
            return mGson.fromJson(mBackingPreference.get(), mType);
        } else {
            return getDefaultValue();
        }
    }

    @Override
    protected void performSet(@NonNull final T newValue, final boolean notifyListeners) {
        mBackingPreference.set(mGson.toJson(newValue, mType), notifyListeners);
    }

}
