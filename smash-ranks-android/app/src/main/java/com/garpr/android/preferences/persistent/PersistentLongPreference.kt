package com.garpr.android.preferences.persistent

import com.garpr.android.preferences.KeyValueStore

class PersistentLongPreference(
        key: String,
        defaultValue: Long?,
        keyValueStore: KeyValueStore
) : BasePersistentPreference<Long>(
        key,
        defaultValue,
        keyValueStore
) {

    override fun get(): Long? {
        if (hasValueInStore()) {
            // at this point, returning the fallback value is impossible
            return keyValueStore.getLong(key, 0L)
        } else {
            return defaultValue
        }
    }

    override fun performSet(newValue: Long, notifyListeners: Boolean) {
        keyValueStore.setLong(key, newValue)

        if (notifyListeners) {
            notifyListeners()
        }
    }

}
