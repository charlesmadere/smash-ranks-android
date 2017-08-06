package com.garpr.android.models

import com.garpr.android.BaseTest
import com.garpr.android.BuildConfig
import com.google.gson.Gson
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.*
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class RegionTest : BaseTest() {

    lateinit private var mChicago: AbsRegion
    lateinit private var mGoogle: AbsRegion
    lateinit private var mNorcal: AbsRegion
    lateinit private var mNyc: AbsRegion
    lateinit private var mRegionsBundle: RegionsBundle

    @Inject
    lateinit protected var mGson: Gson


    companion object {
        private const val JSON_REGION_CHICAGO = "{\"ranking_num_tourneys_attended\":2,\"display_name\":\"Chicago\",\"id\":\"chicago\",\"tournament_qualified_day_limit\":999}"
        private const val JSON_REGION_GOOGLE = "{\"display_name\":\"Google MTV\",\"id\":\"googlemtv\",\"tournament_qualified_day_limit\":999}"
        private const val JSON_REGION_NORCAL = "{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":45,\"display_name\":\"Norcal\",\"id\":\"norcal\",\"tournament_qualified_day_limit\":1000,\"endpoint\":\"gar_pr\"}"
        private const val JSON_REGION_NYC = "{\"ranking_num_tourneys_attended\":6,\"ranking_activity_day_limit\":90,\"display_name\":\"NYC Metro Area\",\"id\":\"nyc\",\"tournament_qualified_day_limit\":999,\"endpoint\":\"not_gar_pr\"}"
        private const val JSON_REGIONS_BUNDLE = "{\"regions\":[{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":60,\"display_name\":\"Alabama\",\"id\":\"alabama\",\"tournament_qualified_day_limit\":999},{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":60,\"display_name\":\"Central Florida\",\"id\":\"cfl\",\"tournament_qualified_day_limit\":999},{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":60,\"display_name\":\"Chicago\",\"id\":\"chicago\",\"tournament_qualified_day_limit\":999},{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":75,\"display_name\":\"Georgia\",\"id\":\"georgia\",\"tournament_qualified_day_limit\":180},{\"ranking_num_tourneys_attended\":3,\"ranking_activity_day_limit\":999,\"display_name\":\"Georgia Smash 4\",\"id\":\"georgia smash 4\",\"tournament_qualified_day_limit\":999},{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":60,\"display_name\":\"Georgia Teams\",\"id\":\"georgia teams\",\"tournament_qualified_day_limit\":999},{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":60,\"display_name\":\"GeorgiaTeams\",\"id\":\"georgiateams\",\"tournament_qualified_day_limit\":999},{\"ranking_num_tourneys_attended\":6,\"ranking_activity_day_limit\":91,\"display_name\":\"Long Island\",\"id\":\"li\",\"tournament_qualified_day_limit\":366},{\"ranking_num_tourneys_attended\":6,\"ranking_activity_day_limit\":90,\"display_name\":\"NYC Metro Area\",\"id\":\"nyc\",\"tournament_qualified_day_limit\":999},{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":60,\"display_name\":\"NYC Smash 64\",\"id\":\"nyc64\",\"tournament_qualified_day_limit\":999},{\"ranking_num_tourneys_attended\":4,\"ranking_activity_day_limit\":99999,\"display_name\":\"New England\",\"id\":\"newengland\",\"tournament_qualified_day_limit\":999},{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":90,\"display_name\":\"New Jersey\",\"id\":\"newjersey\",\"tournament_qualified_day_limit\":9999999},{\"ranking_num_tourneys_attended\":4,\"ranking_activity_day_limit\":120,\"display_name\":\"North Carolina\",\"id\":\"northcarolina\",\"tournament_qualified_day_limit\":365},{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":60,\"display_name\":\"Philadelphia\",\"id\":\"philadelphia\",\"tournament_qualified_day_limit\":999},{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":60,\"display_name\":\"Pittsburgh\",\"id\":\"pittsburgh\",\"tournament_qualified_day_limit\":999},{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":60,\"display_name\":\"South Carolina\",\"id\":\"southcarolina\",\"tournament_qualified_day_limit\":999},{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":60,\"display_name\":\"Westchester\",\"id\":\"westchester\",\"tournament_qualified_day_limit\":999}]}"
    }

    @Before
    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)

        mChicago = mGson.fromJson(JSON_REGION_CHICAGO, AbsRegion::class.java)
        mGoogle = mGson.fromJson(JSON_REGION_GOOGLE, AbsRegion::class.java)
        mNorcal = mGson.fromJson(JSON_REGION_NORCAL, AbsRegion::class.java)
        mNyc = mGson.fromJson(JSON_REGION_NYC, AbsRegion::class.java)
        mRegionsBundle = mGson.fromJson(JSON_REGIONS_BUNDLE, RegionsBundle::class.java)
    }

    @Test
    @Throws(Exception::class)
    fun testHasActivityRequirements() {
        assertFalse(mChicago.hasActivityRequirements())
        assertFalse(mGoogle.hasActivityRequirements())
        assertTrue(mNorcal.hasActivityRequirements())
        assertFalse(mNyc.hasActivityRequirements())
    }

    @Test
    @Throws(Exception::class)
    fun testKind() {
        assertEquals(AbsRegion.Kind.LITE, mChicago.kind)
        assertEquals(AbsRegion.Kind.LITE, mGoogle.kind)
        assertEquals(AbsRegion.Kind.FULL, mNorcal.kind)
        assertEquals(AbsRegion.Kind.FULL, mNyc.kind)
    }

    @Test
    @Throws(Exception::class)
    fun testComparatorAlphabeticalOrder() {
        val regionsBundle = mGson.fromJson(JSON_REGIONS_BUNDLE, RegionsBundle::class.java)
        assertNotNull(regionsBundle)

        val regions = regionsBundle.regions ?: throw NullPointerException()
        assertNotNull(regions)

        Collections.sort(regions, AbsRegion.ALPHABETICAL_ORDER)
        assertEquals("alabama", regions[0].id)
        assertEquals("cfl", regions[1].id)
        assertEquals("chicago", regions[2].id)
        assertEquals("georgia", regions[3].id)
        assertEquals("newengland", regions[8].id)
        assertEquals("pittsburgh", regions[14].id)
        assertEquals("westchester", regions[16].id)
    }

}
