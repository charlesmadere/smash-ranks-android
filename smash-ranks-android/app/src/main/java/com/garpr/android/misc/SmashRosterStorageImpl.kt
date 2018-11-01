package com.garpr.android.misc

import com.garpr.android.models.Endpoint
import com.garpr.android.models.Region
import com.garpr.android.models.SmashCompetitor
import com.garpr.android.preferences.KeyValueStoreProvider
import com.google.gson.Gson

class SmashRosterStorageImpl(
        private val gson: Gson,
        private val keyValueStoreProvider: KeyValueStoreProvider,
        private val packageName: String,
        private val timber: Timber
) : SmashRosterStorage {

    companion object {
        private const val TAG = "SmashRosterStorageImpl"
    }

    override fun deleteFromStorage(endpoint: Endpoint) {
        getKeyValueStore(endpoint).clear()
        timber.d(TAG, "deleted $endpoint from storage")
    }

    private fun getKeyValueStore(endpoint: Endpoint) = keyValueStoreProvider.getKeyValueStore(
            "$packageName.SmashRosterStorage.$endpoint")

    override fun getSmashCompetitor(endpoint: Endpoint, playerId: String?): SmashCompetitor? {
        if (playerId.isNullOrBlank()) {
            return null
        }

        val smashCompetitor = getKeyValueStore(endpoint).getString(playerId, null)
        return gson.fromJson(smashCompetitor, SmashCompetitor::class.java)
    }

    override fun getSmashCompetitor(region: Region, playerId: String?): SmashCompetitor? {
        return getSmashCompetitor(region.endpoint, playerId)
    }

    override fun writeToStorage(endpoint: Endpoint, smashRoster: Map<String, SmashCompetitor>?) {
        if (smashRoster.isNullOrEmpty()) {
            deleteFromStorage(endpoint)
            return
        }

        val keyValueStoreEditor = getKeyValueStore(endpoint).batchEdit()
        keyValueStoreEditor.clear()

        for (entry in smashRoster) {
            keyValueStoreEditor.putString(entry.key, gson.toJson(entry.value,
                    SmashCompetitor::class.java))
        }

        keyValueStoreEditor.apply()
        timber.d(TAG, "wrote ${smashRoster.size} $endpoint competitor(s) to storage")
    }

}
