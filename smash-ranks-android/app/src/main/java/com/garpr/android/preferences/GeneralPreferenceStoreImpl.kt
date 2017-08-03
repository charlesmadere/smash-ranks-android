package com.garpr.android.preferences

import com.garpr.android.models.FavoritePlayer
import com.garpr.android.models.NightMode
import com.garpr.android.models.LiteRegion
import com.garpr.android.preferences.persistent.PersistentGsonPreference
import com.garpr.android.preferences.persistent.PersistentIntegerPreference
import com.google.gson.Gson

class GeneralPreferenceStoreImpl(
        gson: Gson,
        override val keyValueStore: KeyValueStore,
        defaultRegion: LiteRegion
) : GeneralPreferenceStore {

    override fun clear() {
        keyValueStore.clear()
    }

    override val currentRegion = PersistentGsonPreference("CURRENT_REGION", defaultRegion,
            keyValueStore, LiteRegion::class.java, gson)

    override val identity = PersistentGsonPreference<FavoritePlayer>("IDENTITY",
            null, keyValueStore, FavoritePlayer::class.java, gson)

    override val lastVersion = PersistentIntegerPreference("LAST_VERSION", null,
            keyValueStore)

    override val nightMode = PersistentGsonPreference("NIGHT_MODE", NightMode.SYSTEM,
            keyValueStore, NightMode::class.java, gson)

}
