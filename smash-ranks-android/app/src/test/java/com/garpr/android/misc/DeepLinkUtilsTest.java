package com.garpr.android.misc;

import android.app.Application;
import android.net.Uri;

import com.garpr.android.BaseTest;
import com.garpr.android.BuildConfig;
import com.garpr.android.models.Endpoint;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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

    @Inject
    Application mApplication;

    @Inject
    DeepLinkUtils mDeepLinkUtils;


    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        getTestAppComponent().inject(this);
    }

    @Test
    public void testEmptyStringBuildIntentStack() throws Exception {
        // TODO
//        final Intent[] intentStack = mDeepLinkUtils.buildIntentStack(mApplication, "");
//        assertNull(intentStack);
    }

    @Test
    public void testEmptyStringIsValidUri() throws Exception {
        assertFalse(mDeepLinkUtils.isValidUri(""));
    }

    @Test
    public void testNullIntentBuildIntentStack() throws Exception {
        // TODO
//        final Intent[] intentStack = mDeepLinkUtils.buildIntentStack(mApplication, (Intent) null);
//        assertNull(intentStack);
    }

    @Test
    public void testNullStringBuildIntentStack() throws Exception {
        // TODO
//        final Intent[] intentStack = mDeepLinkUtils.buildIntentStack(mApplication, (String) null);
//        assertNull(intentStack);
    }

    @Test
    public void testNullStringGetEndpoint() throws Exception {
        assertNull(mDeepLinkUtils.getEndpoint(mApplication, (String) null));
    }

    @Test
    public void testNullStringIsValidUri() throws Exception {
        assertFalse(mDeepLinkUtils.isValidUri(null));
    }

    @Test
    public void testNullUriGetEndpoint() throws Exception {
        assertNull(mDeepLinkUtils.getEndpoint(mApplication, (Uri) null));
    }

    @Test
    public void testNullUriBuildIntentStack() throws Exception {
        // TODO
//        final Intent[] intentStack = mDeepLinkUtils.buildIntentStack(mApplication, (Uri) null);
//        assertNull(intentStack);
    }

    @Test
    public void testPlayerGingerIsValidUri() throws Exception {
        assertTrue(mDeepLinkUtils.isValidUri(PLAYER_GINGER));
    }

    @Test
    public void testPlayerSfatBuildIntentStack() throws Exception {
        // TODO
//        final Intent[] intentStack = mDeepLinkUtils.buildIntentStack(mApplication, PLAYER_SFAT);
//        assertNotNull(intentStack);
//        assertEquals(intentStack.length, 3);
    }

    @Test
    public void testPlayerSfatIsValidUri() throws Exception {
        assertTrue(mDeepLinkUtils.isValidUri(PLAYER_SFAT));
    }

    @Test
    public void testPlayerSwedishDelightIsValidUri() throws Exception {
        assertTrue(mDeepLinkUtils.isValidUri(PLAYER_SWEDISH_DELIGHT));
    }

    @Test
    public void testPlayersGeorgiaIsValidUri() throws Exception {
        assertTrue(mDeepLinkUtils.isValidUri(PLAYERS_GEORGIA));
    }

    @Test
    public void testPlayersNorcalBuildIntentStack() throws Exception {
        // TODO
//        final Intent[] intentStack = mDeepLinkUtils.buildIntentStack(mApplication, NORCAL_PLAYERS);
//        assertNotNull(intentStack);
//        assertEquals(intentStack.length, 2);
    }

    @Test
    public void testPlayersNorcalGetEndpoint() throws Exception {
        assertEquals(Endpoint.GAR_PR, mDeepLinkUtils.getEndpoint(mApplication, PLAYERS_NORCAL));
    }

    @Test
    public void testPlayersNorcalIsValidUri() throws Exception {
        assertTrue(mDeepLinkUtils.isValidUri(PLAYERS_NORCAL));
    }

    @Test
    public void testRankingsGoogleMtvBuildIntentStack() throws Exception {
        // TODO
//        final Intent[] intentStack = mDeepLinkUtils.buildIntentStack(mApplication, GOOGLEMTV_RANKINGS);
//        assertNotNull(intentStack);
//        assertEquals(intentStack.length, 2);
    }

    @Test
    public void testRankingsGoogleMtvIsValidUri() throws Exception {
        assertTrue(mDeepLinkUtils.isValidUri(RANKINGS_GOOGLEMTV));
    }

    @Test
    public void testRankingsLongIslandIsValidUri() throws Exception {
        assertTrue(mDeepLinkUtils.isValidUri(RANKINGS_LONG_ISLAND));
    }

    @Test
    public void testRankingsNorcalBuildIntentStack() throws Exception {
        // TODO
//        final Intent[] intentStack = mDeepLinkUtils.buildIntentStack(mApplication, NORCAL_RANKINGS);
//        assertNotNull(intentStack);
//        assertEquals(intentStack.length, 1);
    }

    @Test
    public void testRankingsNorcalIsValidUri() throws Exception {
        assertTrue(mDeepLinkUtils.isValidUri(RANKINGS_NORCAL));
    }

    @Test
    public void testTournamentApolloIiiIsValidUri() throws Exception {
        assertTrue(mDeepLinkUtils.isValidUri(TOURNAMENT_APOLLO_III));
    }

    @Test
    public void testTournamentNorcalValidated2IsValidUri() throws Exception {
        assertTrue(mDeepLinkUtils.isValidUri(TOURNAMENT_NORCAL_VALIDATED_2));
    }

    @Test
    public void testTournamentNorcalValidated2BuildIntentStack() throws Exception {
        // TODO
//        final Intent[] intentStack = mDeepLinkUtils.buildIntentStack(mApplication,
//                TOURNAMENT_NORCAL_VALIDATED_2);
//
//        assertNotNull(intentStack);
//        assertEquals(intentStack.length, 2);
    }

    @Test
    public void testTournamentsNorcalIsValidUri() throws Exception {
        assertTrue(mDeepLinkUtils.isValidUri(TOURNAMENTS_NORCAL));
    }

    @Test
    public void testTournamentsNycBuildIntentStack() throws Exception {
        // TODO
    }

    @Test
    public void testTournamentsNycGetEndpoint() throws Exception {
        assertEquals(Endpoint.NOT_GAR_PR, mDeepLinkUtils.getEndpoint(mApplication,
                TOURNAMENTS_NYC));
    }

    @Test
    public void testTournamentsNycIsValidUri() throws Exception {
        assertTrue(mDeepLinkUtils.isValidUri(TOURNAMENTS_NYC));
    }

    @Test
    public void testWhitespaceStringBuildIntentStack() throws Exception {
        // TODO
//        final Intent[] intentStack = mDeepLinkUtils.buildIntentStack(mApplication, " ");
//        assertNull(intentStack);
    }

}
