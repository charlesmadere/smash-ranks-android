package com.garpr.android.features.home

import androidx.annotation.AnyThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.RankingsBundle
import com.garpr.android.features.common.viewModels.BaseViewModel
import com.garpr.android.misc.Schedulers
import com.garpr.android.repositories.IdentityRepository
import com.garpr.android.repositories.RegionRepository
import com.garpr.android.sync.rankings.RankingsPollingManager
import com.garpr.android.sync.roster.SmashRosterSyncManager

class HomeViewModel(
        private val identityRepository: IdentityRepository,
        rankingsPollingManager: RankingsPollingManager,
        private val regionRepository: RegionRepository,
        private val schedulers: Schedulers,
        smashRosterSyncManager: SmashRosterSyncManager
) : BaseViewModel() {

    val identity: AbsPlayer?
        get() = state.identity

    private val _stateLiveData = MutableLiveData<State>()
    val stateLiveData: LiveData<State> = _stateLiveData

    private var state: State = State(
            title = regionRepository.region.displayName
    )

    init {
        initListeners()
        rankingsPollingManager.enableOrDisable()
        smashRosterSyncManager.enableOrDisable()
    }

    private fun initListeners() {
        disposables.add(identityRepository.identityObservable
                .subscribeOn(schedulers.background)
                .observeOn(schedulers.background)
                .subscribe { identity ->
                    refreshIdentity(identity.orNull())
                })

        disposables.add(regionRepository.observable
                .subscribeOn(schedulers.background)
                .observeOn(schedulers.background)
                .map { region -> region.displayName }
                .subscribe { regionDisplayName ->
                    refreshTitle(regionDisplayName)
                })
    }

    @AnyThread
    fun onRankingsBundleChange(bundle: RankingsBundle?, hasContent: Boolean) {
        state = state.copy(
                hasHomeContent = hasContent,
                subtitleDate = bundle?.time?.shortForm
        )
        refreshState()
    }

    @AnyThread
    fun onTournamentsBundleChange(isEmpty: Boolean) {
        state = state.copy(hasTournaments = !isEmpty)
        refreshState()
    }

    @AnyThread
    private fun refreshIdentity(identity: AbsPlayer?) {
        state = state.copy(
                identity = identity,
                showYourself = identity != null
        )
        refreshState()
    }

    @AnyThread
    private fun refreshTitle(title: CharSequence?) {
        state = state.copy(title = title)
        refreshState()
    }

    @AnyThread
    private fun refreshState() {
        state = state.copy(
                showSearch = state.hasHomeContent || state.hasTournaments
        )
        _stateLiveData.postValue(state)
    }

    data class State(
            val identity: AbsPlayer? = null,
            val hasHomeContent: Boolean = false,
            val hasTournaments: Boolean = false,
            val showSearch: Boolean = false,
            val showYourself: Boolean = false,
            val subtitleDate: CharSequence? = null,
            val title: CharSequence? = null
    )

}
