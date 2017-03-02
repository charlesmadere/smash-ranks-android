package com.garpr.android.preferences.persistent;

import com.garpr.android.BaseTest;
import com.garpr.android.BuildConfig;
import com.garpr.android.models.AbsPlayer;
import com.garpr.android.preferences.KeyValueStore;
import com.garpr.android.preferences.Preference;
import com.google.gson.Gson;

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
public class PersistentGsonPreferenceTest extends BaseTest {

    private static final String JSON_LITE_PLAYER_1 = "{\"id\":\"583a4a15d2994e0577b05c74\",\"name\":\"homemadewaffles\"}";
    private static final String JSON_LITE_PLAYER_2 = "{\"id\":\"5877eb55d2994e15c7dea97e\",\"name\":\"Spark\"}";

    @Inject
    Gson mGson;

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
        final AbsPlayer player = mGson.fromJson(JSON_LITE_PLAYER_1, AbsPlayer.class);
        final Preference<AbsPlayer> preference = new PersistentGsonPreference<>("gson", player,
                mKeyValueStore, AbsPlayer.class, mGson);
        preference.delete();
        assertNotNull(preference.get());
        assertEquals(player, preference.get());
    }

    @Test
    public void testDeleteWithNullDefaultValue() throws Exception {
        final Preference<AbsPlayer> preference = new PersistentGsonPreference<>("gson", null,
                mKeyValueStore, AbsPlayer.class, mGson);
        preference.delete();
        assertNull(preference.get());
    }

    @Test
    public void testExistsWithDefaultValue() throws Exception {
        final AbsPlayer player = mGson.fromJson(JSON_LITE_PLAYER_1, AbsPlayer.class);
        final Preference<AbsPlayer> preference = new PersistentGsonPreference<>("gson", player,
                mKeyValueStore, AbsPlayer.class, mGson);
        assertTrue(preference.exists());

        preference.delete();
        assertTrue(preference.exists());
    }

    @Test
    public void testExistsWithNullDefaultValue() throws Exception {
        final Preference<AbsPlayer> preference = new PersistentGsonPreference<>("gson", null,
                mKeyValueStore, AbsPlayer.class, mGson);
        assertFalse(preference.exists());

        preference.delete();
        assertFalse(preference.exists());
    }

    @Test
    public void testGetDefaultValue() throws Exception {
        final AbsPlayer player = mGson.fromJson(JSON_LITE_PLAYER_2, AbsPlayer.class);
        final Preference<AbsPlayer> preference = new PersistentGsonPreference<>("gson", player,
                mKeyValueStore, AbsPlayer.class, mGson);
        assertNotNull(preference.getDefaultValue());
        assertEquals(player, preference.getDefaultValue());
    }

    @Test
    public void testGetDefaultValueWithNull() throws Exception {
        final Preference<AbsPlayer> preference = new PersistentGsonPreference<>("gson", null,
                mKeyValueStore, AbsPlayer.class, mGson);
        assertNull(preference.getDefaultValue());
    }

    @Test
    public void testGetWithDefaultValue() throws Exception {
        final AbsPlayer player = mGson.fromJson(JSON_LITE_PLAYER_1, AbsPlayer.class);
        final Preference<AbsPlayer> preference = new PersistentGsonPreference<>("gson", player,
                mKeyValueStore, AbsPlayer.class, mGson);
        assertNotNull(preference.get());
        assertEquals(player, preference.get());
    }

    @Test
    public void testGetWithNullDefaultValue() throws Exception {
        final Preference<AbsPlayer> preference = new PersistentGsonPreference<>("gson", null,
                mKeyValueStore, AbsPlayer.class, mGson);
        assertNull(preference.get());
    }

    @Test
    public void testSetWithDefaultValue() throws Exception {
        final AbsPlayer player1 = mGson.fromJson(JSON_LITE_PLAYER_1, AbsPlayer.class);
        final Preference<AbsPlayer> preference = new PersistentGsonPreference<>("gson", player1,
                mKeyValueStore, AbsPlayer.class, mGson);

        final AbsPlayer player2 = mGson.fromJson(JSON_LITE_PLAYER_2, AbsPlayer.class);
        preference.set(player2);
        assertEquals(player2, preference.get());

        preference.set((AbsPlayer) null);
        assertEquals(player1, preference.get());
    }

    @Test
    public void testSetWithNullDefaultValue() throws Exception {
        final Preference<AbsPlayer> preference = new PersistentGsonPreference<>("gson", null,
                mKeyValueStore, AbsPlayer.class, mGson);

        final AbsPlayer player = mGson.fromJson(JSON_LITE_PLAYER_1, AbsPlayer.class);
        preference.set(player);
        assertEquals(player, preference.get());

        preference.set((AbsPlayer) null);
        assertNull(preference.get());
    }

}
