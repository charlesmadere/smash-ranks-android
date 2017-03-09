package com.garpr.android.misc;

import com.garpr.android.BaseTest;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TimberTest extends BaseTest {

    private static final String TAG = "TimberTest";

    @Inject
    Timber mTimber;


    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        getTestAppComponent().inject(this);
    }

    @Test
    public void testClearEntries() throws Exception {
        mTimber.clearEntries();
        List<Timber.Entry> entries = mTimber.getEntries();
        assertTrue(entries.isEmpty());

        mTimber.d(TAG, "blah");
        mTimber.clearEntries();
        entries = mTimber.getEntries();
        assertTrue(entries.isEmpty());
    }

    @Test
    public void testGetEntries() throws Exception {
        List<Timber.Entry> entries = mTimber.getEntries();
        assertTrue(entries.isEmpty());
    }

    @Test
    public void testGetEntriesWithOne() throws Exception {
        mTimber.d(TAG, "one");
        final List<Timber.Entry> entries = mTimber.getEntries();
        assertEquals(entries.size(), 1);
    }

    @Test
    public void testGetEntriesWithTwo() throws Exception {
        mTimber.d(TAG, "one");
        mTimber.w(TAG, "two");
        final List<Timber.Entry> entries = mTimber.getEntries();
        assertEquals(entries.size(), 2);
    }

    @Test
    public void testGetEntriesWithThree() throws Exception {
        mTimber.d(TAG, "one");
        mTimber.w(TAG, "two");
        mTimber.e(TAG, "three");
        final List<Timber.Entry> entries = mTimber.getEntries();
        assertEquals(entries.size(), 3);
    }

}
