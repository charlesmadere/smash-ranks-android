package com.garpr.android.preferences;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.LinkedList;

public abstract class BasePreference<T> implements Preference<T> {

    private final LinkedList<WeakReference<OnPreferenceChangeListener<T>>> mListeners;
    private final String mKey;
    private final String mName;
    private final T mDefaultValue;


    public BasePreference(@NonNull final String name, @NonNull final String key,
            @Nullable final T defaultValue) {
        mListeners = new LinkedList<>();
        mKey = key;
        mName = name;
        mDefaultValue = defaultValue;
    }

    @Override
    public void addListener(@NonNull final OnPreferenceChangeListener<T> listener) {
        synchronized (mListeners) {
            boolean addListener = true;
            final Iterator<WeakReference<OnPreferenceChangeListener<T>>> iterator =
                    mListeners.iterator();

            while (iterator.hasNext()) {
                final WeakReference<OnPreferenceChangeListener<T>> reference = iterator.next();
                final OnPreferenceChangeListener<T> item = reference.get();

                if (item == null) {
                    iterator.remove();
                } else if (item == listener) {
                    addListener = false;
                }
            }

            if (addListener) {
                mListeners.add(new WeakReference<>(listener));
            }
        }
    }

    @Nullable
    @Override
    public T getDefaultValue() {
        return mDefaultValue;
    }

    @NonNull
    @Override
    public String getKey() {
        return mKey;
    }

    @NonNull
    @Override
    public String getName() {
        return mName;
    }

    protected void notifyListeners() {
        synchronized (mListeners) {
            final Iterator<WeakReference<OnPreferenceChangeListener<T>>> iterator =
                    mListeners.iterator();

            while (iterator.hasNext()) {
                final WeakReference<OnPreferenceChangeListener<T>> reference = iterator.next();
                final OnPreferenceChangeListener<T> item = reference.get();

                if (item == null) {
                    iterator.remove();
                } else {
                    item.onPreferenceChange(this);
                }
            }
        }
    }

    protected abstract void performSet(@NonNull final T newValue, final boolean notifyListeners);

    @Override
    public void removeListener(@NonNull final OnPreferenceChangeListener<T> listener) {
        synchronized (mListeners) {
            final Iterator<WeakReference<OnPreferenceChangeListener<T>>> iterator =
                    mListeners.iterator();

            while (iterator.hasNext()) {
                final WeakReference<OnPreferenceChangeListener<T>> next = iterator.next();
                final OnPreferenceChangeListener<T> item = next.get();

                if (item == null || item == listener) {
                    iterator.remove();
                }
            }
        }
    }

    @Override
    public void set(@Nullable final T newValue) {
        set(newValue, true);
    }

    @Override
    public void set(@NonNull final Preference<T> preference) {
        set(preference.get());
    }

    @Override
    public void set(@NonNull final Preference<T> preference, final boolean notifyListeners) {
        set(preference.get(), notifyListeners);
    }

    @Override
    public void set(@Nullable final T newValue, final boolean notifyListeners) {
        if (newValue == null) {
            remove(notifyListeners);
        } else {
            performSet(newValue, notifyListeners);
        }
    }

}
