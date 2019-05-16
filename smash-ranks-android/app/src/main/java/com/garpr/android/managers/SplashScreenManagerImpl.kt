package com.garpr.android.managers

import com.garpr.android.preferences.GeneralPreferenceStore

class SplashScreenManagerImpl(
        private val generalPreferenceStore: GeneralPreferenceStore
) : SplashScreenManager {

    override fun setSplashScreenComplete() {
        generalPreferenceStore.hajimeteKimasu.set(false)
    }

    override val showSplashScreen: Boolean
        get() = generalPreferenceStore.hajimeteKimasu.get() == true

}
