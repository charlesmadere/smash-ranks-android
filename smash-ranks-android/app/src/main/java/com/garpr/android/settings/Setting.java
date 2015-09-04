package com.garpr.android.settings;


import android.content.SharedPreferences;

import com.garpr.android.misc.Utils;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.LinkedList;


public abstract class Setting<T> {


    protected final T mDefaultValue;
    protected final String mKey;

    private final LinkedList<WeakReference<OnSettingChangedListener<T>>> mListeners;
    private final String mName;




    protected Setting(final String name, final String key) {
        this(name, key, null);
    }


    protected Setting(final String name, final String key, final T defaultValue) {
        if (!Utils.validStrings(name, key)) {
            throw new IllegalArgumentException("name and key can't be null / empty / whitespace");
        }

        mListeners = new LinkedList<>();
        mName = name;

        mDefaultValue = defaultValue;
        mKey = key;
    }


    public final void attachListener(final OnSettingChangedListener<T> listener) {
        if (listener == null) {
            throw new IllegalArgumentException("listener can't be null");
        }

        synchronized (mListeners) {
            boolean addListener = true;
            final Iterator<WeakReference<OnSettingChangedListener<T>>> iterator =
                    mListeners.iterator();

            while (iterator.hasNext()) {
                final WeakReference<OnSettingChangedListener<T>> wr = iterator.next();
                final OnSettingChangedListener<T> oscl = wr.get();

                if (oscl == null) {
                    iterator.remove();
                } else if (oscl == listener) {
                    addListener = false;
                }
            }

            if (addListener) {
                mListeners.add(new WeakReference<>(listener));
            }
        }
    }


    public void delete() {
        writeSharedPreferences().remove(mKey).apply();
    }


    public final void detachListener(final OnSettingChangedListener<T> listener) {
        synchronized (mListeners) {
            final Iterator<WeakReference<OnSettingChangedListener<T>>> iterator =
                    mListeners.iterator();

            while (iterator.hasNext()) {
                final WeakReference<OnSettingChangedListener<T>> wr = iterator.next();
                final OnSettingChangedListener<T> oscl = wr.get();

                if (oscl == null || oscl == listener) {
                    iterator.remove();
                }
            }
        }
    }


    public boolean exists() {
        return readSharedPreferences().contains(mKey);
    }


    public abstract T get();


    protected final SharedPreferences readSharedPreferences() {
        return Settings.get(mName);
    }


    public final void set(final T newValue) {
        set(newValue, true);
    }


    public void set(final T newValue, final boolean notifyListeners) {
        if (notifyListeners) {
            synchronized (mListeners) {
                final Iterator<WeakReference<OnSettingChangedListener<T>>> iterator =
                        mListeners.iterator();

                while (iterator.hasNext()) {
                    final WeakReference<OnSettingChangedListener<T>> wr = iterator.next();
                    final OnSettingChangedListener<T> oscl = wr.get();

                    if (oscl == null) {
                        iterator.remove();
                    } else {
                        oscl.onSettingChanged(newValue);
                    }
                }
            }
        }
    }


    public final void set(final Setting<T> setting) {
        set(setting.get());
    }


    @Override
    public final String toString() {
        final StringBuilder string = new StringBuilder()
                .append(mKey)
                .append("=(");

        if (exists()) {
            string.append("doesn't exist");
        } else {
            final T setting = get();

            if (setting == null) {
                string.append("null");
            } else {
                string.append(setting.toString());
            }
        }

        string.append(") listeners=(");

        synchronized (mListeners) {
            string.append(mListeners.size());
        }

        string.append(')');
        return string.toString();
    }


    protected final SharedPreferences.Editor writeSharedPreferences() {
        return Settings.edit(mName);
    }




    public interface OnSettingChangedListener<T> {


        void onSettingChanged(final T setting);


    }


}
