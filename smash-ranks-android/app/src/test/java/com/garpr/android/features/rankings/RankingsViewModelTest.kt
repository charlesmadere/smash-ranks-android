package com.garpr.android.features.rankings

import com.garpr.android.BaseTest
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.PreviousRank
import com.garpr.android.data.models.RankedPlayer
import com.garpr.android.data.models.RankingsBundle
import com.garpr.android.data.models.Region
import com.garpr.android.data.models.SimpleDate
import com.garpr.android.features.rankings.RankingsViewModel.ListItem
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.misc.Timber
import com.garpr.android.repositories.IdentityRepository
import com.garpr.android.repositories.RankingsRepository
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.inject
import org.robolectric.RobolectricTestRunner
import java.util.Date

@RunWith(RobolectricTestRunner::class)
class RankingsViewModelTest : BaseTest() {

    private lateinit var viewModel: RankingsViewModel

    protected val identityRepository: IdentityRepository by inject()
    protected val threadUtils: ThreadUtils by inject()
    protected val timber: Timber by inject()

    companion object {
        private val AERIUS = RankedPlayer(
                id = "597d28bed2994e34028b4cbe",
                name = "Aerius",
                rank = 36,
                rating = 32.437035049106726f,
                previousRank = 38
        )

        private val CAPTAIN_SMUCKERS = RankedPlayer(
                id = "545b23548ab65f7a95f7487b",
                name = "Captain Smuckers",
                rank = 5,
                rating = 40.30532007306529f
        )

        private val CHARLEZARD = RankedPlayer(
                id = "587a951dd2994e15c7dea9fe",
                name = "Charlezard",
                rating = 25.019734920402804f,
                rank = 91,
                previousRank = 91
        )

        private val HAX = RankedPlayer(
                id = "53c64dba8ab65f6e6651f7bc",
                name = "Hax",
                rank = 2,
                rating = 41.899999329230674f
        )

        private val IMYT = RankedPlayer(
                id = "5877eb55d2994e15c7dea98b",
                name = "Imyt",
                rating = 33.313937433274404f,
                rank = 28,
                previousRank = 27
        )

        private val SNAP = RankedPlayer(
                id = "59213f1ad2994e1d79144956",
                name = "Snap",
                rating = 33.55312453420609f,
                rank = 25,
                previousRank = 26
        )

        private val SWEDISH_DELIGHT = RankedPlayer(
                id = "545b240b8ab65f7a95f74940",
                name = "Swedish Delight",
                rating = 42.520605543563484f,
                rank = 1
        )

        private val GEORGIA = Region(
                activeTf = true,
                rankingActivityDayLimit = 90,
                rankingNumTourneysAttended = 3,
                tournamentQualifiedDayLimit = 90,
                displayName = "Georgia",
                id = "georgia",
                endpoint = Endpoint.NOT_GAR_PR
        )

        private val GOOGLE_MTV = Region(
                displayName = "Google MTV",
                id = "googlemtv",
                endpoint = Endpoint.GAR_PR
        )

        private val NORCAL = Region(
                rankingActivityDayLimit = 60,
                rankingNumTourneysAttended = 2,
                tournamentQualifiedDayLimit = 1000,
                displayName = "Norcal",
                id = "norcal",
                endpoint = Endpoint.GAR_PR
        )

        private val NYC = Region(
                activeTf = true,
                rankingActivityDayLimit = 365,
                rankingNumTourneysAttended = 4,
                tournamentQualifiedDayLimit = 999,
                displayName = "NYC Metro Area",
                id = "nyc",
                endpoint = Endpoint.NOT_GAR_PR
        )

        private val GOOGLE_MTV_RANKINGS_BUNDLE = RankingsBundle(
                rankingCriteria = GOOGLE_MTV,
                time = SimpleDate(Date(1514764800000L)),
                id = "5cfa9b9bd2994e25ed9320cb",
                region = GOOGLE_MTV.id
        )

        private val NORCAL_RANKINGS_BUNDLE = RankingsBundle(
                rankings = listOf(SNAP, IMYT, AERIUS, CHARLEZARD),
                rankingCriteria = NORCAL,
                time = SimpleDate(Date(1574121600000L)),
                id = "5dd24219d2994e6b6ca25860",
                region = NORCAL.id
        )

        private val NYC_RANKINGS_BUNDLE = RankingsBundle(
                rankings = listOf(SWEDISH_DELIGHT, HAX, CAPTAIN_SMUCKERS),
                rankingCriteria = NYC,
                time = SimpleDate(Date(1574035200000L)),
                id = "5dd2090c421aa97778f4f9c6",
                region = NYC.id
        )
    }

    @Before
    override fun setUp() {
        super.setUp()

        viewModel = RankingsViewModel(identityRepository, RankingsRepositoryOverride(),
                threadUtils, timber)
    }

    @Test
    fun testFetchRankingsUpdatesIsFetchingState() {
        val states = mutableListOf<Boolean>()

        viewModel.stateLiveData.observeForever {
            states.add(it.isFetching)
        }

        viewModel.fetchRankings(NORCAL)
        assertEquals(2, states.size)
        assertTrue(states[0])
        assertFalse(states[1])
    }

    @Test
    fun testFetchRankingsWithGeorgia() {
        var state: RankingsViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        viewModel.fetchRankings(GEORGIA)
        assertEquals(true, state?.hasError)
        assertEquals(false, state?.isEmpty)
        assertEquals(false, state?.isFetching)
        assertNull(state?.list)
        assertNull(state?.searchResults)
        assertNull(state?.rankingsBundle)
    }

    @Test
    fun testFetchRankingsWithGoogleMtv() {
        var state: RankingsViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        viewModel.fetchRankings(GOOGLE_MTV)
        assertEquals(false, state?.hasError)
        assertEquals(true, state?.isEmpty)
        assertEquals(false, state?.isFetching)
        assertTrue(state?.list.isNullOrEmpty())
        assertNull(state?.searchResults)
        assertEquals(GOOGLE_MTV_RANKINGS_BUNDLE, state?.rankingsBundle)
    }

    @Test
    fun testFetchRankingsWithNorcal() {
        var state: RankingsViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        identityRepository.setIdentity(CHARLEZARD, NORCAL)

        viewModel.fetchRankings(NORCAL)
        assertEquals(false, state?.hasError)
        assertEquals(false, state?.isEmpty)
        assertEquals(false, state?.isFetching)
        assertEquals(4, state?.list?.size)
        assertNull(state?.searchResults)
        assertEquals(NORCAL_RANKINGS_BUNDLE, state?.rankingsBundle)

        var player = state?.list?.get(0) as ListItem.Player
        assertEquals(SNAP, player.player)
        assertEquals(false, player.isIdentity)
        assertEquals(PreviousRank.INCREASE, player.previousRank)
        assertFalse(player.rank.isBlank())
        assertFalse(player.rating.isBlank())

        player = state?.list?.get(1) as ListItem.Player
        assertEquals(IMYT, player.player)
        assertEquals(false, player.isIdentity)
        assertEquals(PreviousRank.DECREASE, player.previousRank)
        assertFalse(player.rank.isBlank())
        assertFalse(player.rating.isBlank())

        player = state?.list?.get(2) as ListItem.Player
        assertEquals(AERIUS, player.player)
        assertEquals(false, player.isIdentity)
        assertEquals(PreviousRank.INCREASE, player.previousRank)
        assertFalse(player.rank.isBlank())
        assertFalse(player.rating.isBlank())

        player = state?.list?.get(3) as ListItem.Player
        assertEquals(CHARLEZARD, player.player)
        assertEquals(true, player.isIdentity)
        assertEquals(PreviousRank.INVISIBLE, player.previousRank)
        assertFalse(player.rank.isBlank())
        assertFalse(player.rating.isBlank())
    }

    @Test
    fun testFetchRankingsWithNyc() {
        var state: RankingsViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        identityRepository.setIdentity(CHARLEZARD, NORCAL)

        viewModel.fetchRankings(NYC)
        assertEquals(false, state?.hasError)
        assertEquals(false, state?.isEmpty)
        assertEquals(false, state?.isFetching)
        assertEquals(3, state?.list?.size)
        assertNull(state?.searchResults)
        assertEquals(NYC_RANKINGS_BUNDLE, state?.rankingsBundle)

        var player = state?.list?.get(0) as ListItem.Player
        assertEquals(SWEDISH_DELIGHT, player.player)
        assertEquals(false, player.isIdentity)
        assertEquals(PreviousRank.GONE, player.previousRank)
        assertFalse(player.rank.isBlank())
        assertFalse(player.rating.isBlank())

        player = state?.list?.get(1) as ListItem.Player
        assertEquals(HAX, player.player)
        assertEquals(false, player.isIdentity)
        assertEquals(PreviousRank.GONE, player.previousRank)
        assertFalse(player.rank.isBlank())
        assertFalse(player.rating.isBlank())

        player = state?.list?.get(2) as ListItem.Player
        assertEquals(CAPTAIN_SMUCKERS, player.player)
        assertEquals(false, player.isIdentity)
        assertEquals(PreviousRank.GONE, player.previousRank)
        assertFalse(player.rank.isBlank())
        assertFalse(player.rating.isBlank())
    }

    @Test
    fun testIdentityChangeDoesUpdateListAndSearchResults() {
        var state: RankingsViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        viewModel.fetchRankings(NORCAL)

        var list = state?.list
        assertEquals(4, list?.size)
        state?.list?.filterIsInstance(ListItem.Player::class.java)
                ?.forEach { assertFalse(it.isIdentity) }

        var searchResults = state?.searchResults
        assertNull(searchResults)

        identityRepository.setIdentity(CHARLEZARD, NORCAL)
        viewModel.search("r")

        list = state?.list
        assertEquals(4, list?.size)

        var player = list?.get(0) as ListItem.Player
        assertEquals(SNAP, player.player)
        assertFalse(player.isIdentity)

        player = list[1] as ListItem.Player
        assertEquals(IMYT, player.player)
        assertFalse(player.isIdentity)

        player = list[2] as ListItem.Player
        assertEquals(AERIUS, player.player)
        assertFalse(player.isIdentity)

        player = list[3] as ListItem.Player
        assertEquals(CHARLEZARD, player.player)
        assertTrue(player.isIdentity)

        searchResults = state?.searchResults
        assertEquals(2, searchResults?.size)

        player = searchResults?.get(0) as ListItem.Player
        assertEquals(AERIUS, player.player)
        assertFalse(player.isIdentity)

        player = searchResults[1] as ListItem.Player
        assertEquals(CHARLEZARD, player.player)
        assertTrue(player.isIdentity)
    }

    @Test
    fun testSearchNorcalWithA() {
        var state: RankingsViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        viewModel.fetchRankings(NORCAL)
        viewModel.search("a")
        assertEquals(false, state?.hasError)
        assertEquals(false, state?.isEmpty)
        assertEquals(false, state?.isFetching)
        assertEquals(3, state?.searchResults?.size)
        assertEquals(NORCAL_RANKINGS_BUNDLE, state?.rankingsBundle)

        var player = state?.searchResults?.get(0) as ListItem.Player
        assertEquals(SNAP, player.player)

        player = state?.searchResults?.get(1) as ListItem.Player
        assertEquals(AERIUS, player.player)

        player = state?.searchResults?.get(2) as ListItem.Player
        assertEquals(CHARLEZARD, player.player)
    }

    @Test
    fun testSearchNorcalWithWww() {
        var state: RankingsViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        viewModel.fetchRankings(NORCAL)
        viewModel.search("www")
        assertEquals(false, state?.hasError)
        assertEquals(false, state?.isEmpty)
        assertEquals(false, state?.isFetching)
        assertEquals(true, state?.searchResults?.isEmpty())
        assertEquals(NORCAL_RANKINGS_BUNDLE, state?.rankingsBundle)
    }

    @Test
    fun testSearchNorcalWithBlankString() {
        var state: RankingsViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        viewModel.fetchRankings(NORCAL)
        viewModel.search(" ")
        assertEquals(false, state?.hasError)
        assertEquals(false, state?.isEmpty)
        assertEquals(false, state?.isFetching)
        assertNull(state?.searchResults)
        assertEquals(NORCAL_RANKINGS_BUNDLE, state?.rankingsBundle)
    }

    @Test
    fun testSearchNorcalWithEmptyString() {
        var state: RankingsViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        viewModel.fetchRankings(NORCAL)
        viewModel.search("")
        assertEquals(false, state?.hasError)
        assertEquals(false, state?.isEmpty)
        assertEquals(false, state?.isFetching)
        assertNull(state?.searchResults)
        assertEquals(NORCAL_RANKINGS_BUNDLE, state?.rankingsBundle)
    }

    @Test
    fun testSearchNorcalWithNullString() {
        var state: RankingsViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        viewModel.fetchRankings(NORCAL)
        viewModel.search(null)
        assertEquals(false, state?.hasError)
        assertEquals(false, state?.isEmpty)
        assertEquals(false, state?.isFetching)
        assertNull(state?.searchResults)
        assertEquals(NORCAL_RANKINGS_BUNDLE, state?.rankingsBundle)
    }

    @Test
    fun testSearchNycWithBlankString() {
        var state: RankingsViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        viewModel.fetchRankings(NYC)
        viewModel.search(" ")
        assertEquals(false, state?.hasError)
        assertEquals(false, state?.isEmpty)
        assertEquals(false, state?.isFetching)
        assertNull(state?.searchResults)
        assertEquals(NYC_RANKINGS_BUNDLE, state?.rankingsBundle)
    }

    @Test
    fun testSearchNycWithEmptyString() {
        var state: RankingsViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        viewModel.fetchRankings(NYC)
        viewModel.search("")
        assertEquals(false, state?.hasError)
        assertEquals(false, state?.isEmpty)
        assertEquals(false, state?.isFetching)
        assertNull(state?.searchResults)
        assertEquals(NYC_RANKINGS_BUNDLE, state?.rankingsBundle)
    }

    @Test
    fun testSearchNycWithNullString() {
        var state: RankingsViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        viewModel.fetchRankings(NYC)
        viewModel.search(null)
        assertEquals(false, state?.hasError)
        assertEquals(false, state?.isEmpty)
        assertEquals(false, state?.isFetching)
        assertNull(state?.searchResults)
        assertEquals(NYC_RANKINGS_BUNDLE, state?.rankingsBundle)
    }

    @Test
    fun testSearchNycWithX() {
        var state: RankingsViewModel.State? = null

        viewModel.stateLiveData.observeForever {
            state = it
        }

        viewModel.fetchRankings(NYC)
        viewModel.search("x")
        assertEquals(false, state?.hasError)
        assertEquals(false, state?.isEmpty)
        assertEquals(false, state?.isFetching)
        assertEquals(1, state?.searchResults?.size)
        assertEquals(NYC_RANKINGS_BUNDLE, state?.rankingsBundle)

        val player = state?.searchResults?.get(0) as ListItem.Player
        assertEquals(HAX, player.player)
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
