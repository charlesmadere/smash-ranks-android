package com.garpr.android.preferences.persistent;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.garpr.android.preferences.KeyValueStore;

public class PersistentLongPreference extends BasePersistentPreference<Long> {

    public PersistentLongPreference(@NonNull final String key, @Nullable final Long defaultValue,
            @NonNull final KeyValueStore keyValueStore) {
        super(key, defaultValue, keyValueStore);
    }

    @Nullable
    @Override
    public Long get() {
        if (hasValueInStore()) {
            // at this point, returning the fallback value is impossible
            return getKeyValueStore().getLong(getKey(), 0L);
        } else {
            return getDefaultValue();
        }
    }

    @Override
    protected void performSet(@NonNull final Long newValue, final boolean notifyListeners) {
        getKeyValueStore().setLong(getKey(), newValue);

        if (notifyListeners) {
            notifyListeners();
        }
    }

}
