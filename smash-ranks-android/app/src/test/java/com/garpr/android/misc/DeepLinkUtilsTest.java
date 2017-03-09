package com.garpr.android.misc;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;

import com.garpr.android.BaseTest;
import com.garpr.android.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class DeepLinkUtilsTest extends BaseTest {

    private static final String GOOGLEMTV_RANKINGS = "https://www.garpr.com/#/googlemtv/rankings";

    private static final String NORCAL_PLAYERS = "https://www.garpr.com/#/norcal/players";
    private static final String NORCAL_RANKINGS = "https://www.garpr.com/#/norcal/rankings";

    private static final String PLAYER_SFAT = "https://www.garpr.com/#/norcal/players/588852e8d2994e3bbfa52d88";

    private static final String TOURNAMENT_NORCAL_VALIDATED_2 = "https://www.garpr.com/#/norcal/tournaments/58a00514d2994e4d0f2e25a6";

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
        final Intent[] intentStack = mDeepLinkUtils.buildIntentStack(mApplication, "");
        assertNull(intentStack);
    }

    @Test
    public void testGoogleMtvRankingsBuildIntentStack() throws Exception {
        final Intent[] intentStack = mDeepLinkUtils.buildIntentStack(mApplication, GOOGLEMTV_RANKINGS);
        assertNotNull(intentStack);
        assertEquals(intentStack.length, 2);
    }

    @Test
    public void testNorcalPlayersBuildIntentStack() throws Exception {
        final Intent[] intentStack = mDeepLinkUtils.buildIntentStack(mApplication, NORCAL_PLAYERS);
        assertNotNull(intentStack);
        assertEquals(intentStack.length, 2);
    }

    @Test
    public void testNorcalRankingsBuildIntentStack() throws Exception {
        final Intent[] intentStack = mDeepLinkUtils.buildIntentStack(mApplication, NORCAL_RANKINGS);
        assertNotNull(intentStack);
        assertEquals(intentStack.length, 1);
    }

    @Test
    public void testNorcalValidated2BuildIntentStack() throws Exception {
        final Intent[] intentStack = mDeepLinkUtils.buildIntentStack(mApplication,
                TOURNAMENT_NORCAL_VALIDATED_2);

        assertNotNull(intentStack);
        assertEquals(intentStack.length, 2);
    }

    @Test
    public void testNullIntentBuildIntentStack() throws Exception {
        final Intent[] intentStack = mDeepLinkUtils.buildIntentStack(mApplication, (Intent) null);
        assertNull(intentStack);
    }

    @Test
    public void testNullStringBuildIntentStack() throws Exception {
        final Intent[] intentStack = mDeepLinkUtils.buildIntentStack(mApplication, (String) null);
        assertNull(intentStack);
    }

    @Test
    public void testNullUriBuildIntentStack() throws Exception {
        final Intent[] intentStack = mDeepLinkUtils.buildIntentStack(mApplication, (Uri) null);
        assertNull(intentStack);
    }

    @Test
    public void testSfatBuildIntentStack() throws Exception {
        final Intent[] intentStack = mDeepLinkUtils.buildIntentStack(mApplication, PLAYER_SFAT);
        assertNotNull(intentStack);
        assertEquals(intentStack.length, 3);
    }

    @Test
    public void testWhitespaceStringBuildIntentStack() throws Exception {
        final Intent[] intentStack = mDeepLinkUtils.buildIntentStack(mApplication, " ");
        assertNull(intentStack);
    }

}
