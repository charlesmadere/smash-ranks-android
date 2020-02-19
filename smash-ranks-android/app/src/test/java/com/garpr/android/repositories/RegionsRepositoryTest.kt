package com.garpr.android.repositories

import com.garpr.android.data.exceptions.FailedToFetchRegionsException
import com.garpr.android.data.models.AbsRegion
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.LiteRegion
import com.garpr.android.data.models.Region
import com.garpr.android.data.models.RegionsBundle
import com.garpr.android.misc.Timber
import com.garpr.android.networking.AbsServerApi
import com.garpr.android.test.BaseTest
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.koin.test.inject

class RegionsRepositoryTest : BaseTest() {

    private lateinit var regionsRepository: RegionsRepository
    private val serverApi = ServerApiOverride()

    protected val timber: Timber by inject()

    @Before
    override fun setUp() {
        super.setUp()

        regionsRepository = RegionsRepositoryImpl(serverApi, timber)
    }

    @Test
    fun testGetRegions() {
        val regionsBundle = regionsRepository.getRegions()
                .blockingGet()
        assertFalse(regionsBundle.regions.isNullOrEmpty())
        assertEquals(5, regionsBundle.regions?.size)

        var region = regionsBundle.regions?.get(0) as? Region
        assertEquals(GOOGLE_MTV, region)
        assertEquals(Endpoint.GAR_PR, region?.endpoint)

        region = regionsBundle.regions?.get(1) as? Region
        assertEquals(NORCAL, region)
        assertEquals(Endpoint.GAR_PR, region?.endpoint)

        region = regionsBundle.regions?.get(2) as? Region
        assertEquals(AUSTIN, region)
        assertEquals(Endpoint.NOT_GAR_PR, region?.endpoint)

        region = regionsBundle.regions?.get(3) as? Region
        assertEquals(CHICAGO, region)
        assertEquals(Endpoint.NOT_GAR_PR, region?.endpoint)

        region = regionsBundle.regions?.get(4) as? Region
        assertEquals(NYC, region)
        assertEquals(Endpoint.NOT_GAR_PR, region?.endpoint)
    }

    @Test
    fun testGetRegionsWithEmptyGarPrRegionsBundle() {
        serverApi.garPrRegionsBundle = EMPTY_REGIONS_BUNDLE

        val regionsBundle = regionsRepository.getRegions()
                .blockingGet()
        assertFalse(regionsBundle.regions.isNullOrEmpty())
        assertEquals(3, regionsBundle.regions?.size)

        var region = regionsBundle.regions?.get(0) as? Region
        assertEquals(AUSTIN, region)
        assertEquals(Endpoint.NOT_GAR_PR, region?.endpoint)

        region = regionsBundle.regions?.get(1) as? Region
        assertEquals(CHICAGO, region)
        assertEquals(Endpoint.NOT_GAR_PR, region?.endpoint)

        region = regionsBundle.regions?.get(2) as? Region
        assertEquals(NYC, region)
        assertEquals(Endpoint.NOT_GAR_PR, region?.endpoint)
    }

    @Test
    fun testGetRegionsWithEmptyNotGarPrRegionsBundle() {
        serverApi.notGarPrRegionsBundle = EMPTY_REGIONS_BUNDLE

        val regionsBundle = regionsRepository.getRegions()
                .blockingGet()
        assertFalse(regionsBundle.regions.isNullOrEmpty())
        assertEquals(2, regionsBundle.regions?.size)

        var region = regionsBundle.regions?.get(0) as? Region
        assertEquals(GOOGLE_MTV, region)
        assertEquals(Endpoint.GAR_PR, region?.endpoint)

        region = regionsBundle.regions?.get(1) as? Region
        assertEquals(NORCAL, region)
        assertEquals(Endpoint.GAR_PR, region?.endpoint)
    }

    @Test
    fun testGetRegionsWithEmptyGarPrAndNotGarPrRegionsBundles() {
        serverApi.garPrRegionsBundle = EMPTY_REGIONS_BUNDLE
        serverApi.notGarPrRegionsBundle = EMPTY_REGIONS_BUNDLE

        var regionsBundle: RegionsBundle? = null
        var throwable: Throwable? = null

        try {
            regionsBundle = regionsRepository.getRegions()
                    .blockingGet()
        } catch (t: Throwable) {
            throwable = t
        }

        assertNull(regionsBundle)
        assertNotNull(throwable)
        assertTrue(throwable is FailedToFetchRegionsException)
    }

    companion object {
        private val AUSTIN: AbsRegion = LiteRegion(
                displayName = "Austin",
                id = "austin"
        )

        private val CHICAGO: AbsRegion = LiteRegion(
                displayName = "Chicago",
                id = "chicago"
        )

        private val GOOGLE_MTV: AbsRegion = LiteRegion(
                displayName = "Google MTV",
                id = "googlemtv"
        )

        private val NORCAL: AbsRegion = LiteRegion(
                displayName = "Norcal",
                id = "norcal"
        )

        private val NYC: AbsRegion = LiteRegion(
                displayName = "NYC Metric Area",
                id = "nyc"
        )

        private val EMPTY_REGIONS_BUNDLE = RegionsBundle()

        private val GAR_PR_REGIONS_BUNDLE = RegionsBundle(
                regions = listOf(GOOGLE_MTV, NORCAL)
        )

        private val NOT_GAR_PR_REGIONS_BUNDLE = RegionsBundle(
                regions = listOf(AUSTIN, CHICAGO, NYC)
        )
    }

    private class ServerApiOverride(
            internal var garPrRegionsBundle: RegionsBundle? = GAR_PR_REGIONS_BUNDLE,
            internal var notGarPrRegionsBundle: RegionsBundle? = NOT_GAR_PR_REGIONS_BUNDLE
    ) : AbsServerApi() {

        override fun getRegions(endpoint: Endpoint): Single<RegionsBundle> {
            val regionsBundle = when (endpoint) {
                Endpoint.GAR_PR -> garPrRegionsBundle
                Endpoint.NOT_GAR_PR -> notGarPrRegionsBundle
            }

            return if (regionsBundle == null) {
                Single.error(NullPointerException())
            } else {
                Single.just(regionsBundle)
            }
        }

    }

}
