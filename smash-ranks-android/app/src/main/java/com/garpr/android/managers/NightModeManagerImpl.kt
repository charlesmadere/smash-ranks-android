package com.garpr.android.managers

import com.garpr.android.data.models.NightMode
import com.garpr.android.managers.NightModeManager.OnNightModeChangeListener
import com.garpr.android.misc.Timber
import com.garpr.android.preferences.GeneralPreferenceStore
import com.garpr.android.wrappers.WeakReferenceWrapper

class NightModeManagerImpl(
        private val generalPreferenceStore: GeneralPreferenceStore,
        private val timber: Timber
) : NightModeManager {

    private val listeners = mutableSetOf<WeakReferenceWrapper<OnNightModeChangeListener>>()


    companion object {
        private const val TAG = "NightModeManagerImpl"
    }

    override fun addListener(listener: OnNightModeChangeListener) {
        cleanListeners()

        synchronized (listeners) {
            listeners.add(WeakReferenceWrapper(listener))
        }
    }

    private fun cleanListeners(listenerToRemove: OnNightModeChangeListener? = null) {
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

    override var nightMode: NightMode
        get() {
            return generalPreferenceStore.nightMode.get() ?: throw IllegalStateException(
                    "nightMode preference is null!")
        }
        set(value) {
            timber.d(TAG, "Theme was \"${generalPreferenceStore.nightMode.get()}\"," +
                    " is now being changed to \"$value\".")
            generalPreferenceStore.nightMode.set(value)
            notifyListeners()
        }

    private fun notifyListeners() {
        cleanListeners()

        synchronized (listeners) {
            val iterator = listeners.iterator()

            while (iterator.hasNext()) {
                iterator.next().get()?.onNightModeChange(this)
            }
        }
    }

    override fun removeListener(listener: OnNightModeChangeListener?) {
        cleanListeners(listener)
    }

}
