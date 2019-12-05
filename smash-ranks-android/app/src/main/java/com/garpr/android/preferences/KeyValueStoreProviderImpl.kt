package com.garpr.android.preferences

import android.content.Context

class KeyValueStoreProviderImpl(
        private val context: Context
) : KeyValueStoreProvider {

    override fun getKeyValueStore(name: String) = KeyValueStoreImpl(context, name)

}
