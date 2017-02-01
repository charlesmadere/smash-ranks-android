package com.garpr.android.preferences.persistent;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.garpr.android.preferences.KeyValueStore;

public class PersistentStringPreference extends BasePersistentPreference<String> {

    public PersistentStringPreference(@NonNull final String name, @NonNull final String key,
            @Nullable final String defaultValue, @NonNull final KeyValueStore keyValueStore) {
        super(name, key, defaultValue, keyValueStore);
    }

    @Override
    public boolean exists() {
        return super.exists() || !TextUtils.isEmpty(getDefaultValue());
    }

    @Nullable
    @Override
    public String get() {
        if (hasValueInStore()) {
            // at this point, returning the fallback value is impossible
            return getKeyValueStore().getString(getName(), getKey(), null);
        } else {
            return getDefaultValue();
        }
    }

    @Override
    protected void performSet(@NonNull final String newValue, final boolean notifyListeners) {
        getKeyValueStore().setString(getName(), getKey(), newValue);

        if (notifyListeners) {
            notifyListeners();
        }
    }

}
