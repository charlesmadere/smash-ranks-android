package com.garpr.android.preferences

interface PreferenceStore {

    val keyValueStore: KeyValueStore

    fun clear() {
        keyValueStore.clear()
    }

}
