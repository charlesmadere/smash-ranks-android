package com.garpr.android.sync.roster

import com.garpr.android.data.models.SmashRosterSyncResult
import io.reactivex.Completable
import io.reactivex.Observable

interface SmashRosterSyncManager {

    var isEnabled: Boolean

    val observeSyncState: Observable<State>

    val syncResult: SmashRosterSyncResult?

    val syncState: State

    fun enableOrDisable()

    fun sync(): Completable

    enum class State {
        NOT_SYNCING, SYNCING
    }

}
