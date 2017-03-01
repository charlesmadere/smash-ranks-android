package com.garpr.android.misc;

import com.garpr.android.BaseTest;
import com.garpr.android.BuildConfig;
import com.garpr.android.models.AbsPlayer;
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
public class IdentityManagerTest extends BaseTest {

    private static final String JSON_LITE_PLAYER = "{\"id\":\"583a4a15d2994e0577b05c74\",\"name\":\"homemadewaffles\"}";

    @Inject
    Gson mGson;

    @Inject
    IdentityManager mIdentityManager;


    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        getTestAppComponent().inject(this);
    }

    @Test
    public void testAddListener() throws Exception {
        final AbsPlayer[] array = new AbsPlayer[1];

        final IdentityManager.OnIdentityChangeListener listener =
                new IdentityManager.OnIdentityChangeListener() {
            @Override
            public void onIdentityChange(final IdentityManager identityManager) {
                array[0] = identityManager.getIdentity();
            }
        };

        mIdentityManager.addListener(listener);
        assertNull(array[0]);

        final AbsPlayer player = mGson.fromJson(JSON_LITE_PLAYER, AbsPlayer.class);
        mIdentityManager.setIdentity(player);
        assertEquals(player, array[0]);

        mIdentityManager.setIdentity(null);
        assertNull(array[0]);
    }

    @Test
    public void testGetAndSetIdentity() throws Exception {
        assertNull(mIdentityManager.getIdentity());

        final AbsPlayer player = mGson.fromJson(JSON_LITE_PLAYER, AbsPlayer.class);
        mIdentityManager.setIdentity(player);
        assertNotNull(mIdentityManager.getIdentity());
        assertEquals(mIdentityManager.getIdentity(), player);

        mIdentityManager.setIdentity(null);
        assertNull(mIdentityManager.getIdentity());
    }

    @Test
    public void testHasIdentity() throws Exception {
        assertFalse(mIdentityManager.hasIdentity());

        mIdentityManager.setIdentity(mGson.fromJson(JSON_LITE_PLAYER, AbsPlayer.class));
        assertTrue(mIdentityManager.hasIdentity());

        mIdentityManager.setIdentity(null);
        assertFalse(mIdentityManager.hasIdentity());
    }

    @Test
    public void testIsIdWithNull() throws Exception {
        assertFalse(mIdentityManager.isId(null));

        mIdentityManager.setIdentity(mGson.fromJson(JSON_LITE_PLAYER, AbsPlayer.class));
        assertFalse(mIdentityManager.isId(null));

        mIdentityManager.setIdentity(null);
        assertFalse(mIdentityManager.isId(null));
    }

    @Test
    public void testIsIdWithPlayer() throws Exception {
        final AbsPlayer player = mGson.fromJson(JSON_LITE_PLAYER, AbsPlayer.class);
        assertFalse(mIdentityManager.isId(player.getId()));

        mIdentityManager.setIdentity(player);
        assertTrue(mIdentityManager.isId(player.getId()));

        mIdentityManager.setIdentity(null);
        assertFalse(mIdentityManager.isId(player.getId()));
    }

    @Test
    public void testIsPlayerWithNull() throws Exception {
        assertFalse(mIdentityManager.isPlayer(null));

        mIdentityManager.setIdentity(mGson.fromJson(JSON_LITE_PLAYER, AbsPlayer.class));
        assertFalse(mIdentityManager.isPlayer(null));

        mIdentityManager.setIdentity(null);
        assertFalse(mIdentityManager.isPlayer(null));
    }

    @Test
    public void testIsPlayerWithPlayer() throws Exception {
        final AbsPlayer player = mGson.fromJson(JSON_LITE_PLAYER, AbsPlayer.class);
        assertFalse(mIdentityManager.isPlayer(player));

        mIdentityManager.setIdentity(player);
        assertTrue(mIdentityManager.isPlayer(player));

        mIdentityManager.setIdentity(null);
        assertFalse(mIdentityManager.isPlayer(player));
    }

    @Test
    public void testRemoveListener() throws Exception {
        final AbsPlayer[] array = new AbsPlayer[1];

        final IdentityManager.OnIdentityChangeListener listener =
                new IdentityManager.OnIdentityChangeListener() {
            @Override
            public void onIdentityChange(final IdentityManager identityManager) {
                array[0] = identityManager.getIdentity();
            }
        };

        mIdentityManager.addListener(listener);
        final AbsPlayer player = mGson.fromJson(JSON_LITE_PLAYER, AbsPlayer.class);
        mIdentityManager.setIdentity(player);
        assertEquals(player, array[0]);

        mIdentityManager.removeListener(listener);
        mIdentityManager.setIdentity(null);
        assertEquals(player, array[0]);
    }

}
