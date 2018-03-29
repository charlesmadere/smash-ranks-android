package com.garpr.android.misc

import com.garpr.android.models.Region
import com.garpr.android.models.SmashCharacter
import com.garpr.android.models.SmashCompetitor
import com.garpr.android.models.SmashRoster
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

    override fun deleteFromStorage(region: Region) {
        getKeyValueStore(region).clear()
        timber.d(TAG, "deleted ${region.endpoint.title} from storage")
    }

    private fun getKeyValueStore(region: Region) = keyValueStoreProvider.getKeyValueStore(
            "$packageName.SmashRosterStorage.${region.endpoint.title}")

    override fun getSmashCompetitor(region: Region, playerId: String?): SmashCompetitor? {
        if (playerId == null || playerId.isBlank()) {
            return null
        }

        val smashCompetitor = getKeyValueStore(region).getString(playerId, null)
        return gson.fromJson(smashCompetitor, SmashCompetitor::class.java)
    }

    override fun writeToStorage(region: Region, smashRoster: SmashRoster?) {
        if (smashRoster?.competitors == null || smashRoster.competitors.isEmpty()) {
            deleteFromStorage(region)
            return
        }

        val keyValueStoreEditor = getKeyValueStore(region).batchEdit()
        keyValueStoreEditor.clear()

        for (entry in smashRoster.competitors) {
            keyValueStoreEditor.putString(entry.key, gson.toJson(entry.value,
                    SmashCharacter::class.java))
        }

        keyValueStoreEditor.apply()
        timber.d(TAG, "wrote ${region.endpoint.title}.${region.id} to storage")
    }

}
