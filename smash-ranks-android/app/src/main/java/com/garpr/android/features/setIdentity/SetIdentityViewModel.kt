package com.garpr.android.features.setIdentity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.data.models.Optional
import com.garpr.android.data.models.PlayersBundle
import com.garpr.android.data.models.Region
import com.garpr.android.extensions.takeSingle
import com.garpr.android.features.common.viewModels.BaseViewModel
import com.garpr.android.misc.PlayerListBuilder
import com.garpr.android.misc.PlayerListBuilder.PlayerListItem
import com.garpr.android.misc.Schedulers
import com.garpr.android.misc.Searchable
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.misc.Timber
import com.garpr.android.repositories.IdentityRepository
import com.garpr.android.repositories.PlayersRepository
import io.reactivex.Single
import io.reactivex.functions.BiFunction

class SetIdentityViewModel(
        private val identityRepository: IdentityRepository,
        private val playerListBuilder: PlayerListBuilder,
        private val playersRepository: PlayersRepository,
        private val schedulers: Schedulers,
        private val threadUtils: ThreadUtils,
        private val timber: Timber
) : BaseViewModel(), Searchable {

    var selectedIdentity: AbsPlayer?
        get() = state.selectedIdentity ?: identityRepositoryIdentity
        set(value) {
            val saveIconStatus = if (state.list.isNullOrEmpty()) {
                SaveIconStatus.GONE
            } else {
                val iri = identityRepositoryIdentity

                if (iri != null && iri == value || iri == null && value == null) {
                    SaveIconStatus.DISABLED
                } else {
                    SaveIconStatus.ENABLED
                }
            }

            state = state.copy(
                    selectedIdentity = value,
                    saveIconStatus = saveIconStatus
            )
        }

    val warnBeforeClose: Boolean
        get() = state.saveIconStatus == SaveIconStatus.ENABLED

    private var identityRepositoryIdentity: FavoritePlayer? = null

    private val _stateLiveData = MutableLiveData<State>()
    val stateLiveData: LiveData<State> = _stateLiveData

    private var state: State = State()
        set(value) {
            field = value
            _stateLiveData.postValue(value)
        }

    init {
        initListeners()
    }

    fun fetchPlayers(region: Region) {
        state = state.copy(isFetching = true)

        disposables.add(Single.zip(
                playersRepository.getPlayers(region),
                identityRepository.identityObservable.takeSingle(),
                BiFunction<PlayersBundle, Optional<FavoritePlayer>,
                        Pair<PlayersBundle, Optional<FavoritePlayer>>> { t1, t2 ->
                            Pair(t1, t2)
                        })
                .subscribeOn(schedulers.background)
                .observeOn(schedulers.background)
                .subscribe({ (bundle, identity) ->
                    val list = playerListBuilder.create(bundle, identity.orNull())

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
                            isEmpty = false,
                            isFetching = false,
                            isRefreshEnabled = true,
                            showSearchIcon = false,
                            list = null,
                            searchResults = null,
                            saveIconStatus = SaveIconStatus.GONE
                    )
                }))
    }

    private fun initListeners() {
        disposables.add(identityRepository.identityObservable
                .subscribeOn(schedulers.background)
                .observeOn(schedulers.background)
                .subscribe { identity ->
                    identityRepositoryIdentity = identity.orNull()
                })
    }

    fun saveSelectedIdentity(region: Region) {
        val identity = checkNotNull(selectedIdentity)
        identityRepository.setIdentity(identity, region)
    }

    override fun search(query: String?) {
        threadUtils.background.submit {
            val searchResults = playerListBuilder.search(state.list, query)
            state = state.copy(searchResults = searchResults)
        }
    }

    companion object {
        private const val TAG = "SetIdentityViewModel"
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
