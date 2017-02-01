package com.garpr.android.preferences.persistent;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.garpr.android.preferences.KeyValueStore;

public class PersistentFloatPreference extends BasePersistentPreference<Float> {

    public PersistentFloatPreference(@NonNull final String name, @NonNull final String key,
            @Nullable final Float defaultValue, @NonNull final KeyValueStore keyValueStore) {
        super(name, key, defaultValue, keyValueStore);
    }

    @Nullable
    @Override
    public Float get() {
        if (hasValueInStore()) {
            // at this point, returning the fallback value is impossible
            return getKeyValueStore().getFloat(getName(), getKey(), 0f);
        } else {
            return getDefaultValue();
        }
    }

    @Override
    protected void performSet(@NonNull final Float newValue, final boolean notifyListeners) {
        getKeyValueStore().setFloat(getName(), getKey(), newValue);

        if (notifyListeners) {
            notifyListeners();
        }
    }

}
