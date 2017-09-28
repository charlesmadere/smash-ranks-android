package com.garpr.android.sync

import com.garpr.android.models.SmashRosterSyncResult
import com.garpr.android.preferences.SmashRosterPreferenceStore

class SmashRosterSyncManagerImpl(
        private val smashRosterPreferenceStore: SmashRosterPreferenceStore
) : SmashRosterSyncManager {

    override var enabled: Boolean
        get() = smashRosterPreferenceStore.enabled.get() == true
        set(value) {
            smashRosterPreferenceStore.enabled.set(value)
        }

    override val syncResult: SmashRosterSyncResult?
        get() = smashRosterPreferenceStore.syncResult.get()

}
