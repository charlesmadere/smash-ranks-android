package com.garpr.android.sync

import android.support.annotation.UiThread
import android.support.annotation.WorkerThread
import com.garpr.android.misc.RegionManager
import com.garpr.android.misc.SmashRosterStorage
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.models.SmashRosterSyncResult
import com.garpr.android.networking.ServerApi
import com.garpr.android.preferences.SmashRosterPreferenceStore
import com.garpr.android.sync.SmashRosterSyncManager.Listeners
import java.lang.ref.WeakReference

class SmashRosterSyncManagerImpl(
        private val regionManager: RegionManager,
        private val serverApi: ServerApi,
        private val smashRosterPreferenceStore: SmashRosterPreferenceStore,
        private val smashRosterStorage: SmashRosterStorage,
        private val threadUtils: ThreadUtils
) : SmashRosterSyncManager {

    private val listeners = mutableListOf<WeakReference<Listeners>>()


    override fun addListener(listener: Listeners) {
        synchronized (listeners) {
            var addListener = true
            val iterator = listeners.iterator()

            while (iterator.hasNext()) {
                val reference = iterator.next()
                val item = reference.get()

                if (item == null) {
                    iterator.remove()
                } else if (item == listener) {
                    addListener = false
                }
            }

            if (addListener) {
                listeners.add(WeakReference(listener))
            }
        }
    }

    override var isEnabled: Boolean
        get() = smashRosterPreferenceStore.enabled.get() == true
        set(value) {
            smashRosterPreferenceStore.enabled.set(value)
        }

    @UiThread
    private fun notifyListenersOfOnSyncBegin() {
        synchronized (listeners) {
            val iterator = listeners.iterator()

            while (iterator.hasNext()) {
                val reference = iterator.next()
                val item = reference.get()

                if (item == null) {
                    iterator.remove()
                } else {
                    item.onSmashRosterSyncBegin(this)
                }
            }
        }
    }

    @UiThread
    private fun notifyListenersOfOnSyncComplete() {
        synchronized (listeners) {
            val iterator = listeners.iterator()

            while (iterator.hasNext()) {
                val reference = iterator.next()
                val item = reference.get()

                if (item == null) {
                    iterator.remove()
                } else {
                    item.onSmashRosterSyncComplete(this)
                }
            }
        }
    }

    @WorkerThread
    private fun performSync() {
        // TODO
    }

    override fun removeListener(listener: Listeners) {
        synchronized (listeners) {
            val iterator = listeners.iterator()

            while (iterator.hasNext()) {
                val next = iterator.next()
                val item = next.get()

                if (item == null || item == listener) {
                    iterator.remove()
                }
            }
        }
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
