package com.garpr.android.preferences.persistent

import com.garpr.android.preferences.BasePreference
import com.garpr.android.preferences.KeyValueStore

abstract class BasePersistentPreference<T>(
        key: String,
        defaultValue: T?,
        protected val keyValueStore: KeyValueStore
) : BasePreference<T>(
        key,
        defaultValue
) {

    override fun delete(notifyListeners: Boolean) {
        keyValueStore.remove(key)

        if (notifyListeners) {
            notifyListeners()
        }
    }

    override fun exists(): Boolean {
        return hasValueInStore() || defaultValue != null
    }

    protected fun hasValueInStore(): Boolean {
        return key in keyValueStore
    }

}
