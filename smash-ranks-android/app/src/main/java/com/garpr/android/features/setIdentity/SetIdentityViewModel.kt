package com.garpr.android.features.setIdentity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.Region
import com.garpr.android.features.common.viewModels.BaseViewModel
import com.garpr.android.misc.PlayerListBuilder
import com.garpr.android.misc.PlayerListBuilder.PlayerListItem
import com.garpr.android.misc.Searchable
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.misc.Timber
import com.garpr.android.repositories.IdentityRepository
import com.garpr.android.repositories.PlayersRepository

class SetIdentityViewModel(
        private val identityRepository: IdentityRepository,
        private val playerListBuilder: PlayerListBuilder,
        private val playersRepository: PlayersRepository,
        private val threadUtils: ThreadUtils,
        private val timber: Timber
) : BaseViewModel(), Searchable {

    var selectedIdentity: AbsPlayer?
        get() = state.selectedIdentity ?: identityRepository.identity
        set(value) {
            val saveIconStatus = if (state.list.isNullOrEmpty()) {
                SaveIconStatus.GONE
            } else if (identityRepository.hasIdentity && identityRepository.isPlayer(value)
                    || !identityRepository.hasIdentity && value == null) {
                SaveIconStatus.DISABLED
            } else {
                SaveIconStatus.ENABLED
            }

            state = state.copy(
                    selectedIdentity = value,
                    saveIconStatus = saveIconStatus
            )
        }

    val warnBeforeClose: Boolean
        get() = state.saveIconStatus == SaveIconStatus.ENABLED

    private val _stateLiveData = MutableLiveData<State>()
    val stateLiveData: LiveData<State> = _stateLiveData

    private var state: State = State()
        set(value) {
            field = value
            _stateLiveData.postValue(value)
        }

    companion object {
        private const val TAG = "SetIdentityViewModel"
    }

    fun fetchPlayers(region: Region) {
        state = state.copy(isFetching = true)

        disposables.add(playersRepository.getPlayers(region)
                .subscribe({
                    val list = playerListBuilder.create(it)

                    state = state.copy(
                            selectedIdentity = null,
                            hasError = false,
                            isEmpty = list.isNullOrEmpty(),
                            isFetching = false,
                            isRefreshEnabled = list.isNullOrEmpty(),
                            showSearchIcon = !list.isNullOrEmpty(),
                            list = list,
                            searchResults = null,
                            saveIconStatus = SaveIconStatus.DISABLED
                    )
                }, {
                    timber.e(TAG, "Error fetching players", it)

                    state = state.copy(
                            selectedIdentity = null,
                            hasError = true,
                            isEmpty = true,
                            isFetching = false,
                            isRefreshEnabled = true,
                            showSearchIcon = false,
                            list = null,
                            searchResults = null,
                            saveIconStatus = SaveIconStatus.GONE
                    )
                }))
    }

    fun saveSelectedIdentity(region: Region) {
        val identity = checkNotNull(selectedIdentity)
        identityRepository.setIdentity(identity, region)
    }

    override fun search(query: String?) {
        threadUtils.background.submit {
            val results = playerListBuilder.search(query, state.list)
            state = state.copy(searchResults = results)
        }
    }

    data class State(
            val selectedIdentity: AbsPlayer? = null,
            val hasError: Boolean = false,
            val isEmpty: Boolean = false,
            val isFetching: Boolean = false,
            val isRefreshEnabled: Boolean = true,
            val showSearchIcon: Boolean = false,
            val list: List<PlayerListItem>? = null,
            val searchResults: List<PlayerListItem>? = null,
            val saveIconStatus: SaveIconStatus = SaveIconStatus.GONE
    )

    enum class SaveIconStatus {
        DISABLED, ENABLED, GONE
    }

}
