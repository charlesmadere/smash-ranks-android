package com.garpr.android.models;

import com.garpr.android.BaseTest;
import com.garpr.android.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class EndpointTest extends BaseTest {

    @Test
    public void testGetApiPath() throws Exception {
        assertEquals("https://www.garpr.com:3001/", Endpoint.GAR_PR.getApiPath());
        assertEquals("https://www.notgarpr.com:3001/", Endpoint.NOT_GAR_PR.getApiPath());
    }

    @Test
    public void testGetBasePath() throws Exception {
        assertEquals("https://www.garpr.com", Endpoint.GAR_PR.getBasePath());
        assertEquals("https://www.notgarpr.com", Endpoint.NOT_GAR_PR.getBasePath());
    }

    @Test
    public void testGetHeadToHeadApiPath() throws Exception {
        assertEquals("https://www.garpr.com:3001/norcal/matches/5877eb55d2994e15c7dea97e?opponent=5877eb55d2994e15c7dea98b",
                Endpoint.GAR_PR.getHeadToHeadApiPath("norcal", "5877eb55d2994e15c7dea97e", "5877eb55d2994e15c7dea98b"));

        assertEquals("https://www.notgarpr.com:3001/nyc/matches/545b240b8ab65f7a95f74940?opponent=545b233a8ab65f7a95f74854",
                Endpoint.NOT_GAR_PR.getHeadToHeadApiPath("nyc", "545b240b8ab65f7a95f74940", "545b233a8ab65f7a95f74854"));
    }

    @Test
    public void testGetMatchesApiPath() throws Exception {
        assertEquals("https://www.garpr.com:3001/norcal/matches/5877eb55d2994e15c7dea97e",
                Endpoint.GAR_PR.getMatchesApiPath("norcal", "5877eb55d2994e15c7dea97e"));

        assertEquals("https://www.notgarpr.com:3001/nyc/matches/545b240b8ab65f7a95f74940",
                Endpoint.NOT_GAR_PR.getMatchesApiPath("nyc", "545b240b8ab65f7a95f74940"));
    }

    @Test
    public void testGetPlayerApiPath() throws Exception {
        assertEquals("https://www.garpr.com:3001/norcal/players/588852e7d2994e3bbfa52d6e",
                Endpoint.GAR_PR.getPlayerApiPath("norcal", "588852e7d2994e3bbfa52d6e"));

        assertEquals("https://www.notgarpr.com:3001/newjersey/players/545c854e8ab65f127805bd6f",
                Endpoint.NOT_GAR_PR.getPlayerApiPath("newjersey", "545c854e8ab65f127805bd6f"));
    }

    @Test
    public void testGetPlayersApiPath() throws Exception {
        assertEquals("https://www.garpr.com:3001/norcal/players",
                Endpoint.GAR_PR.getPlayersApiPath("norcal"));

        assertEquals("https://www.notgarpr.com:3001/nyc/players",
                Endpoint.NOT_GAR_PR.getPlayersApiPath("nyc"));
    }

    @Test
    public void testGetPlayerWebPath() throws Exception {
        assertEquals("https://www.garpr.com/#/norcal/players/588852e7d2994e3bbfa52d6e",
                Endpoint.GAR_PR.getPlayerWebPath("norcal", "588852e7d2994e3bbfa52d6e"));

        assertEquals("https://www.notgarpr.com/#/newjersey/players/545c854e8ab65f127805bd6f",
                Endpoint.NOT_GAR_PR.getPlayerWebPath("newjersey", "545c854e8ab65f127805bd6f"));
    }

    @Test
    public void testGetRankingsApiPath() throws Exception {
        assertEquals("https://www.garpr.com:3001/norcal/rankings",
                Endpoint.GAR_PR.getRankingsApiPath("norcal"));

        assertEquals("https://www.notgarpr.com:3001/chicago/rankings",
                Endpoint.NOT_GAR_PR.getRankingsApiPath("chicago"));
    }

    @Test
    public void testGetRegionsApiPath() throws Exception {
        assertEquals("https://www.garpr.com:3001/regions", Endpoint.GAR_PR.getRegionsApiPath());
        assertEquals("https://www.notgarpr.com:3001/regions", Endpoint.NOT_GAR_PR.getRegionsApiPath());
    }

    @Test
    public void testGetTournamentApiPath() throws Exception {
        assertEquals("https://www.garpr.com:3001/norcal/tournaments/5888282dd2994e0d53b14559",
                Endpoint.GAR_PR.getTournamentApiPath("norcal", "5888282dd2994e0d53b14559"));

        assertEquals("https://www.notgarpr.com:3001/georgia/tournaments/58aa66a01d41c82d83408886",
                Endpoint.NOT_GAR_PR.getTournamentApiPath("georgia", "58aa66a01d41c82d83408886"));
    }

    @Test
    public void testGetTournamentsApiPath() throws Exception {
        assertEquals("https://www.garpr.com:3001/norcal/tournaments",
                Endpoint.GAR_PR.getTournamentsApiPath("norcal"));

        assertEquals("https://www.notgarpr.com:3001/georgia/tournaments",
                Endpoint.NOT_GAR_PR.getTournamentsApiPath("georgia"));
    }

    @Test
    public void testGetTournamentWebPath() throws Exception {
        assertEquals("https://www.garpr.com/#/norcal/tournaments/58d8c3e8d2994e057e91f7fd",
                Endpoint.GAR_PR.getTournamentWebPath("norcal", "58d8c3e8d2994e057e91f7fd"));

        assertEquals("https://www.notgarpr.com/#/newjersey/tournaments/58bdc6e31d41c867e937fc15",
                Endpoint.NOT_GAR_PR.getTournamentWebPath("newjersey", "58bdc6e31d41c867e937fc15"));
    }

    @Test
    public void testGetWebPath() throws Exception {
        assertEquals("https://www.garpr.com/#/", Endpoint.GAR_PR.getWebPath());
        assertEquals("https://www.notgarpr.com/#/", Endpoint.NOT_GAR_PR.getWebPath());
    }

}
