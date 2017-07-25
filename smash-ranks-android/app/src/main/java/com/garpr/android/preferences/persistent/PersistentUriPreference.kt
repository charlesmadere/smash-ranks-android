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

    private val mBackingPreference = PersistentStringPreference(key, null, keyValueStore)


    override fun exists(): Boolean {
        return mBackingPreference.exists() || defaultValue != null
    }

    override fun get(): Uri? {
        val string = mBackingPreference.get()

        if (string == null) {
            return defaultValue
        } else {
            return Uri.parse(string)
        }
    }

    override fun performSet(newValue: Uri, notifyListeners: Boolean) {
        mBackingPreference[newValue.toString()] = notifyListeners
    }

}
