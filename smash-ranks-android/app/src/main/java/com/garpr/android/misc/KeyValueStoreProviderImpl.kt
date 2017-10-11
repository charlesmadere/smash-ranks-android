package com.garpr.android.misc

import android.app.Application
import com.garpr.android.preferences.KeyValueStoreImpl

class KeyValueStoreProviderImpl(
        private val application: Application
) : KeyValueStoreProvider {

    override fun getKeyValueStore(name: String) = KeyValueStoreImpl(application, name)

}
