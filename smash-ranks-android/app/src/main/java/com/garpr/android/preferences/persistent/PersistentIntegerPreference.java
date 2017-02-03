package com.garpr.android.preferences.persistent;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.garpr.android.preferences.KeyValueStore;

public class PersistentIntegerPreference extends BasePersistentPreference<Integer> {

    public PersistentIntegerPreference(@NonNull final String key,
            @Nullable final Integer defaultValue, @NonNull final KeyValueStore keyValueStore) {
        super(key, defaultValue, keyValueStore);
    }

    @Nullable
    @Override
    public Integer get() {
        if (hasValueInStore()) {
            // at this point, returning the fallback value is impossible
            return getKeyValueStore().getInteger(getKey(), 0);
        } else {
            return getDefaultValue();
        }
    }

    @Override
    protected void performSet(@NonNull final Integer newValue, final boolean notifyListeners) {
        getKeyValueStore().setInteger(getKey(), newValue);

        if (notifyListeners) {
            notifyListeners();
        }
    }

}
