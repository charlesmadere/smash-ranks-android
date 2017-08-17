package com.garpr.android.preferences.persistent

import com.garpr.android.preferences.KeyValueStore

class PersistentStringPreference(
        key: String,
        defaultValue: String?,
        keyValueStore: KeyValueStore
) : BasePersistentPreference<String>(
        key,
        defaultValue,
        keyValueStore
) {

    override fun get(): String? {
        if (hasValueInStore) {
            // at this point, returning the fallback value is impossible
            return keyValueStore.getString(key, null)
        } else {
            return defaultValue
        }
    }

    override fun performSet(newValue: String, notifyListeners: Boolean) {
        keyValueStore.setString(key, newValue)

        if (notifyListeners) {
            notifyListeners()
        }
    }

}
