package com.garpr.android.features.rankings

import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.LitePlayer
import com.garpr.android.data.models.PreviousRank
import com.garpr.android.data.models.RankedPlayer
import com.garpr.android.data.models.RankingsBundle
import com.garpr.android.data.models.Region
import com.garpr.android.data.models.SimpleDate
import com.garpr.android.features.common.viewModels.BaseViewModelTest
import com.garpr.android.features.player.SmashRosterAvatarUrlHelper
import com.garpr.android.features.rankings.RankingsAndFavoritesViewModel.ListItem
import com.garpr.android.misc.Schedulers
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.misc.Timber
import com.garpr.android.repositories.FavoritePlayersRepository
import com.garpr.android.repositories.IdentityRepository
import com.garpr.android.repositories.RankingsRepository
import com.garpr.android.sync.roster.SmashRosterStorage
import com.garpr.android.sync.roster.SmashRosterSyncManager
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.koin.core.inject
import java.util.Calendar

class RankingsAndFavoritesViewModelTest : BaseViewModelTest() {

    private lateinit var viewModel: RankingsAndFavoritesViewModel

    protected val favoritePlayersRepository: FavoritePlayersRepository by inject()
    protected val identityRepository: IdentityRepository by inject()
    protected val schedulers: Schedulers by inject()
    protected val smashRosterAvatarUrlHelper: SmashRosterAvatarUrlHelper by inject()
    protected val smashRosterStorage: SmashRosterStorage by inject()
    protected val smashRosterSyncManager: SmashRosterSyncManager by inject()
    protected val threadUtils: ThreadUtils by inject()
    protected val timber: Timber by inject()

    @Before
    override fun setUp() {
        super.setUp()

        viewModel = RankingsAndFavoritesViewModel(favoritePlayersRepository, identityRepository,
                RankingsRepositoryOverride(), schedulers, smashRosterAvatarUrlHelper,
                smashRosterStorage, smashRosterSyncManager, threadUtils, timber)
    }

    @Test
    fun testAddAndRemoveFavoritePlayersCausesUpdates() {
        var state: RankingsAndFavoritesViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        assertEquals(4, state?.list?.size)
        assertEquals(ListItem.Header.Favorites, state?.list?.get(0))
        assertEquals(ListItem.Empty.Favorites, state?.list?.get(1))
        assertEquals(ListItem.Header.Rankings, state?.list?.get(2))
        assertEquals(ListItem.Fetching.Rankings, state?.list?.get(3))

        favoritePlayersRepository.addPlayer(IMYT, NORCAL)
        assertEquals(4, state?.list?.size)
        assertEquals(ListItem.Header.Favorites, state?.list?.get(0))

        var player = state?.list?.get(1) as ListItem.Player
        assertFalse(player.isIdentity)
        assertEquals(IMYT, player.player)
        assertNull(player.rawRank)
        assertEquals(PreviousRank.GONE, player.previousRank)
        assertNull(player.rank)
        assertNull(player.rating)
        assertEquals(NORCAL.displayName, player.regionDisplayName)

        assertEquals(ListItem.Header.Rankings, state?.list?.get(2))
        assertEquals(ListItem.Fetching.Rankings, state?.list?.get(3))

        favoritePlayersRepository.addPlayer(SNAP, NORCAL)
        assertEquals(5, state?.list?.size)
        assertEquals(ListItem.Header.Favorites, state?.list?.get(0))

        player = state?.list?.get(1) as ListItem.Player
        assertFalse(player.isIdentity)
        assertEquals(IMYT, player.player)
        assertNull(player.rawRank)
        assertEquals(PreviousRank.GONE, player.previousRank)
        assertNull(player.rank)
        assertNull(player.rating)
        assertEquals(NORCAL.displayName, player.regionDisplayName)

        player = state?.list?.get(2) as ListItem.Player
        assertFalse(player.isIdentity)
        assertEquals(SNAP, player.player)
        assertNull(player.rawRank)
        assertEquals(PreviousRank.GONE, player.previousRank)
        assertNull(player.rank)
        assertNull(player.rating)
        assertEquals(NORCAL.displayName, player.regionDisplayName)

        assertEquals(ListItem.Header.Rankings, state?.list?.get(3))
        assertEquals(ListItem.Fetching.Rankings, state?.list?.get(4))

        favoritePlayersRepository.removePlayer(IMYT, NORCAL)
        assertEquals(4, state?.list?.size)
        assertEquals(ListItem.Header.Favorites, state?.list?.get(0))

        player = state?.list?.get(1) as ListItem.Player
        assertFalse(player.isIdentity)
        assertEquals(SNAP, player.player)
        assertNull(player.rawRank)
        assertEquals(PreviousRank.GONE, player.previousRank)
        assertNull(player.rank)
        assertNull(player.rating)
        assertEquals(NORCAL.displayName, player.regionDisplayName)

        assertEquals(ListItem.Header.Rankings, state?.list?.get(2))
        assertEquals(ListItem.Fetching.Rankings, state?.list?.get(3))
    }

    @Test
    fun testAddFavoritePlayerCausesUpdate() {
        var state: RankingsAndFavoritesViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        assertEquals(4, state?.list?.size)
        assertEquals(ListItem.Header.Favorites, state?.list?.get(0))
        assertEquals(ListItem.Empty.Favorites, state?.list?.get(1))
        assertEquals(ListItem.Header.Rankings, state?.list?.get(2))
        assertEquals(ListItem.Fetching.Rankings, state?.list?.get(3))

        favoritePlayersRepository.addPlayer(IBDW, NYC)
        favoritePlayersRepository.addPlayer(MIKKUZ, NORCAL)
        assertEquals(5, state?.list?.size)
        assertEquals(ListItem.Header.Favorites, state?.list?.get(0))

        var player = state?.list?.get(1) as ListItem.Player
        assertEquals(MIKKUZ, player.player)
        assertFalse(player.isIdentity)
        assertNull(player.rawRank)
        assertEquals(PreviousRank.GONE, player.previousRank)
        assertNull(player.rank)
        assertNull(player.rating)
        assertEquals(NORCAL.displayName, player.regionDisplayName)

        player = state?.list?.get(2) as ListItem.Player
        assertEquals(IBDW, player.player)
        assertFalse(player.isIdentity)
        assertNull(player.rawRank)
        assertEquals(PreviousRank.GONE, player.previousRank)
        assertNull(player.rank)
        assertNull(player.rating)
        assertEquals(NYC.displayName, player.regionDisplayName)

        assertEquals(ListItem.Header.Rankings, state?.list?.get(3))
        assertEquals(ListItem.Fetching.Rankings, state?.list?.get(4))

        identityRepository.setIdentity(CHARLEZARD, NORCAL)
        assertEquals(6, state?.list?.size)

        val identity = state?.list?.get(0) as ListItem.Identity
        assertEquals(CHARLEZARD, identity.player)
        assertEquals(PreviousRank.GONE, identity.previousRank)
        assertNull(identity.avatar)
        assertNull(identity.rank)
        assertNull(identity.rating)
        assertEquals(CHARLEZARD.name, identity.tag)

        assertEquals(ListItem.Header.Favorites, state?.list?.get(1))

        player = state?.list?.get(2) as ListItem.Player
        assertEquals(MIKKUZ, player.player)
        assertFalse(player.isIdentity)
        assertNull(player.rawRank)
        assertEquals(PreviousRank.GONE, player.previousRank)
        assertNull(player.rank)
        assertNull(player.rating)
        assertEquals(NORCAL.displayName, player.regionDisplayName)

        player = state?.list?.get(3) as ListItem.Player
        assertEquals(IBDW, player.player)
        assertFalse(player.isIdentity)
        assertNull(player.rawRank)
        assertEquals(PreviousRank.GONE, player.previousRank)
        assertNull(player.rank)
        assertNull(player.rating)
        assertEquals(NYC.displayName, player.regionDisplayName)

        assertEquals(ListItem.Header.Rankings, state?.list?.get(4))
        assertEquals(ListItem.Fetching.Rankings, state?.list?.get(5))
    }

    @Test
    fun testFetchRankingsWithNorcal() {
        val states = mutableListOf<RankingsAndFavoritesViewModel.State>()

        viewModel.stateLiveData.observeForever {
            states.add(it)
        }

        assertEquals(1, states.size)
        assertEquals(false, states[0].hasContent)
        assertEquals(false, states[0].isRefreshing)
        assertEquals(4, states[0].list.size)
        assertEquals(ListItem.Header.Favorites, states[0].list[0])
        assertEquals(ListItem.Empty.Favorites, states[0].list[1])
        assertEquals(ListItem.Header.Rankings, states[0].list[2])
        assertEquals(ListItem.Fetching.Rankings, states[0].list[3])
        assertNull(states[0].searchResults)
        assertNull(states[0].rankingsBundle)

        viewModel.fetchRankings(NORCAL)
        assertEquals(3, states.size)
        assertEquals(false, states[1].hasContent)
        assertEquals(true, states[1].isRefreshing)
        assertEquals(4, states[1].list.size)
        assertEquals(ListItem.Header.Favorites, states[1].list[0])
        assertEquals(ListItem.Empty.Favorites, states[1].list[1])
        assertEquals(ListItem.Header.Rankings, states[1].list[2])
        assertEquals(ListItem.Fetching.Rankings, states[1].list[3])
        assertNull(states[1].searchResults)
        assertNull(states[1].rankingsBundle)

        assertEquals(true, states[2].hasContent)
        assertEquals(false, states[2].isRefreshing)
        assertEquals(9, states[2].list.size)
        assertEquals(ListItem.Header.Favorites, states[2].list[0])
        assertEquals(ListItem.Empty.Favorites, states[2].list[1])

        val activityRequirements = states[2].list[2] as ListItem.ActivityRequirements
        assertEquals(NORCAL.rankingActivityDayLimit, activityRequirements.rankingActivityDayLimit)
        assertEquals(NORCAL.rankingNumTourneysAttended, activityRequirements.rankingNumTourneysAttended)

        assertEquals(ListItem.Header.Rankings, states[2].list[3])

        var player = states[2].list[4] as ListItem.Player
        assertEquals(NMW, player.player)
        assertFalse(player.isIdentity)
        assertEquals(NMW.rank, player.rawRank)
        assertEquals(PreviousRank.INVISIBLE, player.previousRank)
        assertFalse(player.rank.isNullOrBlank())
        assertFalse(player.rating.isNullOrBlank())
        assertNull(player.regionDisplayName)

        player = states[2].list[5] as ListItem.Player
        assertEquals(AZEL, player.player)
        assertFalse(player.isIdentity)
        assertEquals(AZEL.rank, player.rawRank)
        assertEquals(PreviousRank.INVISIBLE, player.previousRank)
        assertFalse(player.rank.isNullOrBlank())
        assertFalse(player.rating.isNullOrBlank())
        assertNull(player.regionDisplayName)

        player = states[2].list[6] as ListItem.Player
        assertEquals(UMARTH, player.player)
        assertFalse(player.isIdentity)
        assertEquals(UMARTH.rank, player.rawRank)
        assertEquals(PreviousRank.INVISIBLE, player.previousRank)
        assertFalse(player.rank.isNullOrBlank())
        assertFalse(player.rating.isNullOrBlank())
        assertNull(player.regionDisplayName)

        player = states[2].list[7] as ListItem.Player
        assertEquals(YCZ6, player.player)
        assertFalse(player.isIdentity)
        assertEquals(YCZ6.rank, player.rawRank)
        assertEquals(PreviousRank.DECREASE, player.previousRank)
        assertFalse(player.rank.isNullOrBlank())
        assertFalse(player.rating.isNullOrBlank())
        assertNull(player.regionDisplayName)

        player = states[2].list[8] as ListItem.Player
        assertEquals(SNAP, player.player)
        assertFalse(player.isIdentity)
        assertEquals(SNAP.rank, player.rawRank)
        assertEquals(PreviousRank.INCREASE, player.previousRank)
        assertFalse(player.rank.isNullOrBlank())
        assertFalse(player.rating.isNullOrBlank())
        assertNull(player.regionDisplayName)

        assertNull(states[2].searchResults)
        assertEquals(NORCAL_RANKINGS_BUNDLE, states[2].rankingsBundle)
    }

    @Test
    fun testFetchRankingsWithGoogleMtvAndCharlezardAsIdentityAndSomeFavorites() {
        favoritePlayersRepository.addPlayer(CHARLEZARD, NORCAL)
        favoritePlayersRepository.addPlayer(IMYT, NORCAL)
        favoritePlayersRepository.addPlayer(SNAP, NORCAL)
        identityRepository.setIdentity(CHARLEZARD, NORCAL)

        val states = mutableListOf<RankingsAndFavoritesViewModel.State>()

        viewModel.stateLiveData.observeForever {
            states.add(it)
        }

        assertEquals(1, states.size)
        assertFalse(states[0].hasContent)
        assertFalse(states[0].isRefreshing)
        assertEquals(7, states[0].list.size)

        var identity = states[0].list[0] as ListItem.Identity
        assertEquals(CHARLEZARD, identity.player)
        assertEquals(PreviousRank.GONE, identity.previousRank)
        assertNull(identity.avatar)
        assertNull(identity.rank)
        assertNull(identity.rating)
        assertEquals(CHARLEZARD.name, identity.tag)

        assertEquals(ListItem.Header.Favorites, states[0].list[1])

        var player = states[0].list[2] as ListItem.Player
        assertEquals(CHARLEZARD, player.player)
        assertTrue(player.isIdentity)
        assertNull(player.rawRank)
        assertEquals(PreviousRank.GONE, player.previousRank)
        assertNull(player.rank)
        assertNull(player.rating)
        assertEquals(NORCAL.displayName, player.regionDisplayName)

        player = states[0].list[3] as ListItem.Player
        assertEquals(IMYT, player.player)
        assertFalse(player.isIdentity)
        assertNull(player.rawRank)
        assertEquals(PreviousRank.GONE, player.previousRank)
        assertNull(player.rank)
        assertNull(player.rating)
        assertEquals(NORCAL.displayName, player.regionDisplayName)

        player = states[0].list[4] as ListItem.Player
        assertEquals(SNAP, player.player)
        assertFalse(player.isIdentity)
        assertNull(player.rawRank)
        assertEquals(PreviousRank.GONE, player.previousRank)
        assertNull(player.rank)
        assertNull(player.rating)
        assertEquals(NORCAL.displayName, player.regionDisplayName)

        assertEquals(ListItem.Header.Rankings, states[0].list[5])
        assertEquals(ListItem.Fetching.Rankings, states[0].list[6])
        assertNull(states[0].searchResults)
        assertNull(states[0].rankingsBundle)

        viewModel.fetchRankings(GOOGLE_MTV)
        assertEquals(3, states.size)
        assertFalse(states[1].hasContent)
        assertTrue(states[1].isRefreshing)
        assertEquals(7, states[1].list.size)

        identity = states[1].list[0] as ListItem.Identity
        assertEquals(CHARLEZARD, identity.player)
        assertEquals(PreviousRank.GONE, identity.previousRank)
        assertNull(identity.avatar)
        assertNull(identity.rank)
        assertNull(identity.rating)
        assertEquals(CHARLEZARD.name, identity.tag)

        assertEquals(ListItem.Header.Favorites, states[1].list[1])

        player = states[1].list[2] as ListItem.Player
        assertEquals(CHARLEZARD, player.player)
        assertTrue(player.isIdentity)
        assertNull(player.rawRank)
        assertEquals(PreviousRank.GONE, player.previousRank)
        assertNull(player.rank)
        assertNull(player.rating)
        assertEquals(NORCAL.displayName, player.regionDisplayName)

        player = states[1].list[3] as ListItem.Player
        assertEquals(IMYT, player.player)
        assertFalse(player.isIdentity)
        assertNull(player.rawRank)
        assertEquals(PreviousRank.GONE, player.previousRank)
        assertNull(player.rank)
        assertNull(player.rating)
        assertEquals(NORCAL.displayName, player.regionDisplayName)

        player = states[1].list[4] as ListItem.Player
        assertEquals(SNAP, player.player)
        assertFalse(player.isIdentity)
        assertNull(player.rawRank)
        assertEquals(PreviousRank.GONE, player.previousRank)
        assertNull(player.rank)
        assertNull(player.rating)
        assertEquals(NORCAL.displayName, player.regionDisplayName)

        assertEquals(ListItem.Header.Rankings, states[1].list[5])
        assertEquals(ListItem.Fetching.Rankings, states[1].list[6])
        assertNull(states[1].searchResults)
        assertNull(states[1].rankingsBundle)

        assertTrue(states[2].hasContent)
        assertFalse(states[2].isRefreshing)
        assertEquals(7, states[2].list.size)

        identity = states[2].list[0] as ListItem.Identity
        assertEquals(CHARLEZARD, identity.player)
        assertEquals(PreviousRank.GONE, identity.previousRank)
        assertNull(identity.avatar)
        assertNull(identity.rank)
        assertNull(identity.rating)
        assertEquals(CHARLEZARD.name, identity.tag)

        assertEquals(ListItem.Header.Favorites, states[2].list[1])

        player = states[2].list[2] as ListItem.Player
        assertEquals(CHARLEZARD, player.player)
        assertTrue(player.isIdentity)
        assertNull(player.rawRank)
        assertEquals(PreviousRank.GONE, player.previousRank)
        assertNull(player.rank)
        assertNull(player.rating)
        assertEquals(NORCAL.displayName, player.regionDisplayName)

        player = states[2].list[3] as ListItem.Player
        assertEquals(IMYT, player.player)
        assertFalse(player.isIdentity)
        assertNull(player.rawRank)
        assertEquals(PreviousRank.GONE, player.previousRank)
        assertNull(player.rank)
        assertNull(player.rating)
        assertEquals(NORCAL.displayName, player.regionDisplayName)

        player = states[2].list[4] as ListItem.Player
        assertEquals(SNAP, player.player)
        assertFalse(player.isIdentity)
        assertNull(player.rawRank)
        assertEquals(PreviousRank.GONE, player.previousRank)
        assertNull(player.rank)
        assertNull(player.rating)
        assertEquals(NORCAL.displayName, player.regionDisplayName)

        assertEquals(ListItem.Header.Rankings, states[2].list[5])
        assertEquals(ListItem.Empty.Rankings, states[2].list[6])
        assertNull(states[2].searchResults)
        assertEquals(GOOGLE_MTV_RANKINGS_BUNDLE, states[2].rankingsBundle)
    }

    @Test
    fun testFetchRankingsWithNycAndHaxAsIdentity() {
        identityRepository.setIdentity(HAX, NYC)

        var state: RankingsAndFavoritesViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        viewModel.fetchRankings(NYC)
        assertEquals(false, state?.isRefreshing)
        assertEquals(true, state?.hasContent)
        assertEquals(7, state?.list?.size)

        val identity = state?.list?.get(0) as ListItem.Identity
        assertEquals(HAX, identity.player)
        assertEquals(PreviousRank.GONE, identity.previousRank)
        assertNull(identity.avatar)
        assertFalse(identity.rank.isNullOrBlank())
        assertFalse(identity.rating.isNullOrBlank())
        assertEquals(HAX.name, identity.tag)

        assertEquals(ListItem.Header.Favorites, state?.list?.get(1))
        assertEquals(ListItem.Empty.Favorites, state?.list?.get(2))
        assertEquals(ListItem.Header.Rankings, state?.list?.get(3))

        var player = state?.list?.get(4) as ListItem.Player
        assertEquals(HAX, player.player)
        assertTrue(player.isIdentity)
        assertEquals(HAX.rank, player.rawRank)
        assertEquals(PreviousRank.GONE, player.previousRank)
        assertFalse(player.rank.isNullOrBlank())
        assertFalse(player.rating.isNullOrBlank())
        assertNull(player.regionDisplayName)

        player = state?.list?.get(5) as ListItem.Player
        assertEquals(IBDW, player.player)
        assertFalse(player.isIdentity)
        assertEquals(IBDW.rank, player.rawRank)
        assertEquals(PreviousRank.GONE, player.previousRank)
        assertFalse(player.rank.isNullOrBlank())
        assertFalse(player.rating.isNullOrBlank())
        assertNull(player.regionDisplayName)

        player = state?.list?.get(6) as ListItem.Player
        assertEquals(RISHI, player.player)
        assertFalse(player.isIdentity)
        assertEquals(RISHI.rank, player.rawRank)
        assertEquals(PreviousRank.GONE, player.previousRank)
        assertFalse(player.rank.isNullOrBlank())
        assertFalse(player.rating.isNullOrBlank())
        assertNull(player.regionDisplayName)

        assertNull(state?.searchResults)
        assertEquals(NYC_RANKINGS_BUNDLE, state?.rankingsBundle)
    }

    @Test
    fun testInitialState() {
        var state: RankingsAndFavoritesViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        assertEquals(false, state?.isRefreshing)
        assertEquals(false, state?.hasContent)
        assertEquals(4, state?.list?.size)
        assertEquals(ListItem.Header.Favorites, state?.list?.get(0))
        assertEquals(ListItem.Empty.Favorites, state?.list?.get(1))
        assertEquals(ListItem.Header.Rankings, state?.list?.get(2))
        assertEquals(ListItem.Fetching.Rankings, state?.list?.get(3))
        assertNull(state?.searchResults)
        assertNull(state?.rankingsBundle)
    }

    @Test
    fun testSearchNorcalWithA() {
        var state: RankingsAndFavoritesViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        viewModel.fetchRankings(NORCAL)
        viewModel.searchQuery = "a"
        assertEquals(9, state?.list?.size)
        assertEquals(6, state?.searchResults?.size)
        assertEquals(ListItem.Header.Favorites, state?.searchResults?.get(0))

        val noResults = state?.searchResults?.get(1) as ListItem.NoResults
        assertEquals("a", noResults.query)

        assertEquals(ListItem.Header.Rankings, state?.searchResults?.get(2))

        var player = state?.searchResults?.get(3) as ListItem.Player
        assertEquals(AZEL, player.player)
        assertFalse(player.isIdentity)
        assertEquals(AZEL.rank, player.rawRank)
        assertEquals(PreviousRank.INVISIBLE, player.previousRank)
        assertFalse(player.rank.isNullOrBlank())
        assertFalse(player.rating.isNullOrBlank())
        assertNull(player.regionDisplayName)

        player = state?.searchResults?.get(4) as ListItem.Player
        assertEquals(UMARTH, player.player)
        assertFalse(player.isIdentity)
        assertEquals(UMARTH.rank, player.rawRank)
        assertEquals(PreviousRank.INVISIBLE, player.previousRank)
        assertFalse(player.rank.isNullOrBlank())
        assertFalse(player.rating.isNullOrBlank())
        assertNull(player.regionDisplayName)

        player = state?.searchResults?.get(5) as ListItem.Player
        assertEquals(SNAP, player.player)
        assertFalse(player.isIdentity)
        assertEquals(SNAP.rank, player.rawRank)
        assertEquals(PreviousRank.INCREASE, player.previousRank)
        assertFalse(player.rank.isNullOrBlank())
        assertFalse(player.rating.isNullOrBlank())
        assertNull(player.regionDisplayName)

        assertEquals(NORCAL_RANKINGS_BUNDLE, state?.rankingsBundle)
    }

    @Test
    fun testSearchNorcalWithBlankString() {
        var state: RankingsAndFavoritesViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        viewModel.fetchRankings(NORCAL)
        viewModel.searchQuery = " "
        assertEquals(9, state?.list?.size)
        assertEquals(ListItem.Header.Favorites, state?.list?.get(0))
        assertEquals(ListItem.Empty.Favorites, state?.list?.get(1))

        val activityRequirements = state?.list?.get(2) as ListItem.ActivityRequirements
        assertEquals(NORCAL.rankingActivityDayLimit, activityRequirements.rankingActivityDayLimit)
        assertEquals(NORCAL.rankingNumTourneysAttended, activityRequirements.rankingNumTourneysAttended)
        assertEquals(NORCAL.displayName, activityRequirements.regionDisplayName)

        assertEquals(ListItem.Header.Rankings, state?.list?.get(3))

        var player = state?.list?.get(4) as ListItem.Player
        assertEquals(NMW, player.player)
        assertFalse(player.isIdentity)
        assertEquals(NMW.rank, player.rawRank)
        assertEquals(PreviousRank.INVISIBLE, player.previousRank)
        assertFalse(player.rank.isNullOrBlank())
        assertFalse(player.rating.isNullOrBlank())
        assertNull(player.regionDisplayName)

        player = state?.list?.get(5) as ListItem.Player
        assertEquals(AZEL, player.player)
        assertFalse(player.isIdentity)
        assertEquals(AZEL.rank, player.rawRank)
        assertEquals(PreviousRank.INVISIBLE, player.previousRank)
        assertFalse(player.rank.isNullOrBlank())
        assertFalse(player.rating.isNullOrBlank())
        assertNull(player.regionDisplayName)

        player = state?.list?.get(6) as ListItem.Player
        assertEquals(UMARTH, player.player)
        assertFalse(player.isIdentity)
        assertEquals(UMARTH.rank, player.rawRank)
        assertEquals(PreviousRank.INVISIBLE, player.previousRank)
        assertFalse(player.rank.isNullOrBlank())
        assertFalse(player.rating.isNullOrBlank())
        assertNull(player.regionDisplayName)

        player = state?.list?.get(7) as ListItem.Player
        assertEquals(YCZ6, player.player)
        assertFalse(player.isIdentity)
        assertEquals(YCZ6.rank, player.rawRank)
        assertEquals(PreviousRank.DECREASE, player.previousRank)
        assertFalse(player.rank.isNullOrBlank())
        assertFalse(player.rating.isNullOrBlank())
        assertNull(player.regionDisplayName)

        player = state?.list?.get(8) as ListItem.Player
        assertEquals(SNAP, player.player)
        assertFalse(player.isIdentity)
        assertEquals(SNAP.rank, player.rawRank)
        assertEquals(PreviousRank.INCREASE, player.previousRank)
        assertFalse(player.rank.isNullOrBlank())
        assertFalse(player.rating.isNullOrBlank())
        assertNull(player.regionDisplayName)

        assertNull(state?.searchResults)
        assertEquals(NORCAL_RANKINGS_BUNDLE, state?.rankingsBundle)
    }

    @Test
    fun testSearchNorcalWithEmptyString() {
        var state: RankingsAndFavoritesViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        viewModel.fetchRankings(NORCAL)
        viewModel.searchQuery = ""
        assertEquals(true, state?.hasContent)
        assertEquals(false, state?.isRefreshing)
        assertEquals(9, state?.list?.size)
        assertEquals(ListItem.Header.Favorites, state?.list?.get(0))
        assertEquals(ListItem.Empty.Favorites, state?.list?.get(1))

        val activityRequirements = state?.list?.get(2) as ListItem.ActivityRequirements
        assertEquals(NORCAL.rankingActivityDayLimit, activityRequirements.rankingActivityDayLimit)
        assertEquals(NORCAL.rankingNumTourneysAttended, activityRequirements.rankingNumTourneysAttended)
        assertEquals(NORCAL.displayName, activityRequirements.regionDisplayName)

        assertEquals(ListItem.Header.Rankings, state?.list?.get(3))

        var player = state?.list?.get(4) as ListItem.Player
        assertEquals(NMW, player.player)
        assertFalse(player.isIdentity)
        assertEquals(NMW.rank, player.rawRank)
        assertEquals(PreviousRank.INVISIBLE, player.previousRank)
        assertFalse(player.rank.isNullOrBlank())
        assertFalse(player.rating.isNullOrBlank())
        assertNull(player.regionDisplayName)

        player = state?.list?.get(5) as ListItem.Player
        assertEquals(AZEL, player.player)
        assertFalse(player.isIdentity)
        assertEquals(AZEL.rank, player.rawRank)
        assertEquals(PreviousRank.INVISIBLE, player.previousRank)
        assertFalse(player.rank.isNullOrBlank())
        assertFalse(player.rating.isNullOrBlank())
        assertNull(player.regionDisplayName)

        player = state?.list?.get(6) as ListItem.Player
        assertEquals(UMARTH, player.player)
        assertFalse(player.isIdentity)
        assertEquals(UMARTH.rank, player.rawRank)
        assertEquals(PreviousRank.INVISIBLE, player.previousRank)
        assertFalse(player.rank.isNullOrBlank())
        assertFalse(player.rating.isNullOrBlank())
        assertNull(player.regionDisplayName)

        player = state?.list?.get(7) as ListItem.Player
        assertEquals(YCZ6, player.player)
        assertFalse(player.isIdentity)
        assertEquals(YCZ6.rank, player.rawRank)
        assertEquals(PreviousRank.DECREASE, player.previousRank)
        assertFalse(player.rank.isNullOrBlank())
        assertFalse(player.rating.isNullOrBlank())
        assertNull(player.regionDisplayName)

        player = state?.list?.get(8) as ListItem.Player
        assertEquals(SNAP, player.player)
        assertFalse(player.isIdentity)
        assertEquals(SNAP.rank, player.rawRank)
        assertEquals(PreviousRank.INCREASE, player.previousRank)
        assertFalse(player.rank.isNullOrBlank())
        assertFalse(player.rating.isNullOrBlank())
        assertNull(player.regionDisplayName)

        assertNull(state?.searchResults)
        assertEquals(NORCAL_RANKINGS_BUNDLE, state?.rankingsBundle)
    }

    @Test
    fun testSearchNorcalWithZAndCharlezardAsIdentityAndSomeFavoritesAndThenRemoveIdentity() {
        favoritePlayersRepository.addPlayer(AZEL, NORCAL)
        favoritePlayersRepository.addPlayer(CHARLEZARD, NORCAL)
        favoritePlayersRepository.addPlayer(HAX, NYC)
        favoritePlayersRepository.addPlayer(MIKKUZ, NORCAL)
        favoritePlayersRepository.addPlayer(UMARTH, NORCAL)
        identityRepository.setIdentity(CHARLEZARD, NORCAL)

        var state: RankingsAndFavoritesViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        viewModel.fetchRankings(NORCAL)
        viewModel.searchQuery = "z"
        assertEquals(14, state?.list?.size)
        assertEquals(7, state?.searchResults?.size)
        assertEquals(ListItem.Header.Favorites, state?.searchResults?.get(0))

        var player = state?.searchResults?.get(1) as ListItem.Player
        assertEquals(AZEL, player.player)
        assertFalse(player.isIdentity)
        assertEquals(AZEL.rank, player.rawRank)
        assertEquals(PreviousRank.INVISIBLE, player.previousRank)
        assertFalse(player.rank.isNullOrBlank())
        assertFalse(player.rating.isNullOrBlank())
        assertNull(player.regionDisplayName)

        player = state?.searchResults?.get(2) as ListItem.Player
        assertEquals(CHARLEZARD, player.player)
        assertTrue(player.isIdentity)
        assertNull(player.rawRank)
        assertEquals(PreviousRank.INVISIBLE, player.previousRank)
        assertNull(player.rank)
        assertNull(player.rating)
        assertEquals(NORCAL.displayName, player.regionDisplayName)

        player = state?.searchResults?.get(3) as ListItem.Player
        assertEquals(MIKKUZ, player.player)
        assertFalse(player.isIdentity)
        assertNull(player.rawRank)
        assertEquals(PreviousRank.INVISIBLE, player.previousRank)
        assertNull(player.rank)
        assertNull(player.rating)
        assertEquals(NORCAL.displayName, player.regionDisplayName)

        assertEquals(ListItem.Header.Rankings, state?.searchResults?.get(4))

        player = state?.searchResults?.get(5) as ListItem.Player
        assertEquals(AZEL, player.player)
        assertFalse(player.isIdentity)
        assertEquals(AZEL.rank, player.rawRank)
        assertEquals(PreviousRank.INVISIBLE, player.previousRank)
        assertFalse(player.rank.isNullOrBlank())
        assertFalse(player.rating.isNullOrBlank())
        assertNull(player.regionDisplayName)

        player = state?.searchResults?.get(6) as ListItem.Player
        assertEquals(YCZ6, player.player)
        assertFalse(player.isIdentity)
        assertEquals(YCZ6.rank, player.rawRank)
        assertEquals(PreviousRank.DECREASE, player.previousRank)
        assertFalse(player.rank.isNullOrBlank())
        assertFalse(player.rating.isNullOrBlank())
        assertNull(player.regionDisplayName)

        assertEquals(NORCAL_RANKINGS_BUNDLE, state?.rankingsBundle)

        identityRepository.removeIdentity()
        assertEquals(13, state?.list?.size)
        assertEquals(7, state?.searchResults?.size)
        assertEquals(ListItem.Header.Favorites, state?.searchResults?.get(0))

        player = state?.searchResults?.get(1) as ListItem.Player
        assertEquals(AZEL, player.player)
        assertFalse(player.isIdentity)
        assertEquals(AZEL.rank, player.rawRank)
        assertEquals(PreviousRank.INVISIBLE, player.previousRank)
        assertFalse(player.rank.isNullOrBlank())
        assertFalse(player.rating.isNullOrBlank())
        assertNull(player.regionDisplayName)

        player = state?.searchResults?.get(2) as ListItem.Player
        assertEquals(CHARLEZARD, player.player)
        assertFalse(player.isIdentity)
        assertNull(player.rawRank)
        assertEquals(PreviousRank.INVISIBLE, player.previousRank)
        assertNull(player.rank)
        assertNull(player.rating)
        assertEquals(NORCAL.displayName, player.regionDisplayName)

        player = state?.searchResults?.get(3) as ListItem.Player
        assertEquals(MIKKUZ, player.player)
        assertFalse(player.isIdentity)
        assertNull(player.rawRank)
        assertEquals(PreviousRank.INVISIBLE, player.previousRank)
        assertNull(player.rank)
        assertNull(player.rating)
        assertEquals(NORCAL.displayName, player.regionDisplayName)

        assertEquals(ListItem.Header.Rankings, state?.searchResults?.get(4))

        player = state?.searchResults?.get(5) as ListItem.Player
        assertEquals(AZEL, player.player)
        assertFalse(player.isIdentity)
        assertEquals(AZEL.rank, player.rawRank)
        assertEquals(PreviousRank.INVISIBLE, player.previousRank)
        assertFalse(player.rank.isNullOrBlank())
        assertFalse(player.rating.isNullOrBlank())
        assertNull(player.regionDisplayName)

        player = state?.searchResults?.get(6) as ListItem.Player
        assertEquals(YCZ6, player.player)
        assertFalse(player.isIdentity)
        assertEquals(YCZ6.rank, player.rawRank)
        assertEquals(PreviousRank.DECREASE, player.previousRank)
        assertFalse(player.rank.isNullOrBlank())
        assertFalse(player.rating.isNullOrBlank())
        assertNull(player.regionDisplayName)

        assertEquals(NORCAL_RANKINGS_BUNDLE, state?.rankingsBundle)
    }

    @Test
    fun testSearchNorcalWithNullString() {
        var state: RankingsAndFavoritesViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        viewModel.fetchRankings(NORCAL)
        viewModel.searchQuery = null
        assertEquals(true, state?.hasContent)
        assertEquals(false, state?.isRefreshing)
        assertEquals(9, state?.list?.size)
        assertEquals(ListItem.Header.Favorites, state?.list?.get(0))
        assertEquals(ListItem.Empty.Favorites, state?.list?.get(1))

        val activityRequirements = state?.list?.get(2) as ListItem.ActivityRequirements
        assertEquals(NORCAL.rankingActivityDayLimit, activityRequirements.rankingActivityDayLimit)
        assertEquals(NORCAL.rankingNumTourneysAttended, activityRequirements.rankingNumTourneysAttended)
        assertEquals(NORCAL.displayName, activityRequirements.regionDisplayName)

        assertEquals(ListItem.Header.Rankings, state?.list?.get(3))

        var player = state?.list?.get(4) as ListItem.Player
        assertEquals(NMW, player.player)
        assertFalse(player.isIdentity)
        assertEquals(NMW.rank, player.rawRank)
        assertEquals(PreviousRank.INVISIBLE, player.previousRank)
        assertFalse(player.rank.isNullOrBlank())
        assertFalse(player.rating.isNullOrBlank())
        assertNull(player.regionDisplayName)

        player = state?.list?.get(5) as ListItem.Player
        assertEquals(AZEL, player.player)
        assertFalse(player.isIdentity)
        assertEquals(AZEL.rank, player.rawRank)
        assertEquals(PreviousRank.INVISIBLE, player.previousRank)
        assertFalse(player.rank.isNullOrBlank())
        assertFalse(player.rating.isNullOrBlank())
        assertNull(player.regionDisplayName)

        player = state?.list?.get(6) as ListItem.Player
        assertEquals(UMARTH, player.player)
        assertFalse(player.isIdentity)
        assertEquals(UMARTH.rank, player.rawRank)
        assertEquals(PreviousRank.INVISIBLE, player.previousRank)
        assertFalse(player.rank.isNullOrBlank())
        assertFalse(player.rating.isNullOrBlank())
        assertNull(player.regionDisplayName)

        player = state?.list?.get(7) as ListItem.Player
        assertEquals(YCZ6, player.player)
        assertFalse(player.isIdentity)
        assertEquals(YCZ6.rank, player.rawRank)
        assertEquals(PreviousRank.DECREASE, player.previousRank)
        assertFalse(player.rank.isNullOrBlank())
        assertFalse(player.rating.isNullOrBlank())
        assertNull(player.regionDisplayName)

        player = state?.list?.get(8) as ListItem.Player
        assertEquals(SNAP, player.player)
        assertFalse(player.isIdentity)
        assertEquals(SNAP.rank, player.rawRank)
        assertEquals(PreviousRank.INCREASE, player.previousRank)
        assertFalse(player.rank.isNullOrBlank())
        assertFalse(player.rating.isNullOrBlank())
        assertNull(player.regionDisplayName)

        assertNull(state?.searchResults)
        assertEquals(NORCAL_RANKINGS_BUNDLE, state?.rankingsBundle)
    }

    @Test
    fun testSearchNorcalWithWaduAndImytAsIdentity() {
        identityRepository.setIdentity(IMYT, NORCAL)

        var state: RankingsAndFavoritesViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        viewModel.fetchRankings(NORCAL)
        viewModel.searchQuery = "wadu"
        assertEquals(10, state?.list?.size)
        assertEquals(4, state?.searchResults?.size)
        assertEquals(ListItem.Header.Favorites, state?.searchResults?.get(0))

        var noResults = state?.searchResults?.get(1) as ListItem.NoResults
        assertEquals("wadu", noResults.query)

        assertEquals(ListItem.Header.Rankings, state?.searchResults?.get(2))

        noResults = state?.searchResults?.get(3) as ListItem.NoResults
        assertEquals("wadu", noResults.query)

        assertEquals(NORCAL_RANKINGS_BUNDLE, state?.rankingsBundle)
    }

    companion object {
        private val AZEL = RankedPlayer(
                id = "588852e8d2994e3bbfa52d9f",
                name = "Azel",
                rating = 42.30158803535407f,
                rank = 2,
                previousRank = 2
        )

        private val CHARLEZARD = LitePlayer(
                id = "587a951dd2994e15c7dea9fe",
                name = "Charlezard"
        )

        private val IMYT = LitePlayer(
                id = "5877eb55d2994e15c7dea98b",
                name = "Imyt"
        )

        private val IBDW = RankedPlayer(
                id = "57470593e59257583a0756db",
                name = "iBDW",
                rating = 41.98998559382721f,
                rank = 2
        )

        private val HAX = RankedPlayer(
                id = "53c64dba8ab65f6e6651f7bc",
                name = "Hax",
                rating = 42.71520580698563f,
                rank = 1
        )

        private val NMW = RankedPlayer(
                id = "5e2427d5d2994e6bf4d676eb",
                name = "NMW",
                rating = 42.556818424504314f,
                rank = 1,
                previousRank = 1
        )

        private val RISHI = RankedPlayer(
                id = "5778339fe592575dfd89bd0e",
                name = "Rishi",
                rating = 41.55162805506685f,
                rank = 3
        )

        private val MIKKUZ = LitePlayer(
                id = "583a4a15d2994e0577b05c74",
                name = "mikkuz"
        )

        private val SNAP = RankedPlayer(
                id = "59213f1ad2994e1d79144956",
                name = "Snap",
                rating = 33.59832158713955f,
                rank = 18,
                previousRank = 20
        )

        private val UMARTH = RankedPlayer(
                id = "5877eb55d2994e15c7dea977",
                name = "Umarth",
                rating = 40.90797800656901f,
                rank = 3,
                previousRank = 3
        )

        private val YCZ6 = RankedPlayer(
                id = "5888542ad2994e3bbfa52de4",
                name = "ycz6",
                rating = 35.02962221777487f,
                rank = 12,
                previousRank = 11
        )

        private val GOOGLE_MTV = Region(
                displayName = "Google MTV",
                id = "googlemtv",
                endpoint = Endpoint.GAR_PR
        )

        private val NORCAL = Region(
                rankingActivityDayLimit = 60,
                rankingNumTourneysAttended = 2,
                displayName = "Norcal",
                id = "norcal",
                endpoint = Endpoint.GAR_PR
        )

        private val NYC = Region(
                displayName = "New York City",
                id = "nyc",
                endpoint = Endpoint.NOT_GAR_PR
        )

        private val GOOGLE_MTV_RANKINGS_BUNDLE = RankingsBundle(
                rankingCriteria = GOOGLE_MTV,
                time = with(Calendar.getInstance()) {
                    clear()
                    set(Calendar.YEAR, 2017)
                    set(Calendar.MONTH, Calendar.MAY)
                    set(Calendar.DAY_OF_MONTH, 20)
                    SimpleDate(time)
                },
                id = "E6CF8660",
                region = GOOGLE_MTV.id
        )

        private val NORCAL_RANKINGS_BUNDLE = RankingsBundle(
                rankingCriteria = NORCAL,
                rankings = listOf(NMW, AZEL, UMARTH, YCZ6, SNAP),
                time = with(Calendar.getInstance()) {
                    clear()
                    set(Calendar.YEAR, 2018)
                    set(Calendar.MONTH, Calendar.APRIL)
                    set(Calendar.DAY_OF_MONTH, 28)
                    SimpleDate(time)
                },
                id = "1A9C9DD4",
                region = NORCAL.id
        )

        private val NYC_RANKINGS_BUNDLE = RankingsBundle(
                rankingCriteria = NYC,
                rankings = listOf(HAX, IBDW, RISHI),
                time = with(Calendar.getInstance()) {
                    clear()
                    set(Calendar.YEAR, 2019)
                    set(Calendar.MONTH, Calendar.OCTOBER)
                    set(Calendar.DAY_OF_MONTH, 17)
                    SimpleDate(time)
                },
                id = "7F053ECF",
                region = NYC.id
        )
    }

    private class RankingsRepositoryOverride : RankingsRepository {

        override fun getRankings(region: Region): Single<RankingsBundle> {
            return when (region) {
                GOOGLE_MTV -> Single.just(GOOGLE_MTV_RANKINGS_BUNDLE)
                NORCAL -> Single.just(NORCAL_RANKINGS_BUNDLE)
                NYC -> Single.just(NYC_RANKINGS_BUNDLE)
                else -> Single.error(NoSuchElementException())
            }
        }

    }

}
