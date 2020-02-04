package com.garpr.android.features.settings

import androidx.annotation.AnyThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.data.models.NightMode
import com.garpr.android.data.models.PollFrequency
import com.garpr.android.data.models.Region
import com.garpr.android.data.models.SmashRosterSyncResult
import com.garpr.android.extensions.toJavaUri
import com.garpr.android.features.common.viewModels.BaseViewModel
import com.garpr.android.misc.Schedulers
import com.garpr.android.misc.Timber
import com.garpr.android.repositories.FavoritePlayersRepository
import com.garpr.android.repositories.IdentityRepository
import com.garpr.android.repositories.NightModeRepository
import com.garpr.android.repositories.RegionRepository
import com.garpr.android.sync.rankings.RankingsPollingManager
import com.garpr.android.sync.roster.SmashRosterSyncManager
import java.net.URI as JavaUri

class SettingsViewModel(
        private val favoritePlayersRepository: FavoritePlayersRepository,
        private val identityRepository: IdentityRepository,
        private val nightModeRepository: NightModeRepository,
        private val rankingsPollingManager: RankingsPollingManager,
        private val regionRepository: RegionRepository,
        private val schedulers: Schedulers,
        private val smashRosterSyncManager: SmashRosterSyncManager,
        private val timber: Timber
) : BaseViewModel() {

    private val _favoritePlayersStateLiveData = MutableLiveData<FavoritePlayersState>(FavoritePlayersState.Fetching)
    val favoritePlayersStateLiveData: LiveData<FavoritePlayersState> = _favoritePlayersStateLiveData

    private val _identityStateLiveData = MutableLiveData<IdentityState>(IdentityState.Fetching)
    val identityStateLiveData: LiveData<IdentityState> = _identityStateLiveData

    private val _nightModeLiveData = MutableLiveData<NightMode>(nightModeRepository.nightMode)
    val nightModeLiveData: LiveData<NightMode> = _nightModeLiveData

    private val _regionLiveData = MutableLiveData<Region>(regionRepository.region)
    val regionLiveData: LiveData<Region> = _regionLiveData

    private val _smashRosterStateLiveData = MutableLiveData<SmashRosterState>(SmashRosterState.Fetching)
    val smashRosterStateLiveData: LiveData<SmashRosterState> = _smashRosterStateLiveData

    private var rankingsPollingState: RankingsPollingState = RankingsPollingState(
            isChargingRequired = rankingsPollingManager.isChargingRequired,
            isEnabled = rankingsPollingManager.isEnabled,
            isVibrationEnabled = rankingsPollingManager.isVibrationEnabled,
            isWifiRequired = rankingsPollingManager.isWifiRequired,
            ringtoneUri = rankingsPollingManager.ringtone,
            pollFrequency = rankingsPollingManager.pollFrequency
    )
        set(value) {
            field = value
            _rankingsPollingStateLiveData.postValue(value)
        }

    private val _rankingsPollingStateLiveData = MutableLiveData<RankingsPollingState>(rankingsPollingState)
    val rankingsPollingStateLiveData: LiveData<RankingsPollingState> = _rankingsPollingStateLiveData

    companion object {
        private const val TAG = "SettingsViewModel"
    }

    init {
        initListeners()
    }

    fun deleteFavoritePlayers() {
        favoritePlayersRepository.clear()
    }

    fun deleteIdentity() {
        identityRepository.removeIdentity()
    }

    private fun initListeners() {
        disposables.add(favoritePlayersRepository.sizeObservable
                .subscribeOn(schedulers.background)
                .observeOn(schedulers.background)
                .subscribe { size ->
                    _favoritePlayersStateLiveData.postValue(FavoritePlayersState.Fetched(size))
                })

        disposables.add(identityRepository.identityObservable
                .subscribeOn(schedulers.background)
                .observeOn(schedulers.background)
                .subscribe { identity ->
                    _identityStateLiveData.postValue(IdentityState.Fetched(identity.orNull()))
                })

        disposables.add(nightModeRepository.observable
                .subscribeOn(schedulers.background)
                .observeOn(schedulers.background)
                .subscribe { nightMode ->
                    _nightModeLiveData.postValue(nightMode)
                })

        disposables.add(regionRepository.observable
                .subscribeOn(schedulers.background)
                .observeOn(schedulers.background)
                .subscribe { region ->
                    _regionLiveData.postValue(region)
                })

        disposables.add(smashRosterSyncManager.isSyncingObservable
                .subscribeOn(schedulers.background)
                .observeOn(schedulers.background)
                .subscribe { isSyncing ->
                    _smashRosterStateLiveData.postValue(if (isSyncing) {
                        SmashRosterState.Syncing
                    } else {
                        SmashRosterState.Fetched(smashRosterSyncManager.syncResult)
                    })
                })
    }

    @AnyThread
    private fun refreshRankingsPollingState() {
        rankingsPollingState = rankingsPollingState.copy(
                isChargingRequired = rankingsPollingManager.isChargingRequired,
                isEnabled = rankingsPollingManager.isEnabled,
                isVibrationEnabled = rankingsPollingManager.isVibrationEnabled,
                isWifiRequired = rankingsPollingManager.isWifiRequired,
                pollFrequency = rankingsPollingManager.pollFrequency,
                ringtoneUri = rankingsPollingManager.ringtone
        )
    }

    fun setNightMode(nightMode: NightMode) {
        nightModeRepository.nightMode = nightMode
    }

    fun setRankingsPollingIsChargingRequired(isChargingRequired: Boolean) {
        rankingsPollingManager.isChargingRequired = isChargingRequired
        refreshRankingsPollingState()
    }

    fun setRankingsPollingIsEnabled(isEnabled: Boolean) {
        rankingsPollingManager.isEnabled = isEnabled
        refreshRankingsPollingState()
    }

    fun setRankingsPollingIsVibrationEnabled(isVibrationEnabled: Boolean) {
        rankingsPollingManager.isVibrationEnabled = isVibrationEnabled
        refreshRankingsPollingState()
    }

    fun setRankingsPollingIsWifiRequired(isWifiRequired: Boolean) {
        rankingsPollingManager.isWifiRequired = isWifiRequired
        refreshRankingsPollingState()
    }

    fun setRankingsPollingPollFrequency(pollFrequency: PollFrequency) {
        rankingsPollingManager.pollFrequency = pollFrequency
        refreshRankingsPollingState()
    }

    fun setRankingsPollingRingtoneUri(ringtone: String?) {
        rankingsPollingManager.ringtone = ringtone.toJavaUri()
        refreshRankingsPollingState()
    }

    fun syncSmashRoster() {
        timber.d(TAG, "User requested a manual sync of the smash roster")

        disposables.add(smashRosterSyncManager.sync()
                .subscribeOn(schedulers.background)
                .observeOn(schedulers.background)
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

    data class RankingsPollingState(
            val isChargingRequired: Boolean,
            val isEnabled: Boolean,
            val isVibrationEnabled: Boolean,
            val isWifiRequired: Boolean,
            val ringtoneUri: JavaUri?,
            val pollFrequency: PollFrequency
    )

    sealed class SmashRosterState {
        class Fetched(
                val result: SmashRosterSyncResult?
        ) : SmashRosterState()

        object Fetching : SmashRosterState()
        object Syncing : SmashRosterState()
    }

}
