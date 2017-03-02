package com.garpr.android.models;

import com.garpr.android.BaseTest;
import com.garpr.android.BuildConfig;
import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

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
    public void testChronologicalOrder() throws Exception {
        final List<SimpleDate> list = new ArrayList<>();
        list.add(new SimpleDate(2));
        list.add(new SimpleDate(0));
        list.add(new SimpleDate(1));
        list.add(new SimpleDate(5));
        list.add(new SimpleDate(20));

        Collections.sort(list, SimpleDate.CHRONOLOGICAL_ORDER);
        assertEquals(list.get(0).getDate().getTime(), 0);
        assertEquals(list.get(1).getDate().getTime(), 1);
        assertEquals(list.get(2).getDate().getTime(), 2);
        assertEquals(list.get(3).getDate().getTime(), 5);
        assertEquals(list.get(4).getDate().getTime(), 20);
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
    public void testReverseChronologicalOrder() throws Exception {
        final List<SimpleDate> list = new ArrayList<>();
        list.add(new SimpleDate(2));
        list.add(new SimpleDate(0));
        list.add(new SimpleDate(1));
        list.add(new SimpleDate(5));
        list.add(new SimpleDate(20));

        Collections.sort(list, SimpleDate.REVERSE_CHRONOLOGICAL_ORDER);
        assertEquals(list.get(0).getDate().getTime(), 20);
        assertEquals(list.get(1).getDate().getTime(), 5);
        assertEquals(list.get(2).getDate().getTime(), 2);
        assertEquals(list.get(3).getDate().getTime(), 1);
        assertEquals(list.get(4).getDate().getTime(), 0);
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
