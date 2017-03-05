package com.garpr.android.misc;

import com.garpr.android.BaseTest;
import com.garpr.android.BuildConfig;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class FavoritePlayersManagerTest extends BaseTest {

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        getTestAppComponent().inject(this);
    }

    // TODO

}
