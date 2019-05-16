package com.garpr.android.sync.roster

import android.annotation.SuppressLint
import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.PollFrequency
import com.garpr.android.data.models.SmashRosterSyncResult
import com.garpr.android.misc.SmashRosterStorage
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.misc.Timber
import com.garpr.android.networking.ServerApi
import com.garpr.android.preferences.SmashRosterPreferenceStore
import com.garpr.android.sync.roster.SmashRosterSyncManager.OnSyncListeners
import com.garpr.android.wrappers.WeakReferenceWrapper
import com.garpr.android.wrappers.WorkManagerWrapper
import java.util.concurrent.TimeUnit

class SmashRosterSyncManagerImpl(
        private val serverApi: ServerApi,
        private val smashRosterPreferenceStore: SmashRosterPreferenceStore,
        private val smashRosterStorage: SmashRosterStorage,
        private val threadUtils: ThreadUtils,
        private val timber: Timber,
        private val workManagerWrapper: WorkManagerWrapper
) : SmashRosterSyncManager {

    private val listeners = mutableSetOf<WeakReferenceWrapper<OnSyncListeners>>()


    companion object {
        private const val TAG = "SmashRosterSyncManagerImpl"
    }

    override fun addListener(listener: OnSyncListeners) {
        cleanListeners()

        synchronized (listeners) {
            listeners.add(WeakReferenceWrapper(listener))
        }
    }

    private fun cleanListeners(listenerToRemove: OnSyncListeners? = null) {
        synchronized (listeners) {
            val iterator = listeners.iterator()

            while (iterator.hasNext()) {
                val listener = iterator.next().get()

                if (listener == null || listener == listenerToRemove) {
                    iterator.remove()
                }
            }
        }
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

    private var hajimeteSync: Boolean
        get() = smashRosterPreferenceStore.hajimeteSync.get() == true
        set(value) = smashRosterPreferenceStore.hajimeteSync.set(value)

    override var isEnabled: Boolean
        get() = smashRosterPreferenceStore.enabled.get() == true
        set(value) {
            smashRosterPreferenceStore.enabled.set(value)
            enableOrDisable()
        }

    override var isSyncing: Boolean = false

    @UiThread
    private fun notifyListenersOfOnSyncBegin() {
        cleanListeners()

        synchronized (listeners) {
            val iterator = listeners.iterator()

            while (iterator.hasNext()) {
                iterator.next().get()?.onSmashRosterSyncBegin(this)
            }
        }
    }

    @UiThread
    private fun notifyListenersOfOnSyncComplete() {
        cleanListeners()

        synchronized (listeners) {
            val iterator = listeners.iterator()

            while (iterator.hasNext()) {
                iterator.next().get()?.onSmashRosterSyncComplete(this)
            }
        }
    }

    @WorkerThread
    private fun performSync() {
        val garPrRosterResponse = serverApi.getSmashRoster(Endpoint.GAR_PR)
        val notGarPrRosterResponse = serverApi.getSmashRoster(Endpoint.NOT_GAR_PR)

        val garPrRoster = if (garPrRosterResponse.isSuccessful) {
            garPrRosterResponse.body
        } else {
            null
        }

        val notGarPrRoster = if (notGarPrRosterResponse.isSuccessful) {
            notGarPrRosterResponse.body
        } else {
            null
        }

        val smashRosterSyncResult: SmashRosterSyncResult

        if (garPrRoster == null || notGarPrRoster == null) {
            smashRosterStorage.deleteFromStorage(Endpoint.GAR_PR)
            smashRosterStorage.deleteFromStorage(Endpoint.NOT_GAR_PR)

            smashRosterSyncResult = SmashRosterSyncResult(
                    success = false,
                    httpCode = garPrRosterResponse.code,
                    message = garPrRosterResponse.message
            )
        } else {
            smashRosterStorage.writeToStorage(Endpoint.GAR_PR, garPrRoster)
            smashRosterStorage.writeToStorage(Endpoint.NOT_GAR_PR, notGarPrRoster)

            smashRosterSyncResult = SmashRosterSyncResult(
                    success = true,
                    httpCode = garPrRosterResponse.code,
                    message = garPrRosterResponse.message
            )
        }

        syncResult = smashRosterSyncResult
    }

    @UiThread
    private fun performSyncFromUiThread() {
        notifyListenersOfOnSyncBegin()

        threadUtils.runOnBackground(Runnable {
            performSync()

            synchronized (isSyncing) {
                isSyncing = false
            }

            threadUtils.runOnUi(Runnable {
                notifyListenersOfOnSyncComplete()
            })
        })
    }

    @WorkerThread
    private fun performSyncFromWorkerThread() {
        threadUtils.runOnUi(Runnable {
            notifyListenersOfOnSyncBegin()
        })

        performSync()

        synchronized (isSyncing) {
            isSyncing = false
        }

        threadUtils.runOnUi(Runnable {
            notifyListenersOfOnSyncComplete()
        })
    }

    private val pollFrequency: PollFrequency = PollFrequency.EVERY_5_DAYS

    override fun removeListener(listener: OnSyncListeners) {
        cleanListeners(listener)
    }

    @SuppressLint("WrongThread")
    override fun sync() {
        synchronized (isSyncing) {
            if (isSyncing) {
                return
            } else {
                isSyncing = true
            }
        }

        hajimeteSync = false
        timber.d(TAG, "syncing now...")

        if (threadUtils.isUiThread) {
            performSyncFromUiThread()
        } else {
            performSyncFromWorkerThread()
        }
    }

    override var syncResult: SmashRosterSyncResult?
        get() = smashRosterPreferenceStore.syncResult.get()
        set(value) = smashRosterPreferenceStore.syncResult.set(value)

}
