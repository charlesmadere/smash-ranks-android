package com.garpr.android.preferences.persistent;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.garpr.android.preferences.KeyValueStore;

public class PersistentLongPreference extends BasePersistentPreference<Long> {

    public PersistentLongPreference(@NonNull final String name, @NonNull final String key,
            @Nullable final Long defaultValue, @NonNull final KeyValueStore keyValueStore) {
        super(name, key, defaultValue, keyValueStore);
    }

    @Nullable
    @Override
    public Long get() {
        if (hasValueInStore()) {
            // at this point, returning the fallback value is impossible
            return getKeyValueStore().getLong(getName(), getKey(), 0L);
        } else {
            return getDefaultValue();
        }
    }

    @Override
    protected void performSet(@NonNull final Long newValue, final boolean notifyListeners) {
        getKeyValueStore().setLong(getName(), getKey(), newValue);

        if (notifyListeners) {
            notifyListeners();
        }
    }

}
