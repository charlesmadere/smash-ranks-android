package com.garpr.android.preferences;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface Preference<T> {

    void addListener(@NonNull final OnPreferenceChangeListener<T> listener);

    void delete();

    void delete(final boolean notifyListeners);

    boolean exists();

    @Nullable
    T get();

    @Nullable
    T getDefaultValue();

    @NonNull
    String getKey();

    void removeListener(@NonNull final OnPreferenceChangeListener<T> listener);

    void set(@Nullable final T newValue);

    void set(@Nullable final T newValue, final boolean notifyListeners);

    void set(@NonNull final Preference<T> preference);

    void set(@NonNull final Preference<T> preference, final boolean notifyListeners);


    interface OnPreferenceChangeListener<T> {
        void onPreferenceChange(final Preference<T> preference);
    }

}
