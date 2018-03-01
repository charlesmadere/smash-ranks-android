package com.garpr.android.preferences

import com.garpr.android.models.SmashRosterSyncResult
import com.garpr.android.preferences.persistent.PersistentBooleanPreference
import com.garpr.android.preferences.persistent.PersistentGsonPreference
import com.google.gson.Gson

class SmashRosterPreferenceStoreImpl(
        gson: Gson,
        override val keyValueStore: KeyValueStore
) : SmashRosterPreferenceStore {

    override val enabled by lazy { PersistentBooleanPreference("ENABLED", true,
            keyValueStore) }

    override val syncResult by lazy { PersistentGsonPreference<SmashRosterSyncResult>("SYNC_RESULT",
            null, keyValueStore, SmashRosterSyncResult::class.java, gson) }

}
