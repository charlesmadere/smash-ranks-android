package com.garpr.android.misc;

import com.garpr.android.BaseTest;
import com.garpr.android.BuildConfig;
import com.garpr.android.models.AbsPlayer;
import com.garpr.android.models.FavoritePlayer;
import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class FavoritePlayersManagerTest extends BaseTest {

    private static final String JSON_PLAYER_1 = "{\"id\":\"583a4a15d2994e0577b05c74\",\"name\":\"homemadewaffles\"}";
    private static final String JSON_PLAYER_2 = "{\"id\":\"5877eb55d2994e15c7dea97e\",\"name\":\"Spark\"}";

    @Inject
    FavoritePlayersManager mFavoritePlayersManager;

    @Inject
    Gson mGson;

    @Inject
    RegionManager mRegionManager;


    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        getTestAppComponent().inject(this);
    }

    @Test
    public void testAddListener() throws Exception {
        final List[] array = new List[1];

        final FavoritePlayersManager.OnFavoritePlayersChangeListener listener =
                new FavoritePlayersManager.OnFavoritePlayersChangeListener() {
            @Override
            public void onFavoritePlayersChanged(final FavoritePlayersManager manager) {
                array[0] = manager.getPlayers();
            }
        };

        mFavoritePlayersManager.addListener(listener);
        assertNull(array[0]);

        final AbsPlayer hmw = mGson.fromJson(JSON_PLAYER_1, AbsPlayer.class);
        mFavoritePlayersManager.addPlayer(hmw, mRegionManager.getRegion());
        assertNotNull(array[0]);
        assertEquals(array[0].size(), 1);
        assertEquals(array[0].get(0), hmw);

        mFavoritePlayersManager.removePlayer(hmw);
        assertTrue(array[0] == null || array[0].isEmpty());
    }

    @Test
    public void testAddPlayers() throws Exception {
        final AbsPlayer hmw = mGson.fromJson(JSON_PLAYER_1, AbsPlayer.class);
        final AbsPlayer spark = mGson.fromJson(JSON_PLAYER_2, AbsPlayer.class);
        mFavoritePlayersManager.addPlayer(hmw, mRegionManager.getRegion());
        mFavoritePlayersManager.addPlayer(spark, mRegionManager.getRegion());

        final List<AbsPlayer> players = mFavoritePlayersManager.getAbsPlayers();
        assertNotNull(players);
        assertEquals(players.size(), 2);
        assertTrue(players.contains(hmw));
        assertTrue(players.contains(spark));
    }

    @Test
    public void testClear() throws Exception {
        mFavoritePlayersManager.clear();
        assertTrue(mFavoritePlayersManager.isEmpty());

        final AbsPlayer hmw = mGson.fromJson(JSON_PLAYER_1, AbsPlayer.class);
        mFavoritePlayersManager.addPlayer(hmw, mRegionManager.getRegion());
        mFavoritePlayersManager.clear();
        assertTrue(mFavoritePlayersManager.isEmpty());

        final AbsPlayer spark = mGson.fromJson(JSON_PLAYER_2, AbsPlayer.class);
        mFavoritePlayersManager.addPlayer(spark, mRegionManager.getRegion());
        mFavoritePlayersManager.addPlayer(hmw, mRegionManager.getRegion());
        mFavoritePlayersManager.clear();
        assertTrue(mFavoritePlayersManager.isEmpty());

    }

    @Test
    public void testContainsPlayer() throws Exception {
        final AbsPlayer hmw = mGson.fromJson(JSON_PLAYER_1, AbsPlayer.class);
        final AbsPlayer spark = mGson.fromJson(JSON_PLAYER_2, AbsPlayer.class);

        assertFalse(mFavoritePlayersManager.containsPlayer(hmw));
        assertFalse(mFavoritePlayersManager.containsPlayer(hmw.getId()));
        assertFalse(mFavoritePlayersManager.containsPlayer(spark));
        assertFalse(mFavoritePlayersManager.containsPlayer(spark.getId()));

        mFavoritePlayersManager.addPlayer(hmw, mRegionManager.getRegion());
        assertTrue(mFavoritePlayersManager.containsPlayer(hmw));
        assertTrue(mFavoritePlayersManager.containsPlayer(hmw.getId()));
        assertFalse(mFavoritePlayersManager.containsPlayer(spark));
        assertFalse(mFavoritePlayersManager.containsPlayer(spark.getId()));

        mFavoritePlayersManager.removePlayer(spark);
        assertTrue(mFavoritePlayersManager.containsPlayer(hmw));
        assertTrue(mFavoritePlayersManager.containsPlayer(hmw.getId()));
        assertFalse(mFavoritePlayersManager.containsPlayer(spark));
        assertFalse(mFavoritePlayersManager.containsPlayer(spark.getId()));

        mFavoritePlayersManager.removePlayer(hmw);
        assertFalse(mFavoritePlayersManager.containsPlayer(hmw));
        assertFalse(mFavoritePlayersManager.containsPlayer(hmw.getId()));
        assertFalse(mFavoritePlayersManager.containsPlayer(spark));
        assertFalse(mFavoritePlayersManager.containsPlayer(spark.getId()));

        mFavoritePlayersManager.addPlayer(spark, mRegionManager.getRegion());
        assertFalse(mFavoritePlayersManager.containsPlayer(hmw));
        assertFalse(mFavoritePlayersManager.containsPlayer(hmw.getId()));
        assertTrue(mFavoritePlayersManager.containsPlayer(spark));
        assertTrue(mFavoritePlayersManager.containsPlayer(spark.getId()));
    }

    @Test
    public void testAbsGetPlayers() throws Exception {
        List<AbsPlayer> players = mFavoritePlayersManager.getAbsPlayers();
        assertTrue(players == null || players.isEmpty());

        final AbsPlayer hmw = mGson.fromJson(JSON_PLAYER_1, AbsPlayer.class);
        final AbsPlayer spark = mGson.fromJson(JSON_PLAYER_2, AbsPlayer.class);
        mFavoritePlayersManager.addPlayer(hmw, mRegionManager.getRegion());
        mFavoritePlayersManager.addPlayer(spark, mRegionManager.getRegion());

        players = mFavoritePlayersManager.getAbsPlayers();
        assertEquals(players.size(), 2);
        assertTrue(players.contains(hmw));
        assertTrue(players.contains(spark));
    }

    @Test
    public void testGetPlayers() throws Exception {
        List<FavoritePlayer> players = mFavoritePlayersManager.getPlayers();
        assertNull(players);

        List<AbsPlayer> absPlayers = mFavoritePlayersManager.getAbsPlayers();
        assertNull(absPlayers);

        final AbsPlayer spark = mGson.fromJson(JSON_PLAYER_2, AbsPlayer.class);
        mFavoritePlayersManager.addPlayer(spark, mRegionManager.getRegion());

        players = mFavoritePlayersManager.getPlayers();
        assertNotNull(players);
        assertEquals(players.size(), 1);

        absPlayers = mFavoritePlayersManager.getAbsPlayers();
        assertNotNull(absPlayers);
        assertEquals(absPlayers.size(), 1);

        mFavoritePlayersManager.removePlayer(spark);

        players = mFavoritePlayersManager.getPlayers();
        assertNull(players);

        absPlayers = mFavoritePlayersManager.getAbsPlayers();
        assertNull(absPlayers);
    }

    @Test
    public void testIsEmpty() throws Exception {
        assertTrue(mFavoritePlayersManager.isEmpty());

        final AbsPlayer hmw = mGson.fromJson(JSON_PLAYER_1, AbsPlayer.class);
        mFavoritePlayersManager.addPlayer(hmw, mRegionManager.getRegion());
        assertFalse(mFavoritePlayersManager.isEmpty());

        mFavoritePlayersManager.removePlayer(hmw);
        assertTrue(mFavoritePlayersManager.isEmpty());
    }

    @Test
    public void testRemoveListener() throws Exception {
        final List[] array = new List[1];

        final FavoritePlayersManager.OnFavoritePlayersChangeListener listener =
                new FavoritePlayersManager.OnFavoritePlayersChangeListener() {
            @Override
            public void onFavoritePlayersChanged(final FavoritePlayersManager manager) {
                array[0] = manager.getPlayers();
            }
        };

        mFavoritePlayersManager.addListener(listener);
        assertNull(array[0]);

        final AbsPlayer hmw = mGson.fromJson(JSON_PLAYER_1, AbsPlayer.class);
        mFavoritePlayersManager.addPlayer(hmw, mRegionManager.getRegion());
        assertNotNull(array[0]);
        assertEquals(array[0].size(), 1);
        assertEquals(array[0].get(0), hmw);

        mFavoritePlayersManager.removeListener(listener);
        mFavoritePlayersManager.removePlayer(hmw);
        assertNotNull(array[0]);
        assertEquals(array[0].size(), 1);
        assertEquals(array[0].get(0), hmw);
    }

    @Test
    public void testRemovePlayers() throws Exception {
        final AbsPlayer hmw = mGson.fromJson(JSON_PLAYER_1, AbsPlayer.class);
        final AbsPlayer spark = mGson.fromJson(JSON_PLAYER_2, AbsPlayer.class);
        mFavoritePlayersManager.addPlayer(hmw, mRegionManager.getRegion());
        mFavoritePlayersManager.addPlayer(spark, mRegionManager.getRegion());

        mFavoritePlayersManager.removePlayer(hmw);
        mFavoritePlayersManager.removePlayer(spark);
        List<AbsPlayer> players = mFavoritePlayersManager.getAbsPlayers();
        assertTrue(players == null || players.isEmpty());

        mFavoritePlayersManager.addPlayer(spark, mRegionManager.getRegion());
        mFavoritePlayersManager.addPlayer(hmw, mRegionManager.getRegion());
        mFavoritePlayersManager.removePlayer(spark.getId());
        mFavoritePlayersManager.removePlayer(hmw.getId());
        players = mFavoritePlayersManager.getAbsPlayers();
        assertTrue(players == null || players.isEmpty());
    }

    @Test
    public void testSize() throws Exception {
        assertEquals(mFavoritePlayersManager.size(), 0);

        final AbsPlayer hmw = mGson.fromJson(JSON_PLAYER_1, AbsPlayer.class);
        mFavoritePlayersManager.addPlayer(hmw, mRegionManager.getRegion());
        assertEquals(mFavoritePlayersManager.size(), 1);

        final AbsPlayer spark = mGson.fromJson(JSON_PLAYER_2, AbsPlayer.class);
        mFavoritePlayersManager.addPlayer(spark, mRegionManager.getRegion());
        assertEquals(mFavoritePlayersManager.size(), 2);

        mFavoritePlayersManager.removePlayer(hmw);
        assertEquals(mFavoritePlayersManager.size(), 1);

        mFavoritePlayersManager.clear();
        assertEquals(mFavoritePlayersManager.size(), 0);
    }

}
