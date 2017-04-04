package com.garpr.android.models;

import com.garpr.android.BaseTest;
import com.garpr.android.BuildConfig;
import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class RankingTest extends BaseTest {

    private static final String JSON_RANKING = "{\"id\":\"545b240b8ab65f7a95f74940\",\"name\":\"Swedish Delight\",\"rank\":1,\"rating\":42.68614684395572}";
    private static final String JSON_RANKING_WITH_NULL_PREVIOUS_RANK = "{\"rating\":17.65839790444456,\"name\":\"Hoof\",\"rank\":194,\"previous_rank\":null,\"id\":\"58cedd58d2994e057e91f771\"}";
    private static final String JSON_RANKING_WITH_PREVIOUS_RANK = "{\"rating\":33.457372946321904,\"name\":\"gaR\",\"rank\":31,\"previous_rank\":30,\"id\":\"58523b44d2994e15c7dea945\"}";

    @Inject
    Gson mGson;


    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        getTestAppComponent().inject(this);
    }

    @Test
    public void testFromJson() throws Exception {
        final Ranking ranking = mGson.fromJson(JSON_RANKING, Ranking.class);
        assertEquals(ranking.getRank(), 1);
        assertNull(ranking.getPreviousRank());

        assertNotNull(ranking.getPlayer());
        assertEquals("545b240b8ab65f7a95f74940", ranking.getPlayer().getId());
        assertEquals("Swedish Delight", ranking.getPlayer().getName());
    }

    @Test
    public void testFromJsonWithNullPreviousRank() throws Exception {
        final Ranking ranking = mGson.fromJson(JSON_RANKING_WITH_NULL_PREVIOUS_RANK, Ranking.class);
        assertEquals(ranking.getRank(), 194);
        assertNull(ranking.getPreviousRank());

        assertNotNull(ranking.getPlayer());
        assertEquals("58cedd58d2994e057e91f771", ranking.getPlayer().getId());
        assertEquals("Hoof", ranking.getPlayer().getName());
    }

    @Test
    public void testFromJsonWithPreviousRank() throws Exception {
        final Ranking ranking = mGson.fromJson(JSON_RANKING_WITH_PREVIOUS_RANK, Ranking.class);
        assertEquals(ranking.getRank(), 31);
        assertNotNull(ranking.getPreviousRank());
        assertEquals(ranking.getPreviousRank(), Integer.valueOf(30));

        assertNotNull(ranking.getPlayer());
        assertEquals("58523b44d2994e15c7dea945", ranking.getPlayer().getId());
        assertEquals("gaR", ranking.getPlayer().getName());
    }

}
