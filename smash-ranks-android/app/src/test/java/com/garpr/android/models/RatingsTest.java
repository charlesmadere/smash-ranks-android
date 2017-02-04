package com.garpr.android.models;

import com.garpr.android.BaseTest;
import com.garpr.android.BuildConfig;
import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class RatingsTest extends BaseTest {

    private static final String JSON_EMPTY = "{}";
    private static final String JSON_ONE_REGION = "{\"norcal\":{\"mu\":15.269053375132192,\"sigma\":5.775310761618352}}";
    private static final String JSON_TWO_REGIONS = "{\"googlemtv\":{\"mu\":41.24745212960596,\"sigma\":1.9514551959778643},\"norcal\":{\"mu\":31.594266212778816,\"sigma\":4.079428288254091}}";

    @Inject
    Gson mGson;


    @Before
    public void setUp() throws Exception {
        super.setUp();
        getTestAppComponent().inject(this);
    }

    @Test
    public void testFromJsonEmpty() throws Exception {
        final Ratings ratings = mGson.fromJson(JSON_EMPTY, Ratings.class);
        assertNull(ratings);
    }

    @Test
    public void testFromJsonOneRegion() throws Exception {
        final Ratings ratings = mGson.fromJson(JSON_ONE_REGION, Ratings.class);
        assertNotNull(ratings);
        assertEquals(ratings.size(), 1);

        final Rating rating = ratings.get(0);
        assertEquals(rating.getRegion(), "norcal");
    }

    @Test
    public void testFromJsonTwoRegions() throws Exception {
        final Ratings ratings = mGson.fromJson(JSON_TWO_REGIONS, Ratings.class);
        assertNotNull(ratings);
        assertEquals(ratings.size(), 2);

        Rating rating = ratings.get(0);
        assertEquals(rating.getRegion(), "googlemtv");

        rating = ratings.get(1);
        assertEquals(rating.getRegion(), "norcal");
    }

    @Test
    public void testFromNull() throws Exception {
        final Ratings ratings = mGson.fromJson((String) null, Ratings.class);
        assertNull(ratings);
    }

}
