package com.garpr.android.features.deepLink

import com.garpr.android.BaseTest
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.Region
import com.garpr.android.data.models.RegionsBundle
import com.garpr.android.misc.Timber
import com.garpr.android.repositories.RegionsRepository
import io.reactivex.Single
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
                regions = listOf(NORCAL, NYC)
        )

        private const val PLAYER_GINGER = "https://www.notgarpr.com/#/chicago/players/57983b42e592573cf1845ff2"
        private const val PLAYER_SFAT = "https://www.garpr.com/#/norcal/players/588852e8d2994e3bbfa52d88"
        private const val PLAYER_SWEDISH_DELIGHT = "https://www.notgarpr.com/#/nyc/players/545b240b8ab65f7a95f74940"

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
    fun testBreadcrumbsWithNorcalAndNull() {
        viewModel.initialize(NORCAL, null)

        var urlParseError: Unit? = null
        var networkError: Unit? = null
        var breadcrumbs: List<DeepLinkViewModel.Breadcrumb>? = null

        viewModel.urlParseErrorLiveData.observeForever {
            urlParseError = it
        }

        viewModel.networkErrorLiveData.observeForever {
            networkError = it
        }

        viewModel.breadcrumbsLiveData.observeForever {
            breadcrumbs = it
        }

        viewModel.fetchBreadcrumbs()
        assertNotNull(urlParseError)
        assertNull(networkError)
        assertNull(breadcrumbs)
    }

    @Test
    fun testBreadcrumbsWithNorcalAndSfat() {
        viewModel.initialize(NORCAL, PLAYER_SFAT)

        var urlParseError: Unit? = null
        var networkError: Unit? = null
        var breadcrumbs: List<DeepLinkViewModel.Breadcrumb>? = null

        viewModel.urlParseErrorLiveData.observeForever {
            urlParseError = it
        }

        viewModel.networkErrorLiveData.observeForever {
            networkError = it
        }

        viewModel.breadcrumbsLiveData.observeForever {
            breadcrumbs = it
        }

        viewModel.fetchBreadcrumbs()
        assertNull(urlParseError)
        assertNull(networkError)
        assertNotNull(breadcrumbs)
    }

    private class RegionsRepositoryOverride(
            internal var regionsBundle: RegionsBundle = REGIONS_BUNDLE
    ) : RegionsRepository {

        override fun getRegions(): Single<RegionsBundle> {
            return Single.just(regionsBundle)
        }

    }

}
