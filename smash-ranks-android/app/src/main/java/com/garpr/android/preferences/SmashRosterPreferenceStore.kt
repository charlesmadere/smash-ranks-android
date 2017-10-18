package com.garpr.android.preferences

import com.garpr.android.models.SmashRosterSyncResult

interface SmashRosterPreferenceStore {

    val enabled: Preference<Boolean>

    val syncResult: Preference<SmashRosterSyncResult>

}
