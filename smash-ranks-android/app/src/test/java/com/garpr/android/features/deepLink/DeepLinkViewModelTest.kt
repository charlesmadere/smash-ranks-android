package com.garpr.android.features.deepLink

import com.garpr.android.BaseTest
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.Region
import com.garpr.android.data.models.RegionsBundle
import com.garpr.android.extensions.require
import com.garpr.android.features.deepLink.DeepLinkViewModel.Breadcrumb
import com.garpr.android.misc.Timber
import com.garpr.android.repositories.RegionsRepository
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class DeepLinkViewModelTest : BaseTest() {

    private lateinit var viewModel: DeepLinkViewModel
    private val regionsRepository = RegionsRepositoryOverride()

    @Inject
    protected lateinit var timber: Timber


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

        private const val RANKINGS_GOOGLEMTV = "https://www.garpr.com/#/googlemtv/rankings"
        private const val RANKINGS_LONG_ISLAND = "https://www.notgarpr.com/#/li/rankings"
        private const val RANKINGS_NORCAL = "https://www.garpr.com/#/norcal/rankings"
    }

    @Before
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)

        viewModel = DeepLinkViewModel(regionsRepository, timber)
    }

    @Test
    fun testBreadcrumbsWithNorcalAndEmptyString() {
        viewModel.initialize(NORCAL, "")

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
        viewModel.initialize(NORCAL, null)

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
    fun testBreadcrumbsWithNorcalAndGinger() {
        viewModel.initialize(NORCAL, PLAYER_GINGER)

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
    fun testBreadcrumbsWithNorcalAndSfat() {
        viewModel.initialize(NORCAL, PLAYER_SFAT)

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

    private class RegionsRepositoryOverride(
            internal var regionsBundle: RegionsBundle? = REGIONS_BUNDLE
    ) : RegionsRepository {

        override fun getRegions(): Single<RegionsBundle> {
            val bundle = regionsBundle

            return if (bundle == null) {
                Single.error(NullPointerException())
            } else {
                Single.just(regionsBundle)
            }
        }

    }

}
