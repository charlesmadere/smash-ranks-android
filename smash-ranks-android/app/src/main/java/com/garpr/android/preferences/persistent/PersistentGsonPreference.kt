package com.garpr.android.preferences.persistent

import com.garpr.android.preferences.KeyValueStore
import com.google.gson.Gson
import java.lang.reflect.Type

class PersistentGsonPreference<T>(
        key: String,
        defaultValue: T?,
        keyValueStore: KeyValueStore,
        private val mType: Type,
        private val mGson: Gson
) : BasePersistentPreference<T>(
        key,
        defaultValue,
        keyValueStore
) {

    private val mBackingPreference = PersistentStringPreference(key, null, keyValueStore)


    override fun exists(): Boolean {
        return mBackingPreference.exists() || defaultValue != null
    }

    override fun get(): T? {
        if (mBackingPreference.exists()) {
            return mGson.fromJson<T>(mBackingPreference.get(), mType)
        } else {
            return defaultValue
        }
    }

    override fun performSet(newValue: T, notifyListeners: Boolean) {
        mBackingPreference[mGson.toJson(newValue, mType)] = notifyListeners
    }

}
