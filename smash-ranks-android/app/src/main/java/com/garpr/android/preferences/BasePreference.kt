package com.garpr.android.preferences

import com.garpr.android.preferences.Preference.OnPreferenceChangeListener
import com.garpr.android.wrappers.WeakReferenceWrapper

abstract class BasePreference<T>(
        override val key: String,
        override val defaultValue: T?
) : Preference<T> {

    private val listeners = mutableSetOf<WeakReferenceWrapper<OnPreferenceChangeListener<T>>>()


    override fun addListener(listener: OnPreferenceChangeListener<T>) {
        cleanListeners()

        synchronized (listeners) {
            listeners.add(WeakReferenceWrapper(listener))
        }
    }

    private fun cleanListeners(listenerToRemove: OnPreferenceChangeListener<T>? = null) {
        synchronized (listeners) {
            val iterator = listeners.iterator()

            while (iterator.hasNext()) {
                val listener = iterator.next().get()

                if (listener == null || listener == listenerToRemove) {
                    iterator.remove()
                }
            }
        }
    }

    protected fun notifyListeners() {
        cleanListeners()

        synchronized (listeners) {
            val iterator = listeners.iterator()

            while (iterator.hasNext()) {
                iterator.next().get()?.onPreferenceChange(this)
            }
        }
    }

    protected abstract fun performSet(newValue: T, notifyListeners: Boolean = true)

    override fun removeListener(listener: OnPreferenceChangeListener<T>) {
        cleanListeners(listener)
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
