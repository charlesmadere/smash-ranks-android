package com.garpr.android.preferences

import com.garpr.android.preferences.Preference.OnPreferenceChangeListener
import java.lang.ref.WeakReference

abstract class BasePreference<T>(
        override val key: String,
        override val defaultValue: T?
) : Preference<T> {

    private val mListeners: MutableList<WeakReference<OnPreferenceChangeListener<T>>> = mutableListOf()


    override fun addListener(listener: OnPreferenceChangeListener<T>) {
        synchronized (mListeners) {
            var addListener = true
            val iterator = mListeners.iterator()

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
                mListeners.add(WeakReference<OnPreferenceChangeListener<T>>(listener))
            }
        }
    }

    override fun delete() {
        delete(true)
    }

    protected fun notifyListeners() {
        synchronized(mListeners) {
            val iterator = mListeners.iterator()

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

    protected abstract fun performSet(newValue: T, notifyListeners: Boolean)

    override fun removeListener(listener: OnPreferenceChangeListener<T>) {
        synchronized (mListeners) {
            val iterator = mListeners.iterator()

            while (iterator.hasNext()) {
                val next = iterator.next()
                val item = next.get()

                if (item == null || item === listener) {
                    iterator.remove()
                }
            }
        }
    }

    override fun set(newValue: T?) {
        set(newValue, true)
    }

    override fun set(preference: Preference<T>) {
        set(preference.get())
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
