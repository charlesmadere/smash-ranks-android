package com.garpr.android.sync

import com.garpr.android.models.SmashRosterSyncResult

interface SmashRosterSyncManager {

    interface Listeners {
        fun onSmashRosterSyncBegin(smashRosterSyncManager: SmashRosterSyncManager)

        fun onSmashRosterSyncComplete(smashRosterSyncManager: SmashRosterSyncManager)
    }

    fun addListener(listener: Listeners)

    var enabled: Boolean

    fun removeListener(listener: Listeners)

    fun sync()

    val syncResult: SmashRosterSyncResult?

}
