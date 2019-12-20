package com.garpr.android.preferences

import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.data.models.NightMode
import com.garpr.android.data.models.Region
import com.garpr.android.preferences.persistent.PersistentBooleanPreference
import com.garpr.android.preferences.persistent.PersistentIntegerPreference
import com.garpr.android.preferences.persistent.PersistentMoshiPreference
import com.squareup.moshi.Moshi

class GeneralPreferenceStoreImpl(
        override val keyValueStore: KeyValueStore,
        moshi: Moshi,
        defaultRegion: Region
) : GeneralPreferenceStore {

    override val currentRegion by lazy {
        PersistentMoshiPreference(
                key = "CURRENT_REGION",
                defaultValue = defaultRegion,
                keyValueStore = keyValueStore,
                jsonAdapter = moshi.adapter(Region::class.java)
        )
    }

    override val hajimeteKimasu by lazy {
        PersistentBooleanPreference(
                key = "HAJIMETE_KIMASU",
                defaultValue = true,
                keyValueStore = keyValueStore
        )
    }

    override val identity by lazy {
        PersistentMoshiPreference(
                key = "IDENTITY",
                defaultValue = null,
                keyValueStore = keyValueStore,
                jsonAdapter = moshi.adapter(FavoritePlayer::class.java)
        )
    }

    override val lastVersion by lazy {
        PersistentIntegerPreference(
                key = "LAST_VERSION",
                defaultValue = null,
                keyValueStore = keyValueStore
        )
    }

    override val nightMode by lazy {
        PersistentMoshiPreference(
                key = "NIGHT_MODE",
                defaultValue = NightMode.SYSTEM,
                keyValueStore = keyValueStore,
                jsonAdapter = moshi.adapter(NightMode::class.java)
        )
    }

}
