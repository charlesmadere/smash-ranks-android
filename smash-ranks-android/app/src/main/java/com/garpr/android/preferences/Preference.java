package com.garpr.android.preferences;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface Preference<T> {

    void addListener(@NonNull final OnPreferenceChangeListener<T> listener);

    boolean exists();

    @Nullable
    T get();

    @Nullable
    T getDefaultValue();

    @NonNull
    String getKey();

    @NonNull
    String getName();

    void remove();

    void remove(final boolean notifyListeners);

    void removeListener(@NonNull final OnPreferenceChangeListener<T> listener);

    void set(@Nullable final T newValue);

    void set(@Nullable final T newValue, final boolean notifyListeners);

    void set(@NonNull final Preference<T> preference);

    void set(@NonNull final Preference<T> preference, final boolean notifyListeners);


    interface OnPreferenceChangeListener<T> {
        void onPreferenceChange(final Preference<T> preference);
    }

}
