package com.garpr.android.misc

import android.content.Context
import android.content.ContextWrapper
import com.garpr.android.misc.RegionManager.OnRegionChangeListener
import com.garpr.android.models.Region
import com.garpr.android.preferences.GeneralPreferenceStore
import com.garpr.android.preferences.RankingsPollingPreferenceStore
import java.lang.ref.WeakReference

class RegionManagerImpl(
        private val generalPreferenceStore: GeneralPreferenceStore,
        private val rankingsPollingPreferenceStore: RankingsPollingPreferenceStore,
        private val timber: Timber
) : RegionManager {

    private val listeners = mutableListOf<WeakReference<OnRegionChangeListener>>()


    companion object {
        private const val TAG = "RegionManagerImpl"
    }

    override fun addListener(listener: OnRegionChangeListener) {
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

    override fun getRegion(context: Context?): Region {
        if (context != null) {
            if (context is RegionManager.RegionHandle) {
                val region = (context as RegionManager.RegionHandle).currentRegion

                if (region != null) {
                    return region
                }
            }

            if (context is ContextWrapper) {
                return getRegion(context.baseContext)
            }
        }

        return generalPreferenceStore.currentRegion.get() ?: throw IllegalStateException(
                "currentRegion preference is null!")
    }

    private fun notifyListeners() {
        synchronized (listeners) {
            val iterator = listeners.iterator()

            while (iterator.hasNext()) {
                val reference = iterator.next()
                val item = reference.get()

                if (item == null) {
                    iterator.remove()
                } else {
                    item.onRegionChange(this)
                }
            }
        }
    }

    override fun removeListener(listener: OnRegionChangeListener?) {
        synchronized (listeners) {
            val iterator = listeners.iterator()

            while (iterator.hasNext()) {
                val reference = iterator.next()
                val item = reference.get()

                if (item == null || item == listener) {
                    iterator.remove()
                }
            }
        }
    }

    override fun setRegion(region: Region) {
        timber.d(TAG, "old region is \"${generalPreferenceStore.currentRegion.get()}\", "
                + "new region is \"$region\"")
        rankingsPollingPreferenceStore.lastPoll.delete()
        rankingsPollingPreferenceStore.rankingsDate.delete()
        generalPreferenceStore.currentRegion.set(region)
        notifyListeners()
    }

}
