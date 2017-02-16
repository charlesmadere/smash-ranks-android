package com.garpr.android.misc;

import android.content.Intent;

import com.garpr.android.BaseTest;
import com.garpr.android.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class DeepLinkUtilsTest extends BaseTest {

    private static final String NORCAL_PLAYERS = "https://www.garpr.com/#/norcal/players";
    private static final String NORCAL_RANKINGS = "https://www.garpr.com/#/norcal/rankings";

    private static final String PLAYER_SFAT = "https://www.garpr.com/#/norcal/players/588852e8d2994e3bbfa52d88";

    private static final String TOURNAMENT_NORCAL_VALIDATED_2 = "https://www.garpr.com/#/norcal/tournaments/58a00514d2994e4d0f2e25a6";

    @Inject
    DeepLinkUtils mDeepLinkUtils;


    @Override
    public void setUp() throws Exception {
        super.setUp();
        getTestAppComponent().inject(this);
    }

    @Test
    public void testNorcalPlayersBuildIntentStack() throws Exception {
        final Intent[] intentStack = mDeepLinkUtils.buildIntentStack(
                RuntimeEnvironment.application, NORCAL_PLAYERS);

        assertNotNull(intentStack);
        assertEquals(intentStack.length, 1);
    }

    @Test
    public void testNorcalRankingsBuildIntentStack() throws Exception {
        final Intent[] intentStack = mDeepLinkUtils.buildIntentStack(
                RuntimeEnvironment.application, NORCAL_RANKINGS);

        assertNotNull(intentStack);
        assertEquals(intentStack.length, 1);
    }

    @Test
    public void testNorcalValidated2BuildIntentStack() throws Exception {
        final Intent[] intentStack = mDeepLinkUtils.buildIntentStack(
                RuntimeEnvironment.application, TOURNAMENT_NORCAL_VALIDATED_2);

        assertNotNull(intentStack);
        assertEquals(intentStack.length, 2);
    }

    @Test
    public void testSfatBuildIntentStack() throws Exception {
        final Intent[] intentStack = mDeepLinkUtils.buildIntentStack(
                RuntimeEnvironment.application, PLAYER_SFAT);

        assertNotNull(intentStack);
        assertEquals(intentStack.length, 2);
    }

}
