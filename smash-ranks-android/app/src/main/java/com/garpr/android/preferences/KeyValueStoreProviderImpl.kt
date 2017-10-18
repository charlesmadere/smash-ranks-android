package com.garpr.android.preferences

import android.app.Application

class KeyValueStoreProviderImpl(
        private val application: Application
) : KeyValueStoreProvider {

    override fun getKeyValueStore(name: String) = KeyValueStoreImpl(application, name)

}
