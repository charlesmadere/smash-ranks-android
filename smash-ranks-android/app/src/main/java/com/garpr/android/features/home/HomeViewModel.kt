package com.garpr.android.features.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.garpr.android.data.models.AbsRegion
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.data.models.RankingsBundle
import com.garpr.android.data.models.TournamentsBundle
import com.garpr.android.features.common.viewModels.BaseViewModel
import com.garpr.android.repositories.IdentityRepository
import com.garpr.android.sync.rankings.RankingsPollingManager
import com.garpr.android.sync.roster.SmashRosterSyncManager
import javax.inject.Inject

class HomeViewModel @Inject constructor(
        private val identityRepository: IdentityRepository,
        rankingsPollingManager: RankingsPollingManager,
        smashRosterSyncManager: SmashRosterSyncManager
) : BaseViewModel(), IdentityRepository.OnIdentityChangeListener {

    private val _stateLiveData = MutableLiveData<State>()
    val stateLiveData: LiveData<State> = _stateLiveData

    private var state: State = State(showYourself = identityRepository.hasIdentity)

    val identity: FavoritePlayer?
        get() = identityRepository.identity

    init {
        rankingsPollingManager.enableOrDisable()
        smashRosterSyncManager.enableOrDisable()
        identityRepository.addListener(this)
    }

    override fun onCleared() {
        identityRepository.removeListener(this)
        super.onCleared()
    }

    fun onFavoritePlayersChange(favoritePlayers: List<FavoritePlayer>?) {
        state = state.copy(hasFavoritePlayers = favoritePlayers?.isNotEmpty() == true)
        refreshState()
    }

    override fun onIdentityChange(identityRepository: IdentityRepository) {
        state = state.copy(showYourself = identityRepository.hasIdentity)
        refreshState()
    }

    fun onRankingsBundleChange(region: AbsRegion, bundle: RankingsBundle?) {
        state = state.copy(
                hasRankings = bundle?.rankings?.isNotEmpty() == true,
                showActivityRequirements =
                        bundle?.rankingCriteria?.rankingActivityDayLimit != null &&
                        bundle.rankingCriteria.rankingNumTourneysAttended != null &&
                        bundle.rankingCriteria.tournamentQualifiedDayLimit != null,
                title = region.displayName,
                subtitleDate = bundle?.time?.shortForm
        )

        refreshState()
    }

    fun onTournamentsBundleChange(bundle: TournamentsBundle?) {
        state = state.copy(hasTournaments = bundle?.tournaments?.isNotEmpty() == true)
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
