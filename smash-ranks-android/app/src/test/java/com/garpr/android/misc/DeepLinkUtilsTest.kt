package com.garpr.android.misc

import android.app.Application
import android.content.Intent
import android.net.Uri
import com.garpr.android.BaseTest
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.Region
import com.garpr.android.data.models.RegionsBundle
import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class DeepLinkUtilsTest : BaseTest() {

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

        private val CHICAGO = Region(
                rankingNumTourneysAttended = 2,
                rankingActivityDayLimit = 60,
                displayName = "Chicago",
                id = "chicago",
                tournamentQualifiedDayLimit = 999,
                endpoint = Endpoint.NOT_GAR_PR
        )

        private val GEORGIA = Region(
                rankingNumTourneysAttended = 2,
                rankingActivityDayLimit = 75,
                displayName = "Georgia",
                id = "georgia",
                tournamentQualifiedDayLimit = 180,
                endpoint = Endpoint.NOT_GAR_PR
        )

        private val GOOGLE_MTV = Region(
                rankingNumTourneysAttended = 1,
                rankingActivityDayLimit = 60,
                displayName = "Google MTV",
                id = "googlemtv",
                tournamentQualifiedDayLimit = 999,
                endpoint = Endpoint.GAR_PR
        )

        private val NORCAL = Region(
                rankingNumTourneysAttended = 2,
                rankingActivityDayLimit = 30,
                displayName = "Norcal",
                id = "norcal",
                tournamentQualifiedDayLimit = 1000,
                endpoint = Endpoint.GAR_PR
        )

        private val NYC = Region(
                rankingNumTourneysAttended = 6,
                rankingActivityDayLimit = 90,
                displayName = "NYC Metro Area",
                id = "nyc",
                tournamentQualifiedDayLimit = 999,
                endpoint = Endpoint.NOT_GAR_PR
        )

        private const val TOURNAMENT_APOLLO_III = "https://www.notgarpr.com/#/nyc/tournaments/58c72c801d41c8259fa1f8bf"
        private const val TOURNAMENT_NORCAL_VALIDATED_2 = "https://www.garpr.com/#/norcal/tournaments/58a00514d2994e4d0f2e25a6"

        private const val TOURNAMENTS_NORCAL = "https://www.garpr.com/#/norcal/tournaments"
        private const val TOURNAMENTS_NYC = "https://www.notgarpr.com/#/nyc/tournaments"

        private const val JSON_REGIONS_BUNDLE = "{\"regions\":[{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":60,\"display_name\":\"Chicago\",\"id\":\"chicago\",\"tournament_qualified_day_limit\":999,\"endpoint\":\"not_gar_pr\"},{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":75,\"display_name\":\"Georgia\",\"id\":\"georgia\",\"tournament_qualified_day_limit\":180,\"endpoint\":\"not_gar_pr\"},{\"ranking_num_tourneys_attended\":6,\"ranking_activity_day_limit\":90,\"display_name\":\"NYC Metro Area\",\"id\":\"nyc\",\"tournament_qualified_day_limit\":99999,\"endpoint\":\"not_gar_pr\"},{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":90,\"display_name\":\"New Jersey\",\"id\":\"newjersey\",\"tournament_qualified_day_limit\":9999999,\"endpoint\":\"not_gar_pr\"},{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":30,\"display_name\":\"Norcal\",\"id\":\"norcal\",\"tournament_qualified_day_limit\":1000,\"endpoint\":\"gar_pr\"}]}"
        private const val JSON_REGIONS_BUNDLE_EMPTY = "{\"regions\":[]}"
    }

    @Before
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)

        regionsBundle = gson.fromJson(JSON_REGIONS_BUNDLE, RegionsBundle::class.java)
        regionsBundleEmpty = gson.fromJson(JSON_REGIONS_BUNDLE_EMPTY, RegionsBundle::class.java)
    }

    @Test
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
    fun testEmptyStringBuildIntentStack() {
        assertNull(deepLinkUtils.buildIntentStack(application, "", GOOGLE_MTV))
    }

    @Test
    fun testEmptyStringIsValidUri() {
        assertFalse(deepLinkUtils.isValidUri(""))
    }

    @Test
    fun testNullIntentBuildIntentStack() {
        assertNull(deepLinkUtils.buildIntentStack(application, null as Intent?, GEORGIA))
    }

    @Test
    fun testNullIntentGetEndpoint() {
        assertNull(deepLinkUtils.getEndpoint(null as Intent?))
    }

    @Test
    fun testNullIntentIsValidUri() {
        assertFalse(deepLinkUtils.isValidUri(null as Intent?))
    }

    @Test
    fun testNullStringBuildIntentStack() {
        assertNull(deepLinkUtils.buildIntentStack(application, null as String?, NORCAL))
    }

    @Test
    fun testNullStringGetEndpoint() {
        assertNull(deepLinkUtils.getEndpoint(null as String?))
    }

    @Test
    fun testNullStringIsValidUri() {
        assertFalse(deepLinkUtils.isValidUri(null as String?))
    }

    @Test
    fun testNullUriBuildIntentStack() {
        assertNull(deepLinkUtils.buildIntentStack(application, null as Uri?, NYC))
    }

    @Test
    fun testNullUriGetEndpoint() {
        assertNull(deepLinkUtils.getEndpoint(null as Uri?))
    }

    @Test
    fun testNullUriIsValidUri() {
        assertFalse(deepLinkUtils.isValidUri(null as Uri?))
    }

    @Test
    fun testPlayerGingerBuildIntentStack() {
        val intentStack = deepLinkUtils.buildIntentStack(application, PLAYER_GINGER, CHICAGO)
        assertNotNull(intentStack)
        assertEquals(3, intentStack?.size)
    }

    @Test
    fun testPlayerGingerGetEndpoint() {
        assertEquals(Endpoint.NOT_GAR_PR, deepLinkUtils.getEndpoint(PLAYER_GINGER))
        assertEquals(Endpoint.NOT_GAR_PR, deepLinkUtils.getEndpoint(Uri.parse(PLAYER_GINGER)))
    }

    @Test
    fun testPlayerGingerIsValidUri() {
        assertTrue(deepLinkUtils.isValidUri(PLAYER_GINGER))
        assertTrue(deepLinkUtils.isValidUri(Uri.parse(PLAYER_GINGER)))
    }

    @Test
    fun testPlayerSfatBuildIntentStack() {
        val intentStack = deepLinkUtils.buildIntentStack(application, PLAYER_SFAT, NORCAL)
        assertNotNull(intentStack)
        assertEquals(3, intentStack?.size)
    }

    @Test
    fun testPlayerSfatIsValidUri() {
        assertTrue(deepLinkUtils.isValidUri(PLAYER_SFAT))
        assertTrue(deepLinkUtils.isValidUri(Uri.parse(PLAYER_SFAT)))
    }

    @Test
    fun testPlayerSwedishDelightBuildIntentStack() {
        val intentStack = deepLinkUtils.buildIntentStack(application, PLAYER_SWEDISH_DELIGHT, NYC)
        assertNotNull(intentStack)
        assertEquals(3, intentStack?.size)
    }

    @Test
    fun testPlayerSwedishDelightIsValidUri() {
        assertTrue(deepLinkUtils.isValidUri(PLAYER_SWEDISH_DELIGHT))
        assertTrue(deepLinkUtils.isValidUri(Uri.parse(PLAYER_SWEDISH_DELIGHT)))
    }

    @Test
    fun testPlayersGeorgiaIsValidUri() {
        assertTrue(deepLinkUtils.isValidUri(PLAYERS_GEORGIA))
        assertTrue(deepLinkUtils.isValidUri(Uri.parse(PLAYERS_GEORGIA)))
    }

    @Test
    fun testPlayersNorcalBuildIntentStack() {
        val intentStack = deepLinkUtils.buildIntentStack(application, PLAYERS_NORCAL, NORCAL)
        assertNotNull(intentStack)
        assertEquals(2, intentStack?.size)
    }

    @Test
    fun testPlayersNorcalGetEndpoint() {
        assertEquals(Endpoint.GAR_PR, deepLinkUtils.getEndpoint(PLAYERS_NORCAL))
        assertEquals(Endpoint.GAR_PR, deepLinkUtils.getEndpoint(Uri.parse(PLAYERS_NORCAL)))
    }

    @Test
    fun testPlayersNorcalIsValidUri() {
        assertTrue(deepLinkUtils.isValidUri(PLAYERS_NORCAL))
        assertTrue(deepLinkUtils.isValidUri(Uri.parse(PLAYERS_NORCAL)))
    }

    @Test
    fun testRankingsGoogleMtvBuildIntentStack() {
        val intentStack = deepLinkUtils.buildIntentStack(application, RANKINGS_GOOGLEMTV, GOOGLE_MTV)
        assertNotNull(intentStack)
        assertEquals(2, intentStack?.size)
    }

    @Test
    fun testRankingsGoogleMtvIsValidUri() {
        assertTrue(deepLinkUtils.isValidUri(RANKINGS_GOOGLEMTV))
        assertTrue(deepLinkUtils.isValidUri(Uri.parse(RANKINGS_GOOGLEMTV)))
    }

    @Test
    fun testRankingsLongIslandIsValidUri() {
        assertTrue(deepLinkUtils.isValidUri(RANKINGS_LONG_ISLAND))
        assertTrue(deepLinkUtils.isValidUri(Uri.parse(RANKINGS_LONG_ISLAND)))
    }

    @Test
    fun testRankingsNorcalBuildIntentStack() {
        val intentStack = deepLinkUtils.buildIntentStack(application, RANKINGS_NORCAL, NORCAL)
        assertNotNull(intentStack)
        assertEquals(1, intentStack?.size)
    }

    @Test
    fun testRankingsNorcalGetEndpoint() {
        assertEquals(Endpoint.GAR_PR, deepLinkUtils.getEndpoint(RANKINGS_NORCAL))
        assertEquals(Endpoint.GAR_PR, deepLinkUtils.getEndpoint(Uri.parse(RANKINGS_NORCAL)))
    }

    @Test
    fun testRankingsNorcalIsValidUri() {
        assertTrue(deepLinkUtils.isValidUri(RANKINGS_NORCAL))
        assertTrue(deepLinkUtils.isValidUri(Uri.parse(RANKINGS_NORCAL)))
    }

    @Test
    fun testTournamentApolloIiiBuildIntentStack() {
        val intentStack = deepLinkUtils.buildIntentStack(application, TOURNAMENT_APOLLO_III, NYC)
        assertNotNull(intentStack)
        assertEquals(3, intentStack?.size)
    }

    @Test
    fun testTournamentApolloIiiIsValidUri() {
        assertTrue(deepLinkUtils.isValidUri(TOURNAMENT_APOLLO_III))
        assertTrue(deepLinkUtils.isValidUri(Uri.parse(TOURNAMENT_APOLLO_III)))
    }

    @Test
    fun testTournamentNorcalValidated2IsValidUri() {
        assertTrue(deepLinkUtils.isValidUri(TOURNAMENT_NORCAL_VALIDATED_2))
        assertTrue(deepLinkUtils.isValidUri(Uri.parse(TOURNAMENT_NORCAL_VALIDATED_2)))
    }

    @Test
    fun testTournamentNorcalValidated2BuildIntentStack() {
        val intentStack = deepLinkUtils.buildIntentStack(application,
                TOURNAMENT_NORCAL_VALIDATED_2, NORCAL)
        assertNotNull(intentStack)
        assertEquals(2, intentStack?.size)
    }

    @Test
    fun testTournamentsNorcalIsValidUri() {
        assertTrue(deepLinkUtils.isValidUri(TOURNAMENTS_NORCAL))
        assertTrue(deepLinkUtils.isValidUri(Uri.parse(TOURNAMENTS_NORCAL)))
    }

    @Test
    fun testTournamentsNycBuildIntentStack() {
        val intentStack = deepLinkUtils.buildIntentStack(application, TOURNAMENTS_NYC, NYC)
        assertNotNull(intentStack)
        assertEquals(2, intentStack?.size)
    }

    @Test
    fun testTournamentsNycGetEndpoint() {
        assertEquals(Endpoint.NOT_GAR_PR, deepLinkUtils.getEndpoint(TOURNAMENTS_NYC))
        assertEquals(Endpoint.NOT_GAR_PR, deepLinkUtils.getEndpoint(Uri.parse(TOURNAMENTS_NYC)))
    }

    @Test
    fun testTournamentsNycIsValidUri() {
        assertTrue(deepLinkUtils.isValidUri(TOURNAMENTS_NYC))
        assertTrue(deepLinkUtils.isValidUri(Uri.parse(TOURNAMENTS_NYC)))
    }

    @Test
    fun testWhitespaceStringBuildIntentStack() {
        assertNull(deepLinkUtils.buildIntentStack(application, " ", GEORGIA))
    }

    @Test
    fun testWhitespaceStringGetEndpoint() {
        assertNull(deepLinkUtils.getEndpoint(" "))
    }

    @Test
    fun testWhitespaceStringIsValidUri() {
        assertFalse(deepLinkUtils.isValidUri(" "))
    }

}
