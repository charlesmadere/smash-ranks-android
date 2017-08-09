package com.garpr.android.preferences.persistent

import com.garpr.android.preferences.KeyValueStore
import com.google.gson.Gson
import java.lang.reflect.Type

class PersistentGsonPreference<T>(
        key: String,
        defaultValue: T?,
        keyValueStore: KeyValueStore,
        private val type: Type,
        private val gson: Gson
) : BasePersistentPreference<T>(
        key,
        defaultValue,
        keyValueStore
) {

    private val backingPreference = PersistentStringPreference(key, null, keyValueStore)


    override val exists: Boolean
        get() = backingPreference.exists || defaultValue != null

    override fun get(): T? {
        if (backingPreference.exists) {
            return gson.fromJson<T>(backingPreference.get(), type)
        } else {
            return defaultValue
        }
    }

    override fun performSet(newValue: T, notifyListeners: Boolean) {
        backingPreference.set(gson.toJson(newValue, type), notifyListeners)
    }

}
