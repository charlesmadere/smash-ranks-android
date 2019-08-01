package com.garpr.android.features.setIdentity

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.PlayersBundle
import com.garpr.android.data.models.Region
import com.garpr.android.extensions.safeEquals
import com.garpr.android.features.common.viewModels.BaseViewModel
import com.garpr.android.misc.Searchable
import com.garpr.android.misc.ThreadUtils2
import com.garpr.android.misc.Timber
import com.garpr.android.repositories.IdentityRepository
import com.garpr.android.repositories.PlayersRepository
import javax.inject.Inject

class SetIdentityViewModel @Inject constructor(
        private val identityRepository: IdentityRepository,
        private val playersRepository: PlayersRepository,
        private val threadUtils: ThreadUtils2,
        private val timber: Timber
) : BaseViewModel(), Searchable {

    var selectedIdentity: AbsPlayer?
        get() = state.selectedIdentity ?: identityRepository.identity
        set(value) {
            val saveIconStatus = if (state.list.isNullOrEmpty()) {
                SaveIconStatus.GONE
            } else if (identityRepository.isPlayer(value)) {
                SaveIconStatus.VISIBLE
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

    @WorkerThread
    private fun createList(bundle: PlayersBundle?): List<ListItem>? {
        val players = bundle?.players

        if (players.isNullOrEmpty()) {
            return null
        }

        val list = mutableListOf<ListItem>()
        var previousChar: Char? = null
        var letterDividerListId: Long = Long.MIN_VALUE + 1L

        ////////////////////////
        // add letter players //
        ////////////////////////

        players.forEach {
            val char = it.name.first()

            if (char.isLetter()) {
                if (!char.safeEquals(previousChar, true)) {
                    previousChar = char

                    list.add(ListItem.Divider.Letter(
                            letter = char.toUpperCase().toString(),
                            listId = letterDividerListId
                    ))

                    ++letterDividerListId
                }

                list.add(ListItem.Player(it))
            }
        }

        ///////////////////////
        // add digit players //
        ///////////////////////

        var addedDigitDivider = false

        players.forEach {
            val char = it.name.first()

            if (char.isDigit()) {
                if (!addedDigitDivider) {
                    addedDigitDivider = true
                    list.add(ListItem.Divider.Digit)
                }

                list.add(ListItem.Player(it))
            }
        }

        ///////////////////////
        // add other players //
        ///////////////////////

        var addedOtherDivider = false

        players.forEach {
            val char = it.name.first()

            if (!char.isLetterOrDigit()) {
                if (!addedOtherDivider) {
                    addedOtherDivider = true
                    list.add(ListItem.Divider.Other)
                }

                list.add(ListItem.Player(it))
            }
        }

        return list
    }

    fun fetchPlayers(region: Region) {
        state = state.copy(isFetching = true)

        disposables.add(playersRepository.getPlayers(region)
                .subscribe({
                    val list = createList(it)

                    state = state.copy(
                            isEmpty = list.isNullOrEmpty(),
                            isFetching = false,
                            isRefreshEnabled = false,
                            hasError = false,
                            showSearchIcon = true,
                            list = list,
                            searchResults = null,
                            saveIconStatus = SaveIconStatus.VISIBLE
                    )
                }, {
                    timber.e(TAG, "Error fetching players", it)

                    state = state.copy(
                            selectedIdentity = null,
                            isFetching = false,
                            isRefreshEnabled = true,
                            hasError = true,
                            showSearchIcon = false,
                            list = null,
                            searchResults = null,
                            saveIconStatus = SaveIconStatus.GONE
                    )
                }))
    }

    fun saveSelectedIdentity(region: Region) {
        val identity = selectedIdentity ?: throw IllegalStateException("selectedIdentity can't be null")
        identityRepository.setIdentity(identity, region)
    }

    override fun search(query: String?) {
        threadUtils.background.submit {
            val results = search(query, state.list)
            state = state.copy(searchResults = results)
        }
    }

    @WorkerThread
    private fun search(query: String?, list: List<ListItem>?): List<ListItem>? {
        if (query.isNullOrBlank() || list.isNullOrEmpty()) {
            return list
        }

        val results = mutableListOf<ListItem>()
        val trimmedQuery = query.trim()

        for (i in list.indices) {
            val objectI = list[i]

            if (objectI is ListItem.Divider) {
                var addedDivider = false
                var j = i + 1

                while (j < list.size) {
                    val objectJ = list[j]

                    if (objectJ is ListItem.Player) {
                        if (objectJ.player.name.contains(trimmedQuery, true)) {
                            if (!addedDivider) {
                                addedDivider = true
                                results.add(objectI)
                            }

                            results.add(objectJ)
                        }

                        ++j
                    } else {
                        j = list.size
                    }
                }
            }
        }

        return results
    }

    sealed class ListItem {
        abstract val listId: Long

        sealed class Divider : ListItem() {
            object Digit : Divider() {
                override val listId: Long = Long.MAX_VALUE - 1L
            }

            class Letter(
                    override val listId: Long,
                    val letter: String
            ) : Divider()

            object Other : Divider() {
                override val listId: Long = Long.MAX_VALUE - 2L
            }
        }

        class Player(
                val player: AbsPlayer
        ) : ListItem() {
            override val listId: Long = player.hashCode().toLong()
        }
    }

    data class State(
            val selectedIdentity: AbsPlayer? = null,
            val isEmpty: Boolean = false,
            val isFetching: Boolean = false,
            val isRefreshEnabled: Boolean = true,
            val hasError: Boolean = false,
            val showSearchIcon: Boolean = false,
            val list: List<ListItem>? = null,
            val searchResults: List<ListItem>? = null,
            val saveIconStatus: SaveIconStatus = SaveIconStatus.GONE
    )

    enum class SaveIconStatus {
        GONE, VISIBLE, ENABLED
    }

}
