package com.garpr.android.preferences

import com.garpr.android.data.models.SmashRosterSyncResult
import com.garpr.android.preferences.persistent.PersistentBooleanPreference
import com.garpr.android.preferences.persistent.PersistentMoshiPreference
import com.squareup.moshi.Moshi

class SmashRosterPreferenceStoreImpl(
        override val keyValueStore: KeyValueStore,
        moshi: Moshi
) : SmashRosterPreferenceStore {

    override val enabled by lazy { PersistentBooleanPreference("ENABLED", true,
            keyValueStore) }

    override val hajimeteSync by lazy { PersistentBooleanPreference("HAJIMETE_SYNC",
            true, keyValueStore) }

    override val syncResult by lazy { PersistentMoshiPreference<SmashRosterSyncResult>(
            "SYNC_RESULT", null, keyValueStore, moshi,
            SmashRosterSyncResult::class.java) }

}
