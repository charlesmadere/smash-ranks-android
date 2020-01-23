package com.garpr.android.preferences

class TestKeyValueStoreProviderImpl : KeyValueStoreProvider {

    private val stores = mutableMapOf<String, KeyValueStore>()

    override fun getKeyValueStore(name: String): KeyValueStore {
        var store = stores[name]

        if (store == null) {
            store = TestKeyValueStoreImpl()
            stores[name] = store
        }

        return store
    }

}
