package com.garpr.android.features.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.data.models.RankingsBundle
import com.garpr.android.features.common.viewModels.BaseViewModel
import com.garpr.android.repositories.FavoritePlayersRepository
import com.garpr.android.repositories.IdentityRepository
import com.garpr.android.sync.rankings.RankingsPollingManager
import com.garpr.android.sync.roster.SmashRosterSyncManager

class HomeViewModel(
        private val favoritePlayersRepository: FavoritePlayersRepository,
        private val identityRepository: IdentityRepository,
        rankingsPollingManager: RankingsPollingManager,
        smashRosterSyncManager: SmashRosterSyncManager
) : BaseViewModel(), FavoritePlayersRepository.OnFavoritePlayersChangeListener,
        IdentityRepository.OnIdentityChangeListener {

    private val _stateLiveData = MutableLiveData<State>()
    val stateLiveData: LiveData<State> = _stateLiveData

    private var state = State(
            showYourself = identityRepository.hasIdentity
    )

    val identity: FavoritePlayer?
        get() = identityRepository.identity

    init {
        favoritePlayersRepository.addListener(this)
        identityRepository.addListener(this)
        rankingsPollingManager.enableOrDisable()
        smashRosterSyncManager.enableOrDisable()
    }

    override fun onCleared() {
        identityRepository.removeListener(this)
        favoritePlayersRepository.removeListener(this)
        super.onCleared()
    }

    override fun onFavoritePlayersChange(favoritePlayersRepository: FavoritePlayersRepository) {
        state = state.copy(hasFavoritePlayers = !favoritePlayersRepository.isEmpty)
        refreshState()
    }

    override fun onIdentityChange(identityRepository: IdentityRepository) {
        state = state.copy(showYourself = identityRepository.hasIdentity)
        refreshState()
    }

    fun onRankingsBundleChange(bundle: RankingsBundle?, isEmpty: Boolean) {
        state = state.copy(
                hasRankings = !isEmpty,
                showActivityRequirements =
                        bundle?.rankingCriteria?.rankingActivityDayLimit != null &&
                        bundle.rankingCriteria.rankingNumTourneysAttended != null &&
                        bundle.rankingCriteria.tournamentQualifiedDayLimit != null,
                title = bundle?.rankingCriteria?.displayName,
                subtitleDate = bundle?.time?.shortForm
        )

        refreshState()
    }

    fun onTournamentsBundleChange(isEmpty: Boolean) {
        state = state.copy(hasTournaments = !isEmpty)
        refreshState()
    }

    private fun refreshState() {
        state = state.copy(
                showSearch = state.hasRankings || state.hasTournaments || state.hasFavoritePlayers
        )
        _stateLiveData.postValue(state)
    }

    data class State(
            val hasFavoritePlayers: Boolean = false,
            val hasRankings: Boolean = false,
            val hasTournaments: Boolean = false,
            val showActivityRequirements: Boolean = false,
            val showSearch: Boolean = false,
            val showYourself: Boolean = false,
            val subtitleDate: CharSequence? = null,
            val title: CharSequence? = null
    )

}
