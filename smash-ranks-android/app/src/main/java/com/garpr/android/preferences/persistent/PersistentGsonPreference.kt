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
        return if (backingPreference.exists) {
            gson.fromJson<T>(backingPreference.get(), type)
        } else {
            defaultValue
        }
    }

    override fun performSet(newValue: T) {
        backingPreference.set(gson.toJson(newValue, type))
    }

}
