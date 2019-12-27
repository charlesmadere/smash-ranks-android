package com.garpr.android.preferences.persistent

import com.garpr.android.preferences.KeyValueStore
import com.squareup.moshi.JsonAdapter

class PersistentMoshiPreference<T : Any>(
        key: String,
        defaultValue: T?,
        keyValueStore: KeyValueStore,
        private val jsonAdapter: JsonAdapter<T>
) : BasePersistentPreference<T>(
        key,
        defaultValue,
        keyValueStore
) {

    private val backingPreference = PersistentStringPreference(
            key = key,
            defaultValue = null,
            keyValueStore = keyValueStore
    )

    override val exists: Boolean
        get() = backingPreference.exists || defaultValue != null

    override fun get(): T? {
        val json = if (backingPreference.exists) backingPreference.get() else null

        return if (json == null) {
            defaultValue
        } else {
            jsonAdapter.fromJson(json)
        }
    }

    override fun performSet(newValue: T) {
        backingPreference.set(jsonAdapter.toJson(newValue))
    }

}
