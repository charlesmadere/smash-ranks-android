package com.garpr.android.preferences

import com.garpr.android.models.FavoritePlayer
import com.garpr.android.models.LiteRegion
import com.garpr.android.models.NightMode

interface GeneralPreferenceStore : BasePreferenceStore {

    val currentRegion: Preference<LiteRegion>

    val identity: Preference<FavoritePlayer>

    val lastVersion: Preference<Int>

    val nightMode: Preference<NightMode>

}
