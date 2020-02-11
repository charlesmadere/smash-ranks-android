package com.garpr.android.sync.roster

import android.annotation.SuppressLint
import androidx.annotation.WorkerThread
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.PollFrequency
import com.garpr.android.data.models.SmashCompetitor
import com.garpr.android.data.models.SmashRosterSyncResult
import com.garpr.android.extensions.httpCode
import com.garpr.android.misc.Schedulers
import com.garpr.android.misc.Timber
import com.garpr.android.preferences.SmashRosterPreferenceStore
import com.garpr.android.repositories.SmashRosterRepository
import com.garpr.android.wrappers.WorkManagerWrapper
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.CompletableSubject
import java.util.concurrent.TimeUnit

class SmashRosterSyncManagerImpl(
        private val schedulers: Schedulers,
        private val smashRosterPreferenceStore: SmashRosterPreferenceStore,
        private val smashRosterRepository: SmashRosterRepository,
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

    private var isSyncing: Boolean = false
        set(value) {
            field = value
            isSyncingSubject.onNext(value)
        }

    private val isSyncingSubject = BehaviorSubject.createDefault(isSyncing)
    override val isSyncingObservable: Observable<Boolean> = isSyncingSubject.hide()

    override var syncResult: SmashRosterSyncResult?
        get() = smashRosterPreferenceStore.syncResult.get()
        set(value) {
            smashRosterPreferenceStore.syncResult.set(value)
        }

    private fun disable() {
        timber.d(TAG, "disabling smash roster sync...")
        workManagerWrapper.cancelAllWorkByTag(TAG)
        timber.d(TAG, "smash roster sync has been disabled")
    }

    private fun enable() {
        timber.d(TAG, "enabling smash roster sync...")

        val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .setRequiresBatteryNotLow(true)
                .setRequiresCharging(true)
                .setRequiresDeviceIdle(true)
                .setRequiresStorageNotLow(true)
                .build()

        val pollFrequency = smashRosterPreferenceStore.pollFrequency.get()
                ?: PollFrequency.EVERY_2_WEEKS

        val periodicRequest = PeriodicWorkRequest.Builder(
                SmashRosterSyncWorker::class.java,
                pollFrequency.timeInSeconds,
                TimeUnit.SECONDS
        )
                .addTag(TAG)
                .setConstraints(constraints)
                .build()

        workManagerWrapper.enqueue(periodicRequest)
        timber.d(TAG, "smash roster sync has been enabled")

        if (hajimeteSync) {
            hajimeteSync()
        }
    }

    override fun enableOrDisable() {
        if (isEnabled) {
            enable()
        } else {
            disable()
        }
    }

    @SuppressLint("CheckResult")
    private fun hajimeteSync() {
        hajimeteSync = false
        timber.d(TAG, "performing hajimete sync...")

        sync()
                .subscribeOn(schedulers.background)
                .observeOn(schedulers.background)
                .subscribe({
                    timber.d(TAG, "successfully finished hajimete sync")
                }, {
                    timber.e(TAG, "Exception when performing hajimete sync", it)
                })
    }

    @WorkerThread
    private fun performSync() {
        if (isSyncing) {
            timber.w(TAG, "sync already in progress, canceling this sync...")
            return
        }

        isSyncing = true
        timber.d(TAG, "syncing now...")

        var garPrRoster: Map<String, SmashCompetitor>? = null
        var notGarPrRoster: Map<String, SmashCompetitor>? = null
        var throwable: Throwable? = null

        try {
            garPrRoster = smashRosterRepository.getSmashRoster(Endpoint.GAR_PR)
                    .blockingGet()
        } catch (e: Exception) {
            throwable = e
            timber.e(TAG, "Exception when fetching GAR PR roster", e)
        }

        try {
            notGarPrRoster = smashRosterRepository.getSmashRoster(Endpoint.NOT_GAR_PR)
                    .blockingGet()
        } catch (e: Exception) {
            throwable = e
            timber.e(TAG, "Exception when fetching Not GAR PR roster", e)
        }

        smashRosterStorage.clear()

        syncResult = if (throwable == null) {
            smashRosterStorage.writeToStorage(Endpoint.GAR_PR, garPrRoster)
            smashRosterStorage.writeToStorage(Endpoint.NOT_GAR_PR, notGarPrRoster)
            SmashRosterSyncResult(success = true)
        } else {
            SmashRosterSyncResult(
                    success = false,
                    httpCode = throwable.httpCode,
                    message = throwable.message
            )
        }

        timber.d(TAG, "finished sync")
        isSyncing = false
    }

    override fun sync(): Completable {
        val subject = CompletableSubject.create()

        val disposable = isSyncingObservable
                .filter { isSyncing -> !isSyncing }
                .doOnNext {
                    subject.onComplete()
                }
                .subscribe()

        return subject
                .doOnComplete {
                    disposable.dispose()
                }
                .doOnSubscribe {
                    performSync()
                }
    }

    companion object {
        private const val TAG = "SmashRosterSyncManagerImpl"
    }

}
