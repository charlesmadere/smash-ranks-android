package com.garpr.android.preferences.persistent

import com.garpr.android.extensions.toJavaUri
import com.garpr.android.preferences.KeyValueStore
import java.net.URI as JavaUri

class PersistentUriPreference(
        key: String,
        defaultValue: JavaUri?,
        keyValueStore: KeyValueStore
) : BasePersistentPreference<JavaUri>(
        key,
        defaultValue,
        keyValueStore
) {

    private val backingPreference = PersistentStringPreference(
            key = key,
            defaultValue = null,
            keyValueStore = keyValueStore
    )

    override val exists: Boolean
        get() = backingPreference.exists || defaultValue != null

    override fun get(): JavaUri? {
        return if (backingPreference.exists) {
            backingPreference.get().toJavaUri()
        } else {
            defaultValue
        }
    }

    override fun performSet(newValue: JavaUri) {
        backingPreference.set(newValue.toString())
    }

}
