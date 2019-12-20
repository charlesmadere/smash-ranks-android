package com.garpr.android.preferences

import com.garpr.android.data.models.PollFrequency
import com.garpr.android.data.models.SmashRosterSyncResult
import com.garpr.android.preferences.persistent.PersistentBooleanPreference
import com.garpr.android.preferences.persistent.PersistentMoshiPreference
import com.squareup.moshi.Moshi

class SmashRosterPreferenceStoreImpl(
        override val keyValueStore: KeyValueStore,
        moshi: Moshi
) : SmashRosterPreferenceStore {

    override val enabled by lazy {
        PersistentBooleanPreference(
                key = "ENABLED",
                defaultValue = true,
                keyValueStore = keyValueStore
        )
    }

    override val hajimeteSync by lazy {
        PersistentBooleanPreference(
                key = "HAJIMETE_SYNC",
                defaultValue = true,
                keyValueStore = keyValueStore
        )
    }

    override val pollFrequency by lazy {
        PersistentMoshiPreference(
                key = "POLL_FREQUENCY",
                defaultValue = PollFrequency.EVERY_2_WEEKS,
                keyValueStore = keyValueStore,
                jsonAdapter = moshi.adapter(PollFrequency::class.java)
        )
    }

    override val syncResult by lazy {
        PersistentMoshiPreference(
                key = "SYNC_RESULT",
                defaultValue = null,
                keyValueStore = keyValueStore,
                jsonAdapter = moshi.adapter(SmashRosterSyncResult::class.java)
        )
    }

}
