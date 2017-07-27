package com.garpr.android.preferences

interface Preference<T> {

    interface OnPreferenceChangeListener<T> {
        fun onPreferenceChange(preference: Preference<T>)
    }

    fun addListener(listener: OnPreferenceChangeListener<T>)

    val defaultValue: T?

    fun delete()

    fun delete(notifyListeners: Boolean)

    fun exists(): Boolean

    fun get(): T?

    val key: String

    fun removeListener(listener: OnPreferenceChangeListener<T>)

    fun set(newValue: T?)

    operator fun set(newValue: T?, notifyListeners: Boolean)

    fun set(preference: Preference<T>)

    operator fun set(preference: Preference<T>, notifyListeners: Boolean)

}
