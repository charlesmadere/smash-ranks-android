package com.garpr.android.features.splash

import androidx.annotation.AnyThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.Region
import com.garpr.android.features.common.viewModels.BaseViewModel
import com.garpr.android.misc.Schedulers
import com.garpr.android.misc.Timber
import com.garpr.android.preferences.GeneralPreferenceStore
import com.garpr.android.repositories.IdentityRepository
import com.garpr.android.repositories.RegionRepository

class SplashScreenViewModel(
        private val generalPreferenceStore: GeneralPreferenceStore,
        private val identityRepository: IdentityRepository,
        private val regionRepository: RegionRepository,
        private val schedulers: Schedulers,
        private val timber: Timber
)  : BaseViewModel() {

    private val _stateLiveData = MutableLiveData<State>()
    val stateLiveData: LiveData<State> = _stateLiveData

    private var state: State = State(
            isSplashScreenComplete = generalPreferenceStore.hajimeteKimasu.get() == false,
            region = regionRepository.region
    )
        set(value) {
            field = value
            _stateLiveData.postValue(value)
        }

    companion object {
        private const val TAG = "SplashScreenViewModel"
    }

    init {
        initListeners()
    }

    private fun initListeners() {
        disposables.add(generalPreferenceStore.hajimeteKimasu.observable
                .subscribeOn(schedulers.background)
                .observeOn(schedulers.background)
                .subscribe { optional ->
                    refreshIsSplashScreenComplete(optional.orElse(false))
                })

        disposables.add(identityRepository.identityObservable
                .subscribeOn(schedulers.background)
                .observeOn(schedulers.background)
                .subscribe { optional ->
                    refreshIdentity(optional.item)
                })

        disposables.add(regionRepository.observable
                .subscribeOn(schedulers.background)
                .observeOn(schedulers.background)
                .subscribe { region ->
                    refreshRegion(region)
                })
    }

    @AnyThread
    private fun refreshIsSplashScreenComplete(hajimeteKimasu: Boolean) {
        state = state.copy(isSplashScreenComplete = !hajimeteKimasu)
    }

    @AnyThread
    private fun refreshIdentity(identity: AbsPlayer?) {
        state = state.copy(identity = identity)
    }

    @AnyThread
    private fun refreshRegion(region: Region) {
        state = state.copy(region = region)
    }

    fun removeIdentity() {
        identityRepository.removeIdentity()
    }

    fun setSplashScreenComplete() {
        timber.d(TAG, "splash screen has been completed")
        generalPreferenceStore.hajimeteKimasu.set(false)
    }

    data class State(
            val identity: AbsPlayer? = null,
            val isSplashScreenComplete: Boolean,
            val region: Region
    )

}
