package com.garpr.android.data.models

import com.garpr.android.BaseTest
import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class MatchTest : BaseTest() {

    private lateinit var match1: Match
    private lateinit var match2: Match
    private lateinit var match3: Match

    @Inject
    protected lateinit var gson: Gson


    companion object {
        private const val JSON_MATCH_1 = "{\"opponent_name\":\"Mao\",\"tournament_name\":\"Norcal Validated 1\",\"result\":\"win\",\"opponent_id\":\"588852e8d2994e3bbfa52dca\",\"tournament_id\":\"588850d5d2994e3bbfa52d67\",\"tournament_date\":\"01/14/17\"}"
        private const val JSON_MATCH_2 = "{\"opponent_name\":\"Arcadia\",\"tournament_name\":\"Get Smashed #108\",\"result\":\"excluded\",\"opponent_id\":\"5877eb55d2994e15c7dea981\",\"tournament_id\":\"58bfaed4d2994e057e91f71b\",\"tournament_date\":\"03/07/17\"}"
        private const val JSON_MATCH_3 = "{\"opponent_name\":\"Darrell\",\"tournament_name\":\"The Gator Games #3\",\"result\":\"lose\",\"opponent_id\":\"587a951dd2994e15c7deaa00\",\"tournament_id\":\"58a9139cd2994e756952ad94\",\"tournament_date\":\"02/18/17\"}"
    }

    @Before
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)

        match1 = gson.fromJson(JSON_MATCH_1, Match::class.java)
        match2 = gson.fromJson(JSON_MATCH_2, Match::class.java)
        match3 = gson.fromJson(JSON_MATCH_3, Match::class.java)
    }

    @Test
    @Throws(Exception::class)
    fun testEquals() {
        assertEquals(match1, match1)
        assertEquals(match2, match2)
        assertEquals(match3, match3)
    }

    @Test
    @Throws(Exception::class)
    fun testFromJson1() {
        assertEquals("588852e8d2994e3bbfa52dca", match1.opponent.id)
        assertEquals("Mao", match1.opponent.name)
        assertEquals("588850d5d2994e3bbfa52d67", match1.tournament.id)
        assertEquals("Norcal Validated 1", match1.tournament.name)
        assertEquals(MatchResult.WIN, match1.result)

        val date = gson.fromJson("\"01/14/17\"", SimpleDate::class.java)
        assertEquals(match1.tournament.date, date)
    }

    @Test
    @Throws(Exception::class)
    fun testFromJson2() {
        assertEquals("5877eb55d2994e15c7dea981", match2.opponent.id)
        assertEquals("Arcadia", match2.opponent.name)
        assertEquals("58bfaed4d2994e057e91f71b", match2.tournament.id)
        assertEquals("Get Smashed #108", match2.tournament.name)
        assertEquals(MatchResult.EXCLUDED, match2.result)

        val date = gson.fromJson("\"03/07/17\"", SimpleDate::class.java)
        assertEquals(match2.tournament.date, date)
    }

    @Test
    @Throws(Exception::class)
    fun testFromJson3() {
        assertEquals("587a951dd2994e15c7deaa00", match3.opponent.id)
        assertEquals("Darrell", match3.opponent.name)
        assertEquals("58a9139cd2994e756952ad94", match3.tournament.id)
        assertEquals("The Gator Games #3", match3.tournament.name)
        assertEquals(MatchResult.LOSE, match3.result)

        val date = gson.fromJson("\"02/18/17\"", SimpleDate::class.java)
        assertEquals(match3.tournament.date, date)
    }

    @Test
    @Throws(Exception::class)
    fun testFromJsonNull() {
        val match = gson.fromJson(null as String?, Match::class.java)
        assertNull(match)
    }

    @Test
    @Throws(Exception::class)
    fun testHashCode() {
        assertEquals(match1.hashCode(), match1.hashCode())
        assertEquals(match2.hashCode(), match2.hashCode())
        assertEquals(match3.hashCode(), match3.hashCode())
    }

}
