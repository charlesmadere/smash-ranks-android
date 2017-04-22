package com.garpr.android.preferences.persistent;

import android.net.Uri;

import com.garpr.android.BaseTest;
import com.garpr.android.BuildConfig;
import com.garpr.android.preferences.KeyValueStore;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class PersistentUriPreferenceTest extends BaseTest {

    private static final String AMAZON = "https://www.amazon.com/";
    private static final String GOOGLE = "https://www.google.com/";
    private static final String POLYGON = "http://www.polygon.com/";

    @Inject
    KeyValueStore mKeyValueStore;


    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();

        getTestAppComponent().inject(this);
    }

    @Test
    public void testGet() throws Exception {
        final PersistentUriPreference preference = new PersistentUriPreference("test", null,
                mKeyValueStore);
        assertNull(preference.get());
    }

    @Test
    public void testGetAndSet() throws Exception {
        final PersistentUriPreference preference = new PersistentUriPreference("test", null,
                mKeyValueStore);
        assertNull(preference.get());

        final Uri google = Uri.parse(GOOGLE);
        preference.set(google);
        assertNotNull(preference.get());
        assertEquals(preference.get(), google);
    }

    @Test
    public void testGetAndSetAndDelete() throws Exception {
        final PersistentUriPreference preference = new PersistentUriPreference("test", null,
                mKeyValueStore);
        assertNull(preference.get());

        final Uri amazon = Uri.parse(AMAZON);
        preference.set(amazon);
        assertNotNull(preference.get());
        assertEquals(preference.get(), amazon);

        final Uri polygon = Uri.parse(POLYGON);
        preference.set(polygon);
        assertNotNull(preference.get());
        assertEquals(preference.get(), polygon);

        preference.delete();
        assertNull(preference.get());
    }

    @Test
    public void testGetAndSetAndDeleteAndExists() throws Exception {
        final PersistentUriPreference preference = new PersistentUriPreference("test", null,
                mKeyValueStore);
        assertFalse(preference.exists());
        assertNull(preference.get());

        final Uri amazon = Uri.parse(AMAZON);
        preference.set(amazon);
        assertTrue(preference.exists());
        assertNotNull(preference.get());
        assertEquals(preference.get(), amazon);

        final Uri polygon = Uri.parse(POLYGON);
        preference.set(polygon);
        assertTrue(preference.exists());
        assertNotNull(preference.get());
        assertEquals(preference.get(), polygon);

        preference.set(Uri.EMPTY);
        assertTrue(preference.exists());
        assertNotNull(preference.get());
        assertEquals(preference.get(), Uri.EMPTY);

        preference.delete();
        assertNull(preference.get());
    }

    @Test
    public void testGetDefaultValue() throws Exception {
        final Uri polygon = Uri.parse(POLYGON);
        final PersistentUriPreference preference = new PersistentUriPreference("test", polygon,
                mKeyValueStore);
        assertNotNull(preference.get());
        assertEquals(preference.get(), polygon);
    }

}
