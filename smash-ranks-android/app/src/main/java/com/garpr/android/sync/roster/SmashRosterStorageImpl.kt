package com.garpr.android.sync.roster

import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.Region
import com.garpr.android.data.models.SmashCompetitor
import com.garpr.android.misc.Timber
import com.garpr.android.preferences.KeyValueStoreProvider
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi

class SmashRosterStorageImpl(
        private val keyValueStoreProvider: KeyValueStoreProvider,
        private val moshi: Moshi,
        private val packageName: String,
        private val timber: Timber
) : SmashRosterStorage {

    private val smashCompetitorAdapter: JsonAdapter<SmashCompetitor> by lazy {
        moshi.adapter(SmashCompetitor::class.java)
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

        val json = getKeyValueStore(endpoint).getString(playerId, null)

        return if (json == null) {
            null
        } else {
            smashCompetitorAdapter.fromJson(json)
        }
    }

    override fun getSmashCompetitor(region: Region, playerId: String?): SmashCompetitor? {
        return getSmashCompetitor(region.endpoint, playerId)
    }

    override fun writeToStorage(endpoint: Endpoint, smashRoster: Map<String, SmashCompetitor>?) {
        if (smashRoster.isNullOrEmpty()) {
            deleteFromStorage(endpoint)
            return
        }

        val keyValueStoreEditor = getKeyValueStore(endpoint).batchEdit().clear()

        smashRoster.forEach { (key, value) ->
            val json = smashCompetitorAdapter.toJson(value)
            keyValueStoreEditor.putString(key, json)
        }

        keyValueStoreEditor.apply()
        timber.d(TAG, "wrote ${smashRoster.size} $endpoint competitor(s) to storage")
    }

    companion object {
        private const val TAG = "SmashRosterStorageImpl"
    }

}
