package com.garpr.android.misc;

import com.garpr.android.BaseTest;
import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;

import static org.junit.Assert.assertNull;

public class PreviousRankUtilsTest extends BaseTest {

    private static final String JSON_RANKING_DECREASE = "";
    private static final String JSON_RANKING_INCREASE = "";

    @Inject
    Gson mGson;

    @Inject
    PreviousRankUtils mPreviousRankUtils;


    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        getTestAppComponent().inject(this);
    }

    @Test
    public void testCheckRankingWithNull() throws Exception {
        assertNull(mPreviousRankUtils.checkRanking(null));
    }

}
