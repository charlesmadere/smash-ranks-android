package com.garpr.android.features.rankings

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.data.models.Optional
import com.garpr.android.data.models.PreviousRank
import com.garpr.android.data.models.RankingsBundle
import com.garpr.android.data.models.Region
import com.garpr.android.extensions.truncate
import com.garpr.android.features.common.viewModels.BaseViewModel
import com.garpr.android.features.player.SmashRosterAvatarUrlHelper
import com.garpr.android.misc.Schedulers
import com.garpr.android.misc.Searchable
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.misc.Timber
import com.garpr.android.repositories.IdentityRepository
import com.garpr.android.repositories.RankingsRepository
import com.garpr.android.sync.roster.SmashRosterStorage
import com.garpr.android.sync.roster.SmashRosterSyncManager
import io.reactivex.functions.BiFunction
import java.text.NumberFormat

class RankingsViewModel(
        private val identityRepository: IdentityRepository,
        private val rankingsRepository: RankingsRepository,
        private val schedulers: Schedulers,
        private val smashRosterAvatarUrlHelper: SmashRosterAvatarUrlHelper,
        private val smashRosterStorage: SmashRosterStorage,
        private val smashRosterSyncManager: SmashRosterSyncManager,
        private val threadUtils: ThreadUtils,
        private val timber: Timber
) : BaseViewModel(), Searchable {

    private val _stateLiveData = MutableLiveData<State>()
    val stateLiveData: LiveData<State> = _stateLiveData

    private var state: State = State()
        set(value) {
            field = value
            _stateLiveData.postValue(value)
        }

    companion object {
        private const val TAG = "RankingsViewModel"
        private val NUMBER_FORMAT = NumberFormat.getInstance()
    }

    init {
        initListeners()
    }

    @WorkerThread
    private fun createList(bundle: RankingsBundle?, identity: FavoritePlayer?): List<ListItem>? {
        val rankings = bundle?.rankings

        if (rankings.isNullOrEmpty()) {
            return null
        }

        val previousRankSupported = rankings.any { it.previousRank != null }
        val list = mutableListOf<ListItem>()

        bundle.rankings.mapTo(list) { player ->
            val previousRank = if (previousRankSupported) {
                checkNotNull(player.previousRank) {
                    "This is impossible here if RankedPlayerConverter does its job correctly."
                }

                if (player.rank == player.previousRank || player.previousRank == Int.MIN_VALUE) {
                    PreviousRank.INVISIBLE
                } else if (player.rank < player.previousRank) {
                    PreviousRank.INCREASE
                } else {
                    PreviousRank.DECREASE
                }
            } else {
                PreviousRank.GONE
            }

            ListItem.Player(
                    player = player,
                    isIdentity = AbsPlayer.safeEquals(identity, player),
                    previousRank = previousRank,
                    rank = NUMBER_FORMAT.format(player.rank),
                    rating = player.rating.truncate()
            )
        }

        insertOrRemoveIdentityAtFrontOfList(
                list = list,
                identity = identity
        )

        return list
    }

    fun fetchRankings(region: Region) {
        state = state.copy(isFetching = true)

        disposables.add(rankingsRepository.getRankings(region)
                .zipWith(identityRepository.identityObservable.take(1).singleOrError(),
                        BiFunction<RankingsBundle, Optional<FavoritePlayer>,
                                Pair<RankingsBundle, Optional<FavoritePlayer>>> { t1, t2 ->
                                    Pair(t1, t2)
                                })
                .subscribeOn(schedulers.background)
                .observeOn(schedulers.background)
                .subscribe({ (bundle, identity) ->
                    val list = createList(bundle, identity.item)

                    state = state.copy(
                            hasError = false,
                            isEmpty = list.isNullOrEmpty(),
                            isFetching = false,
                            list = list,
                            searchResults = null,
                            rankingsBundle = bundle
                    )
                }, {
                    timber.e(TAG, "Error fetching rankings", it)

                    state = state.copy(
                            hasError = true,
                            isEmpty = false,
                            isFetching = false,
                            list = null,
                            searchResults = null,
                            rankingsBundle = null
                    )
                }))
    }

    private fun initListeners() {
        disposables.add(identityRepository.identityObservable
                .withLatestFrom(smashRosterSyncManager.isSyncingObservable
                        .filter { isSyncing -> !isSyncing },
                        BiFunction<Optional<FavoritePlayer>, Boolean,
                                Optional<FavoritePlayer>> { optional, _ ->
                                    optional
                                })
                .subscribeOn(schedulers.background)
                .observeOn(schedulers.background)
                .subscribe { optional ->
                    refreshListItems(optional.item)
                })
    }

    @WorkerThread
    private fun insertOrRemoveIdentityAtFrontOfList(
            list: MutableList<ListItem>,
            identity: FavoritePlayer?
    ) {
        list.removeAll { listItem -> listItem is ListItem.Identity }

        if (identity == null) {
            return
        }

        val listItem = list.filterIsInstance(ListItem.Player::class.java)
                .firstOrNull { listItem -> listItem.isIdentity }

        val smashCompetitor = smashRosterStorage.getSmashCompetitor(
                region = identity.region,
                playerId = identity.id
        )

        val avatar = smashRosterAvatarUrlHelper.getAvatarUrl(
                avatarPath = smashCompetitor?.avatar?.largeButFallbackToMediumThenOriginalThenSmall
        )

        val tag: String = if (smashCompetitor?.tag?.isNotBlank() == true) {
            smashCompetitor.tag
        } else if (listItem?.player?.name?.isNotBlank() == true) {
            listItem.player.name
        } else {
            identity.name
        }

        list.add(0, if (listItem == null) {
            // the user's identity does not exist in the rankings list
            ListItem.Identity(
                    player = identity,
                    previousRank = PreviousRank.GONE,
                    avatar = avatar,
                    tag = tag
            )
        } else {
            ListItem.Identity(
                    player = identity,
                    previousRank = listItem.previousRank,
                    avatar = avatar,
                    rank = listItem.rank,
                    rating = listItem.rating,
                    tag = tag
            )
        })
    }

    @WorkerThread
    private fun refreshListItems(identity: FavoritePlayer?) {
        val list = refreshListItems(state.list, identity)
        val searchResults = refreshListItems(state.searchResults, identity)

        state = state.copy(
                list = list,
                searchResults = searchResults
        )
    }

    @WorkerThread
    private fun refreshListItems(list: List<ListItem>?, identity: FavoritePlayer?): List<ListItem>? {
        return if (list.isNullOrEmpty()) {
            list
        } else {
            val newList = mutableListOf<ListItem>()

            list.mapTo(newList) { listItem ->
                if (listItem is ListItem.Player) {
                    listItem.copy(isIdentity = listItem.player == identity)
                } else {
                    listItem
                }
            }

            insertOrRemoveIdentityAtFrontOfList(
                    list = newList,
                    identity = identity
            )

            newList
        }
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
            return null
        }

        val trimmedQuery = query.trim()

        val results = list
                .filterIsInstance(ListItem.Player::class.java)
                .filter { listItem ->
                    listItem.player.name.contains(trimmedQuery, ignoreCase = true)
                }

        return if (results.isEmpty()) {
            listOf(ListItem.NoResults(trimmedQuery))
        } else {
            results
        }
    }

    sealed class ListItem {
        abstract val listId: Long

        class Identity(
                val player: FavoritePlayer,
                val previousRank: PreviousRank,
                val avatar: String? = null,
                val rank: String? = null,
                val rating: String? = null,
                val tag: String
        ) : ListItem() {
            override val listId: Long = Long.MAX_VALUE - 1L
        }

        class NoResults(
                val query: String
        ) : ListItem() {
            override val listId: Long = Long.MAX_VALUE - 2L
        }

        data class Player(
                val player: AbsPlayer,
                val isIdentity: Boolean,
                val previousRank: PreviousRank,
                val rank: String,
                val rating: String
        ) : ListItem() {
            override val listId: Long = player.hashCode().toLong()
        }
    }

    data class State(
            val hasError: Boolean = false,
            val isEmpty: Boolean = false,
            val isFetching: Boolean = false,
            val list: List<ListItem>? = null,
            val searchResults: List<ListItem>? = null,
            val rankingsBundle: RankingsBundle? = null
    )

}
