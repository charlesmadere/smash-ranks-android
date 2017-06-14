package com.garpr.android.preferences.persistent;

import android.support.annotation.NonNull;

import com.garpr.android.preferences.KeyValueStore;

public interface BasePreferenceStore {

    void clear();

    @NonNull
    KeyValueStore getKeyValueStore();

}
