package com.garpr.android.features.splash

import com.garpr.android.features.common.viewModels.BaseViewModel
import com.garpr.android.preferences.GeneralPreferenceStore
import javax.inject.Inject

class SplashScreenViewModel @Inject constructor(
        private val generalPreferenceStore: GeneralPreferenceStore
)  : BaseViewModel() {

    val showSplashScreen: Boolean
        get() = generalPreferenceStore.hajimeteKimasu.get() == true

    fun setSplashScreenComplete() {
        generalPreferenceStore.hajimeteKimasu.set(false)
    }

}
