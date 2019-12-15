package com.garpr.android.sync.roster

import com.garpr.android.data.models.SmashRosterSyncResult
import io.reactivex.Completable
import io.reactivex.Observable

interface SmashRosterSyncManager {

    var isEnabled: Boolean

    val isSyncing: Boolean

    val observeIsSyncing: Observable<Boolean>

    val syncResult: SmashRosterSyncResult?

    fun enableOrDisable()

    fun sync(): Completable

}
