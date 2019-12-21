package com.garpr.android.repositories

import com.garpr.android.data.models.NightMode
import com.garpr.android.extensions.requireValue
import com.garpr.android.misc.Timber
import com.garpr.android.preferences.GeneralPreferenceStore
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class NightModeRepositoryImpl(
        private val generalPreferenceStore: GeneralPreferenceStore,
        private val timber: Timber
) : NightModeRepository {

    private val subject = BehaviorSubject.createDefault<NightMode>(
            checkNotNull(generalPreferenceStore.nightMode.get()) {
                "The user's nightMode preference is null, this should be impossible."
            }
    )

    override var nightMode: NightMode
        get() = subject.requireValue()
        set(value) {
            timber.d(TAG, "Theme was \"${subject.requireValue()}\"," +
                    " is now being changed to \"$value\".")
            generalPreferenceStore.nightMode.set(value)
            subject.onNext(value)
        }

    override val observable: Observable<NightMode> = subject.hide()

    companion object {
        private const val TAG = "NightModeRepositoryImpl"
    }

}
