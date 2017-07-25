package com.garpr.android.preferences.persistent

import com.garpr.android.preferences.KeyValueStore

class PersistentIntegerPreference(
        key: String,
        defaultValue: Int?,
        keyValueStore: KeyValueStore
) : BasePersistentPreference<Int>(
        key,
        defaultValue,
        keyValueStore
) {

    override fun get(): Int? {
        if (hasValueInStore()) {
            // at this point, returning the fallback value is impossible
            return keyValueStore.getInteger(key, 0)
        } else {
            return defaultValue
        }
    }

    override fun performSet(newValue: Int, notifyListeners: Boolean) {
        keyValueStore.setInteger(key, newValue)

        if (notifyListeners) {
            notifyListeners()
        }
    }

}
