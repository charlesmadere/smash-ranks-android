package com.garpr.android.misc;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;

import com.garpr.android.BaseTest;
import com.garpr.android.BuildConfig;
import com.garpr.android.models.Endpoint;
import com.garpr.android.models.Region;
import com.garpr.android.models.RegionsBundle;
import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class DeepLinkUtilsTest extends BaseTest {

    private static final String PLAYER_GINGER = "https://www.notgarpr.com/#/chicago/players/57983b42e592573cf1845ff2";
    private static final String PLAYER_SFAT = "https://www.garpr.com/#/norcal/players/588852e8d2994e3bbfa52d88";
    private static final String PLAYER_SWEDISH_DELIGHT = "https://www.notgarpr.com/#/nyc/players/545b240b8ab65f7a95f74940";

    private static final String PLAYERS_GEORGIA = "https://www.notgarpr.com/#/georgia/players";
    private static final String PLAYERS_NORCAL = "https://www.garpr.com/#/norcal/players";

    private static final String RANKINGS_GOOGLEMTV = "https://www.garpr.com/#/googlemtv/rankings";
    private static final String RANKINGS_LONG_ISLAND = "https://www.notgarpr.com/#/li/rankings";
    private static final String RANKINGS_NORCAL = "https://www.garpr.com/#/norcal/rankings";

    private static final String TOURNAMENT_APOLLO_III = "https://www.notgarpr.com/#/nyc/tournaments/58c72c801d41c8259fa1f8bf";
    private static final String TOURNAMENT_NORCAL_VALIDATED_2 = "https://www.garpr.com/#/norcal/tournaments/58a00514d2994e4d0f2e25a6";

    private static final String TOURNAMENTS_NORCAL = "https://www.garpr.com/#/norcal/tournaments";
    private static final String TOURNAMENTS_NYC = "https://www.notgarpr.com/#/nyc/tournaments";

    private static final String JSON_REGION_CHICAGO = "{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":60,\"display_name\":\"Chicago\",\"id\":\"chicago\",\"tournament_qualified_day_limit\":999}";
    private static final String JSON_REGION_GEORGIA = "{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":75,\"display_name\":\"Georgia\",\"id\":\"georgia\",\"tournament_qualified_day_limit\":180}";
    private static final String JSON_REGION_GOOGLE_MTV = "{\"ranking_num_tourneys_attended\":1,\"ranking_activity_day_limit\":60,\"display_name\":\"Google MTV\",\"id\":\"googlemtv\",\"tournament_qualified_day_limit\":999}";
    private static final String JSON_REGION_NORCAL = "{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":30,\"display_name\":\"Norcal\",\"id\":\"norcal\",\"tournament_qualified_day_limit\":1000}";
    private static final String JSON_REGION_NYC = "{\"ranking_num_tourneys_attended\":6,\"ranking_activity_day_limit\":90,\"display_name\":\"NYC Metro Area\",\"id\":\"nyc\",\"tournament_qualified_day_limit\":999}";

    private static final String JSON_REGIONS_BUNDLE = "{\"regions\":[{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":60,\"display_name\":\"Alabama\",\"id\":\"alabama\",\"tournament_qualified_day_limit\":999},{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":60,\"display_name\":\"Central Florida\",\"id\":\"cfl\",\"tournament_qualified_day_limit\":999},{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":60,\"display_name\":\"Chicago\",\"id\":\"chicago\",\"tournament_qualified_day_limit\":999},{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":75,\"display_name\":\"Georgia\",\"id\":\"georgia\",\"tournament_qualified_day_limit\":180},{\"ranking_num_tourneys_attended\":3,\"ranking_activity_day_limit\":999,\"display_name\":\"Georgia Smash 4\",\"id\":\"georgia smash 4\",\"tournament_qualified_day_limit\":999},{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":60,\"display_name\":\"Georgia Teams\",\"id\":\"georgia teams\",\"tournament_qualified_day_limit\":999},{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":60,\"display_name\":\"GeorgiaTeams\",\"id\":\"georgiateams\",\"tournament_qualified_day_limit\":999},{\"ranking_num_tourneys_attended\":6,\"ranking_activity_day_limit\":91,\"display_name\":\"Long Island\",\"id\":\"li\",\"tournament_qualified_day_limit\":366},{\"ranking_num_tourneys_attended\":6,\"ranking_activity_day_limit\":90,\"display_name\":\"NYC Metro Area\",\"id\":\"nyc\",\"tournament_qualified_day_limit\":99999},{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":60,\"display_name\":\"NYC Smash 64\",\"id\":\"nyc64\",\"tournament_qualified_day_limit\":999},{\"ranking_num_tourneys_attended\":4,\"ranking_activity_day_limit\":99999,\"display_name\":\"New England\",\"id\":\"newengland\",\"tournament_qualified_day_limit\":999},{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":90,\"display_name\":\"New Jersey\",\"id\":\"newjersey\",\"tournament_qualified_day_limit\":9999999},{\"ranking_num_tourneys_attended\":4,\"ranking_activity_day_limit\":120,\"display_name\":\"North Carolina\",\"id\":\"northcarolina\",\"tournament_qualified_day_limit\":365},{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":60,\"display_name\":\"Philadelphia\",\"id\":\"philadelphia\",\"tournament_qualified_day_limit\":999},{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":60,\"display_name\":\"Pittsburgh\",\"id\":\"pittsburgh\",\"tournament_qualified_day_limit\":999},{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":60,\"display_name\":\"South Carolina\",\"id\":\"southcarolina\",\"tournament_qualified_day_limit\":999},{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":60,\"display_name\":\"Westchester\",\"id\":\"westchester\",\"tournament_qualified_day_limit\":999},{\"ranking_num_tourneys_attended\":1,\"ranking_activity_day_limit\":60,\"display_name\":\"Google MTV\",\"id\":\"googlemtv\",\"tournament_qualified_day_limit\":999},{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":30,\"display_name\":\"Norcal\",\"id\":\"norcal\",\"tournament_qualified_day_limit\":1000}]}";
    private static final String JSON_REGIONS_BUNDLE_EMPTY = "{\"regions\":[]}";

    private Region mChicago;
    private Region mGeorgia;
    private Region mGoogleMtv;
    private Region mNorcal;
    private Region mNyc;
    private RegionsBundle mRegionsBundle;
    private RegionsBundle mRegionsBundleEmpty;

    @Inject
    Application mApplication;

    @Inject
    DeepLinkUtils mDeepLinkUtils;

    @Inject
    Gson mGson;


    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        getTestAppComponent().inject(this);

        mChicago = mGson.fromJson(JSON_REGION_CHICAGO, Region.class);
        mGeorgia = mGson.fromJson(JSON_REGION_GEORGIA, Region.class);
        mGoogleMtv = mGson.fromJson(JSON_REGION_GOOGLE_MTV, Region.class);
        mNorcal = mGson.fromJson(JSON_REGION_NORCAL, Region.class);
        mNyc = mGson.fromJson(JSON_REGION_NYC, Region.class);

        mRegionsBundle = mGson.fromJson(JSON_REGIONS_BUNDLE, RegionsBundle.class);
        mRegionsBundleEmpty = mGson.fromJson(JSON_REGIONS_BUNDLE_EMPTY, RegionsBundle.class);
    }

    @Test
    public void testGetRegion() throws Exception {
        assertNull(mDeepLinkUtils.getRegion(null, null));
        assertNull(mDeepLinkUtils.getRegion(null, Endpoint.GAR_PR));
        assertNull(mDeepLinkUtils.getRegion(mRegionsBundle, null));
        assertNull(mDeepLinkUtils.getRegion(mRegionsBundleEmpty, null));
        assertNull(mDeepLinkUtils.getRegion(mRegionsBundleEmpty, Endpoint.NOT_GAR_PR));

        Region region = mDeepLinkUtils.getRegion(mRegionsBundle, Endpoint.GAR_PR);
        assertNotNull(region);

        region = mDeepLinkUtils.getRegion(mRegionsBundle, Endpoint.NOT_GAR_PR);
        assertNotNull(region);
    }

    @Test
    public void testEmptyStringBuildIntentStack() throws Exception {
        assertNull(mDeepLinkUtils.buildIntentStack(mApplication, "", mGoogleMtv));
    }

    @Test
    public void testEmptyStringIsValidUri() throws Exception {
        assertFalse(mDeepLinkUtils.isValidUri(""));
    }

    @Test
    public void testNullIntentBuildIntentStack() throws Exception {
        assertNull(mDeepLinkUtils.buildIntentStack(mApplication, (Intent) null, mGeorgia));
    }

    @Test
    public void testNullIntentGetEndpoint() throws Exception {
        assertNull(mDeepLinkUtils.getEndpoint((Intent) null));
    }

    @Test
    public void testNullIntentIsValidUri() throws Exception {
        assertFalse(mDeepLinkUtils.isValidUri((Intent) null));
    }

    @Test
    public void testNullStringBuildIntentStack() throws Exception {
        assertNull(mDeepLinkUtils.buildIntentStack(mApplication, (String) null, mNorcal));
    }

    @Test
    public void testNullStringGetEndpoint() throws Exception {
        assertNull(mDeepLinkUtils.getEndpoint((String) null));
    }

    @Test
    public void testNullStringIsValidUri() throws Exception {
        assertFalse(mDeepLinkUtils.isValidUri((String) null));
    }

    @Test
    public void testNullUriBuildIntentStack() throws Exception {
        assertNull(mDeepLinkUtils.buildIntentStack(mApplication, (Uri) null, mNyc));
    }

    @Test
    public void testNullUriGetEndpoint() throws Exception {
        assertNull(mDeepLinkUtils.getEndpoint((Uri) null));
    }

    @Test
    public void testNullUriIsValidUri() throws Exception {
        assertFalse(mDeepLinkUtils.isValidUri((Uri) null));
    }

    @Test
    public void testPlayerGingerBuildIntentStack() throws Exception {
        final Intent[] intentStack = mDeepLinkUtils.buildIntentStack(mApplication, PLAYER_GINGER,
                mChicago);
        assertNotNull(intentStack);
        assertEquals(intentStack.length, 3);
    }

    @Test
    public void testPlayerGingerGetEndpoint() throws Exception {
        assertEquals(Endpoint.NOT_GAR_PR, mDeepLinkUtils.getEndpoint(PLAYER_GINGER));
        assertEquals(Endpoint.NOT_GAR_PR, mDeepLinkUtils.getEndpoint(Uri.parse(PLAYER_GINGER)));
    }

    @Test
    public void testPlayerGingerIsValidUri() throws Exception {
        assertTrue(mDeepLinkUtils.isValidUri(PLAYER_GINGER));
        assertTrue(mDeepLinkUtils.isValidUri(Uri.parse(PLAYER_GINGER)));
    }

    @Test
    public void testPlayerSfatBuildIntentStack() throws Exception {
        final Intent[] intentStack = mDeepLinkUtils.buildIntentStack(mApplication, PLAYER_SFAT,
                mNorcal);
        assertNotNull(intentStack);
        assertEquals(intentStack.length, 3);
    }

    @Test
    public void testPlayerSfatIsValidUri() throws Exception {
        assertTrue(mDeepLinkUtils.isValidUri(PLAYER_SFAT));
        assertTrue(mDeepLinkUtils.isValidUri(Uri.parse(PLAYER_SFAT)));
    }

    @Test
    public void testPlayerSwedishDelightBuildIntentStack() throws Exception {
        final Intent[] intentStack = mDeepLinkUtils.buildIntentStack(mApplication,
                PLAYER_SWEDISH_DELIGHT, mNyc);
        assertNotNull(intentStack);
        assertEquals(intentStack.length, 3);
    }

    @Test
    public void testPlayerSwedishDelightIsValidUri() throws Exception {
        assertTrue(mDeepLinkUtils.isValidUri(PLAYER_SWEDISH_DELIGHT));
        assertTrue(mDeepLinkUtils.isValidUri(Uri.parse(PLAYER_SWEDISH_DELIGHT)));
    }

    @Test
    public void testPlayersGeorgiaIsValidUri() throws Exception {
        assertTrue(mDeepLinkUtils.isValidUri(PLAYERS_GEORGIA));
        assertTrue(mDeepLinkUtils.isValidUri(Uri.parse(PLAYERS_GEORGIA)));
    }

    @Test
    public void testPlayersNorcalBuildIntentStack() throws Exception {
        final Intent[] intentStack = mDeepLinkUtils.buildIntentStack(mApplication, PLAYERS_NORCAL,
                mNorcal);
        assertNotNull(intentStack);
        assertEquals(intentStack.length, 2);
    }

    @Test
    public void testPlayersNorcalGetEndpoint() throws Exception {
        assertEquals(Endpoint.GAR_PR, mDeepLinkUtils.getEndpoint(PLAYERS_NORCAL));
        assertEquals(Endpoint.GAR_PR, mDeepLinkUtils.getEndpoint(Uri.parse(PLAYERS_NORCAL)));
    }

    @Test
    public void testPlayersNorcalIsValidUri() throws Exception {
        assertTrue(mDeepLinkUtils.isValidUri(PLAYERS_NORCAL));
        assertTrue(mDeepLinkUtils.isValidUri(Uri.parse(PLAYERS_NORCAL)));
    }

    @Test
    public void testRankingsGoogleMtvBuildIntentStack() throws Exception {
        final Intent[] intentStack = mDeepLinkUtils.buildIntentStack(mApplication,
                RANKINGS_GOOGLEMTV, mGoogleMtv);
        assertNotNull(intentStack);
        assertEquals(intentStack.length, 2);
    }

    @Test
    public void testRankingsGoogleMtvIsValidUri() throws Exception {
        assertTrue(mDeepLinkUtils.isValidUri(RANKINGS_GOOGLEMTV));
        assertTrue(mDeepLinkUtils.isValidUri(Uri.parse(RANKINGS_GOOGLEMTV)));
    }

    @Test
    public void testRankingsLongIslandIsValidUri() throws Exception {
        assertTrue(mDeepLinkUtils.isValidUri(RANKINGS_LONG_ISLAND));
        assertTrue(mDeepLinkUtils.isValidUri(Uri.parse(RANKINGS_LONG_ISLAND)));
    }

    @Test
    public void testRankingsNorcalBuildIntentStack() throws Exception {
        final Intent[] intentStack = mDeepLinkUtils.buildIntentStack(mApplication, RANKINGS_NORCAL,
                mNorcal);
        assertNotNull(intentStack);
        assertEquals(intentStack.length, 1);
    }

    @Test
    public void testRankingsNorcalGetEndpoint() throws Exception {
        assertEquals(Endpoint.GAR_PR, mDeepLinkUtils.getEndpoint(RANKINGS_NORCAL));
        assertEquals(Endpoint.GAR_PR, mDeepLinkUtils.getEndpoint(Uri.parse(RANKINGS_NORCAL)));
    }

    @Test
    public void testRankingsNorcalIsValidUri() throws Exception {
        assertTrue(mDeepLinkUtils.isValidUri(RANKINGS_NORCAL));
        assertTrue(mDeepLinkUtils.isValidUri(Uri.parse(RANKINGS_NORCAL)));
    }

    @Test
    public void testTournamentApolloIiiBuildIntentStack() throws Exception {
        final Intent[] intentStack = mDeepLinkUtils.buildIntentStack(mApplication,
                TOURNAMENT_APOLLO_III, mNyc);
        assertNotNull(intentStack);
        assertEquals(intentStack.length, 3);
    }

    @Test
    public void testTournamentApolloIiiIsValidUri() throws Exception {
        assertTrue(mDeepLinkUtils.isValidUri(TOURNAMENT_APOLLO_III));
        assertTrue(mDeepLinkUtils.isValidUri(Uri.parse(TOURNAMENT_APOLLO_III)));
    }

    @Test
    public void testTournamentNorcalValidated2IsValidUri() throws Exception {
        assertTrue(mDeepLinkUtils.isValidUri(TOURNAMENT_NORCAL_VALIDATED_2));
        assertTrue(mDeepLinkUtils.isValidUri(Uri.parse(TOURNAMENT_NORCAL_VALIDATED_2)));
    }

    @Test
    public void testTournamentNorcalValidated2BuildIntentStack() throws Exception {
        final Intent[] intentStack = mDeepLinkUtils.buildIntentStack(mApplication,
                TOURNAMENT_NORCAL_VALIDATED_2, mNorcal);
        assertNotNull(intentStack);
        assertEquals(intentStack.length, 2);
    }

    @Test
    public void testTournamentsNorcalIsValidUri() throws Exception {
        assertTrue(mDeepLinkUtils.isValidUri(TOURNAMENTS_NORCAL));
        assertTrue(mDeepLinkUtils.isValidUri(Uri.parse(TOURNAMENTS_NORCAL)));
    }

    @Test
    public void testTournamentsNycBuildIntentStack() throws Exception {
        final Intent[] intentStack = mDeepLinkUtils.buildIntentStack(mApplication, TOURNAMENTS_NYC,
                mNyc);
        assertNotNull(intentStack);
        assertEquals(intentStack.length, 2);
    }

    @Test
    public void testTournamentsNycGetEndpoint() throws Exception {
        assertEquals(Endpoint.NOT_GAR_PR, mDeepLinkUtils.getEndpoint(TOURNAMENTS_NYC));
        assertEquals(Endpoint.NOT_GAR_PR, mDeepLinkUtils.getEndpoint(Uri.parse(TOURNAMENTS_NYC)));
    }

    @Test
    public void testTournamentsNycIsValidUri() throws Exception {
        assertTrue(mDeepLinkUtils.isValidUri(TOURNAMENTS_NYC));
        assertTrue(mDeepLinkUtils.isValidUri(Uri.parse(TOURNAMENTS_NYC)));
    }

    @Test
    public void testWhitespaceStringBuildIntentStack() throws Exception {
        assertNull(mDeepLinkUtils.buildIntentStack(mApplication, " ", mGeorgia));
    }

    @Test
    public void testWhitespaceStringGetEndpoint() throws Exception {
        assertNull(mDeepLinkUtils.getEndpoint(" "));
    }

    @Test
    public void testWhitespaceStringIsValidUri() throws Exception {
        assertFalse(mDeepLinkUtils.isValidUri(" "));
    }

}
