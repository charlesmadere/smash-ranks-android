package com.garpr.android.preferences;

import com.garpr.android.BaseTest;
import com.garpr.android.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import javax.inject.Inject;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class KeyValueStoreTest extends BaseTest {

    @Inject
    KeyValueStore mKeyValueStore;


    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        getTestAppComponent().inject(this);
    }

    @Test
    public void testClear() throws Exception {
        mKeyValueStore.clear();
        assertNull(mKeyValueStore.getAll());
    }

    @Test
    public void testContains() throws Exception {
        assertFalse(mKeyValueStore.contains("hello"));
    }

    @Test
    public void testContainsAndSet() throws Exception {
        assertFalse(mKeyValueStore.contains("hello"));

        mKeyValueStore.setString("hello", "world");
        assertTrue(mKeyValueStore.contains("hello"));
    }

    @Test
    public void testContainsAndSetAndRemove() throws Exception {
        assertFalse(mKeyValueStore.contains("hello"));

        mKeyValueStore.setString("hello", "world");
        assertTrue(mKeyValueStore.contains("hello"));

        mKeyValueStore.remove("hello");
        assertFalse(mKeyValueStore.contains("hello"));
    }

    @Test
    public void testGetBoolean() throws Exception {
        assertFalse(mKeyValueStore.getBoolean("boolean", false));
    }

    @Test
    public void testGetAndSetBoolean() throws Exception {
        assertFalse(mKeyValueStore.getBoolean("boolean", false));

        mKeyValueStore.setBoolean("boolean", true);
        assertTrue(mKeyValueStore.getBoolean("boolean", false));

        mKeyValueStore.setBoolean("boolean", false);
        assertFalse(mKeyValueStore.getBoolean("boolean", false));
    }

}
