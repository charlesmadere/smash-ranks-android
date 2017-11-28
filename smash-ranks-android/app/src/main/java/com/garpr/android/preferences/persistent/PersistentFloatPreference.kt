package com.garpr.android.preferences.persistent

import com.garpr.android.preferences.KeyValueStore

class PersistentFloatPreference(
        key: String,
        defaultValue: Float?,
        keyValueStore: KeyValueStore
) : BasePersistentPreference<Float>(
        key,
        defaultValue,
        keyValueStore
) {

    override fun get(): Float? {
        return if (hasValueInStore) {
            // at this point, returning the fallback value is impossible
            keyValueStore.getFloat(key, 0f)
        } else {
            defaultValue
        }
    }

    override fun performSet(newValue: Float, notifyListeners: Boolean) {
        keyValueStore.setFloat(key, newValue)

        if (notifyListeners) {
            notifyListeners()
        }
    }

}
