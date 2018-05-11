package com.garpr.android.models

import com.garpr.android.BaseTest
import com.google.gson.Gson
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class RankingCriteriaTest : BaseTest() {

    companion object {
        private const val RANKING_CRITERIA_1_JSON = "{\"ranking_activity_day_limit\":999,\"activeTF\":false,\"tournament_qualified_day_limit\":999,\"ranking_num_tourneys_attended\":3,\"id\":\"georgia smash 4\",\"display_name\":\"Georgia Smash 4\"}"
        private const val RANKING_CRITERIA_2_JSON = "{\"ranking_activity_day_limit\":110,\"activeTF\":true,\"tournament_qualified_day_limit\":90,\"ranking_num_tourneys_attended\":6,\"id\":\"nyc\",\"display_name\":\"NYC Metro Area\"}"
        private const val RANKING_CRITERIA_3_JSON = "{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":45,\"display_name\":\"Norcal\",\"id\":\"norcal\",\"tournament_qualified_day_limit\":1000}"
    }

    @Inject
    protected lateinit var gson: Gson

    private lateinit var rankingCriteria1: RankingCriteria
    private lateinit var rankingCriteria2: RankingCriteria
    private lateinit var rankingCriteria3: RankingCriteria


    @Before
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)

        rankingCriteria1 = gson.fromJson(RANKING_CRITERIA_1_JSON, AbsRegion::class.java)
        rankingCriteria2 = gson.fromJson(RANKING_CRITERIA_2_JSON, AbsRegion::class.java)
        rankingCriteria3 = gson.fromJson(RANKING_CRITERIA_3_JSON, AbsRegion::class.java)
    }

    @Test
    @Throws(Exception::class)
    fun testIsActiveRankingCriteria1() {
        assertFalse(rankingCriteria1.isActive)
    }

    @Test
    @Throws(Exception::class)
    fun testIsActiveRankingCriteria2() {
        assertTrue(rankingCriteria2.isActive)
    }

    @Test
    @Throws(Exception::class)
    fun testIsActiveRankingCriteria3() {
        assertTrue(rankingCriteria3.isActive)
    }

}
