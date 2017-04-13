package com.garpr.android.preferences;

import com.garpr.android.BaseTest;
import com.garpr.android.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Map;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
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
        assertFalse(mKeyValueStore.contains("boolean"));
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
    public void testGetAll() throws Exception {
        final Map<String, ?> all = mKeyValueStore.getAll();
        assertTrue(all == null || all.isEmpty());
    }

    @Test
    public void testGetAllAndSet() throws Exception {
        Map<String, ?> all = mKeyValueStore.getAll();
        assertTrue(all == null || all.isEmpty());

        mKeyValueStore.setFloat("float", Float.MIN_VALUE);
        all = mKeyValueStore.getAll();
        assertNotNull(all);
        assertEquals(all.size(), 1);

        mKeyValueStore.setString("String", "Hello, World!");
        all = mKeyValueStore.getAll();
        assertNotNull(all);
        assertEquals(all.size(), 2);
    }

    @Test
    public void testGetAllAndSetAndRemove() throws Exception {
        Map<String, ?> all = mKeyValueStore.getAll();
        assertTrue(all == null || all.isEmpty());

        mKeyValueStore.setFloat("float", Float.MIN_VALUE);
        all = mKeyValueStore.getAll();
        assertNotNull(all);
        assertEquals(all.size(), 1);

        mKeyValueStore.setString("String", "Hello, World!");
        all = mKeyValueStore.getAll();
        assertNotNull(all);
        assertEquals(all.size(), 2);

        mKeyValueStore.remove("float");
        all = mKeyValueStore.getAll();
        assertNotNull(all);
        assertEquals(all.size(), 1);

        mKeyValueStore.remove("hello");
        all = mKeyValueStore.getAll();
        assertNotNull(all);
        assertEquals(all.size(), 1);

        mKeyValueStore.remove("String");
        all = mKeyValueStore.getAll();
        assertTrue(all == null || all.isEmpty());
    }

    @Test
    public void testGetBoolean() throws Exception {
        assertFalse(mKeyValueStore.getBoolean("boolean", false));
        assertTrue(mKeyValueStore.getBoolean("boolean", true));
    }

    @Test
    public void testGetAndSetBoolean() throws Exception {
        assertFalse(mKeyValueStore.getBoolean("boolean", false));

        mKeyValueStore.setBoolean("boolean", true);
        assertTrue(mKeyValueStore.getBoolean("boolean", false));

        mKeyValueStore.setBoolean("boolean", false);
        assertFalse(mKeyValueStore.getBoolean("boolean", false));
    }

    @Test
    public void testGetFloat() throws Exception {
        assertEquals(Float.valueOf(mKeyValueStore.getFloat("float", Float.MIN_VALUE)),
                Float.valueOf(Float.MIN_VALUE));

        assertEquals(Float.valueOf(mKeyValueStore.getFloat("float", Float.MAX_VALUE)),
                Float.valueOf(Float.MAX_VALUE));
    }

    @Test
    public void testGetAndSetFloat() throws Exception {
        assertEquals(Float.valueOf(mKeyValueStore.getFloat("float", Float.MIN_VALUE)),
                Float.valueOf(Float.MIN_VALUE));

        assertEquals(Float.valueOf(mKeyValueStore.getFloat("float", Float.MAX_VALUE)),
                Float.valueOf(Float.MAX_VALUE));

        mKeyValueStore.setFloat("float", Float.MAX_VALUE);
        assertEquals(Float.valueOf(mKeyValueStore.getFloat("float", Float.MIN_VALUE)),
                Float.valueOf(Float.MAX_VALUE));

        mKeyValueStore.setFloat("float", Float.MIN_VALUE);
        assertEquals(Float.valueOf(mKeyValueStore.getFloat("float", Float.MIN_VALUE)),
                Float.valueOf(Float.MIN_VALUE));
    }

    @Test
    public void testRemove() throws Exception {
        mKeyValueStore.remove("boolean");
        assertFalse(mKeyValueStore.contains("boolean"));
    }

    @Test
    public void testRemoveAndSet() throws Exception {
        mKeyValueStore.remove("long");
        assertFalse(mKeyValueStore.contains("long"));

        mKeyValueStore.setLong("long", 100);
        assertTrue(mKeyValueStore.contains("long"));

        mKeyValueStore.remove("long");
        assertFalse(mKeyValueStore.contains("long"));
    }

}
