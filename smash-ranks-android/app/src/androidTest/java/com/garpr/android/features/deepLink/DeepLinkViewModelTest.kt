package com.garpr.android.features.deepLink

import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.Region
import com.garpr.android.data.models.RegionsBundle
import com.garpr.android.extensions.require
import com.garpr.android.features.common.viewModels.BaseViewModelTest
import com.garpr.android.features.deepLink.DeepLinkViewModel.Breadcrumb
import com.garpr.android.features.home.HomeTab
import com.garpr.android.misc.Schedulers
import com.garpr.android.misc.Timber
import com.garpr.android.repositories.RegionRepository
import com.garpr.android.repositories.RegionsRepository
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.koin.core.inject

class DeepLinkViewModelTest : BaseViewModelTest() {

    private lateinit var viewModel: DeepLinkViewModel
    private val regionsRepository = RegionsRepositoryOverride()

    protected val regionRepository: RegionRepository by inject()
    protected val schedulers: Schedulers by inject()
    protected val timber: Timber by inject()

    companion object {
        private val CHICAGO = Region(
                displayName = "Chicago",
                id = "chicago",
                endpoint = Endpoint.NOT_GAR_PR
        )

        private val NORCAL = Region(
                displayName = "Norcal",
                id = "norcal",
                endpoint = Endpoint.GAR_PR
        )

        private val NYC = Region(
                displayName = "NYC Metro Area",
                id = "nyc",
                endpoint = Endpoint.NOT_GAR_PR
        )

        private val REGIONS_BUNDLE = RegionsBundle(
                regions = listOf(CHICAGO, NORCAL, NYC)
        )

        private const val PLAYER_GINGER_ID = "57983b42e592573cf1845ff2"
        private const val PLAYER_SFAT_ID = "588852e8d2994e3bbfa52d88"
        private const val PLAYER_SWEDISH_DELIGHT_ID = "545b240b8ab65f7a95f74940"

        private const val PLAYER_GINGER = "https://www.notgarpr.com/#/chicago/players/$PLAYER_GINGER_ID"
        private const val PLAYER_SFAT = "https://www.garpr.com/#/norcal/players/$PLAYER_SFAT_ID"
        private const val PLAYER_SWEDISH_DELIGHT = "https://www.notgarpr.com/#/nyc/players/$PLAYER_SWEDISH_DELIGHT_ID"

        private const val PLAYERS_GEORGIA = "https://www.notgarpr.com/#/georgia/players"
        private const val PLAYERS_NORCAL = "https://www.garpr.com/#/norcal/players"

        private const val RANKINGS_NORCAL = "https://www.garpr.com/#/norcal/rankings"
        private const val RANKINGS_NYC = "https://www.notgarpr.com/#/nyc/rankings"

        private const val TOURNAMENT_APOLLO_III_ID = "58c72c801d41c8259fa1f8bf"
        private const val TOURNAMENT_NORCAL_VALIDATED_2_ID = "58a00514d2994e4d0f2e25a6"

        private const val TOURNAMENT_APOLLO_III = "https://www.notgarpr.com/#/nyc/tournaments/$TOURNAMENT_APOLLO_III_ID"
        private const val TOURNAMENT_NORCAL_VALIDATED_2 = "https://www.garpr.com/#/norcal/tournaments/$TOURNAMENT_NORCAL_VALIDATED_2_ID"

        private const val TOURNAMENTS_NORCAL = "https://www.garpr.com/#/norcal/tournaments"
        private const val TOURNAMENTS_NYC = "https://www.notgarpr.com/#/nyc/tournaments"
    }

    @Before
    override fun setUp() {
        super.setUp()

        viewModel = DeepLinkViewModel(regionRepository, regionsRepository, schedulers, timber)
    }

    @Test
    fun testBreadcrumbsWithChicagoAndNorcalValidated2Tournament() {
        regionRepository.region = CHICAGO
        viewModel.initialize(TOURNAMENT_NORCAL_VALIDATED_2)

        var breadcrumbs: List<Breadcrumb>? = null
        var networkError: Unit? = null
        var urlParseError: Unit? = null

        viewModel.breadcrumbsLiveData.observeForever {
            breadcrumbs = it
        }

        viewModel.networkErrorLiveData.observeForever {
            networkError = it
        }

        viewModel.urlParseErrorLiveData.observeForever {
            urlParseError = it
        }

        viewModel.fetchBreadcrumbs()
        assertNotNull(breadcrumbs)
        assertNull(networkError)
        assertNull(urlParseError)

        assertEquals(3, breadcrumbs?.size)

        val home = breadcrumbs.require(0) as Breadcrumb.Home
        assertNull(home.initialPosition)

        val tournaments = breadcrumbs.require(1) as Breadcrumb.Tournaments
        assertEquals(NORCAL, tournaments.region)

        val tournament = breadcrumbs.require(2) as Breadcrumb.Tournament
        assertEquals(NORCAL, tournament.region)
        assertEquals(TOURNAMENT_NORCAL_VALIDATED_2_ID, tournament.tournamentId)
    }

    @Test
    fun testBreadcrumbsWithNorcalAndEmptyString() {
        regionRepository.region = NORCAL
        viewModel.initialize("")

        var breadcrumbs: List<Breadcrumb>? = null
        var networkError: Unit? = null
        var urlParseError: Unit? = null

        viewModel.breadcrumbsLiveData.observeForever {
            breadcrumbs = it
        }

        viewModel.networkErrorLiveData.observeForever {
            networkError = it
        }

        viewModel.urlParseErrorLiveData.observeForever {
            urlParseError = it
        }

        viewModel.fetchBreadcrumbs()
        assertNull(breadcrumbs)
        assertNull(networkError)
        assertNotNull(urlParseError)
    }

    @Test
    fun testBreadcrumbsWithNorcalAndNull() {
        regionRepository.region = NORCAL
        viewModel.initialize(null)

        var breadcrumbs: List<Breadcrumb>? = null
        var networkError: Unit? = null
        var urlParseError: Unit? = null

        viewModel.breadcrumbsLiveData.observeForever {
            breadcrumbs = it
        }

        viewModel.networkErrorLiveData.observeForever {
            networkError = it
        }

        viewModel.urlParseErrorLiveData.observeForever {
            urlParseError = it
        }

        viewModel.fetchBreadcrumbs()
        assertNull(breadcrumbs)
        assertNull(networkError)
        assertNotNull(urlParseError)
    }

    @Test
    fun testBreadcrumbsWithNorcalAndGeorgiaPlayers() {
        regionRepository.region = NORCAL
        viewModel.initialize(PLAYERS_GEORGIA)

        var breadcrumbs: List<Breadcrumb>? = null
        var networkError: Unit? = null
        var urlParseError: Unit? = null

        viewModel.breadcrumbsLiveData.observeForever {
            breadcrumbs = it
        }

        viewModel.networkErrorLiveData.observeForever {
            networkError = it
        }

        viewModel.urlParseErrorLiveData.observeForever {
            urlParseError = it
        }

        viewModel.fetchBreadcrumbs()
        assertNotNull(breadcrumbs)
        assertNull(networkError)
        assertNull(urlParseError)

        // this should be null/empty because we're not including Georgia in the RegionsBundle
        assertTrue(breadcrumbs.isNullOrEmpty())
    }

    @Test
    fun testBreadcrumbsWithNorcalAndGinger() {
        regionRepository.region = NORCAL
        viewModel.initialize(PLAYER_GINGER)

        var breadcrumbs: List<Breadcrumb>? = null
        var networkError: Unit? = null
        var urlParseError: Unit? = null

        viewModel.breadcrumbsLiveData.observeForever {
            breadcrumbs = it
        }

        viewModel.networkErrorLiveData.observeForever {
            networkError = it
        }

        viewModel.urlParseErrorLiveData.observeForever {
            urlParseError = it
        }

        viewModel.fetchBreadcrumbs()
        assertNotNull(breadcrumbs)
        assertNull(networkError)
        assertNull(urlParseError)

        assertEquals(3, breadcrumbs?.size)

        val home = breadcrumbs.require(0) as Breadcrumb.Home
        assertNull(home.initialPosition)

        val players = breadcrumbs.require(1) as Breadcrumb.Players
        assertEquals(CHICAGO, players.region)

        val player = breadcrumbs.require(2) as Breadcrumb.Player
        assertEquals(CHICAGO, player.region)
        assertEquals(PLAYER_GINGER_ID, player.playerId)
    }

    @Test
    fun testBreadcrumbsWithNorcalAndNorcalRankings() {
        regionRepository.region = NORCAL
        viewModel.initialize(RANKINGS_NORCAL)

        var breadcrumbs: List<Breadcrumb>? = null
        var networkError: Unit? = null
        var urlParseError: Unit? = null

        viewModel.breadcrumbsLiveData.observeForever {
            breadcrumbs = it
        }

        viewModel.networkErrorLiveData.observeForever {
            networkError = it
        }

        viewModel.urlParseErrorLiveData.observeForever {
            urlParseError = it
        }

        viewModel.fetchBreadcrumbs()
        assertNotNull(breadcrumbs)
        assertNull(networkError)
        assertNull(urlParseError)

        assertEquals(1, breadcrumbs?.size)

        val home = breadcrumbs.require(0) as Breadcrumb.Home
        assertEquals(HomeTab.RANKINGS, home.initialPosition)
    }

    @Test
    fun testBreadcrumbsWithNorcalAndNorcalTournaments() {
        regionRepository.region = NORCAL
        viewModel.initialize(TOURNAMENTS_NORCAL)

        var breadcrumbs: List<Breadcrumb>? = null
        var networkError: Unit? = null
        var urlParseError: Unit? = null

        viewModel.breadcrumbsLiveData.observeForever {
            breadcrumbs = it
        }

        viewModel.networkErrorLiveData.observeForever {
            networkError = it
        }

        viewModel.urlParseErrorLiveData.observeForever {
            urlParseError = it
        }

        viewModel.fetchBreadcrumbs()
        assertNotNull(breadcrumbs)
        assertNull(networkError)
        assertNull(urlParseError)

        assertEquals(1, breadcrumbs?.size)

        val home = breadcrumbs.require(0) as Breadcrumb.Home
        assertEquals(HomeTab.TOURNAMENTS, home.initialPosition)
    }

    @Test
    fun testBreadcrumbsWithNorcalAndNycRankings() {
        regionRepository.region = NORCAL
        viewModel.initialize(RANKINGS_NYC)

        var breadcrumbs: List<Breadcrumb>? = null
        var networkError: Unit? = null
        var urlParseError: Unit? = null

        viewModel.breadcrumbsLiveData.observeForever {
            breadcrumbs = it
        }

        viewModel.networkErrorLiveData.observeForever {
            networkError = it
        }

        viewModel.urlParseErrorLiveData.observeForever {
            urlParseError = it
        }

        viewModel.fetchBreadcrumbs()
        assertNotNull(breadcrumbs)
        assertNull(networkError)
        assertNull(urlParseError)

        assertEquals(2, breadcrumbs?.size)

        val home = breadcrumbs.require(0) as Breadcrumb.Home
        assertNull(home.initialPosition)

        val players = breadcrumbs.require(1) as Breadcrumb.Rankings
        assertEquals(NYC, players.region)
    }

    @Test
    fun testBreadcrumbsWithNorcalAndSfat() {
        regionRepository.region = NORCAL
        viewModel.initialize(PLAYER_SFAT)

        var breadcrumbs: List<Breadcrumb>? = null
        var networkError: Unit? = null
        var urlParseError: Unit? = null

        viewModel.breadcrumbsLiveData.observeForever {
            breadcrumbs = it
        }

        viewModel.networkErrorLiveData.observeForever {
            networkError = it
        }

        viewModel.urlParseErrorLiveData.observeForever {
            urlParseError = it
        }

        viewModel.fetchBreadcrumbs()
        assertNotNull(breadcrumbs)
        assertNull(networkError)
        assertNull(urlParseError)

        assertEquals(3, breadcrumbs?.size)

        val home = breadcrumbs.require(0) as Breadcrumb.Home
        assertNull(home.initialPosition)

        val players = breadcrumbs.require(1) as Breadcrumb.Players
        assertNull(players.region)

        val player = breadcrumbs.require(2) as Breadcrumb.Player
        assertNull(player.region)
        assertEquals(PLAYER_SFAT_ID, player.playerId)
    }

    @Test
    fun testBreadcrumbsWithNycAndNorcalPlayers() {
        regionRepository.region = NYC
        viewModel.initialize(PLAYERS_NORCAL)

        var breadcrumbs: List<Breadcrumb>? = null
        var networkError: Unit? = null
        var urlParseError: Unit? = null

        viewModel.breadcrumbsLiveData.observeForever {
            breadcrumbs = it
        }

        viewModel.networkErrorLiveData.observeForever {
            networkError = it
        }

        viewModel.urlParseErrorLiveData.observeForever {
            urlParseError = it
        }

        viewModel.fetchBreadcrumbs()
        assertNotNull(breadcrumbs)
        assertNull(networkError)
        assertNull(urlParseError)

        assertEquals(2, breadcrumbs?.size)

        val home = breadcrumbs.require(0) as Breadcrumb.Home
        assertNull(home.initialPosition)

        val players = breadcrumbs.require(1) as Breadcrumb.Players
        assertEquals(NORCAL, players.region)
    }

    @Test
    fun testBreadcrumbsWithNycAndNycTournaments() {
        regionRepository.region = NYC
        viewModel.initialize(TOURNAMENTS_NYC)

        var breadcrumbs: List<Breadcrumb>? = null
        var networkError: Unit? = null
        var urlParseError: Unit? = null

        viewModel.breadcrumbsLiveData.observeForever {
            breadcrumbs = it
        }

        viewModel.networkErrorLiveData.observeForever {
            networkError = it
        }

        viewModel.urlParseErrorLiveData.observeForever {
            urlParseError = it
        }

        viewModel.fetchBreadcrumbs()
        assertNotNull(breadcrumbs)
        assertNull(networkError)
        assertNull(urlParseError)

        assertEquals(1, breadcrumbs?.size)

        val home = breadcrumbs.require(0) as Breadcrumb.Home
        assertEquals(HomeTab.TOURNAMENTS, home.initialPosition)
    }

    @Test
    fun testBreadcrumbsWithNycAndNycApolloIiiTournament() {
        regionRepository.region = NYC
        viewModel.initialize(TOURNAMENT_APOLLO_III)

        var breadcrumbs: List<Breadcrumb>? = null
        var networkError: Unit? = null
        var urlParseError: Unit? = null

        viewModel.breadcrumbsLiveData.observeForever {
            breadcrumbs = it
        }

        viewModel.networkErrorLiveData.observeForever {
            networkError = it
        }

        viewModel.urlParseErrorLiveData.observeForever {
            urlParseError = it
        }

        viewModel.fetchBreadcrumbs()
        assertNotNull(breadcrumbs)
        assertNull(networkError)
        assertNull(urlParseError)

        assertEquals(2, breadcrumbs?.size)

        val home = breadcrumbs.require(0) as Breadcrumb.Home
        assertEquals(HomeTab.TOURNAMENTS, home.initialPosition)

        val tournament = breadcrumbs.require(1) as Breadcrumb.Tournament
        assertNull(tournament.region)
        assertEquals(TOURNAMENT_APOLLO_III_ID, tournament.tournamentId)
    }

    @Test
    fun testBreadcrumbsWithNycAndSwedishDelight() {
        regionRepository.region = NYC
        viewModel.initialize(PLAYER_SWEDISH_DELIGHT)

        var breadcrumbs: List<Breadcrumb>? = null
        var networkError: Unit? = null
        var urlParseError: Unit? = null

        viewModel.breadcrumbsLiveData.observeForever {
            breadcrumbs = it
        }

        viewModel.networkErrorLiveData.observeForever {
            networkError = it
        }

        viewModel.urlParseErrorLiveData.observeForever {
            urlParseError = it
        }

        viewModel.fetchBreadcrumbs()
        assertNotNull(breadcrumbs)
        assertNull(networkError)
        assertNull(urlParseError)

        assertEquals(3, breadcrumbs?.size)

        val home = breadcrumbs.require(0) as Breadcrumb.Home
        assertNull(home.initialPosition)

        val players = breadcrumbs.require(1) as Breadcrumb.Players
        assertNull(players.region)

        val player = breadcrumbs.require(2) as Breadcrumb.Player
        assertNull(player.region)
        assertEquals(PLAYER_SWEDISH_DELIGHT_ID, player.playerId)
    }

    private class RegionsRepositoryOverride(
            internal var regionsBundle: RegionsBundle? = REGIONS_BUNDLE
    ) : RegionsRepository {

        override fun getRegions(): Single<RegionsBundle> {
            val bundle = regionsBundle

            return if (bundle == null) {
                Single.error(NullPointerException())
            } else {
                Single.just(bundle)
            }
        }

    }

}
