package com.garpr.android.preferences.persistent

import com.garpr.android.preferences.KeyValueStore
import com.squareup.moshi.Moshi
import java.lang.reflect.Type

class PersistentMoshiPreference<T>(
        key: String,
        defaultValue: T?,
        keyValueStore: KeyValueStore,
        moshi: Moshi,
        type: Type
) : BasePersistentPreference<T>(
        key,
        defaultValue,
        keyValueStore
) {

    private val typeAdapter = moshi.adapter<T>(type)
    private val backingPreference = PersistentStringPreference(key, null, keyValueStore)

    override val exists: Boolean
        get() = backingPreference.exists || defaultValue != null

    override fun get(): T? {
        val json = if (backingPreference.exists) backingPreference.get() else null

        return if (json == null) {
            defaultValue
        } else {
            typeAdapter.fromJson(json)
        }
    }

    override fun performSet(newValue: T) {
        backingPreference.set(typeAdapter.toJson(newValue))
    }

}
