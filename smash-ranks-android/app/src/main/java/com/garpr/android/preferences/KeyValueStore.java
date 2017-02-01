package com.garpr.android.preferences;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface KeyValueStore {

    void clear(@NonNull final String name);

    boolean contains(@NonNull final String name, @NonNull final String key);

    boolean getBoolean(@NonNull final String name, @NonNull final String key,
            final boolean fallbackValue);

    float getFloat(@NonNull final String name, @NonNull final String key,
            final float fallbackValue);

    int getInteger(@NonNull final String name, @NonNull final String key, final int fallbackValue);

    long getLong(@NonNull final String name, @NonNull final String key, final long fallbackValue);

    String getString(@NonNull final String name, @NonNull final String key,
            @Nullable final String fallbackValue);

    void remove(@NonNull final String name, @NonNull final String key);

    void setBoolean(@NonNull final String name, @NonNull final String key, final boolean value);

    void setFloat(@NonNull final String name, @NonNull final String key, final float value);

    void setInteger(@NonNull final String name, @NonNull final String key, final int value);

    void setLong(@NonNull final String name, @NonNull final String key, final long value);

    void setString(@NonNull final String name, @NonNull final String key,
            @NonNull final String value);

}
