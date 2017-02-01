package com.garpr.android.preferences.persistent;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.garpr.android.preferences.KeyValueStore;

public class PersistentBooleanPreference extends BasePersistentPreference<Boolean> {

    public PersistentBooleanPreference(@NonNull final String name, @NonNull final String key,
            @Nullable final Boolean defaultValue, @NonNull final KeyValueStore keyValueStore) {
        super(name, key, defaultValue, keyValueStore);
    }

    @Nullable
    @Override
    public Boolean get() {
        if (hasValueInStore()) {
            // at this point, returning the fallback value is impossible
            return getKeyValueStore().getBoolean(getName(), getKey(), false);
        } else {
            return getDefaultValue();
        }
    }

    @Override
    protected void performSet(@NonNull final Boolean newValue, final boolean notifyListeners) {
        getKeyValueStore().setBoolean(getName(), getKey(), newValue);

        if (notifyListeners) {
            notifyListeners();
        }
    }

}
