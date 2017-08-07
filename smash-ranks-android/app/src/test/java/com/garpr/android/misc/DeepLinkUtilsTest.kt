package com.garpr.android.misc

import android.app.Application
import android.content.Intent
import android.net.Uri
import com.garpr.android.BaseTest
import com.garpr.android.BuildConfig
import com.garpr.android.models.Endpoint
import com.garpr.android.models.Region
import com.garpr.android.models.RegionsBundle
import com.google.gson.Gson
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class DeepLinkUtilsTest : BaseTest() {

    lateinit private var mChicago: Region
    lateinit private var mGeorgia: Region
    lateinit private var mGoogleMtv: Region
    lateinit private var mNorcal: Region
    lateinit private var mNyc: Region
    lateinit private var mRegionsBundle: RegionsBundle
    lateinit private var mRegionsBundleEmpty: RegionsBundle

    @Inject
    lateinit protected var mApplication: Application

    @Inject
    lateinit protected var mDeepLinkUtils: DeepLinkUtils

    @Inject
    lateinit protected var mGson: Gson


    companion object {
        private const val PLAYER_GINGER = "https://www.notgarpr.com/#/chicago/players/57983b42e592573cf1845ff2"
        private const val PLAYER_SFAT = "https://www.garpr.com/#/norcal/players/588852e8d2994e3bbfa52d88"
        private const val PLAYER_SWEDISH_DELIGHT = "https://www.notgarpr.com/#/nyc/players/545b240b8ab65f7a95f74940"

        private const val PLAYERS_GEORGIA = "https://www.notgarpr.com/#/georgia/players"
        private const val PLAYERS_NORCAL = "https://www.garpr.com/#/norcal/players"

        private const val RANKINGS_GOOGLEMTV = "https://www.garpr.com/#/googlemtv/rankings"
        private const val RANKINGS_LONG_ISLAND = "https://www.notgarpr.com/#/li/rankings"
        private const val RANKINGS_NORCAL = "https://www.garpr.com/#/norcal/rankings"

        private const val TOURNAMENT_APOLLO_III = "https://www.notgarpr.com/#/nyc/tournaments/58c72c801d41c8259fa1f8bf"
        private const val TOURNAMENT_NORCAL_VALIDATED_2 = "https://www.garpr.com/#/norcal/tournaments/58a00514d2994e4d0f2e25a6"

        private const val TOURNAMENTS_NORCAL = "https://www.garpr.com/#/norcal/tournaments"
        private const val TOURNAMENTS_NYC = "https://www.notgarpr.com/#/nyc/tournaments"

        private const val JSON_REGION_CHICAGO = "{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":60,\"display_name\":\"Chicago\",\"id\":\"chicago\",\"tournament_qualified_day_limit\":999,\"endpoint\":\"not_gar_pr\"}"
        private const val JSON_REGION_GEORGIA = "{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":75,\"display_name\":\"Georgia\",\"id\":\"georgia\",\"tournament_qualified_day_limit\":180,\"endpoint\":\"not_gar_pr\"}"
        private const val JSON_REGION_GOOGLE_MTV = "{\"ranking_num_tourneys_attended\":1,\"ranking_activity_day_limit\":60,\"display_name\":\"Google MTV\",\"id\":\"googlemtv\",\"tournament_qualified_day_limit\":999,\"endpoint\":\"gar_pr\"}"
        private const val JSON_REGION_NORCAL = "{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":30,\"display_name\":\"Norcal\",\"id\":\"norcal\",\"tournament_qualified_day_limit\":1000,\"endpoint\":\"gar_pr\"}"
        private const val JSON_REGION_NYC = "{\"ranking_num_tourneys_attended\":6,\"ranking_activity_day_limit\":90,\"display_name\":\"NYC Metro Area\",\"id\":\"nyc\",\"tournament_qualified_day_limit\":999,\"endpoint\":\"not_gar_pr\"}"

        private const val JSON_REGIONS_BUNDLE = "{\"regions\":[{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":60,\"display_name\":\"Chicago\",\"id\":\"chicago\",\"tournament_qualified_day_limit\":999},{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":75,\"display_name\":\"Georgia\",\"id\":\"georgia\",\"tournament_qualified_day_limit\":180},{\"ranking_num_tourneys_attended\":6,\"ranking_activity_day_limit\":90,\"display_name\":\"NYC Metro Area\",\"id\":\"nyc\",\"tournament_qualified_day_limit\":99999},{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":90,\"display_name\":\"New Jersey\",\"id\":\"newjersey\",\"tournament_qualified_day_limit\":9999999},{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":30,\"display_name\":\"Norcal\",\"id\":\"norcal\",\"tournament_qualified_day_limit\":1000}]}"
        private const val JSON_REGIONS_BUNDLE_EMPTY = "{\"regions\":[]}"
    }

    @Before
    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)

        mChicago = mGson.fromJson(JSON_REGION_CHICAGO, Region::class.java)
        mGeorgia = mGson.fromJson(JSON_REGION_GEORGIA, Region::class.java)
        mGoogleMtv = mGson.fromJson(JSON_REGION_GOOGLE_MTV, Region::class.java)
        mNorcal = mGson.fromJson(JSON_REGION_NORCAL, Region::class.java)
        mNyc = mGson.fromJson(JSON_REGION_NYC, Region::class.java)

        mRegionsBundle = mGson.fromJson(JSON_REGIONS_BUNDLE, RegionsBundle::class.java)
        mRegionsBundleEmpty = mGson.fromJson(JSON_REGIONS_BUNDLE_EMPTY, RegionsBundle::class.java)
    }

    @Test
    @Throws(Exception::class)
    fun testGetRegion() {
        assertNull(mDeepLinkUtils.getRegion(null as Intent?, null))
        assertNull(mDeepLinkUtils.getRegion(null as String?, null))
        assertNull(mDeepLinkUtils.getRegion(null as Uri?, null))
        assertNull(mDeepLinkUtils.getRegion("", null))
        assertNull(mDeepLinkUtils.getRegion("   ", null))
        assertNull(mDeepLinkUtils.getRegion(null as Intent?, mRegionsBundle))
        assertNull(mDeepLinkUtils.getRegion(null as String?, mRegionsBundle))
        assertNull(mDeepLinkUtils.getRegion(null as Uri?, mRegionsBundle))
        assertNull(mDeepLinkUtils.getRegion("", mRegionsBundle))
        assertNull(mDeepLinkUtils.getRegion("   ", mRegionsBundle))
        assertNull(mDeepLinkUtils.getRegion(null as Intent?, mRegionsBundleEmpty))
        assertNull(mDeepLinkUtils.getRegion(null as String?, mRegionsBundleEmpty))
        assertNull(mDeepLinkUtils.getRegion(null as Uri?, mRegionsBundleEmpty))
        assertNull(mDeepLinkUtils.getRegion("", mRegionsBundleEmpty))
        assertNull(mDeepLinkUtils.getRegion("   ", mRegionsBundleEmpty))
        assertNull(mDeepLinkUtils.getRegion(PLAYER_GINGER, mRegionsBundleEmpty))
        assertNull(mDeepLinkUtils.getRegion(PLAYER_SFAT, mRegionsBundleEmpty))
        assertNull(mDeepLinkUtils.getRegion(PLAYER_SWEDISH_DELIGHT, mRegionsBundleEmpty))
        assertNull(mDeepLinkUtils.getRegion(RANKINGS_LONG_ISLAND, mRegionsBundleEmpty))
        assertNull(mDeepLinkUtils.getRegion(TOURNAMENTS_NYC, mRegionsBundleEmpty))

        var region = mDeepLinkUtils.getRegion(PLAYER_GINGER, mRegionsBundle)
        assertNotNull(region)
        assertEquals("chicago", region?.id)

        region = mDeepLinkUtils.getRegion(PLAYER_SFAT, mRegionsBundle)
        assertNotNull(region)
        assertEquals("norcal", region?.id)

        region = mDeepLinkUtils.getRegion(RANKINGS_NORCAL, mRegionsBundle)
        assertNotNull(region)
        assertEquals("norcal", region?.id)

        region = mDeepLinkUtils.getRegion(TOURNAMENT_APOLLO_III, mRegionsBundle)
        assertNotNull(region)
        assertEquals("nyc", region?.id)
    }

    @Test
    @Throws(Exception::class)
    fun testEmptyStringBuildIntentStack() {
        assertNull(mDeepLinkUtils.buildIntentStack(mApplication, "", mGoogleMtv))
    }

    @Test
    @Throws(Exception::class)
    fun testEmptyStringIsValidUri() {
        assertFalse(mDeepLinkUtils.isValidUri(""))
    }

    @Test
    @Throws(Exception::class)
    fun testNullIntentBuildIntentStack() {
        assertNull(mDeepLinkUtils.buildIntentStack(mApplication, null as Intent?, mGeorgia))
    }

    @Test
    @Throws(Exception::class)
    fun testNullIntentGetEndpoint() {
        assertNull(mDeepLinkUtils.getEndpoint(null as Intent?))
    }

    @Test
    @Throws(Exception::class)
    fun testNullIntentIsValidUri() {
        assertFalse(mDeepLinkUtils.isValidUri(null as Intent?))
    }

    @Test
    @Throws(Exception::class)
    fun testNullStringBuildIntentStack() {
        assertNull(mDeepLinkUtils.buildIntentStack(mApplication, null as String?, mNorcal))
    }

    @Test
    @Throws(Exception::class)
    fun testNullStringGetEndpoint() {
        assertNull(mDeepLinkUtils.getEndpoint(null as String?))
    }

    @Test
    @Throws(Exception::class)
    fun testNullStringIsValidUri() {
        assertFalse(mDeepLinkUtils.isValidUri(null as String?))
    }

    @Test
    @Throws(Exception::class)
    fun testNullUriBuildIntentStack() {
        assertNull(mDeepLinkUtils.buildIntentStack(mApplication, null as Uri?, mNyc))
    }

    @Test
    @Throws(Exception::class)
    fun testNullUriGetEndpoint() {
        assertNull(mDeepLinkUtils.getEndpoint(null as Uri?))
    }

    @Test
    @Throws(Exception::class)
    fun testNullUriIsValidUri() {
        assertFalse(mDeepLinkUtils.isValidUri(null as Uri?))
    }

    @Test
    @Throws(Exception::class)
    fun testPlayerGingerBuildIntentStack() {
        val intentStack = mDeepLinkUtils.buildIntentStack(mApplication, PLAYER_GINGER, mChicago)
        assertNotNull(intentStack)
        assertEquals(3, intentStack?.size)
    }

    @Test
    @Throws(Exception::class)
    fun testPlayerGingerGetEndpoint() {
        assertEquals(Endpoint.NOT_GAR_PR, mDeepLinkUtils.getEndpoint(PLAYER_GINGER))
        assertEquals(Endpoint.NOT_GAR_PR, mDeepLinkUtils.getEndpoint(Uri.parse(PLAYER_GINGER)))
    }

    @Test
    @Throws(Exception::class)
    fun testPlayerGingerIsValidUri() {
        assertTrue(mDeepLinkUtils.isValidUri(PLAYER_GINGER))
        assertTrue(mDeepLinkUtils.isValidUri(Uri.parse(PLAYER_GINGER)))
    }

    @Test
    @Throws(Exception::class)
    fun testPlayerSfatBuildIntentStack() {
        val intentStack = mDeepLinkUtils.buildIntentStack(mApplication, PLAYER_SFAT, mNorcal)
        assertNotNull(intentStack)
        assertEquals(3, intentStack?.size)
    }

    @Test
    @Throws(Exception::class)
    fun testPlayerSfatIsValidUri() {
        assertTrue(mDeepLinkUtils.isValidUri(PLAYER_SFAT))
        assertTrue(mDeepLinkUtils.isValidUri(Uri.parse(PLAYER_SFAT)))
    }

    @Test
    @Throws(Exception::class)
    fun testPlayerSwedishDelightBuildIntentStack() {
        val intentStack = mDeepLinkUtils.buildIntentStack(mApplication, PLAYER_SWEDISH_DELIGHT,
                mNyc)
        assertNotNull(intentStack)
        assertEquals(3, intentStack?.size)
    }

    @Test
    @Throws(Exception::class)
    fun testPlayerSwedishDelightIsValidUri() {
        assertTrue(mDeepLinkUtils.isValidUri(PLAYER_SWEDISH_DELIGHT))
        assertTrue(mDeepLinkUtils.isValidUri(Uri.parse(PLAYER_SWEDISH_DELIGHT)))
    }

    @Test
    @Throws(Exception::class)
    fun testPlayersGeorgiaIsValidUri() {
        assertTrue(mDeepLinkUtils.isValidUri(PLAYERS_GEORGIA))
        assertTrue(mDeepLinkUtils.isValidUri(Uri.parse(PLAYERS_GEORGIA)))
    }

    @Test
    @Throws(Exception::class)
    fun testPlayersNorcalBuildIntentStack() {
        val intentStack = mDeepLinkUtils.buildIntentStack(mApplication, PLAYERS_NORCAL, mNorcal)
        assertNotNull(intentStack)
        assertEquals(2, intentStack?.size)
    }

    @Test
    @Throws(Exception::class)
    fun testPlayersNorcalGetEndpoint() {
        assertEquals(Endpoint.GAR_PR, mDeepLinkUtils.getEndpoint(PLAYERS_NORCAL))
        assertEquals(Endpoint.GAR_PR, mDeepLinkUtils.getEndpoint(Uri.parse(PLAYERS_NORCAL)))
    }

    @Test
    @Throws(Exception::class)
    fun testPlayersNorcalIsValidUri() {
        assertTrue(mDeepLinkUtils.isValidUri(PLAYERS_NORCAL))
        assertTrue(mDeepLinkUtils.isValidUri(Uri.parse(PLAYERS_NORCAL)))
    }

    @Test
    @Throws(Exception::class)
    fun testRankingsGoogleMtvBuildIntentStack() {
        val intentStack = mDeepLinkUtils.buildIntentStack(mApplication, RANKINGS_GOOGLEMTV,
                mGoogleMtv)
        assertNotNull(intentStack)
        assertEquals(2, intentStack?.size)
    }

    @Test
    @Throws(Exception::class)
    fun testRankingsGoogleMtvIsValidUri() {
        assertTrue(mDeepLinkUtils.isValidUri(RANKINGS_GOOGLEMTV))
        assertTrue(mDeepLinkUtils.isValidUri(Uri.parse(RANKINGS_GOOGLEMTV)))
    }

    @Test
    @Throws(Exception::class)
    fun testRankingsLongIslandIsValidUri() {
        assertTrue(mDeepLinkUtils.isValidUri(RANKINGS_LONG_ISLAND))
        assertTrue(mDeepLinkUtils.isValidUri(Uri.parse(RANKINGS_LONG_ISLAND)))
    }

    @Test
    @Throws(Exception::class)
    fun testRankingsNorcalBuildIntentStack() {
        val intentStack = mDeepLinkUtils.buildIntentStack(mApplication, RANKINGS_NORCAL, mNorcal)
        assertNotNull(intentStack)
        assertEquals(1, intentStack?.size)
    }

    @Test
    @Throws(Exception::class)
    fun testRankingsNorcalGetEndpoint() {
        assertEquals(Endpoint.GAR_PR, mDeepLinkUtils.getEndpoint(RANKINGS_NORCAL))
        assertEquals(Endpoint.GAR_PR, mDeepLinkUtils.getEndpoint(Uri.parse(RANKINGS_NORCAL)))
    }

    @Test
    @Throws(Exception::class)
    fun testRankingsNorcalIsValidUri() {
        assertTrue(mDeepLinkUtils.isValidUri(RANKINGS_NORCAL))
        assertTrue(mDeepLinkUtils.isValidUri(Uri.parse(RANKINGS_NORCAL)))
    }

    @Test
    @Throws(Exception::class)
    fun testTournamentApolloIiiBuildIntentStack() {
        val intentStack = mDeepLinkUtils.buildIntentStack(mApplication, TOURNAMENT_APOLLO_III, mNyc)
        assertNotNull(intentStack)
        assertEquals(3, intentStack?.size)
    }

    @Test
    @Throws(Exception::class)
    fun testTournamentApolloIiiIsValidUri() {
        assertTrue(mDeepLinkUtils.isValidUri(TOURNAMENT_APOLLO_III))
        assertTrue(mDeepLinkUtils.isValidUri(Uri.parse(TOURNAMENT_APOLLO_III)))
    }

    @Test
    @Throws(Exception::class)
    fun testTournamentNorcalValidated2IsValidUri() {
        assertTrue(mDeepLinkUtils.isValidUri(TOURNAMENT_NORCAL_VALIDATED_2))
        assertTrue(mDeepLinkUtils.isValidUri(Uri.parse(TOURNAMENT_NORCAL_VALIDATED_2)))
    }

    @Test
    @Throws(Exception::class)
    fun testTournamentNorcalValidated2BuildIntentStack() {
        val intentStack = mDeepLinkUtils.buildIntentStack(mApplication,
                TOURNAMENT_NORCAL_VALIDATED_2, mNorcal)
        assertNotNull(intentStack)
        assertEquals(2, intentStack?.size)
    }

    @Test
    @Throws(Exception::class)
    fun testTournamentsNorcalIsValidUri() {
        assertTrue(mDeepLinkUtils.isValidUri(TOURNAMENTS_NORCAL))
        assertTrue(mDeepLinkUtils.isValidUri(Uri.parse(TOURNAMENTS_NORCAL)))
    }

    @Test
    @Throws(Exception::class)
    fun testTournamentsNycBuildIntentStack() {
        val intentStack = mDeepLinkUtils.buildIntentStack(mApplication, TOURNAMENTS_NYC, mNyc)
        assertNotNull(intentStack)
        assertEquals(2, intentStack?.size)
    }

    @Test
    @Throws(Exception::class)
    fun testTournamentsNycGetEndpoint() {
        assertEquals(Endpoint.NOT_GAR_PR, mDeepLinkUtils.getEndpoint(TOURNAMENTS_NYC))
        assertEquals(Endpoint.NOT_GAR_PR, mDeepLinkUtils.getEndpoint(Uri.parse(TOURNAMENTS_NYC)))
    }

    @Test
    @Throws(Exception::class)
    fun testTournamentsNycIsValidUri() {
        assertTrue(mDeepLinkUtils.isValidUri(TOURNAMENTS_NYC))
        assertTrue(mDeepLinkUtils.isValidUri(Uri.parse(TOURNAMENTS_NYC)))
    }

    @Test
    @Throws(Exception::class)
    fun testWhitespaceStringBuildIntentStack() {
        assertNull(mDeepLinkUtils.buildIntentStack(mApplication, " ", mGeorgia))
    }

    @Test
    @Throws(Exception::class)
    fun testWhitespaceStringGetEndpoint() {
        assertNull(mDeepLinkUtils.getEndpoint(" "))
    }

    @Test
    @Throws(Exception::class)
    fun testWhitespaceStringIsValidUri() {
        assertFalse(mDeepLinkUtils.isValidUri(" "))
    }

}
