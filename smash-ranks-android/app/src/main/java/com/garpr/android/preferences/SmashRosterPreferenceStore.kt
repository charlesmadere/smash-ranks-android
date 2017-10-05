package com.garpr.android.preferences

import com.garpr.android.models.SmashRosterSyncResult

interface SmashRosterPreferenceStore {

    val isEnabled: Preference<Boolean>

    val syncResult: Preference<SmashRosterSyncResult>

}
