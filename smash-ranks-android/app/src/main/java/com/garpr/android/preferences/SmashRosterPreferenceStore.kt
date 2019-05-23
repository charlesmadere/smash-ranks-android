package com.garpr.android.preferences

import com.garpr.android.data.models.PollFrequency
import com.garpr.android.data.models.SmashRosterSyncResult

interface SmashRosterPreferenceStore : PreferenceStore {

    val enabled: Preference<Boolean>

    val hajimeteSync: Preference<Boolean>

    val pollFrequency: Preference<PollFrequency>

    val syncResult: Preference<SmashRosterSyncResult>

}
