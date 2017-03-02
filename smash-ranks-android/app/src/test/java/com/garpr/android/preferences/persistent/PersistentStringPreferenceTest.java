package com.garpr.android.preferences.persistent;

import com.garpr.android.BaseTest;
import com.garpr.android.BuildConfig;
import com.garpr.android.preferences.KeyValueStore;
import com.garpr.android.preferences.Preference;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import javax.inject.Inject;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class PersistentStringPreferenceTest extends BaseTest {

    @Inject
    KeyValueStore mKeyValueStore;


    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        getTestAppComponent().inject(this);
    }

    @Test
    public void testDeleteWithDefaultValue() throws Exception {
        final Preference<String> preference = new PersistentStringPreference("string", "hello",
                mKeyValueStore);
        preference.delete();
        assertNotNull(preference.get());
        assertTrue(preference.exists());

        preference.set("world");
        preference.delete();
        assertNotNull(preference.get());
        assertTrue(preference.exists());
    }

    @Test
    public void testDeleteWithNullDefaultValue() throws Exception {
        final Preference<String> preference = new PersistentStringPreference("string", null,
                mKeyValueStore);
        preference.delete();
        assertNull(preference.get());
        assertFalse(preference.exists());

        preference.set("nintendo");
        preference.delete();
        assertNull(preference.get());
        assertFalse(preference.exists());
    }

    @Test
    public void testExistsWithDefaultValue() throws Exception {
        final Preference<String> preference = new PersistentStringPreference("string", "hello",
                mKeyValueStore);
        assertTrue(preference.exists());

        preference.delete();
        assertTrue(preference.exists());
    }

    @Test
    public void testExistsWithEmptyDefaultValue() throws Exception {
        final Preference<String> preference = new PersistentStringPreference("string", "",
                mKeyValueStore);
        assertTrue(preference.exists());

        preference.delete();
        assertTrue(preference.exists());
    }

    @Test
    public void testExistsWithNullDefaultValue() throws Exception {
        final Preference<String> preference = new PersistentStringPreference("string", null,
                mKeyValueStore);
        assertFalse(preference.exists());

        preference.delete();
        assertFalse(preference.exists());
    }

    @Test
    public void testExistsWithWhitespaceDefaultValue() throws Exception {
        final Preference<String> preference = new PersistentStringPreference("string", "   ",
                mKeyValueStore);
        assertTrue(preference.exists());

        preference.delete();
        assertTrue(preference.exists());
    }

}
