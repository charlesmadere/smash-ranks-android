package com.garpr.android.sync

import com.garpr.android.models.SmashRosterSyncResult

interface SmashRosterSyncManager {

    var enabled: Boolean

    val syncResult: SmashRosterSyncResult?

}
