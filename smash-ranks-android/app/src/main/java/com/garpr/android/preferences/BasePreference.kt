package com.garpr.android.preferences

import com.garpr.android.preferences.Preference.OnPreferenceChangeListener
import java.lang.ref.WeakReference

abstract class BasePreference<T>(
        override val key: String,
        override val defaultValue: T?
) : Preference<T> {

    private val listeners: MutableList<WeakReference<OnPreferenceChangeListener<T>>> = mutableListOf()


    override fun addListener(listener: OnPreferenceChangeListener<T>) {
        synchronized (listeners) {
            var addListener = true
            val iterator = listeners.iterator()

            while (iterator.hasNext()) {
                val reference = iterator.next()
                val item = reference.get()

                if (item == null) {
                    iterator.remove()
                } else if (item === listener) {
                    addListener = false
                }
            }

            if (addListener) {
                listeners.add(WeakReference<OnPreferenceChangeListener<T>>(listener))
            }
        }
    }

    protected fun notifyListeners() {
        synchronized(listeners) {
            val iterator = listeners.iterator()

            while (iterator.hasNext()) {
                val reference = iterator.next()
                val item = reference.get()

                if (item == null) {
                    iterator.remove()
                } else {
                    item.onPreferenceChange(this)
                }
            }
        }
    }

    protected abstract fun performSet(newValue: T, notifyListeners: Boolean = true)

    override fun removeListener(listener: OnPreferenceChangeListener<T>) {
        synchronized (listeners) {
            val iterator = listeners.iterator()

            while (iterator.hasNext()) {
                val next = iterator.next()
                val item = next.get()

                if (item == null || item === listener) {
                    iterator.remove()
                }
            }
        }
    }

    override fun set(preference: Preference<T>, notifyListeners: Boolean) {
        set(preference.get(), notifyListeners)
    }

    override fun set(newValue: T?, notifyListeners: Boolean) {
        if (newValue == null) {
            delete(notifyListeners)
        } else {
            performSet(newValue, notifyListeners)
        }
    }

}
