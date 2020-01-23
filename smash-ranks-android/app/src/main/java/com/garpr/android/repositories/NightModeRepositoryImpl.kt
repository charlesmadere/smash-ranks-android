package com.garpr.android.repositories

import com.garpr.android.data.models.NightMode
import com.garpr.android.misc.Schedulers
import com.garpr.android.misc.Timber
import com.garpr.android.preferences.GeneralPreferenceStore
import io.reactivex.Observable

class NightModeRepositoryImpl(
        private val generalPreferenceStore: GeneralPreferenceStore,
        schedulers: Schedulers,
        private val timber: Timber
) : NightModeRepository {

    override var nightMode: NightMode
        get() {
            return checkNotNull(generalPreferenceStore.nightMode.get()) {
                "The user's nightMode preference is null, this should be impossible."
            }
        }
        set(value) {
            timber.d(TAG, "theme was \"$nightMode\", is now being changed to \"$value\"")
            generalPreferenceStore.nightMode.set(value)
        }

    override val observable: Observable<NightMode> = generalPreferenceStore.nightMode
            .observable
            .subscribeOn(schedulers.background)
            .observeOn(schedulers.background)
            .map { optional ->
                checkNotNull(optional.orNull()) {
                    "A null nightMode here is impossible and means we have a bug somewhere else."
                }
            }

    companion object {
        private const val TAG = "NightModeRepositoryImpl"
    }

}
