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

    private AbsPlayer mPlayer;

    @Inject
    Gson mGson;

    @Inject
    IdentityManager mIdentityManager;

    @Inject
    RegionManager mRegionManager;


    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        getTestAppComponent().inject(this);

        mPlayer = mGson.fromJson(JSON_LITE_PLAYER, AbsPlayer.class);
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

        mIdentityManager.setIdentity(mPlayer, mRegionManager.getRegion());
        assertEquals(mPlayer, array[0]);

        mIdentityManager.removeIdentity();
        assertNull(array[0]);
    }

    @Test
    public void testGetAndSetIdentity() throws Exception {
        assertNull(mIdentityManager.getIdentity());

        mIdentityManager.setIdentity(mPlayer, mRegionManager.getRegion());
        assertNotNull(mIdentityManager.getIdentity());
        assertEquals(mIdentityManager.getIdentity(), mPlayer);

        mIdentityManager.removeIdentity();
        assertNull(mIdentityManager.getIdentity());
    }

    @Test
    public void testHasIdentity() throws Exception {
        assertFalse(mIdentityManager.hasIdentity());

        mIdentityManager.setIdentity(mPlayer, mRegionManager.getRegion());
        assertTrue(mIdentityManager.hasIdentity());

        mIdentityManager.removeIdentity();
        assertFalse(mIdentityManager.hasIdentity());
    }

    @Test
    public void testIsIdWithNull() throws Exception {
        assertFalse(mIdentityManager.isId(null));

        mIdentityManager.setIdentity(mPlayer, mRegionManager.getRegion());
        assertFalse(mIdentityManager.isId(null));

        mIdentityManager.removeIdentity();
        assertFalse(mIdentityManager.isId(null));
    }

    @Test
    public void testIsIdWithPlayer() throws Exception {
        assertFalse(mIdentityManager.isId(mPlayer.getId()));

        mIdentityManager.setIdentity(mPlayer, mRegionManager.getRegion());
        assertTrue(mIdentityManager.isId(mPlayer.getId()));

        mIdentityManager.removeIdentity();
        assertFalse(mIdentityManager.isId(mPlayer.getId()));
    }

    @Test
    public void testIsPlayerWithNull() throws Exception {
        assertFalse(mIdentityManager.isPlayer(null));

        mIdentityManager.setIdentity(mPlayer, mRegionManager.getRegion());
        assertFalse(mIdentityManager.isPlayer(null));

        mIdentityManager.removeIdentity();
        assertFalse(mIdentityManager.isPlayer(null));
    }

    @Test
    public void testIsPlayerWithPlayer() throws Exception {
        assertFalse(mIdentityManager.isPlayer(mPlayer));

        mIdentityManager.setIdentity(mPlayer, mRegionManager.getRegion());
        assertTrue(mIdentityManager.isPlayer(mPlayer));

        mIdentityManager.removeIdentity();
        assertFalse(mIdentityManager.isPlayer(mPlayer));
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
        mIdentityManager.setIdentity(mPlayer, mRegionManager.getRegion());
        assertEquals(mPlayer, array[0]);

        mIdentityManager.removeListener(listener);
        mIdentityManager.removeIdentity();
        assertEquals(mPlayer, array[0]);
    }

}
