package com.garpr.android.sync

import android.support.annotation.UiThread
import android.support.annotation.WorkerThread
import com.firebase.jobdispatcher.Lifetime
import com.firebase.jobdispatcher.RetryStrategy
import com.garpr.android.managers.RegionManager
import com.garpr.android.misc.SmashRosterStorage
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.misc.Timber
import com.garpr.android.models.SmashRosterSyncResult
import com.garpr.android.networking.ServerApi
import com.garpr.android.preferences.SmashRosterPreferenceStore
import com.garpr.android.sync.SmashRosterSyncManager.OnSyncListeners
import com.garpr.android.wrappers.FirebaseApiWrapper
import com.garpr.android.wrappers.GoogleApiWrapper
import com.garpr.android.wrappers.WeakReferenceWrapper

class SmashRosterSyncManagerImpl(
        private val firebaseApiWrapper: FirebaseApiWrapper,
        private val googleApiWrapper: GoogleApiWrapper,
        private val regionManager: RegionManager,
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
        if (googleApiWrapper.isGooglePlayServicesAvailable) {
            timber.w(TAG, "failed to schedule sync because Google Play Services are unavailable")
            return
        }

        val jobDispatcher = firebaseApiWrapper.jobDispatcher

        val jobBuilder = jobDispatcher.newJobBuilder()
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setReplaceCurrent(true)
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setService(SmashRosterSyncJobService::class.java)
                .setTag(TAG)

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
        // TODO
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
