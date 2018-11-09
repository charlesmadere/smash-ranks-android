package com.garpr.android.sync.roster

import com.garpr.android.models.SmashRosterSyncResult

interface SmashRosterSyncManager {

    interface OnSyncListeners {
        fun onSmashRosterSyncBegin(smashRosterSyncManager: SmashRosterSyncManager)
        fun onSmashRosterSyncComplete(smashRosterSyncManager: SmashRosterSyncManager)
    }

    fun addListener(listener: OnSyncListeners)

    fun enableOrDisable()

    var isEnabled: Boolean

    val isSyncing: Boolean

    fun removeListener(listener: OnSyncListeners)

    fun sync()

    val syncResult: SmashRosterSyncResult?

}
