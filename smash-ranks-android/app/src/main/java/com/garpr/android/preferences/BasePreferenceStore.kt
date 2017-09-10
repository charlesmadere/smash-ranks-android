package com.garpr.android.preferences

abstract class BasePreferenceStore(
        override val keyValueStore: KeyValueStore
) : PreferenceStore {

    override fun clear() {
        keyValueStore.clear()
    }

}
