package com.garpr.android.preferences

interface PreferenceStore {

    fun clear()

    val keyValueStore: KeyValueStore

}
