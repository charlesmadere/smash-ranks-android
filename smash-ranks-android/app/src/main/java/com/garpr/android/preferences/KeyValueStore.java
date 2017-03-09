package com.garpr.android.preferences;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Map;

public interface KeyValueStore {

    void clear();

    boolean contains(@NonNull final String key);

    @Nullable
    Map<String, ?> getAll();

    boolean getBoolean(@NonNull final String key, final boolean fallbackValue);

    float getFloat(@NonNull final String key, final float fallbackValue);

    int getInteger(@NonNull final String key, final int fallbackValue);

    long getLong(@NonNull final String key, final long fallbackValue);

    String getString(@NonNull final String key, @Nullable final String fallbackValue);

    void remove(@NonNull final String key);

    void setBoolean(@NonNull final String key, final boolean value);

    void setFloat(@NonNull final String key, final float value);

    void setInteger(@NonNull final String key, final int value);

    void setLong(@NonNull final String key, final long value);

    void setString(@NonNull final String key, @NonNull final String value);

}
