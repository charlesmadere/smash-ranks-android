package com.garpr.android.models

import com.garpr.android.BaseTest
import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.*
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class RegionsBundleTest : BaseTest() {

    lateinit private var regionsBundle: RegionsBundle

    @Inject
    lateinit protected var gson: Gson


    companion object {
        private const val JSON_REGIONS_BUNDLE = "{\"regions\":[{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":60,\"display_name\":\"Alabama\",\"id\":\"alabama\",\"tournament_qualified_day_limit\":999},{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":60,\"display_name\":\"Central Florida\",\"id\":\"cfl\",\"tournament_qualified_day_limit\":999},{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":60,\"display_name\":\"Chicago\",\"id\":\"chicago\",\"tournament_qualified_day_limit\":999},{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":75,\"display_name\":\"Georgia\",\"id\":\"georgia\",\"tournament_qualified_day_limit\":180},{\"ranking_num_tourneys_attended\":3,\"ranking_activity_day_limit\":999,\"display_name\":\"Georgia Smash 4\",\"id\":\"georgia smash 4\",\"tournament_qualified_day_limit\":999},{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":60,\"display_name\":\"Georgia Teams\",\"id\":\"georgia teams\",\"tournament_qualified_day_limit\":999},{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":60,\"display_name\":\"GeorgiaTeams\",\"id\":\"georgiateams\",\"tournament_qualified_day_limit\":999},{\"ranking_num_tourneys_attended\":6,\"ranking_activity_day_limit\":91,\"display_name\":\"Long Island\",\"id\":\"li\",\"tournament_qualified_day_limit\":366},{\"ranking_num_tourneys_attended\":6,\"ranking_activity_day_limit\":90,\"display_name\":\"NYC Metro Area\",\"id\":\"nyc\",\"tournament_qualified_day_limit\":999},{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":60,\"display_name\":\"NYC Smash 64\",\"id\":\"nyc64\",\"tournament_qualified_day_limit\":999},{\"ranking_num_tourneys_attended\":4,\"ranking_activity_day_limit\":99999,\"display_name\":\"New England\",\"id\":\"newengland\",\"tournament_qualified_day_limit\":999},{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":90,\"display_name\":\"New Jersey\",\"id\":\"newjersey\",\"tournament_qualified_day_limit\":9999999},{\"ranking_num_tourneys_attended\":4,\"ranking_activity_day_limit\":120,\"display_name\":\"North Carolina\",\"id\":\"northcarolina\",\"tournament_qualified_day_limit\":365},{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":60,\"display_name\":\"Philadelphia\",\"id\":\"philadelphia\",\"tournament_qualified_day_limit\":999},{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":60,\"display_name\":\"Pittsburgh\",\"id\":\"pittsburgh\",\"tournament_qualified_day_limit\":999},{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":60,\"display_name\":\"South Carolina\",\"id\":\"southcarolina\",\"tournament_qualified_day_limit\":999},{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":60,\"display_name\":\"Westchester\",\"id\":\"westchester\",\"tournament_qualified_day_limit\":999}]}"
    }

    @Before
    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)

        regionsBundle = gson.fromJson(JSON_REGIONS_BUNDLE, RegionsBundle::class.java)
    }

    @Test
    @Throws(Exception::class)
    fun testComparatorAlphabeticalOrder() {
        val regions = regionsBundle.regions ?: throw NullPointerException()
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
