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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class PersistentLongPreferenceTest extends BaseTest {

    @Inject
    KeyValueStore mKeyValueStore;


    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        getTestAppComponent().inject(this);
    }

    @Test
    public void testAddListener() throws Exception {
        final Preference<Long> preference = new PersistentLongPreference("long", null,
                mKeyValueStore);

        final long[] array = { 0L };

        final Preference.OnPreferenceChangeListener<Long> listener =
                new Preference.OnPreferenceChangeListener<Long>() {
            @Override
            public void onPreferenceChange(final Preference<Long> preference) {
                // noinspection ConstantConditions
                array[0] = preference.get();
            }
        };

        preference.addListener(listener);
        assertEquals(0L, array[0]);

        preference.set(11L);
        assertEquals(11L, array[0]);

        preference.set(-9815L, false);
        assertNotEquals(-9851L, array[0]);

        preference.set(21L);
        assertEquals(21L, array[0]);
    }

    @Test
    public void testDelete() throws Exception {
        final Preference<Long> preference = new PersistentLongPreference("long", -127L,
                mKeyValueStore);

        assertEquals(preference.get(), Long.valueOf(-127L));

        preference.delete();
        assertEquals(preference.get(), Long.valueOf(-127L));

        preference.set(61L);
        assertEquals(preference.get(), Long.valueOf(61L));

        preference.delete();
        assertEquals(preference.get(), Long.valueOf(-127L));
    }

    @Test
    public void testExistsWithDefaultValue() throws Exception {
        final Preference<Long> preference = new PersistentLongPreference("long", 1989L,
                mKeyValueStore);
        assertTrue(preference.exists());

        preference.delete();
        assertTrue(preference.exists());
    }

    @Test
    public void testExistsWithNullDefaultValue() throws Exception {
        final Preference<Long> preference = new PersistentLongPreference("long", null,
                mKeyValueStore);
        assertFalse(preference.exists());

        preference.delete();
        assertFalse(preference.exists());

        preference.set(1998L);
        assertTrue(preference.exists());

        preference.delete();
        assertFalse(preference.exists());
    }

    @Test
    public void testGetAndSet() throws Exception {
        final Preference<Long> preference = new PersistentLongPreference("long", 863L,
                mKeyValueStore);

        assertEquals(preference.get(), Long.valueOf(863L));

        preference.set(0L);
        assertEquals(preference.get(), Long.valueOf(0L));

        preference.set((Long) null);
        assertEquals(preference.get(), Long.valueOf(863L));
        assertEquals(preference.get(), preference.getDefaultValue());

        preference.set(-177L);
        assertEquals(preference.get(), Long.valueOf(-177L));
        assertNotEquals(preference.get(), preference.getDefaultValue());
    }

    @Test
    public void testGetKey() throws Exception {
        final Preference<Long> preference = new PersistentLongPreference("long", null,
                mKeyValueStore);
        assertEquals("long", preference.getKey());
    }

    @Test
    public void testNonNullGetDefaultValue() throws Exception {
        final Preference<Long> preference = new PersistentLongPreference("long", 130L,
                mKeyValueStore);

        assertEquals(preference.getDefaultValue(), Long.valueOf(130L));

        preference.set(Long.MAX_VALUE);
        assertEquals(preference.getDefaultValue(), Long.valueOf(130L));
    }

    @Test
    public void testNullGetDefaultValue() throws Exception {
        final Preference<Long> preference = new PersistentLongPreference("long", null,
                mKeyValueStore);

        assertNull(preference.getDefaultValue());

        preference.set(19L);
        assertNull(preference.getDefaultValue());
    }

    @Test
    public void testRemoveListener() throws Exception {
        final Preference<Long> preference = new PersistentLongPreference("long", null,
                mKeyValueStore);

        final long[] array = { 0L };

        final Preference.OnPreferenceChangeListener<Long> listener =
                new Preference.OnPreferenceChangeListener<Long>() {
            @Override
            public void onPreferenceChange(final Preference<Long> preference) {
                // noinspection ConstantConditions
                array[0] = preference.get();
            }
        };

        preference.addListener(listener);

        preference.set(11L);
        assertEquals(11L, array[0]);

        preference.set(25L);
        assertEquals(25L, array[0]);

        preference.removeListener(listener);

        preference.set(139L);
        assertNotEquals(139L, array[0]);
    }

    @Test
    public void testSetAndGet() throws Exception {
        final Preference<Long> preference = new PersistentLongPreference("long", null,
                mKeyValueStore);

        preference.set(38L);
        assertEquals(preference.get(), Long.valueOf(38L));

        preference.set(-22L);
        assertEquals(preference.get(), Long.valueOf(-22L));

        preference.set((Long) null);
        assertEquals(preference.get(), null);

        preference.set(Long.MIN_VALUE);
        assertEquals(preference.get(), Long.valueOf(Long.MIN_VALUE));
    }

}
