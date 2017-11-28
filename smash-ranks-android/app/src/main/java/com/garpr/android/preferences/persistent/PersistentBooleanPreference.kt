package com.garpr.android.preferences.persistent

import com.garpr.android.preferences.KeyValueStore

class PersistentBooleanPreference(
        key: String,
        defaultValue: Boolean?,
        keyValueStore: KeyValueStore
) : BasePersistentPreference<Boolean>(
        key,
        defaultValue,
        keyValueStore
) {

    override fun get(): Boolean? {
        return if (hasValueInStore) {
            // at this point, returning the fallback value is impossible
            keyValueStore.getBoolean(key, false)
        } else {
            defaultValue
        }
    }

    override fun performSet(newValue: Boolean, notifyListeners: Boolean) {
        keyValueStore.setBoolean(key, newValue)

        if (notifyListeners) {
            notifyListeners()
        }
    }

}
