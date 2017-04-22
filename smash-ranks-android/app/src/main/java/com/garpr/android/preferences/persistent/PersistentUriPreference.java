package com.garpr.android.preferences.persistent;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.garpr.android.preferences.KeyValueStore;
import com.garpr.android.preferences.Preference;

public class PersistentUriPreference extends BasePersistentPreference<Uri> {

    private final Preference<String> mBackingPreference;


    public PersistentUriPreference(@NonNull final String key, @Nullable final Uri defaultValue,
            @NonNull final KeyValueStore keyValueStore) {
        super(key, defaultValue, keyValueStore);
        mBackingPreference = new PersistentStringPreference(key, null, keyValueStore);
    }

    @Override
    public boolean exists() {
        return mBackingPreference.exists() || getDefaultValue() != null;
    }

    @Nullable
    @Override
    public Uri get() {
        final String string = mBackingPreference.get();

        if (string == null) {
            return getDefaultValue();
        } else {
            return Uri.parse(string);
        }
    }

    @Override
    protected void performSet(@NonNull final Uri newValue, final boolean notifyListeners) {
        mBackingPreference.set(newValue.toString(), notifyListeners);
    }

}
