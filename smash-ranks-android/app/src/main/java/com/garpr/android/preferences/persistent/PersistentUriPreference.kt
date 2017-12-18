package com.garpr.android.preferences.persistent

import android.net.Uri
import com.garpr.android.preferences.KeyValueStore

class PersistentUriPreference(
        key: String,
        defaultValue: Uri?,
        keyValueStore: KeyValueStore
) : BasePersistentPreference<Uri>(
        key,
        defaultValue,
        keyValueStore
) {

    private val backingPreference = PersistentStringPreference(key, null, keyValueStore)


    override val exists: Boolean
        get() = backingPreference.exists || defaultValue != null

    override fun get(): Uri? {
        return if (backingPreference.exists) {
            Uri.parse(backingPreference.get())
        } else {
            defaultValue
        }
    }

    override fun performSet(newValue: Uri, notifyListeners: Boolean) {
        backingPreference.set(newValue.toString(), notifyListeners)
    }

}
