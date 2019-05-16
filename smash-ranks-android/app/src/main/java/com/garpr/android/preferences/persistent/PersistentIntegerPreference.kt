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
        return if (hasValueInStore) {
            // at this point, returning the fallback value is impossible
            keyValueStore.getInteger(key, 0)
        } else {
            defaultValue
        }
    }

    override fun performSet(newValue: Int) {
        keyValueStore.setInteger(key, newValue)
        notifyListeners()
    }

}
