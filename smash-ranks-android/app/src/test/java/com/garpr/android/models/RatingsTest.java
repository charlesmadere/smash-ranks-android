package com.garpr.android.models;

import android.os.Parcel;

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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class RatingsTest extends BaseTest {

    private static final String JSON_EMPTY = "{}";
    private static final String JSON_ONE_REGION = "{\"norcal\":{\"mu\":15.269053375132192,\"sigma\":5.775310761618352}}";
    private static final String JSON_TWO_REGIONS = "{\"googlemtv\":{\"mu\":41.24745212960596,\"sigma\":1.9514551959778643},\"norcal\":{\"mu\":31.594266212778816,\"sigma\":4.079428288254091}}";

    @Inject
    Gson mGson;


    @Before
    @Override
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

    @Test
    public void testGetRegionEmptyString() throws Exception {
        final Ratings ratings = mGson.fromJson(JSON_ONE_REGION, Ratings.class);
        assertNull(ratings.getRegion(""));
    }

    @Test
    public void testGetRegionNull() throws Exception {
        final Ratings ratings = mGson.fromJson(JSON_ONE_REGION, Ratings.class);
        assertNull(ratings.getRegion(null));
    }

    @Test
    public void testGetRegionOneRegion() throws Exception {
        final Ratings ratings = mGson.fromJson(JSON_ONE_REGION, Ratings.class);
        assertNotNull(ratings.getRegion("norcal"));
        assertNull(ratings.getRegion("georgia"));
    }

    @Test
    public void testGetRegionTwoRegions() throws Exception {
        final Ratings ratings = mGson.fromJson(JSON_TWO_REGIONS, Ratings.class);
        assertNotNull(ratings.getRegion("googlemtv"));
        assertNotNull(ratings.getRegion("norcal"));
        assertNull(ratings.getRegion("nyc"));
    }

    @Test
    public void testParcelableOneRegion() throws Exception {
        final Ratings before = mGson.fromJson(JSON_ONE_REGION, Ratings.class);

        final Parcel parcel = Parcel.obtain();
        parcel.writeParcelable(before, 0);
        parcel.setDataPosition(0);

        final Ratings after = parcel.readParcelable(Ratings.class.getClassLoader());
        assertEquals(before.size(), after.size());
        assertEquals(before.get(0).getRegion(), after.get(0).getRegion());

        parcel.recycle();
    }

    @Test
    public void testParcelableTwoRegions() throws Exception {
        final Ratings before = mGson.fromJson(JSON_TWO_REGIONS, Ratings.class);

        final Parcel parcel = Parcel.obtain();
        parcel.writeParcelable(before, 0);
        parcel.setDataPosition(0);

        final Ratings after = parcel.readParcelable(Ratings.class.getClassLoader());
        assertEquals(before.size(), after.size());
        assertEquals(before.get(0).getRegion(), after.get(0).getRegion());
        assertEquals(before.get(1).getRegion(), after.get(1).getRegion());

        parcel.recycle();
    }

    @Test
    public void testToJsonEmptyNull() throws Exception {
        final Ratings ratings = mGson.fromJson(JSON_EMPTY, Ratings.class);
        assertNull(ratings);
    }

    @Test
    public void testToJsonOneRegionNotNull() throws Exception {
        final Ratings ratings = mGson.fromJson(JSON_ONE_REGION, Ratings.class);
        assertNotNull(mGson.toJson(ratings, Ratings.class));
    }

    @Test
    public void testToJsonTwoRegionsNotNull() throws Exception {
        final Ratings ratings = mGson.fromJson(JSON_TWO_REGIONS, Ratings.class);
        assertNotNull(mGson.toJson(ratings, Ratings.class));
    }

    @Test
    public void testToJsonAndBackOneRegion() throws Exception {
        Ratings ratings = mGson.fromJson(JSON_ONE_REGION, Ratings.class);
        final String json = mGson.toJson(ratings, Ratings.class);
        ratings = mGson.fromJson(json, Ratings.class);
        assertNotNull(ratings);
        assertEquals(ratings.size(), 1);
    }

    @Test
    public void testToJsonAndBackTwoRegions() throws Exception {
        Ratings ratings = mGson.fromJson(JSON_TWO_REGIONS, Ratings.class);
        final String json = mGson.toJson(ratings, Ratings.class);
        ratings = mGson.fromJson(json, Ratings.class);
        assertNotNull(ratings);
        assertEquals(ratings.size(), 2);
    }

}
