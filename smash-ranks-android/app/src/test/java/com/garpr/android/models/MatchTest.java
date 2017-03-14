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
public class MatchTest extends BaseTest {

    private static final String JSON_ONE = "{\"opponent_name\":\"Mao\",\"tournament_name\":\"Norcal Validated 1\",\"result\":\"win\",\"opponent_id\":\"588852e8d2994e3bbfa52dca\",\"tournament_id\":\"588850d5d2994e3bbfa52d67\",\"tournament_date\":\"01/14/17\"}";
    private static final String JSON_TWO = "{\"opponent_name\":\"Arcadia\",\"tournament_name\":\"Get Smashed #108\",\"result\":\"excluded\",\"opponent_id\":\"5877eb55d2994e15c7dea981\",\"tournament_id\":\"58bfaed4d2994e057e91f71b\",\"tournament_date\":\"03/07/17\"}";
    private static final String JSON_THREE = "{\"opponent_name\":\"Darrell\",\"tournament_name\":\"The Gator Games #3\",\"result\":\"lose\",\"opponent_id\":\"587a951dd2994e15c7deaa00\",\"tournament_id\":\"58a9139cd2994e756952ad94\",\"tournament_date\":\"02/18/17\"}";

    @Inject
    Gson mGson;


    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        getTestAppComponent().inject(this);
    }

    @Test
    public void testFromJsonOne() throws Exception {
        final Match match = mGson.fromJson(JSON_ONE, Match.class);
        assertNotNull(match);

        assertEquals(match.getOpponent().getId(), "588852e8d2994e3bbfa52dca");
        assertEquals(match.getOpponent().getName(), "Mao");
        assertEquals(match.getTournament().getId(), "588850d5d2994e3bbfa52d67");
        assertEquals(match.getTournament().getName(), "Norcal Validated 1");
        assertEquals(match.getResult(), Match.Result.WIN);

        final SimpleDate date = mGson.fromJson("\"01/14/17\"", SimpleDate.class);
        assertEquals(date, match.getTournament().getDate());
    }

    @Test
    public void testFromJsonTwo() throws Exception {
        final Match match = mGson.fromJson(JSON_TWO, Match.class);
        assertNotNull(match);

        assertEquals(match.getOpponent().getId(), "5877eb55d2994e15c7dea981");
        assertEquals(match.getOpponent().getName(), "Arcadia");
        assertEquals(match.getTournament().getId(), "58bfaed4d2994e057e91f71b");
        assertEquals(match.getTournament().getName(), "Get Smashed #108");
        assertEquals(match.getResult(), Match.Result.EXCLUDED);

        final SimpleDate date = mGson.fromJson("\"03/07/17\"", SimpleDate.class);
        assertEquals(date, match.getTournament().getDate());
    }

    @Test
    public void testFromJsonThree() throws Exception {
        final Match match = mGson.fromJson(JSON_THREE, Match.class);
        assertNotNull(match);

        assertEquals(match.getOpponent().getId(), "587a951dd2994e15c7deaa00");
        assertEquals(match.getOpponent().getName(), "Darrell");
        assertEquals(match.getTournament().getId(), "58a9139cd2994e756952ad94");
        assertEquals(match.getTournament().getName(), "The Gator Games #3");
        assertEquals(match.getResult(), Match.Result.LOSE);

        final SimpleDate date = mGson.fromJson("\"02/18/17\"", SimpleDate.class);
        assertEquals(date, match.getTournament().getDate());
    }

    @Test
    public void testFromNull() throws Exception {
        final Match match = mGson.fromJson((String) null, Match.class);
        assertNull(match);
    }

}
