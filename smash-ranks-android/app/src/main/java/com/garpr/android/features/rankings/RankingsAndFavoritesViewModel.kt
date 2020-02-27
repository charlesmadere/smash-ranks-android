package com.garpr.android.features.rankings

import androidx.annotation.AnyThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.data.models.Optional
import com.garpr.android.data.models.PreviousRank
import com.garpr.android.data.models.RankingsBundle
import com.garpr.android.data.models.Region
import com.garpr.android.extensions.takeSingle
import com.garpr.android.extensions.truncate
import com.garpr.android.features.common.viewModels.BaseViewModel
import com.garpr.android.features.player.SmashRosterAvatarUrlHelper
import com.garpr.android.misc.Schedulers
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.misc.Timber
import com.garpr.android.repositories.FavoritePlayersRepository
import com.garpr.android.repositories.IdentityRepository
import com.garpr.android.repositories.RankingsRepository
import com.garpr.android.sync.roster.SmashRosterStorage
import com.garpr.android.sync.roster.SmashRosterSyncManager
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.Function3
import java.text.NumberFormat
import java.util.Collections

class RankingsAndFavoritesViewModel(
        private val favoritePlayersRepository: FavoritePlayersRepository,
        private val identityRepository: IdentityRepository,
        private val rankingsRepository: RankingsRepository,
        private val schedulers: Schedulers,
        private val smashRosterAvatarUrlHelper: SmashRosterAvatarUrlHelper,
        private val smashRosterStorage: SmashRosterStorage,
        private val smashRosterSyncManager: SmashRosterSyncManager,
        private val threadUtils: ThreadUtils,
        private val timber: Timber
) : BaseViewModel() {

    private val _stateLiveData = MutableLiveData<State>()
    val stateLiveData: LiveData<State> = _stateLiveData

    private var state: State = State()
        set(value) {
            field = value
            _stateLiveData.postValue(value)
        }

    var searchQuery: String? = null
        set(value) {
            field = value
            search(value)
        }

    init {
        initListeners()
    }

    @WorkerThread
    private fun createList(favorites: List<FavoritePlayer>?, identity: FavoritePlayer?,
            bundle: RankingsBundle?): List<ListItem> {
        val list = mutableListOf<ListItem>()
        var isPreviousRankSupported = false

        val rankedPlayerList: List<ListItem>? = if (bundle == null) {
            null
        } else if (bundle.rankings.isNullOrEmpty()) {
            emptyList()
        } else {
            isPreviousRankSupported = bundle.rankings.any { it.previousRank != null }

            bundle.rankings.map { rankedPlayer ->
                val previousRank: PreviousRank = if (isPreviousRankSupported) {
                    checkNotNull(rankedPlayer.previousRank) {
                        "This is impossible here if RankedPlayerConverter does its job correctly."
                    }

                    if (rankedPlayer.rank == rankedPlayer.previousRank ||
                            rankedPlayer.previousRank == Int.MIN_VALUE) {
                        PreviousRank.INVISIBLE
                    } else if (rankedPlayer.rank < rankedPlayer.previousRank) {
                        PreviousRank.INCREASE
                    } else {
                        PreviousRank.DECREASE
                    }
                } else {
                    PreviousRank.GONE
                }

                ListItem.Player(
                        player = rankedPlayer,
                        isIdentity = AbsPlayer.safeEquals(identity, rankedPlayer),
                        rawRank = rankedPlayer.rank,
                        previousRank = previousRank,
                        rank = NUMBER_FORMAT.format(rankedPlayer.rank),
                        rating = rankedPlayer.rating.truncate()
                )
            }
        }

        list.add(ListItem.Header.Favorites)

        if (favorites.isNullOrEmpty()) {
            list.add(ListItem.Empty.Favorites)
        } else {
            val combinedFavorites = favorites.map { favorite ->
                val rankedPlayerListItem = rankedPlayerList
                        ?.filterIsInstance(ListItem.Player::class.java)
                        ?.firstOrNull { listItem ->
                            AbsPlayer.safeEquals(listItem.player, favorite)
                        }

                ListItem.Player(
                        player = favorite,
                        isIdentity = AbsPlayer.safeEquals(favorite, identity),
                        rawRank = rankedPlayerListItem?.rawRank,
                        previousRank = if (isPreviousRankSupported) {
                            rankedPlayerListItem?.previousRank ?: PreviousRank.INVISIBLE
                        } else {
                            PreviousRank.GONE
                        },
                        rank = rankedPlayerListItem?.rank,
                        rating = rankedPlayerListItem?.rating,
                        regionDisplayName = if (rankedPlayerListItem == null) {
                            favorite.region.displayName
                        } else {
                            null
                        }
                )
            }

            Collections.sort(combinedFavorites, PLAYER_COMPARATOR)
            list.addAll(combinedFavorites)
        }

        if (bundle?.rankingCriteria?.rankingActivityDayLimit != null &&
                bundle.rankingCriteria.rankingNumTourneysAttended != null) {
            list.add(ListItem.ActivityRequirements(
                    rankingActivityDayLimit = bundle.rankingCriteria.rankingActivityDayLimit,
                    rankingNumTourneysAttended = bundle.rankingCriteria.rankingNumTourneysAttended,
                    regionDisplayName = bundle.rankingCriteria.displayName
            ))
        }

        list.add(ListItem.Header.Rankings)

        if (rankedPlayerList == null) {
            list.add(ListItem.Error.Rankings)
        } else if (rankedPlayerList.isEmpty()) {
            list.add(ListItem.Empty.Rankings)
        } else {
            list.addAll(rankedPlayerList)
        }

        insertOrRemoveIdentityAtFrontOfList(
                list = list,
                identity = identity
        )

        return list
    }

    fun fetchRankings(region: Region) {
        state = state.copy(isRefreshing = true)

        disposables.add(Single.zip(favoritePlayersRepository.playersObservable.takeSingle(),
                identityRepository.identityObservable.takeSingle(),
                rankingsRepository.getRankings(region)
                        .map { bundle ->
                            Optional.of(bundle)
                        }
                        .onErrorReturn { throwable ->
                            timber.e(TAG, "Error fetching rankings", throwable)
                            Optional.empty()
                        },
                Function3<List<FavoritePlayer>, Optional<FavoritePlayer>, Optional<RankingsBundle>,
                        Triple<List<FavoritePlayer>, Optional<FavoritePlayer>, Optional<RankingsBundle>>> { t1, t2, t3 ->
                            Triple(t1, t2, t3)
                        })
                .subscribeOn(schedulers.background)
                .observeOn(schedulers.background)
                .subscribe { (favorites, identityOptional, bundleOptional) ->
                    val identity = identityOptional.orNull()
                    val bundle = bundleOptional.orNull()

                    val list = createList(
                            favorites = favorites,
                            identity = identity,
                            bundle = bundle
                    )

                    state = state.copy(
                            hasContent = !bundle?.rankings.isNullOrEmpty() || !favorites.isNullOrEmpty(),
                            isRefreshing = false,
                            list = list,
                            searchResults = null,
                            rankingsBundle = bundle
                    )
                })
    }

    private fun initListeners() {
        disposables.add(Observable.combineLatest(
                favoritePlayersRepository.playersObservable,
                identityRepository.identityObservable,
                smashRosterSyncManager.isSyncingObservable
                        .filter { isSyncing -> !isSyncing },
                Function3<List<FavoritePlayer>, Optional<FavoritePlayer>, Boolean,
                        Pair<List<FavoritePlayer>, Optional<FavoritePlayer>>> { t1, t2, t3 ->
                            Pair(t1, t2)
                        })
                .subscribeOn(schedulers.background)
                .observeOn(schedulers.background)
                .subscribe { (favorites, identityOptional) ->
                    refreshListItems(
                            favorites = favorites,
                            identity = identityOptional.orNull()
                    )
                })
    }

    @WorkerThread
    private fun insertOrRemoveIdentityAtFrontOfList(list: MutableList<ListItem>,
            identity: FavoritePlayer?) {
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
    private fun refreshListItems(favorites: List<FavoritePlayer>?, identity: FavoritePlayer?) {
        val list = refreshListItems(state.list, favorites, identity)
        val searchResults = search(searchQuery, list)

        state = state.copy(
                list = list ?: emptyList(),
                searchResults = searchResults
        )
    }

    @WorkerThread
    private fun refreshListItems(list: List<ListItem>?, favorites: List<FavoritePlayer>?,
            identity: FavoritePlayer?): List<ListItem>? {
        if (list.isNullOrEmpty()) {
            return list
        }

        val newList = mutableListOf<ListItem>()

        list.mapTo(newList) { listItem ->
            if (listItem is ListItem.Player) {
                listItem.copy(isIdentity = listItem.player == identity)
            } else {
                listItem
            }
        }

        val favoritesIndex = newList.indexOfFirst { it is ListItem.Header.Favorites }

        if (favoritesIndex == -1) {
            throw RuntimeException("This should be impossible, the favorites header is" +
                    " not in the list.")
        }

        var activityRequirementsListItem: ListItem.ActivityRequirements? = null
        val activityRequirementsIndex = newList.indexOfFirst { it is ListItem.ActivityRequirements }

        if (activityRequirementsIndex != -1) {
            activityRequirementsListItem = newList[activityRequirementsIndex] as ListItem.ActivityRequirements
        }

        var rankingsIndex = newList.indexOfFirst { it is ListItem.Header.Rankings }

        if (rankingsIndex == -1) {
            throw RuntimeException("This should be impossible, the rankings header is" +
                    " not in the list.")
        }

        do {
            newList.removeAt(favoritesIndex + 1)
            --rankingsIndex
        } while (favoritesIndex + 1 < rankingsIndex)

        if (favorites.isNullOrEmpty()) {
            newList.add(favoritesIndex + 1, ListItem.Empty.Favorites)
            ++rankingsIndex
        } else {
            val isPreviousRankSupported = newList.any { listItem ->
                listItem is ListItem.Player && (
                        listItem.previousRank == PreviousRank.DECREASE ||
                        listItem.previousRank == PreviousRank.INCREASE ||
                        listItem.previousRank == PreviousRank.INVISIBLE
                )
            }

            val rankedPlayerList = newList.filterIsInstance(ListItem.Player::class.java)
            val favoritePlayersList = favorites.map { favorite ->
                val rankedPlayerListItem = rankedPlayerList.firstOrNull { listItem ->
                    listItem.player == favorite
                }

                ListItem.Player(
                        player = favorite,
                        isIdentity = identity == favorite,
                        rawRank = rankedPlayerListItem?.rawRank,
                        previousRank = if (isPreviousRankSupported) {
                            rankedPlayerListItem?.previousRank ?: PreviousRank.INVISIBLE
                        } else {
                            PreviousRank.GONE
                        },
                        rank = rankedPlayerListItem?.rank,
                        rating = rankedPlayerListItem?.rating,
                        regionDisplayName = if (rankedPlayerListItem == null) {
                            favorite.region.displayName
                        } else {
                            null
                        }
                )
            }

            Collections.sort(favoritePlayersList, PLAYER_COMPARATOR)
            newList.addAll(favoritesIndex + 1, favoritePlayersList)
            rankingsIndex += favorites.size
        }

        if (activityRequirementsListItem != null) {
            newList.add(rankingsIndex, activityRequirementsListItem)
        }

        insertOrRemoveIdentityAtFrontOfList(
                list = newList,
                identity = identity
        )

        return newList
    }

    @AnyThread
    private fun search(query: String?) {
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

        val results = mutableListOf<ListItem>()
        val trimmedQuery = query.trim()
        var addedFavoritePlayer = false
        var inFavoritesSection = false
        var addedRankedPlayer = false
        var inRankingsSection = false

        list.mapNotNullTo(results) { listItem ->
            when (listItem) {
                is ListItem.ActivityRequirements -> null
                is ListItem.Empty -> null
                is ListItem.Error -> listItem
                is ListItem.Fetching -> listItem

                is ListItem.Header.Favorites -> {
                    inFavoritesSection = true
                    inRankingsSection = false
                    listItem
                }

                is ListItem.Header.Rankings -> {
                    inFavoritesSection = false
                    inRankingsSection = true
                    listItem
                }

                is ListItem.Identity -> null
                is ListItem.NoResults -> null

                is ListItem.Player -> {
                    if (listItem.player.name.contains(trimmedQuery, ignoreCase = true)) {
                        if (inFavoritesSection) {
                            addedFavoritePlayer = true
                        } else if (inRankingsSection) {
                            addedRankedPlayer = true
                        } else {
                            throw RuntimeException("This should be impossible, we can't have a" +
                                    " player that is not in any section.")
                        }

                        listItem
                    } else {
                        null
                    }
                }
            }
        }

        if (!addedFavoritePlayer) {
            val index = results.indexOfFirst { it is ListItem.Header.Favorites }

            if (index == -1) {
                throw RuntimeException("This should be impossible, the favorites header is not" +
                        " in the list.")
            }

            results.add(index + 1, ListItem.NoResults.Favorites(trimmedQuery))
        }

        if (!addedRankedPlayer) {
            val index = results.indexOfFirst { it is ListItem.Header.Rankings }

            if (index == -1) {
                throw RuntimeException("This should be impossible, the rankings header is not" +
                        " in the list.")
            }

            results.add(index + 1, ListItem.NoResults.Rankings(trimmedQuery))
        }

        return results
    }

    companion object {
        private val PLAYER_COMPARATOR = Comparator<ListItem.Player> { o1, o2 ->
            var result = 0

            if (o1.rawRank != null && o2.rawRank != null) {
                result = Integer.compare(o1.rawRank, o2.rawRank)
            } else if (o1.rawRank != null) {
                result = -1
            } else if (o2.rawRank != null) {
                result = 1
            }

            if (result == 0) {
                result = AbsPlayer.ALPHABETICAL_ORDER.compare(o1.player, o2.player)
            }

            result
        }

        private val NUMBER_FORMAT = NumberFormat.getInstance()
        private const val TAG = "RankingsAndFavoritesViewModel"
    }

    sealed class ListItem {
        abstract val listId: Long

        class ActivityRequirements(
                val rankingActivityDayLimit: Int,
                val rankingNumTourneysAttended: Int,
                val regionDisplayName: String
        ) : ListItem() {
            override val listId: Long = Long.MIN_VALUE + 1L
        }

        sealed class Empty : ListItem() {
            object Favorites : Empty() {
                override val listId: Long = Long.MIN_VALUE + 2L
            }

            object Rankings : Empty() {
                override val listId: Long = Long.MIN_VALUE + 3L
            }
        }

        sealed class Error : ListItem() {
            object Rankings : Error() {
                override val listId: Long = Long.MIN_VALUE + 5L
            }
        }

        sealed class Fetching : ListItem() {
            object Favorites : Fetching() {
                override val listId: Long = Long.MIN_VALUE + 6L
            }

            object Rankings : Fetching() {
                override val listId: Long = Long.MIN_VALUE + 7L
            }
        }

        sealed class Header : ListItem() {
            object Favorites : Header() {
                override val listId: Long = Long.MIN_VALUE + 8L
            }

            object Rankings : Header() {
                override val listId: Long = Long.MIN_VALUE + 9L
            }
        }

        class Identity(
                val player: FavoritePlayer,
                val previousRank: PreviousRank,
                val avatar: String? = null,
                val rank: String? = null,
                val rating: String? = null,
                val tag: String
        ) : ListItem() {
            override val listId: Long = Long.MIN_VALUE + 10L
        }

        sealed class NoResults(
                val query: String
        ) : ListItem() {
            class Favorites(query: String) : NoResults(query) {
                override val listId: Long = Long.MIN_VALUE + 11L
            }

            class Rankings(query: String) : NoResults(query) {
                override val listId: Long = Long.MIN_VALUE + 12L
            }
        }

        data class Player(
                val player: AbsPlayer,
                val isIdentity: Boolean,
                val rawRank: Int? = null,
                val previousRank: PreviousRank,
                val rank: String? = null,
                val rating: String? = null,
                val regionDisplayName: String? = null
        ) : ListItem() {
            override val listId: Long = player.hashCode().toLong()
        }
    }

    data class State(
            val hasContent: Boolean = false,
            val isRefreshing: Boolean = false,
            val list: List<ListItem> = listOf(ListItem.Header.Favorites,
                    ListItem.Fetching.Favorites, ListItem.Header.Rankings,
                    ListItem.Fetching.Rankings),
            val searchResults: List<ListItem>? = null,
            val rankingsBundle: RankingsBundle? = null
    )

}
