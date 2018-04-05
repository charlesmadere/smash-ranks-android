package com.garpr.android.preferences

import com.garpr.android.models.FavoritePlayer
import com.garpr.android.models.NightMode
import com.garpr.android.models.Region

interface GeneralPreferenceStore : PreferenceStore {

    val currentRegion: Preference<Region>

    val hajimeteKimasu: Preference<Boolean>

    val identity: Preference<FavoritePlayer>

    val lastVersion: Preference<Int>

    val nightMode: Preference<NightMode>

}
