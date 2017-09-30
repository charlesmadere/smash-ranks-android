package com.garpr.android.misc

import com.garpr.android.models.Region
import com.garpr.android.models.SmashCharacter
import com.garpr.android.models.SmashRoster
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
        timber.d(TAG, "deleted ${region.endpoint.title}.${region.id} from storage")
    }

    private fun getKeyValueStore(region: Region) = keyValueStoreProvider.getKeyValueStore(
            "$packageName.SmashRosterStorage.${region.endpoint.title}.${region.id}")

    override fun getSmashCharacter(region: Region, playerId: String?): SmashCharacter? {
        if (playerId == null || playerId.isBlank()) {
            return null
        }

        val smashCharacter = getKeyValueStore(region).getString(playerId, null)
        return gson.fromJson(smashCharacter, SmashCharacter::class.java)
    }

    override fun writeToStorage(region: Region, smashRoster: SmashRoster) {
        if (smashRoster.players == null || smashRoster.players.isEmpty()) {
            deleteFromStorage(region)
            return
        }

        val keyValueStoreEditor = getKeyValueStore(region).batchEdit()
        keyValueStoreEditor.clear()

        for (player in smashRoster.players) {
            keyValueStoreEditor.putString(player.key, gson.toJson(player.value,
                    SmashCharacter::class.java))
        }

        keyValueStoreEditor.apply()
        timber.d(TAG, "wrote ${region.endpoint.title}.${region.id} to storage")
    }

}
