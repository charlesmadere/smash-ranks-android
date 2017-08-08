package com.garpr.android.networking

import com.garpr.android.BaseTest
import com.garpr.android.BuildConfig
import com.garpr.android.misc.Constants
import com.garpr.android.models.Endpoint
import com.garpr.android.models.RegionsBundle
import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class RegionsBundleApiCallTest : BaseTest() {

    lateinit private var garPrRegionsBundle: RegionsBundle
    lateinit private var notGarPrRegionsBundle: RegionsBundle

    @Inject
    lateinit protected var gson: Gson


    companion object {
        private const val JSON_REGIONS_BUNDLE_GAR_PR = "{\"regions\":[{\"ranking_num_tourneys_attended\":1,\"ranking_activity_day_limit\":60,\"display_name\":\"Google MTV\",\"id\":\"googlemtv\",\"tournament_qualified_day_limit\":999},{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":45,\"display_name\":\"Norcal\",\"id\":\"norcal\",\"tournament_qualified_day_limit\":1000}]}"
        private const val JSON_REGIONS_BUNDLE_NOT_GAR_PR = "{\"regions\":[{\"ranking_num_tourneys_attended\":0,\"ranking_activity_day_limit\":0,\"display_name\":\"Alabama\",\"id\":\"alabama\",\"tournament_qualified_day_limit\":999},{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":90,\"display_name\":\"Central Florida\",\"id\":\"cfl\",\"tournament_qualified_day_limit\":999},{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":60,\"display_name\":\"Chicago\",\"id\":\"chicago\",\"tournament_qualified_day_limit\":999},{\"ranking_num_tourneys_attended\":3,\"ranking_activity_day_limit\":90,\"display_name\":\"Georgia\",\"id\":\"georgia\",\"tournament_qualified_day_limit\":90},{\"ranking_num_tourneys_attended\":3,\"ranking_activity_day_limit\":999,\"display_name\":\"Georgia Smash 4\",\"id\":\"georgia smash 4\",\"tournament_qualified_day_limit\":999},{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":60,\"display_name\":\"Georgia Teams\",\"id\":\"georgia teams\",\"tournament_qualified_day_limit\":999},{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":60,\"display_name\":\"GeorgiaTeams\",\"id\":\"georgiateams\",\"tournament_qualified_day_limit\":999},{\"ranking_num_tourneys_attended\":8,\"ranking_activity_day_limit\":123,\"display_name\":\"Long Island\",\"id\":\"li\",\"tournament_qualified_day_limit\":365},{\"ranking_num_tourneys_attended\":6,\"ranking_activity_day_limit\":110,\"display_name\":\"NYC Metro Area\",\"id\":\"nyc\",\"tournament_qualified_day_limit\":90},{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":60,\"display_name\":\"NYC Smash 64\",\"id\":\"nyc64\",\"tournament_qualified_day_limit\":999},{\"ranking_num_tourneys_attended\":4,\"ranking_activity_day_limit\":99999,\"display_name\":\"New England\",\"id\":\"newengland\",\"tournament_qualified_day_limit\":999},{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":90,\"display_name\":\"New Jersey\",\"id\":\"newjersey\",\"tournament_qualified_day_limit\":9999999},{\"ranking_num_tourneys_attended\":4,\"ranking_activity_day_limit\":120,\"display_name\":\"North Carolina\",\"id\":\"northcarolina\",\"tournament_qualified_day_limit\":365},{\"ranking_num_tourneys_attended\":1,\"ranking_activity_day_limit\":0,\"display_name\":\"North Carolina Smash4\",\"id\":\"north carolina smash4\",\"tournament_qualified_day_limit\":999},{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":200,\"display_name\":\"Oregon\",\"id\":\"oregon\",\"tournament_qualified_day_limit\":999},{\"ranking_num_tourneys_attended\":3,\"ranking_activity_day_limit\":90,\"display_name\":\"Philadelphia\",\"id\":\"philadelphia\",\"tournament_qualified_day_limit\":90},{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":60,\"display_name\":\"Pittsburgh\",\"id\":\"pittsburgh\",\"tournament_qualified_day_limit\":999},{\"ranking_num_tourneys_attended\":3,\"ranking_activity_day_limit\":180,\"display_name\":\"South Carolina\",\"id\":\"southcarolina\",\"tournament_qualified_day_limit\":999},{\"ranking_num_tourneys_attended\":1,\"ranking_activity_day_limit\":200,\"display_name\":\"Tennessee\",\"id\":\"tennessee\",\"tournament_qualified_day_limit\":200},{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":60,\"display_name\":\"Westchester\",\"id\":\"westchester\",\"tournament_qualified_day_limit\":999},{\"ranking_num_tourneys_attended\":3,\"ranking_activity_day_limit\":9999,\"display_name\":\"austin\",\"id\":\"austin\",\"tournament_qualified_day_limit\":9999}]}"
    }

    @Before
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)

        garPrRegionsBundle = gson.fromJson(JSON_REGIONS_BUNDLE_GAR_PR, RegionsBundle::class.java)
        notGarPrRegionsBundle = gson.fromJson(JSON_REGIONS_BUNDLE_NOT_GAR_PR, RegionsBundle::class.java)
    }

    @Test
    @Throws(Exception::class)
    fun testGetRegionsBundleWithNonNullGarPrAndNonNullNotGarPr() {
        var result: RegionsBundle? = null

        val listener = object : AbsApiListener<RegionsBundle>() {
            override fun success(`object`: RegionsBundle?) {
                result = `object`
            }
        }

        val serverApi = object : AbsServerApi() {
            override fun getRegions(endpoint: Endpoint?, listener: ApiListener<RegionsBundle>) {
                when (endpoint) {
                    Endpoint.GAR_PR -> { listener.success(garPrRegionsBundle) }
                    Endpoint.NOT_GAR_PR -> { listener.success(notGarPrRegionsBundle) }
                    else -> { throw RuntimeException() }
                }
            }
        }

        RegionsBundleApiCall(listener, serverApi).fetch()
        assertNotNull(result)
        assertNotNull(result?.regions)
        assertEquals(23, result?.regions?.size)
    }

    @Test
    @Throws(Exception::class)
    fun testGetRegionsBundleWithNonNullGarPrAndNullNotGarPr() {
        var result: RegionsBundle? = null

        val listener = object : AbsApiListener<RegionsBundle>() {
            override fun success(`object`: RegionsBundle?) {
                result = `object`
            }
        }

        val serverApi = object : AbsServerApi() {
            override fun getRegions(endpoint: Endpoint?, listener: ApiListener<RegionsBundle>) {
                when (endpoint) {
                    Endpoint.GAR_PR -> { listener.success(garPrRegionsBundle) }
                    Endpoint.NOT_GAR_PR -> { listener.failure(Constants.ERROR_CODE_UNKNOWN) }
                    else -> { throw RuntimeException() }
                }
            }
        }

        RegionsBundleApiCall(listener, serverApi).fetch()
        assertNotNull(result)
        assertNotNull(result?.regions)
        assertEquals(2, result?.regions?.size)
    }

    @Test
    @Throws(Exception::class)
    fun testGetRegionsBundleWithNullGarPrAndNonNullNotGarPr() {
        var result: RegionsBundle? = null

        val listener = object : AbsApiListener<RegionsBundle>() {
            override fun success(`object`: RegionsBundle?) {
                result = `object`
            }
        }

        val serverApi = object : AbsServerApi() {
            override fun getRegions(endpoint: Endpoint?, listener: ApiListener<RegionsBundle>) {
                when (endpoint) {
                    Endpoint.GAR_PR -> { listener.failure(Constants.ERROR_CODE_UNKNOWN) }
                    Endpoint.NOT_GAR_PR -> { listener.success(notGarPrRegionsBundle) }
                    else -> { throw RuntimeException() }
                }
            }
        }

        RegionsBundleApiCall(listener, serverApi).fetch()
        assertNotNull(result)
        assertNotNull(result?.regions)
        assertEquals(21, result?.regions?.size)
    }

    @Test
    @Throws(Exception::class)
    fun testGetRegionsBundleWithNullGarPrAndNullNotGarPr() {
        var result: Int? = null

        val listener = object : AbsApiListener<RegionsBundle>() {
            override fun failure(errorCode: Int) {
                result = errorCode
            }
        }

        val serverApi = object : AbsServerApi() {
            override fun getRegions(endpoint: Endpoint?, listener: ApiListener<RegionsBundle>) {
                listener.success(null)
            }
        }

        RegionsBundleApiCall(listener, serverApi).fetch()
        assertNotNull(result)
        assertEquals(Constants.ERROR_CODE_UNKNOWN, result)
    }

}
