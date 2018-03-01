package com.garpr.android.preferences

interface PreferenceStore {

    fun clear() {
        keyValueStore.clear()
    }

    val keyValueStore: KeyValueStore

}
