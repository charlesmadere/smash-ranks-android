package com.garpr.android.features.settings

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.data.models.NightMode
import com.garpr.android.data.models.Region
import com.garpr.android.data.models.SmashRosterSyncResult
import com.garpr.android.features.common.viewModels.BaseViewModel
import com.garpr.android.misc.Refreshable
import com.garpr.android.misc.Schedulers
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.misc.Timber
import com.garpr.android.repositories.FavoritePlayersRepository
import com.garpr.android.repositories.IdentityRepository
import com.garpr.android.repositories.NightModeRepository
import com.garpr.android.repositories.RegionRepository
import com.garpr.android.sync.roster.SmashRosterSyncManager

class SettingsViewModel(
        private val favoritePlayersRepository: FavoritePlayersRepository,
        private val identityRepository: IdentityRepository,
        private val nightModeRepository: NightModeRepository,
        private val regionRepository: RegionRepository,
        private val schedulers: Schedulers,
        private val smashRosterSyncManager: SmashRosterSyncManager,
        private val threadUtils: ThreadUtils,
        private val timber: Timber
) : BaseViewModel(), FavoritePlayersRepository.OnFavoritePlayersChangeListener,
        IdentityRepository.OnIdentityChangeListener, NightModeRepository.OnNightModeChangeListener,
        Refreshable, RegionRepository.OnRegionChangeListener {

    private val _stateLiveData = MutableLiveData<State>()
    val stateLiveData: LiveData<State> = _stateLiveData

    private var state: State = State(
            nightMode = nightModeRepository.nightMode,
            region = regionRepository.getRegion()
    )
        set(value) {
            field = value
            _stateLiveData.postValue(value)
        }

    companion object {
        private const val TAG = "SettingsViewModel"
    }

    init {
        initListeners()
        refresh()
    }

    fun deleteFavoritePlayers() {
        favoritePlayersRepository.clear()
    }

    fun deleteIdentity() {
        identityRepository.removeIdentity()
    }

    private fun initListeners() {
        favoritePlayersRepository.addListener(this)
        identityRepository.addListener(this)
        nightModeRepository.addListener(this)
        regionRepository.addListener(this)

        disposables.add(smashRosterSyncManager.observeIsSyncing
                .subscribeOn(schedulers.background)
                .subscribe { refreshSmashRosterState(it) })
    }

    override fun onCleared() {
        favoritePlayersRepository.removeListener(this)
        identityRepository.removeListener(this)
        nightModeRepository.removeListener(this)
        regionRepository.removeListener(this)
        super.onCleared()
    }

    override fun onFavoritePlayersChange(favoritePlayersRepository: FavoritePlayersRepository) {
        refreshFavoritePlayersSize()
    }

    override fun onIdentityChange(identityRepository: IdentityRepository) {
        refreshIdentity()
    }

    override fun onNightModeChange(nightModeRepository: NightModeRepository) {
        state = state.copy(
                nightMode = nightModeRepository.nightMode
        )
    }

    override fun onRegionChange(regionRepository: RegionRepository) {
        state = state.copy(
                region = regionRepository.getRegion()
        )
    }

    override fun refresh() {
        threadUtils.background.submit {
            refreshFavoritePlayersSize()
            refreshIdentity()
            refreshSmashRosterState()
        }
    }

    @WorkerThread
    private fun refreshFavoritePlayersSize() {
        state = state.copy(favoritePlayersState = FavoritePlayersState.Fetching)
        val size = favoritePlayersRepository.size
        state = state.copy(favoritePlayersState = FavoritePlayersState.Fetched(size))
    }

    @WorkerThread
    private fun refreshIdentity() {
        state = state.copy(identityState = IdentityState.Fetching)
        val identity = identityRepository.identity
        state = state.copy(identityState = IdentityState.Fetched(identity))
    }

    @WorkerThread
    private fun refreshSmashRosterState(isSyncing: Boolean = smashRosterSyncManager.isSyncing) {
        state = state.copy(smashRosterState = SmashRosterState.Fetching)

        state = if (isSyncing) {
            state.copy(smashRosterState = SmashRosterState.IsSyncing)
        } else {
            val result = smashRosterSyncManager.syncResult
            state.copy(smashRosterState = SmashRosterState.Fetched(result))
        }
    }

    fun setNightMode(nightMode: NightMode) {
        nightModeRepository.nightMode = nightMode
    }

    fun syncSmashRoster() {
        if (smashRosterSyncManager.isSyncing) {
            timber.d(TAG, "User tried to sync the smash roster but it's already syncing")
            return
        }

        timber.d(TAG, "User requested a manual sync of the smash roster")

        disposables.add(smashRosterSyncManager.sync()
                .subscribeOn(schedulers.background)
                .subscribe({
                    timber.d(TAG, "User-requested smash roster sync completed successfully")
                }, {
                    timber.e(TAG, "User-requested smash roster sync failed", it)
                }))
    }

    sealed class FavoritePlayersState {
        class Fetched(
                val size: Int
        ) : FavoritePlayersState()

        object Fetching : FavoritePlayersState()
    }

    sealed class IdentityState {
        class Fetched(
                val identity: FavoritePlayer?
        ) : IdentityState()

        object Fetching : IdentityState()
    }

    sealed class SmashRosterState {
        class Fetched(
                val result: SmashRosterSyncResult?
        ) : SmashRosterState()

        object Fetching : SmashRosterState()
        object IsSyncing : SmashRosterState()
    }

    data class State(
            val identityState: IdentityState = IdentityState.Fetching,
            val favoritePlayersState: FavoritePlayersState = FavoritePlayersState.Fetching,
            val nightMode: NightMode,
            val region: Region,
            val smashRosterState: SmashRosterState = SmashRosterState.Fetching
    )

}
