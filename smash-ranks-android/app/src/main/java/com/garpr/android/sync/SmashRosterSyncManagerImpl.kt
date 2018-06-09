package com.garpr.android.sync

import android.support.annotation.UiThread
import android.support.annotation.WorkerThread
import com.firebase.jobdispatcher.Constraint
import com.firebase.jobdispatcher.Lifetime
import com.firebase.jobdispatcher.RetryStrategy
import com.firebase.jobdispatcher.Trigger
import com.garpr.android.misc.SmashRosterStorage
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.misc.Timber
import com.garpr.android.models.Endpoint
import com.garpr.android.models.PollFrequency
import com.garpr.android.models.SmashRosterSyncResult
import com.garpr.android.networking.ServerApi
import com.garpr.android.preferences.SmashRosterPreferenceStore
import com.garpr.android.sync.SmashRosterSyncManager.OnSyncListeners
import com.garpr.android.wrappers.FirebaseApiWrapper
import com.garpr.android.wrappers.GoogleApiWrapper
import com.garpr.android.wrappers.WeakReferenceWrapper
import java.util.concurrent.TimeUnit

class SmashRosterSyncManagerImpl(
        private val firebaseApiWrapper: FirebaseApiWrapper,
        private val googleApiWrapper: GoogleApiWrapper,
        private val serverApi: ServerApi,
        private val smashRosterPreferenceStore: SmashRosterPreferenceStore,
        private val smashRosterStorage: SmashRosterStorage,
        private val threadUtils: ThreadUtils,
        private val timber: Timber
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
        firebaseApiWrapper.jobDispatcher.cancel(TAG)
        timber.d(TAG, "sync has been disabled")
    }

    private fun enable() {
        if (!googleApiWrapper.isGooglePlayServicesAvailable) {
            timber.w(TAG, "failed to schedule sync because Google Play Services are unavailable")
            return
        }

        val jobDispatcher = firebaseApiWrapper.jobDispatcher

        val jobBuilder = jobDispatcher.newJobBuilder()
                .addConstraint(Constraint.DEVICE_CHARGING)
                .addConstraint(Constraint.ON_UNMETERED_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setReplaceCurrent(true)
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setService(SmashRosterSyncJobService::class.java)
                .setTag(TAG)
                .setTrigger(Trigger.executionWindow(TimeUnit.MINUTES.toSeconds(5).toInt(),
                        PollFrequency.EVERY_5_DAYS.timeInSeconds.toInt()))

        jobDispatcher.mustSchedule(jobBuilder.build())
        timber.d(TAG, "sync has been enabled")
    }

    override fun enableOrDisable() {
        if (isEnabled) {
            enable()
        } else {
            disable()
        }
    }

    override var isEnabled: Boolean
        get() = smashRosterPreferenceStore.enabled.get() == true
        set(value) {
            smashRosterPreferenceStore.enabled.set(value)
            enableOrDisable()
        }

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

        smashRosterPreferenceStore.syncResult.set(smashRosterSyncResult)
    }

    override fun removeListener(listener: OnSyncListeners) {
        cleanListeners(listener)
    }

    override fun sync() {
        threadUtils.runOnUi(Runnable {
            notifyListenersOfOnSyncBegin()

            threadUtils.runOnBackground(Runnable {
                performSync()

                threadUtils.runOnUi(Runnable {
                    notifyListenersOfOnSyncComplete()
                })
            })
        })
    }

    override val syncResult: SmashRosterSyncResult?
        get() = smashRosterPreferenceStore.syncResult.get()

}
