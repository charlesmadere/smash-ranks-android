package com.garpr.android.preferences;

import android.support.annotation.NonNull;

public interface BasePreferenceStore {

    void clear();

    @NonNull
    KeyValueStore getKeyValueStore();

}
