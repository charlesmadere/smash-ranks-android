package com.garpr.android.repositories

import com.garpr.android.data.models.NightMode
import com.garpr.android.misc.Timber
import com.garpr.android.preferences.GeneralPreferenceStore
import com.garpr.android.repositories.NightModeRepository.OnNightModeChangeListener
import com.garpr.android.wrappers.WeakReferenceWrapper

class NightModeRepositoryImpl(
        private val generalPreferenceStore: GeneralPreferenceStore,
        private val timber: Timber
) : NightModeRepository {

    private val listeners = mutableSetOf<WeakReferenceWrapper<OnNightModeChangeListener>>()

    override var nightMode: NightMode
        get() {
            return checkNotNull(generalPreferenceStore.nightMode.get()) {
                "The user's nightMode preference is null, this should be impossible."
            }
        }
        set(value) {
            timber.d(TAG, "Theme was \"${generalPreferenceStore.nightMode.get()}\"," +
                    " is now being changed to \"$value\".")
            generalPreferenceStore.nightMode.set(value)
            notifyListeners()
        }

    companion object {
        private const val TAG = "NightModeRepositoryImpl"
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
