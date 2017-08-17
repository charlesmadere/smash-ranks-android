package com.garpr.android.preferences

interface BasePreferenceStore {

    fun clear()

    val keyValueStore: KeyValueStore

}
