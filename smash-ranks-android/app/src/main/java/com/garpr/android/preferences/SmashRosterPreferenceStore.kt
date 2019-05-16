package com.garpr.android.preferences

import com.garpr.android.data.models.SmashRosterSyncResult

interface SmashRosterPreferenceStore : PreferenceStore {

    val enabled: Preference<Boolean>

    val hajimeteSync: Preference<Boolean>

    val syncResult: Preference<SmashRosterSyncResult>

}
