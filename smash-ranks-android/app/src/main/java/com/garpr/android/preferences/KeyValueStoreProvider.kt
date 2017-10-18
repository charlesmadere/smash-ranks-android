package com.garpr.android.preferences

interface KeyValueStoreProvider {

    fun getKeyValueStore(name: String): KeyValueStore

}
