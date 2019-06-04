package com.garpr.android.repositories

import android.content.Context
import android.content.ContextWrapper
import com.garpr.android.data.models.Region
import com.garpr.android.misc.Timber
import com.garpr.android.preferences.GeneralPreferenceStore
import com.garpr.android.preferences.RankingsPollingPreferenceStore
import com.garpr.android.repositories.RegionManager.OnRegionChangeListener
import com.garpr.android.wrappers.WeakReferenceWrapper

class RegionManagerImpl(
        private val generalPreferenceStore: GeneralPreferenceStore,
        private val rankingsPollingPreferenceStore: RankingsPollingPreferenceStore,
        private val timber: Timber
) : RegionManager {

    private val listeners = mutableSetOf<WeakReferenceWrapper<OnRegionChangeListener>>()


    companion object {
        private const val TAG = "RegionManagerImpl"
    }

    override fun addListener(listener: OnRegionChangeListener) {
        cleanListeners()

        synchronized (listeners) {
            listeners.add(WeakReferenceWrapper(listener))
        }
    }

    private fun cleanListeners(listenerToRemove: OnRegionChangeListener? = null) {
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
        cleanListeners()

        synchronized (listeners) {
            val iterator = listeners.iterator()

            while (iterator.hasNext()) {
                iterator.next().get()?.onRegionChange(this)
            }
        }
    }

    override fun removeListener(listener: OnRegionChangeListener?) {
        cleanListeners(listener)
    }

    override fun setRegion(region: Region) {
        timber.d(TAG, "old region is \"${generalPreferenceStore.currentRegion.get()}\", "
                + "new region is \"$region\"")
        rankingsPollingPreferenceStore.lastPoll.delete()
        rankingsPollingPreferenceStore.rankingsId.delete()
        generalPreferenceStore.currentRegion.set(region)
        notifyListeners()
    }

}
