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
        return if (hasValueInStore) {
            // at this point, returning the fallback value is impossible
            keyValueStore.getString(key, null)
        } else {
            defaultValue
        }
    }

    override fun performSet(newValue: String) {
        keyValueStore.setString(key, newValue)
        notifyListeners()
    }

}
