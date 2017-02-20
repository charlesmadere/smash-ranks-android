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
public class PersistentBooleanPreferenceTest extends BaseTest {

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
        final Preference<Boolean> preference = new PersistentBooleanPreference("boolean",
                Boolean.FALSE, mKeyValueStore);

        assertEquals(preference.getDefaultValue(), Boolean.FALSE);

        preference.set(Boolean.TRUE);
        assertEquals(preference.getDefaultValue(), Boolean.FALSE);
    }

    @Test
    public void testNullGetDefaultValue() throws Exception {
        final Preference<Boolean> preference = new PersistentBooleanPreference("boolean", null,
                mKeyValueStore);

        assertNull(preference.getDefaultValue());
        preference.set(Boolean.TRUE);
        assertNull(preference.getDefaultValue());
    }

}
