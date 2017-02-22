package com.garpr.android.models;

import com.garpr.android.BaseTest;
import com.garpr.android.BuildConfig;
import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Calendar;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class SimpleDateTest extends BaseTest {

    private static final String JSON_ONE = "\"01/05/17\"";
    private static final String JSON_TWO = "\"11/28/89\"";

    @Inject
    Gson mGson;


    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        getTestAppComponent().inject(this);
    }

    @Test
    public void testFromJson() throws Exception {
        SimpleDate simpleDate = mGson.fromJson(JSON_ONE, SimpleDate.class);
        assertNotNull(simpleDate);

        simpleDate = mGson.fromJson(JSON_TWO, SimpleDate.class);
        assertNotNull(simpleDate);
    }

    @Test
    public void testFromNull() throws Exception {
        final SimpleDate simpleDate = mGson.fromJson((String) null, SimpleDate.class);
        assertNull(simpleDate);
    }

    @Test
    public void testGetMonth() throws Exception {
        SimpleDate simpleDate = mGson.fromJson(JSON_ONE, SimpleDate.class);
        assertEquals(simpleDate.getMonth(), Calendar.JANUARY);

        simpleDate = mGson.fromJson(JSON_TWO, SimpleDate.class);
        assertEquals(simpleDate.getMonth(), Calendar.NOVEMBER);
    }

    @Test
    public void testGetYear() throws Exception {
        SimpleDate simpleDate = mGson.fromJson(JSON_ONE, SimpleDate.class);
        assertEquals(simpleDate.getYear(), 2017);

        simpleDate = mGson.fromJson(JSON_TWO, SimpleDate.class);
        assertEquals(simpleDate.getYear(), 1989);
    }

    @Test
    public void testToJson() throws Exception {
        SimpleDate simpleDate1 = mGson.fromJson(JSON_ONE, SimpleDate.class);
        String json = mGson.toJson(simpleDate1, SimpleDate.class);
        SimpleDate simpleDate2 = mGson.fromJson(json, SimpleDate.class);
        assertEquals(simpleDate1, simpleDate2);

        simpleDate1 = mGson.fromJson(JSON_TWO, SimpleDate.class);
        json = mGson.toJson(simpleDate1, SimpleDate.class);
        simpleDate2 = mGson.fromJson(json, SimpleDate.class);
        assertEquals(simpleDate1, simpleDate2);
    }

}
