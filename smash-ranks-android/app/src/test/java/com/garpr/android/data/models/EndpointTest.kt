package com.garpr.android.data.models

import com.garpr.android.BaseTest
import com.garpr.android.data.models.Endpoint
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class EndpointTest : BaseTest() {

    @Test
    fun testGetBasePath() {
        assertEquals("https://www.garpr.com", Endpoint.GAR_PR.basePath)
        assertEquals("https://www.notgarpr.com", Endpoint.NOT_GAR_PR.basePath)
    }

    @Test
    fun testGetPlayerWebPath() {
        assertEquals("https://www.garpr.com/#/norcal/players/588852e7d2994e3bbfa52d6e",
                Endpoint.GAR_PR.getPlayerWebPath("norcal", "588852e7d2994e3bbfa52d6e"))

        assertEquals("https://www.notgarpr.com/#/newjersey/players/545c854e8ab65f127805bd6f",
                Endpoint.NOT_GAR_PR.getPlayerWebPath("newjersey", "545c854e8ab65f127805bd6f"))
    }

    @Test
    fun testGetRankingsWebPath() {
        assertEquals("https://www.garpr.com/#/norcal/rankings",
                Endpoint.GAR_PR.getRankingsWebPath("norcal"))

        assertEquals("https://www.notgarpr.com/#/newjersey/rankings",
                Endpoint.NOT_GAR_PR.getRankingsWebPath("newjersey"))
    }

    @Test
    fun testGetTournamentWebPath() {
        assertEquals("https://www.garpr.com/#/norcal/tournaments/58d8c3e8d2994e057e91f7fd",
                Endpoint.GAR_PR.getTournamentWebPath("norcal", "58d8c3e8d2994e057e91f7fd"))

        assertEquals("https://www.notgarpr.com/#/newjersey/tournaments/58bdc6e31d41c867e937fc15",
                Endpoint.NOT_GAR_PR.getTournamentWebPath("newjersey", "58bdc6e31d41c867e937fc15"))
    }

    @Test
    fun testGetTournamentsWebPath() {
        assertEquals("https://www.garpr.com/#/googlemtv/tournaments",
                Endpoint.GAR_PR.getTournamentsWebPath("googlemtv"))

        assertEquals("https://www.notgarpr.com/#/nyc/tournaments",
                Endpoint.NOT_GAR_PR.getTournamentsWebPath("nyc"))
    }

    @Test
    fun testGetWebPath() {
        assertEquals("https://www.garpr.com/#/", Endpoint.GAR_PR.getWebPath())
        assertEquals("https://www.notgarpr.com/#/", Endpoint.NOT_GAR_PR.getWebPath())

        assertEquals("https://www.garpr.com/#/norcal", Endpoint.GAR_PR.getWebPath("norcal"))
        assertEquals("https://www.notgarpr.com/#/chicago", Endpoint.NOT_GAR_PR.getWebPath("chicago"))
    }

}
