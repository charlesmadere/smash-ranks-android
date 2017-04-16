package com.garpr.android.misc;

import com.garpr.android.BaseTest;
import com.garpr.android.models.Ranking;
import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PreviousRankUtilsTest extends BaseTest {

    private static final String JSON_RANKING_DECREASED = "{\"rating\":30.25666689276485,\"name\":\"boback\",\"rank\":57,\"previous_rank\":42,\"id\":\"5888542ad2994e3bbfa52e1f\"}";
    private static final String JSON_RANKING_INCREASED = "{\"rating\":37.46725497606898,\"name\":\"SAB | Ralph\",\"rank\":6,\"previous_rank\":9,\"id\":\"588852e8d2994e3bbfa52dcf\"}";
    private static final String JSON_RANKING_NULL = "{\"id\":\"53c64dba8ab65f6e6651f7bc\",\"name\":\"Hax\",\"rank\":3,\"rating\":38.977594430937145}";
    private static final String JSON_RANKING_UNCHANGED = "{\"rating\":40.97978935079751,\"name\":\"CLG. | PewPewU\",\"rank\":3,\"previous_rank\":3,\"id\":\"588852e8d2994e3bbfa52da7\"}";

    private Ranking mDecreased;
    private Ranking mIncreased;
    private Ranking mNull;
    private Ranking mUnchanged;

    @Inject
    Gson mGson;

    @Inject
    PreviousRankUtils mPreviousRankUtils;


    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        getTestAppComponent().inject(this);

        mDecreased = mGson.fromJson(JSON_RANKING_DECREASED, Ranking.class);
        mIncreased = mGson.fromJson(JSON_RANKING_INCREASED, Ranking.class);
        mNull = mGson.fromJson(JSON_RANKING_NULL, Ranking.class);
        mUnchanged = mGson.fromJson(JSON_RANKING_UNCHANGED, Ranking.class);
    }

    @Test
    public void testCheckRankingWithDecreasedRanking() throws Exception {
        assertEquals(mPreviousRankUtils.checkRanking(mDecreased), PreviousRankUtils.Info.DECREASE);
    }

    @Test
    public void testCheckRankingWithIncreasedRanking() throws Exception {
        assertEquals(mPreviousRankUtils.checkRanking(mIncreased), PreviousRankUtils.Info.INCREASE);
    }

    @Test
    public void testCheckRankingWithNull() throws Exception {
        assertNull(mPreviousRankUtils.checkRanking(null));
    }

    @Test
    public void testCheckRankingWithNullRanking() throws Exception {
        assertNull(mPreviousRankUtils.checkRanking(mNull));
    }

    @Test
    public void testCheckRankingWithUnchangedRanking() throws Exception {
        assertNull(mPreviousRankUtils.checkRanking(mUnchanged));
    }

}
