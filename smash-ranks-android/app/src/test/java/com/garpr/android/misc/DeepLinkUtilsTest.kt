package com.garpr.android.misc

import android.app.Application
import android.content.Intent
import android.net.Uri
import com.garpr.android.BaseTest
import com.garpr.android.models.Endpoint
import com.garpr.android.models.Region
import com.garpr.android.models.RegionsBundle
import com.google.gson.Gson
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class DeepLinkUtilsTest : BaseTest() {

    private lateinit var chicago: Region
    private lateinit var georgia: Region
    private lateinit var googleMtv: Region
    private lateinit var norcal: Region
    private lateinit var nyc: Region
    private lateinit var regionsBundle: RegionsBundle
    private lateinit var regionsBundleEmpty: RegionsBundle

    @Inject
    protected lateinit var application: Application

    @Inject
    protected lateinit var deepLinkUtils: DeepLinkUtils

    @Inject
    protected lateinit var gson: Gson


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

        private const val JSON_REGIONS_BUNDLE = "{\"regions\":[{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":60,\"display_name\":\"Chicago\",\"id\":\"chicago\",\"tournament_qualified_day_limit\":999,\"endpoint\":\"not_gar_pr\"},{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":75,\"display_name\":\"Georgia\",\"id\":\"georgia\",\"tournament_qualified_day_limit\":180,\"endpoint\":\"not_gar_pr\"},{\"ranking_num_tourneys_attended\":6,\"ranking_activity_day_limit\":90,\"display_name\":\"NYC Metro Area\",\"id\":\"nyc\",\"tournament_qualified_day_limit\":99999,\"endpoint\":\"not_gar_pr\"},{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":90,\"display_name\":\"New Jersey\",\"id\":\"newjersey\",\"tournament_qualified_day_limit\":9999999,\"endpoint\":\"not_gar_pr\"},{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":30,\"display_name\":\"Norcal\",\"id\":\"norcal\",\"tournament_qualified_day_limit\":1000,\"endpoint\":\"gar_pr\"}]}"
        private const val JSON_REGIONS_BUNDLE_EMPTY = "{\"regions\":[]}"
    }

    @Before
    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)

        chicago = gson.fromJson(JSON_REGION_CHICAGO, Region::class.java)
        georgia = gson.fromJson(JSON_REGION_GEORGIA, Region::class.java)
        googleMtv = gson.fromJson(JSON_REGION_GOOGLE_MTV, Region::class.java)
        norcal = gson.fromJson(JSON_REGION_NORCAL, Region::class.java)
        nyc = gson.fromJson(JSON_REGION_NYC, Region::class.java)

        regionsBundle = gson.fromJson(JSON_REGIONS_BUNDLE, RegionsBundle::class.java)
        regionsBundleEmpty = gson.fromJson(JSON_REGIONS_BUNDLE_EMPTY, RegionsBundle::class.java)
    }

    @Test
    @Throws(Exception::class)
    fun testGetRegion() {
        assertNull(deepLinkUtils.getRegion(null as Intent?, null))
        assertNull(deepLinkUtils.getRegion(null as String?, null))
        assertNull(deepLinkUtils.getRegion(null as Uri?, null))
        assertNull(deepLinkUtils.getRegion("", null))
        assertNull(deepLinkUtils.getRegion("   ", null))
        assertNull(deepLinkUtils.getRegion(null as Intent?, regionsBundle))
        assertNull(deepLinkUtils.getRegion(null as String?, regionsBundle))
        assertNull(deepLinkUtils.getRegion(null as Uri?, regionsBundle))
        assertNull(deepLinkUtils.getRegion("", regionsBundle))
        assertNull(deepLinkUtils.getRegion("   ", regionsBundle))
        assertNull(deepLinkUtils.getRegion(null as Intent?, regionsBundleEmpty))
        assertNull(deepLinkUtils.getRegion(null as String?, regionsBundleEmpty))
        assertNull(deepLinkUtils.getRegion(null as Uri?, regionsBundleEmpty))
        assertNull(deepLinkUtils.getRegion("", regionsBundleEmpty))
        assertNull(deepLinkUtils.getRegion("   ", regionsBundleEmpty))
        assertNull(deepLinkUtils.getRegion(PLAYER_GINGER, regionsBundleEmpty))
        assertNull(deepLinkUtils.getRegion(PLAYER_SFAT, regionsBundleEmpty))
        assertNull(deepLinkUtils.getRegion(PLAYER_SWEDISH_DELIGHT, regionsBundleEmpty))
        assertNull(deepLinkUtils.getRegion(RANKINGS_LONG_ISLAND, regionsBundleEmpty))
        assertNull(deepLinkUtils.getRegion(TOURNAMENTS_NYC, regionsBundleEmpty))

        var region = deepLinkUtils.getRegion(PLAYER_GINGER, regionsBundle)
        assertNotNull(region)
        assertEquals("chicago", region?.id)

        region = deepLinkUtils.getRegion(PLAYER_SFAT, regionsBundle)
        assertNotNull(region)
        assertEquals("norcal", region?.id)

        region = deepLinkUtils.getRegion(RANKINGS_NORCAL, regionsBundle)
        assertNotNull(region)
        assertEquals("norcal", region?.id)

        region = deepLinkUtils.getRegion(TOURNAMENT_APOLLO_III, regionsBundle)
        assertNotNull(region)
        assertEquals("nyc", region?.id)
    }

    @Test
    @Throws(Exception::class)
    fun testEmptyStringBuildIntentStack() {
        assertNull(deepLinkUtils.buildIntentStack(application, "", googleMtv))
    }

    @Test
    @Throws(Exception::class)
    fun testEmptyStringIsValidUri() {
        assertFalse(deepLinkUtils.isValidUri(""))
    }

    @Test
    @Throws(Exception::class)
    fun testNullIntentBuildIntentStack() {
        assertNull(deepLinkUtils.buildIntentStack(application, null as Intent?, georgia))
    }

    @Test
    @Throws(Exception::class)
    fun testNullIntentGetEndpoint() {
        assertNull(deepLinkUtils.getEndpoint(null as Intent?))
    }

    @Test
    @Throws(Exception::class)
    fun testNullIntentIsValidUri() {
        assertFalse(deepLinkUtils.isValidUri(null as Intent?))
    }

    @Test
    @Throws(Exception::class)
    fun testNullStringBuildIntentStack() {
        assertNull(deepLinkUtils.buildIntentStack(application, null as String?, norcal))
    }

    @Test
    @Throws(Exception::class)
    fun testNullStringGetEndpoint() {
        assertNull(deepLinkUtils.getEndpoint(null as String?))
    }

    @Test
    @Throws(Exception::class)
    fun testNullStringIsValidUri() {
        assertFalse(deepLinkUtils.isValidUri(null as String?))
    }

    @Test
    @Throws(Exception::class)
    fun testNullUriBuildIntentStack() {
        assertNull(deepLinkUtils.buildIntentStack(application, null as Uri?, nyc))
    }

    @Test
    @Throws(Exception::class)
    fun testNullUriGetEndpoint() {
        assertNull(deepLinkUtils.getEndpoint(null as Uri?))
    }

    @Test
    @Throws(Exception::class)
    fun testNullUriIsValidUri() {
        assertFalse(deepLinkUtils.isValidUri(null as Uri?))
    }

    @Test
    @Throws(Exception::class)
    fun testPlayerGingerBuildIntentStack() {
        val intentStack = deepLinkUtils.buildIntentStack(application, PLAYER_GINGER, chicago)
        assertNotNull(intentStack)
        assertEquals(3, intentStack?.size)
    }

    @Test
    @Throws(Exception::class)
    fun testPlayerGingerGetEndpoint() {
        assertEquals(Endpoint.NOT_GAR_PR, deepLinkUtils.getEndpoint(PLAYER_GINGER))
        assertEquals(Endpoint.NOT_GAR_PR, deepLinkUtils.getEndpoint(Uri.parse(PLAYER_GINGER)))
    }

    @Test
    @Throws(Exception::class)
    fun testPlayerGingerIsValidUri() {
        assertTrue(deepLinkUtils.isValidUri(PLAYER_GINGER))
        assertTrue(deepLinkUtils.isValidUri(Uri.parse(PLAYER_GINGER)))
    }

    @Test
    @Throws(Exception::class)
    fun testPlayerSfatBuildIntentStack() {
        val intentStack = deepLinkUtils.buildIntentStack(application, PLAYER_SFAT, norcal)
        assertNotNull(intentStack)
        assertEquals(3, intentStack?.size)
    }

    @Test
    @Throws(Exception::class)
    fun testPlayerSfatIsValidUri() {
        assertTrue(deepLinkUtils.isValidUri(PLAYER_SFAT))
        assertTrue(deepLinkUtils.isValidUri(Uri.parse(PLAYER_SFAT)))
    }

    @Test
    @Throws(Exception::class)
    fun testPlayerSwedishDelightBuildIntentStack() {
        val intentStack = deepLinkUtils.buildIntentStack(application, PLAYER_SWEDISH_DELIGHT,
                nyc)
        assertNotNull(intentStack)
        assertEquals(3, intentStack?.size)
    }

    @Test
    @Throws(Exception::class)
    fun testPlayerSwedishDelightIsValidUri() {
        assertTrue(deepLinkUtils.isValidUri(PLAYER_SWEDISH_DELIGHT))
        assertTrue(deepLinkUtils.isValidUri(Uri.parse(PLAYER_SWEDISH_DELIGHT)))
    }

    @Test
    @Throws(Exception::class)
    fun testPlayersGeorgiaIsValidUri() {
        assertTrue(deepLinkUtils.isValidUri(PLAYERS_GEORGIA))
        assertTrue(deepLinkUtils.isValidUri(Uri.parse(PLAYERS_GEORGIA)))
    }

    @Test
    @Throws(Exception::class)
    fun testPlayersNorcalBuildIntentStack() {
        val intentStack = deepLinkUtils.buildIntentStack(application, PLAYERS_NORCAL, norcal)
        assertNotNull(intentStack)
        assertEquals(2, intentStack?.size)
    }

    @Test
    @Throws(Exception::class)
    fun testPlayersNorcalGetEndpoint() {
        assertEquals(Endpoint.GAR_PR, deepLinkUtils.getEndpoint(PLAYERS_NORCAL))
        assertEquals(Endpoint.GAR_PR, deepLinkUtils.getEndpoint(Uri.parse(PLAYERS_NORCAL)))
    }

    @Test
    @Throws(Exception::class)
    fun testPlayersNorcalIsValidUri() {
        assertTrue(deepLinkUtils.isValidUri(PLAYERS_NORCAL))
        assertTrue(deepLinkUtils.isValidUri(Uri.parse(PLAYERS_NORCAL)))
    }

    @Test
    @Throws(Exception::class)
    fun testRankingsGoogleMtvBuildIntentStack() {
        val intentStack = deepLinkUtils.buildIntentStack(application, RANKINGS_GOOGLEMTV,
                googleMtv)
        assertNotNull(intentStack)
        assertEquals(2, intentStack?.size)
    }

    @Test
    @Throws(Exception::class)
    fun testRankingsGoogleMtvIsValidUri() {
        assertTrue(deepLinkUtils.isValidUri(RANKINGS_GOOGLEMTV))
        assertTrue(deepLinkUtils.isValidUri(Uri.parse(RANKINGS_GOOGLEMTV)))
    }

    @Test
    @Throws(Exception::class)
    fun testRankingsLongIslandIsValidUri() {
        assertTrue(deepLinkUtils.isValidUri(RANKINGS_LONG_ISLAND))
        assertTrue(deepLinkUtils.isValidUri(Uri.parse(RANKINGS_LONG_ISLAND)))
    }

    @Test
    @Throws(Exception::class)
    fun testRankingsNorcalBuildIntentStack() {
        val intentStack = deepLinkUtils.buildIntentStack(application, RANKINGS_NORCAL, norcal)
        assertNotNull(intentStack)
        assertEquals(1, intentStack?.size)
    }

    @Test
    @Throws(Exception::class)
    fun testRankingsNorcalGetEndpoint() {
        assertEquals(Endpoint.GAR_PR, deepLinkUtils.getEndpoint(RANKINGS_NORCAL))
        assertEquals(Endpoint.GAR_PR, deepLinkUtils.getEndpoint(Uri.parse(RANKINGS_NORCAL)))
    }

    @Test
    @Throws(Exception::class)
    fun testRankingsNorcalIsValidUri() {
        assertTrue(deepLinkUtils.isValidUri(RANKINGS_NORCAL))
        assertTrue(deepLinkUtils.isValidUri(Uri.parse(RANKINGS_NORCAL)))
    }

    @Test
    @Throws(Exception::class)
    fun testTournamentApolloIiiBuildIntentStack() {
        val intentStack = deepLinkUtils.buildIntentStack(application, TOURNAMENT_APOLLO_III, nyc)
        assertNotNull(intentStack)
        assertEquals(3, intentStack?.size)
    }

    @Test
    @Throws(Exception::class)
    fun testTournamentApolloIiiIsValidUri() {
        assertTrue(deepLinkUtils.isValidUri(TOURNAMENT_APOLLO_III))
        assertTrue(deepLinkUtils.isValidUri(Uri.parse(TOURNAMENT_APOLLO_III)))
    }

    @Test
    @Throws(Exception::class)
    fun testTournamentNorcalValidated2IsValidUri() {
        assertTrue(deepLinkUtils.isValidUri(TOURNAMENT_NORCAL_VALIDATED_2))
        assertTrue(deepLinkUtils.isValidUri(Uri.parse(TOURNAMENT_NORCAL_VALIDATED_2)))
    }

    @Test
    @Throws(Exception::class)
    fun testTournamentNorcalValidated2BuildIntentStack() {
        val intentStack = deepLinkUtils.buildIntentStack(application,
                TOURNAMENT_NORCAL_VALIDATED_2, norcal)
        assertNotNull(intentStack)
        assertEquals(2, intentStack?.size)
    }

    @Test
    @Throws(Exception::class)
    fun testTournamentsNorcalIsValidUri() {
        assertTrue(deepLinkUtils.isValidUri(TOURNAMENTS_NORCAL))
        assertTrue(deepLinkUtils.isValidUri(Uri.parse(TOURNAMENTS_NORCAL)))
    }

    @Test
    @Throws(Exception::class)
    fun testTournamentsNycBuildIntentStack() {
        val intentStack = deepLinkUtils.buildIntentStack(application, TOURNAMENTS_NYC, nyc)
        assertNotNull(intentStack)
        assertEquals(2, intentStack?.size)
    }

    @Test
    @Throws(Exception::class)
    fun testTournamentsNycGetEndpoint() {
        assertEquals(Endpoint.NOT_GAR_PR, deepLinkUtils.getEndpoint(TOURNAMENTS_NYC))
        assertEquals(Endpoint.NOT_GAR_PR, deepLinkUtils.getEndpoint(Uri.parse(TOURNAMENTS_NYC)))
    }

    @Test
    @Throws(Exception::class)
    fun testTournamentsNycIsValidUri() {
        assertTrue(deepLinkUtils.isValidUri(TOURNAMENTS_NYC))
        assertTrue(deepLinkUtils.isValidUri(Uri.parse(TOURNAMENTS_NYC)))
    }

    @Test
    @Throws(Exception::class)
    fun testWhitespaceStringBuildIntentStack() {
        assertNull(deepLinkUtils.buildIntentStack(application, " ", georgia))
    }

    @Test
    @Throws(Exception::class)
    fun testWhitespaceStringGetEndpoint() {
        assertNull(deepLinkUtils.getEndpoint(" "))
    }

    @Test
    @Throws(Exception::class)
    fun testWhitespaceStringIsValidUri() {
        assertFalse(deepLinkUtils.isValidUri(" "))
    }

}
