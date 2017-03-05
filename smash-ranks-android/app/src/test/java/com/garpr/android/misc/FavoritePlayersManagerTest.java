package com.garpr.android.misc;

import com.garpr.android.BaseTest;
import com.garpr.android.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import javax.inject.Inject;

import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class FavoritePlayersManagerTest extends BaseTest {

    @Inject
    FavoritePlayersManager mFavoritePlayersManager;


    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        getTestAppComponent().inject(this);
    }

    @Test
    public void testIsEmpty() {
        assertTrue(mFavoritePlayersManager.isEmpty());
    }

}
