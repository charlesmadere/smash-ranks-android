package com.garpr.android.preferences

import com.garpr.android.models.FavoritePlayer
import com.garpr.android.models.NightMode
import com.garpr.android.models.Region

interface GeneralPreferenceStore : BasePreferenceStore {

    val currentRegion: Preference<Region>

    val identity: Preference<FavoritePlayer>

    val lastVersion: Preference<Int>

    val nightMode: Preference<NightMode>

}
