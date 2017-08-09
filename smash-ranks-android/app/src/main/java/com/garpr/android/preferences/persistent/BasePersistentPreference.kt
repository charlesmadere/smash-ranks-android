package com.garpr.android.preferences.persistent

import com.garpr.android.preferences.BasePreference
import com.garpr.android.preferences.KeyValueStore

abstract class BasePersistentPreference<T>(
        key: String,
        defaultValue: T?,
        val keyValueStore: KeyValueStore
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

    override val exists: Boolean
        get() = hasValueInStore || defaultValue != null

    protected val hasValueInStore: Boolean
        get() = key in keyValueStore

}
