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
        if (hasValueInStore()) {
            // at this point, returning the fallback value is impossible
            return keyValueStore.getFloat(key, 0f)
        } else {
            return defaultValue
        }
    }

    override fun performSet(newValue: Float, notifyListeners: Boolean) {
        keyValueStore.setFloat(key, newValue)

        if (notifyListeners) {
            notifyListeners()
        }
    }

}
