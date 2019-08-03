package com.garpr.android.sync.roster

import androidx.annotation.WorkerThread
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.PollFrequency
import com.garpr.android.data.models.SmashCompetitor
import com.garpr.android.data.models.SmashRosterSyncResult
import com.garpr.android.extensions.httpCode
import com.garpr.android.extensions.message
import com.garpr.android.extensions.requireValue
import com.garpr.android.misc.Timber
import com.garpr.android.networking.ServerApi
import com.garpr.android.preferences.SmashRosterPreferenceStore
import com.garpr.android.sync.roster.SmashRosterSyncManager.State
import com.garpr.android.wrappers.WorkManagerWrapper
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit

class SmashRosterSyncManagerImpl(
        private val serverApi: ServerApi,
        private val smashRosterPreferenceStore: SmashRosterPreferenceStore,
        private val smashRosterStorage: SmashRosterStorage,
        private val timber: Timber,
        private val workManagerWrapper: WorkManagerWrapper
) : SmashRosterSyncManager {

    private var hajimeteSync: Boolean
        get() = smashRosterPreferenceStore.hajimeteSync.get() == true
        set(value) = smashRosterPreferenceStore.hajimeteSync.set(value)

    override var isEnabled: Boolean
        get() = smashRosterPreferenceStore.enabled.get() == true
        set(value) {
            smashRosterPreferenceStore.enabled.set(value)
            enableOrDisable()
        }

    private val syncStateSubject = BehaviorSubject.createDefault(State.NOT_SYNCING)
    override val observeSyncState: Observable<State> = syncStateSubject.hide()

    override var syncResult: SmashRosterSyncResult?
        get() = smashRosterPreferenceStore.syncResult.get()
        set(value) = smashRosterPreferenceStore.syncResult.set(value)

    override var syncState: State
        get() = syncStateSubject.requireValue()
        set(value) {
            syncStateSubject.onNext(value)
        }

    companion object {
        private const val TAG = "SmashRosterSyncManagerImpl"
    }

    private fun disable() {
        timber.d(TAG, "disabling syncing...")
        workManagerWrapper.cancelAllWorkByTag(TAG)
        timber.d(TAG, "sync has been disabled")
    }

    private fun enable() {
        timber.d(TAG, "enabling syncing...")

        val constraints = Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .setRequiresCharging(true)
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .build()

        val pollFrequency = smashRosterPreferenceStore.pollFrequency.get()
                ?: PollFrequency.EVERY_10_DAYS

        val periodicRequest = PeriodicWorkRequest.Builder(
                SmashRosterSyncWorker::class.java,
                pollFrequency.timeInSeconds,
                TimeUnit.SECONDS
        )
        .addTag(TAG)
        .setConstraints(constraints)
        .build()

        workManagerWrapper.enqueue(periodicRequest)
        timber.d(TAG, "sync has been enabled")

        if (hajimeteSync) {
            timber.d(TAG, "hajimete sync")
            sync()
        }
    }

    override fun enableOrDisable() {
        if (isEnabled) {
            enable()
        } else {
            disable()
        }
    }

    @WorkerThread
    private fun performSync() {
        if (syncState == State.SYNCING) {
            return
        } else {
            syncState = State.SYNCING
        }

        timber.d(TAG, "syncing now...")
        hajimeteSync = false

        var garPrRoster: Map<String, SmashCompetitor>? = null
        var notGarPrRoster: Map<String, SmashCompetitor>? = null
        var throwable: Throwable? = null

        try {
            garPrRoster = serverApi.getSmashRoster(Endpoint.GAR_PR)
                    .blockingGet()
        } catch (e: Exception) {
            throwable = e
            timber.e(TAG, "Exception when fetching GAR PR roster", e)
        }

        try {
            notGarPrRoster = serverApi.getSmashRoster(Endpoint.NOT_GAR_PR)
                    .blockingGet()
        } catch (e: Exception) {
            throwable = e
            timber.e(TAG, "Exception when fetching Not GAR PR roster", e)
        }

        syncResult = if (garPrRoster.isNullOrEmpty() || notGarPrRoster.isNullOrEmpty() ||
                throwable != null) {
            smashRosterStorage.deleteFromStorage(Endpoint.GAR_PR)
            smashRosterStorage.deleteFromStorage(Endpoint.NOT_GAR_PR)

            SmashRosterSyncResult(
                    success = false,
                    httpCode = throwable.httpCode,
                    message = throwable.message
            )
        } else {
            smashRosterStorage.writeToStorage(Endpoint.GAR_PR, garPrRoster)
            smashRosterStorage.writeToStorage(Endpoint.NOT_GAR_PR, notGarPrRoster)
            SmashRosterSyncResult(success = true)
        }

        timber.d(TAG, "finished sync")
        syncState = State.NOT_SYNCING
    }

    override fun sync(): Completable {
        return Completable.create {
            performSync()
            it.onComplete()
        }
    }

}
