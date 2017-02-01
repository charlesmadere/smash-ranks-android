package com.garpr.android.preferences;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public abstract class BasePersistentPreference<T> extends BasePreference<T> {

    private final KeyValueStore mKeyValueStore;


    public BasePersistentPreference(@NonNull final String name, @NonNull final String key,
            @Nullable final T defaultValue, @NonNull final KeyValueStore keyValueStore) {
        super(name, key, defaultValue);
        mKeyValueStore = keyValueStore;
    }

    protected KeyValueStore getKeyValueStore() {
        return mKeyValueStore;
    }

    protected boolean hasValueInStore() {
        return mKeyValueStore.contains(getName(), getKey());
    }

    @Override
    public void remove() {
        remove(true);
    }

    @Override
    public void remove(final boolean notifyListeners) {
        mKeyValueStore.remove(getName(), getKey());

        if (notifyListeners) {
            notifyListeners();
        }
    }

    @Override
    public void set(@Nullable final T newValue) {
        set(newValue, true);
    }

}
