package com.garpr.android.data.models

import com.garpr.android.BaseTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.Collections

@RunWith(RobolectricTestRunner::class)
class RegionsBundleTest : BaseTest() {

    companion object {
        private val REGIONS_BUNDLE = RegionsBundle(
                regions = listOf(
                        Region(
                                displayName = "Norcal",
                                id = "norcal",
                                endpoint = Endpoint.GAR_PR
                        ),
                        LiteRegion(
                                displayName = "New York City",
                                id = "nyc"
                        ),
                        LiteRegion(
                                displayName = "Google MTV",
                                id = "googlemtv"
                        ),
                        LiteRegion(
                                displayName = "Georgia",
                                id = "georgia"
                        ),
                        Region(
                                displayName = "Florida",
                                id = "florida",
                                endpoint = Endpoint.NOT_GAR_PR
                        ),
                        Region(
                                displayName = "Alabama",
                                id = "alabama",
                                endpoint = Endpoint.NOT_GAR_PR
                        )
                )
        )
    }

    @Test
    fun testComparatorAlphabeticalOrder() {
        val regions = requireNotNull(REGIONS_BUNDLE.regions)
        Collections.sort(regions, AbsRegion.ALPHABETICAL_ORDER)

        assertEquals("alabama", regions[0].id)
        assertEquals("florida", regions[1].id)
        assertEquals("georgia", regions[2].id)
        assertEquals("googlemtv", regions[3].id)
        assertEquals("nyc", regions[4].id)
        assertEquals("norcal", regions[5].id)
    }

    @Test
    fun testComparatorEndpointOrder() {
        val regions = requireNotNull(REGIONS_BUNDLE.regions)
        Collections.sort(regions, AbsRegion.ENDPOINT_ORDER)

        assertEquals("norcal", regions[0].id)
        assertEquals("alabama", regions[1].id)
        assertEquals("florida", regions[2].id)
        assertEquals("georgia", regions[3].id)
        assertEquals("googlemtv", regions[4].id)
        assertEquals("nyc", regions[5].id)
    }

}
