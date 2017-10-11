package com.garpr.android.preferences

import com.garpr.android.models.SmashRosterSyncResult
import com.garpr.android.preferences.persistent.PersistentBooleanPreference
import com.garpr.android.preferences.persistent.PersistentGsonPreference
import com.google.gson.Gson

class SmashRosterPreferenceStoreImpl(
        gson: Gson,
        keyValueStore: KeyValueStore
) : BasePreferenceStore(
        keyValueStore
), SmashRosterPreferenceStore {

    override val isEnabled = PersistentBooleanPreference("IS_ENABLED", true,
            keyValueStore)

    override val syncResult = PersistentGsonPreference<SmashRosterSyncResult>("SYNC_RESULT",
            null, keyValueStore, SmashRosterSyncResult::class.java, gson)

}
