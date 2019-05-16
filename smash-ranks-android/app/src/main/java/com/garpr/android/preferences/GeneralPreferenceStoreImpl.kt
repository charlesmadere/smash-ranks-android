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

    override val currentRegion by lazy { PersistentMoshiPreference("CURRENT_REGION",
            defaultRegion, keyValueStore, moshi, Region::class.java) }

    override val hajimeteKimasu by lazy { PersistentBooleanPreference("HAJIMETE_KIMASU",
            true, keyValueStore) }

    override val identity by lazy { PersistentMoshiPreference<FavoritePlayer>("IDENTITY",
            null, keyValueStore, moshi, FavoritePlayer::class.java) }

    override val lastVersion by lazy { PersistentIntegerPreference("LAST_VERSION",
            null, keyValueStore) }

    override val nightMode by lazy { PersistentMoshiPreference("NIGHT_MODE",
            NightMode.SYSTEM, keyValueStore, moshi, NightMode::class.java) }

}
