package com.garpr.android.models;

import com.garpr.android.BaseTest;
import com.garpr.android.BuildConfig;
import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class AbsPlayerTest extends BaseTest {

    private static final String JSON_FULL_PLAYER = "{\"ratings\":{\"googlemtv\":{\"mu\":37.05546025182014,\"sigma\":2.0824461049194727},\"norcal\":{\"mu\":37.02140742867105,\"sigma\":2.3075802611877165}},\"name\":\"gaR\",\"regions\":[\"norcal\",\"googlemtv\"],\"merge_children\":[\"58523b44d2994e15c7dea945\"],\"id\":\"58523b44d2994e15c7dea945\",\"merged\":false,\"merge_parent\":null}";

    private static final String JSON_LITE_PLAYER_1 = "{\"id\":\"583a4a15d2994e0577b05c74\",\"name\":\"homemadewaffles\"}";
    private static final String JSON_LITE_PLAYER_2 = "{\"id\":\"5877eb55d2994e15c7dea97e\",\"name\":\"Spark\"}";

    @Inject
    Gson mGson;


    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        getTestAppComponent().inject(this);
    }

    @Test
    public void testComparatorAlphabeticalOrder() throws Exception {
        final AbsPlayer zero = mGson.fromJson(JSON_FULL_PLAYER, AbsPlayer.class);
        final AbsPlayer one = mGson.fromJson(JSON_LITE_PLAYER_1, AbsPlayer.class);
        final AbsPlayer two = mGson.fromJson(JSON_LITE_PLAYER_2, AbsPlayer.class);
        final List<AbsPlayer> list = new ArrayList<>(Arrays.asList(two, zero, one));

        Collections.sort(list, AbsPlayer.ALPHABETICAL_ORDER);
        assertEquals(list.get(0), zero);
        assertEquals(list.get(1), one);
        assertEquals(list.get(2), two);
    }

    @Test
    public void testFromJsonFullPlayer() throws Exception {
        final AbsPlayer player = mGson.fromJson(JSON_FULL_PLAYER, AbsPlayer.class);
        assertNotNull(player);

        assertEquals(player.getName(), "gaR");
        assertEquals(player.getId(), "58523b44d2994e15c7dea945");

        assertTrue(player instanceof FullPlayer);
        assertEquals(player.getKind(), AbsPlayer.Kind.FULL);

        final FullPlayer fullPlayer = (FullPlayer) player;
        assertFalse(fullPlayer.hasAliases());
        assertTrue(fullPlayer.hasRatings());
        assertTrue(fullPlayer.hasRegions());
    }

    @Test
    public void testFromJsonLitePlayer() throws Exception {
        final AbsPlayer player = mGson.fromJson(JSON_LITE_PLAYER_1, AbsPlayer.class);
        assertNotNull(player);

        assertEquals(player.getName(), "homemadewaffles");
        assertEquals(player.getId(), "583a4a15d2994e0577b05c74");

        assertTrue(player instanceof LitePlayer);
        assertEquals(player.getKind(), AbsPlayer.Kind.LITE);
    }

    @Test
    public void testFromNull() throws Exception {
        final AbsPlayer player = mGson.fromJson((String) null, AbsPlayer.class);
        assertNull(player);
    }

    @Test
    public void testToJsonAndBackWithLitePlayer() throws Exception {
        final AbsPlayer before = mGson.fromJson(JSON_LITE_PLAYER_1, AbsPlayer.class);
        final String json = mGson.toJson(before, AbsPlayer.class);
        final AbsPlayer after = mGson.fromJson(json, AbsPlayer.class);

        assertEquals(before, after);
        assertEquals(before.getKind(), after.getKind());
    }

}
