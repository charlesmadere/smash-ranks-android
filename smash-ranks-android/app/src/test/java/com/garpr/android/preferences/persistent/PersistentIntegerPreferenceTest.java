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
import static org.junit.Assert.assertNull;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class PersistentIntegerPreferenceTest extends BaseTest {

    @Inject
    KeyValueStore mKeyValueStore;


    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        getTestAppComponent().inject(this);
    }

    @Test
    public void testNonNullGetDefaultValue() throws Exception {
        final Preference<Integer> preference = new PersistentIntegerPreference("integer", 128,
                mKeyValueStore);

        assertEquals(preference.getDefaultValue(), Integer.valueOf(128));

        preference.set(Integer.MAX_VALUE);
        assertEquals(preference.getDefaultValue(), Integer.valueOf(128));
    }

    @Test
    public void testNullGetDefaultValue() throws Exception {
        final Preference<Integer> preference = new PersistentIntegerPreference("integer", null,
                mKeyValueStore);

        assertNull(preference.getDefaultValue());

        preference.set(20);
        assertNull(preference.getDefaultValue());
    }

    @Test
    public void testSetAndGet() throws Exception {
        final Preference<Integer> preference = new PersistentIntegerPreference("integer", null,
                mKeyValueStore);

        preference.set(5);
        assertEquals(preference.get(), Integer.valueOf(5));

        preference.set(10);
        assertEquals(preference.get(), Integer.valueOf(10));

        preference.set((Integer) null);
        assertEquals(preference.get(), null);

        preference.set(Integer.MIN_VALUE);
        assertEquals(preference.get(), Integer.valueOf(Integer.MIN_VALUE));
    }

}
