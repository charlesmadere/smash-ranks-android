package com.garpr.android.misc

import com.garpr.android.preferences.KeyValueStore

interface KeyValueStoreProvider {

    fun getKeyValueStore(name: String): KeyValueStore

}
