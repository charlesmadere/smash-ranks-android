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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class SimpleDateTest extends BaseTest {

    private static final String JSON_ONE = "\"01/05/17\"";

    @Inject
    Gson mGson;


    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        getTestAppComponent().inject(this);
    }

    @Test
    public void testFromJsonOne() throws Exception {
        final SimpleDate simpleDate = mGson.fromJson(JSON_ONE, SimpleDate.class);
        assertNotNull(simpleDate);
    }

    @Test
    public void testFromNull() throws Exception {
        final SimpleDate simpleDate = mGson.fromJson((String) null, SimpleDate.class);
        assertNull(simpleDate);
    }

    @Test
    public void testToJsonOne() throws Exception {
        final SimpleDate simpleDate1 = mGson.fromJson(JSON_ONE, SimpleDate.class);
        final String json = mGson.toJson(simpleDate1, SimpleDate.class);
        final SimpleDate simpleDate2 = mGson.fromJson(json, SimpleDate.class);
        assertEquals(simpleDate1, simpleDate2);
    }

}
