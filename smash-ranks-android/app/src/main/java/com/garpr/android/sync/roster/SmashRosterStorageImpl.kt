package com.garpr.android.sync.roster

import androidx.annotation.WorkerThread
import com.garpr.android.data.database.DbSmashCompetitor
import com.garpr.android.data.database.SmashCompetitorDao
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.Region
import com.garpr.android.data.models.SmashCompetitor
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.misc.Timber
import com.garpr.android.preferences.KeyValueStoreProvider
import com.squareup.moshi.Moshi

class SmashRosterStorageImpl(
        private val keyValueStoreProvider: KeyValueStoreProvider,
        private val moshi: Moshi,
        private val smashCompetitorDao: SmashCompetitorDao,
        private val packageName: String,
        private val threadUtils: ThreadUtils,
        private val timber: Timber
) : SmashRosterStorage {

    override fun clear() {
        timber.d(TAG, "clearing smash roster storage...")
        smashCompetitorDao.deleteAll()
    }

    override fun getSmashCompetitor(endpoint: Endpoint, playerId: String?): SmashCompetitor? {
        if (playerId.isNullOrBlank()) {
            return null
        }

        val dbSmashCompetitor = smashCompetitorDao.get(endpoint, playerId)
        return dbSmashCompetitor?.toSmashCompetitor()
    }

    override fun getSmashCompetitor(region: Region, playerId: String?): SmashCompetitor? {
        return getSmashCompetitor(region.endpoint, playerId)
    }

    override fun migrate() {
        threadUtils.background.submit {
            migrateFromKeyValueStoreToRoom()
        }
    }

    @WorkerThread
    private fun migrateFromKeyValueStoreToRoom() {
        timber.d(TAG, "migrating smash roster from KeyValueStore to Room...")

        val smashCompetitorAdapter = moshi.adapter(SmashCompetitor::class.java)
        val dbSmashCompetitors = mutableListOf<DbSmashCompetitor>()

        Endpoint.values().forEach { endpoint ->
            val keyValueStore = keyValueStoreProvider.getKeyValueStore(
                    "$packageName.SmashRosterStorage.$endpoint")

            keyValueStore.all?.mapNotNullTo(dbSmashCompetitors) { (_, json) ->
                val smashCompetitor = if (json is String && json.isNotBlank()) {
                    smashCompetitorAdapter.fromJson(json)
                } else {
                    null
                }

                if (smashCompetitor == null) {
                    null
                } else {
                    DbSmashCompetitor(
                            smashCompetitor = smashCompetitor,
                            endpoint = endpoint
                    )
                }
            }

            keyValueStore.clear()
        }

        if (dbSmashCompetitors.isEmpty()) {
            timber.d(TAG, "smash roster storage has no competitors to migrate")
            return
        }

        smashCompetitorDao.insertAll(dbSmashCompetitors)
        timber.d(TAG, "finished migration process")
    }

    override fun writeToStorage(endpoint: Endpoint, smashRoster: Map<String, SmashCompetitor>?) {
        val dbSmashCompetitors = smashRoster?.map { (_, smashCompetitor) ->
            DbSmashCompetitor(
                    smashCompetitor = smashCompetitor,
                    endpoint = endpoint
            )
        }

        if (!dbSmashCompetitors.isNullOrEmpty()) {
            smashCompetitorDao.insertAll(dbSmashCompetitors)
        }

        timber.d(TAG, "added ${dbSmashCompetitors?.size ?: 0} $endpoint competitor(s) to Room")
    }

    companion object {
        private const val TAG = "SmashRosterStorageImpl"
    }

}
