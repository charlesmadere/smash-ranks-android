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
        return if (hasValueInStore) {
            // at this point, returning the fallback value is impossible
            keyValueStore.getLong(key, 0L)
        } else {
            defaultValue
        }
    }

    override fun performSet(newValue: Long) {
        keyValueStore.setLong(key, newValue)
        notifyListeners()
    }

}
