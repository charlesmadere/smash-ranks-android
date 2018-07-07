package com.garpr.android.preferences

interface Preference<T> {

    interface OnPreferenceChangeListener<T> {
        fun onPreferenceChange(preference: Preference<T>)
    }

    fun addListener(listener: OnPreferenceChangeListener<T>)

    val defaultValue: T?

    fun delete()

    val exists: Boolean

    fun get(): T?

    val key: String

    fun removeListener(listener: OnPreferenceChangeListener<T>)

    fun set(newValue: T?)

    fun set(preference: Preference<T>)

}
