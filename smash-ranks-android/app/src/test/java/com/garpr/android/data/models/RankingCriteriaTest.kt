package com.garpr.android.data.models

import com.garpr.android.BaseTest
import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class RankingCriteriaTest : BaseTest() {

    @Inject
    protected lateinit var gson: Gson


    companion object {
        private const val RANKING_CRITERIA_1_JSON = "{\"ranking_activity_day_limit\":999,\"activeTF\":false,\"tournament_qualified_day_limit\":999,\"ranking_num_tourneys_attended\":3,\"id\":\"georgia smash 4\",\"display_name\":\"Georgia Smash 4\"}"
        private const val RANKING_CRITERIA_2_JSON = "{\"ranking_activity_day_limit\":110,\"activeTF\":true,\"tournament_qualified_day_limit\":90,\"ranking_num_tourneys_attended\":6,\"id\":\"nyc\",\"display_name\":\"NYC Metro Area\"}"
        private const val RANKING_CRITERIA_3_JSON = "{\"display_name\":\"Norcal\",\"id\":\"norcal\"}"
    }

    @Before
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)
    }

    @Test
    fun testFromJson1() {
        val rc: RankingCriteria = gson.fromJson(
                RANKING_CRITERIA_1_JSON, AbsRegion::class.java)
        assertFalse(rc.isActive)
        assertEquals(999, rc.rankingActivityDayLimit)
        assertEquals(3, rc.rankingNumTourneysAttended)
        assertEquals(999, rc.tournamentQualifiedDayLimit)
    }

    @Test
    fun testFromJson2() {
        val rc: RankingCriteria = gson.fromJson(
                RANKING_CRITERIA_2_JSON, AbsRegion::class.java)
        assertTrue(rc.isActive)
        assertEquals(110, rc.rankingActivityDayLimit)
        assertEquals(6, rc.rankingNumTourneysAttended)
        assertEquals(90, rc.tournamentQualifiedDayLimit)
    }

    @Test
    fun testFromJson3() {
        val rc: RankingCriteria = gson.fromJson(
                RANKING_CRITERIA_3_JSON, AbsRegion::class.java)
        assertTrue(rc.isActive)
        assertNull(rc.rankingActivityDayLimit)
        assertNull(rc.rankingNumTourneysAttended)
        assertNull(rc.tournamentQualifiedDayLimit)
    }

}
