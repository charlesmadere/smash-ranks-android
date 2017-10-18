package com.garpr.android.preferences

import com.garpr.android.models.FavoritePlayer
import com.garpr.android.models.NightMode
import com.garpr.android.models.Region
import com.garpr.android.preferences.persistent.PersistentGsonPreference
import com.garpr.android.preferences.persistent.PersistentIntegerPreference
import com.google.gson.Gson

class GeneralPreferenceStoreImpl(
        gson: Gson,
        keyValueStore: KeyValueStore,
        defaultRegion: Region
) : BasePreferenceStore(
        keyValueStore
), GeneralPreferenceStore {

    override val currentRegion by lazy { PersistentGsonPreference<Region>("CURRENT_REGION",
            defaultRegion, keyValueStore, Region::class.java, gson) }

    override val identity by lazy { PersistentGsonPreference<FavoritePlayer>("IDENTITY",
            null, keyValueStore, FavoritePlayer::class.java, gson) }

    override val lastVersion by lazy { PersistentIntegerPreference("LAST_VERSION",
            null, keyValueStore) }

    override val nightMode by lazy { PersistentGsonPreference("NIGHT_MODE", NightMode.SYSTEM,
            keyValueStore, NightMode::class.java, gson) }

}
