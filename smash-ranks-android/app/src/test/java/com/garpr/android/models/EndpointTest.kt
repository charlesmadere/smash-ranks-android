package com.garpr.android.models

import com.garpr.android.BaseTest
import com.garpr.android.BuildConfig
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class EndpointTest : BaseTest() {

    @Test
    @Throws(Exception::class)
    fun testGetApiPath() {
        assertEquals("https://www.garpr.com:3001/", Endpoint.GAR_PR.apiPath)
        assertEquals("https://www.notgarpr.com:3001/", Endpoint.NOT_GAR_PR.apiPath)
    }

    @Test
    @Throws(Exception::class)
    fun testGetBasePath() {
        assertEquals("https://www.garpr.com", Endpoint.GAR_PR.basePath)
        assertEquals("https://www.notgarpr.com", Endpoint.NOT_GAR_PR.basePath)
    }

    @Test
    @Throws(Exception::class)
    fun testGetHeadToHeadApiPath() {
        assertEquals("https://www.garpr.com:3001/norcal/matches/5877eb55d2994e15c7dea97e?opponent=5877eb55d2994e15c7dea98b",
                Endpoint.GAR_PR.getHeadToHeadApiPath("norcal", "5877eb55d2994e15c7dea97e",
                        "5877eb55d2994e15c7dea98b"))

        assertEquals("https://www.notgarpr.com:3001/nyc/matches/545b240b8ab65f7a95f74940?opponent=545b233a8ab65f7a95f74854",
                Endpoint.NOT_GAR_PR.getHeadToHeadApiPath("nyc", "545b240b8ab65f7a95f74940",
                        "545b233a8ab65f7a95f74854"))
    }

    @Test
    @Throws(Exception::class)
    fun testGetMatchesApiPath() {
        assertEquals("https://www.garpr.com:3001/norcal/matches/5877eb55d2994e15c7dea97e",
                Endpoint.GAR_PR.getMatchesApiPath("norcal", "5877eb55d2994e15c7dea97e"))

        assertEquals("https://www.notgarpr.com:3001/nyc/matches/545b240b8ab65f7a95f74940",
                Endpoint.NOT_GAR_PR.getMatchesApiPath("nyc", "545b240b8ab65f7a95f74940"))
    }

    @Test
    @Throws(Exception::class)
    fun testGetPlayerApiPath() {
        assertEquals("https://www.garpr.com:3001/norcal/players/588852e7d2994e3bbfa52d6e",
                Endpoint.GAR_PR.getPlayerApiPath("norcal", "588852e7d2994e3bbfa52d6e"))

        assertEquals("https://www.notgarpr.com:3001/newjersey/players/545c854e8ab65f127805bd6f",
                Endpoint.NOT_GAR_PR.getPlayerApiPath("newjersey", "545c854e8ab65f127805bd6f"))
    }

    @Test
    @Throws(Exception::class)
    fun testGetPlayersApiPath() {
        assertEquals("https://www.garpr.com:3001/norcal/players",
                Endpoint.GAR_PR.getPlayersApiPath("norcal"))

        assertEquals("https://www.notgarpr.com:3001/nyc/players",
                Endpoint.NOT_GAR_PR.getPlayersApiPath("nyc"))
    }

    @Test
    @Throws(Exception::class)
    fun testGetPlayerWebPath() {
        assertEquals("https://www.garpr.com/#/norcal/players/588852e7d2994e3bbfa52d6e",
                Endpoint.GAR_PR.getPlayerWebPath("norcal", "588852e7d2994e3bbfa52d6e"))

        assertEquals("https://www.notgarpr.com/#/newjersey/players/545c854e8ab65f127805bd6f",
                Endpoint.NOT_GAR_PR.getPlayerWebPath("newjersey", "545c854e8ab65f127805bd6f"))
    }

    @Test
    @Throws(Exception::class)
    fun testGetRankingsApiPath() {
        assertEquals("https://www.garpr.com:3001/norcal/rankings",
                Endpoint.GAR_PR.getRankingsApiPath("norcal"))

        assertEquals("https://www.notgarpr.com:3001/chicago/rankings",
                Endpoint.NOT_GAR_PR.getRankingsApiPath("chicago"))
    }

    @Test
    @Throws(Exception::class)
    fun testGetRankingsWebPath() {
        assertEquals("https://www.garpr.com/#/norcal/rankings",
                Endpoint.GAR_PR.getRankingsWebPath("norcal"))

        assertEquals("https://www.notgarpr.com/#/newjersey/rankings",
                Endpoint.NOT_GAR_PR.getRankingsWebPath("newjersey"))
    }

    @Test
    @Throws(Exception::class)
    fun testGetRegionsApiPath() {
        assertEquals("https://www.garpr.com:3001/regions", Endpoint.GAR_PR.regionsApiPath)
        assertEquals("https://www.notgarpr.com:3001/regions", Endpoint.NOT_GAR_PR.regionsApiPath)
    }

    @Test
    @Throws(Exception::class)
    fun testGetTournamentApiPath() {
        assertEquals("https://www.garpr.com:3001/norcal/tournaments/5888282dd2994e0d53b14559",
                Endpoint.GAR_PR.getTournamentApiPath("norcal", "5888282dd2994e0d53b14559"))

        assertEquals("https://www.notgarpr.com:3001/georgia/tournaments/58aa66a01d41c82d83408886",
                Endpoint.NOT_GAR_PR.getTournamentApiPath("georgia", "58aa66a01d41c82d83408886"))
    }

    @Test
    @Throws(Exception::class)
    fun testGetTournamentsApiPath() {
        assertEquals("https://www.garpr.com:3001/norcal/tournaments",
                Endpoint.GAR_PR.getTournamentsApiPath("norcal"))

        assertEquals("https://www.notgarpr.com:3001/georgia/tournaments",
                Endpoint.NOT_GAR_PR.getTournamentsApiPath("georgia"))
    }

    @Test
    @Throws(Exception::class)
    fun testGetTournamentWebPath() {
        assertEquals("https://www.garpr.com/#/norcal/tournaments/58d8c3e8d2994e057e91f7fd",
                Endpoint.GAR_PR.getTournamentWebPath("norcal", "58d8c3e8d2994e057e91f7fd"))

        assertEquals("https://www.notgarpr.com/#/newjersey/tournaments/58bdc6e31d41c867e937fc15",
                Endpoint.NOT_GAR_PR.getTournamentWebPath("newjersey", "58bdc6e31d41c867e937fc15"))
    }

    @Test
    @Throws(Exception::class)
    fun testGetTournamentsWebPath() {
        assertEquals("https://www.garpr.com/#/googlemtv/tournaments",
                Endpoint.GAR_PR.getTournamentsWebPath("googlemtv"))

        assertEquals("https://www.notgarpr.com/#/nyc/tournaments",
                Endpoint.NOT_GAR_PR.getTournamentsWebPath("nyc"))

    }

    @Test
    @Throws(Exception::class)
    fun testGetWebPath() {
        assertEquals("https://www.garpr.com/#/", Endpoint.GAR_PR.getWebPath())
        assertEquals("https://www.notgarpr.com/#/", Endpoint.NOT_GAR_PR.getWebPath())

        assertEquals("https://www.garpr.com/#/norcal", Endpoint.GAR_PR.getWebPath("norcal"))
        assertEquals("https://www.notgarpr.com/#/chicago", Endpoint.NOT_GAR_PR.getWebPath("chicago"))
    }

}
