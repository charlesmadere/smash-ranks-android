package com.garpr.android.misc

import android.content.Context
import android.content.ContextWrapper
import com.garpr.android.misc.RegionManager.OnRegionChangeListener
import com.garpr.android.models.Region
import com.garpr.android.preferences.Preference
import java.lang.ref.WeakReference

class RegionManagerImpl(
        private val mRegion: Preference<Region>,
        private val mTimber: Timber
) : RegionManager {

    private val mListeners: MutableList<WeakReference<OnRegionChangeListener>> = mutableListOf()


    companion object {
        private const val TAG = "RegionManagerImpl"
    }

    override fun addListener(listener: OnRegionChangeListener) {
        synchronized (mListeners) {
            var addListener = true
            val iterator = mListeners.iterator()

            while (iterator.hasNext()) {
                val reference = iterator.next()
                val item = reference.get()

                if (item == null) {
                    iterator.remove()
                } else if (item === listener) {
                    addListener = false
                }
            }

            if (addListener) {
                mListeners.add(WeakReference<OnRegionChangeListener>(listener))
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

        return mRegion.get() ?: throw IllegalStateException("region is null")
    }

    private fun notifyListeners() {
        synchronized (mListeners) {
            val iterator = mListeners.iterator()

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
        synchronized (mListeners) {
            val iterator = mListeners.iterator()

            while (iterator.hasNext()) {
                val reference = iterator.next()
                val item = reference.get()

                if (item == null || item === listener) {
                    iterator.remove()
                }
            }
        }
    }

    override fun setRegion(region: Region) {
        mTimber.d(TAG, "old region is \"${mRegion.get()}\", new region is \"$region\"")
        mRegion.set(region)
        notifyListeners()
    }

}
