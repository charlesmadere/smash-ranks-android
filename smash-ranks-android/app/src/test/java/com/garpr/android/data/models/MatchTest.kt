package com.garpr.android.data.models

import com.garpr.android.BaseTest
import com.garpr.android.extensions.requireFromJson
import com.squareup.moshi.Moshi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.Collections
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class MatchTest : BaseTest() {

    private lateinit var match1: Match
    private lateinit var match2: Match
    private lateinit var match3: Match

    @Inject
    protected lateinit var moshi: Moshi


    companion object {
        private const val JSON_MATCH_1 = "{\"opponent_name\":\"Mao\",\"tournament_name\":\"Norcal Validated 1\",\"result\":\"win\",\"opponent_id\":\"588852e8d2994e3bbfa52dca\",\"tournament_id\":\"588850d5d2994e3bbfa52d67\",\"tournament_date\":\"01/14/17\"}"
        private const val JSON_MATCH_2 = "{\"opponent_name\":\"Arcadia\",\"tournament_name\":\"Get Smashed #108\",\"result\":\"excluded\",\"opponent_id\":\"5877eb55d2994e15c7dea981\",\"tournament_id\":\"58bfaed4d2994e057e91f71b\",\"tournament_date\":\"03/07/17\"}"
        private const val JSON_MATCH_3 = "{\"opponent_name\":\"Darrell\",\"tournament_name\":\"The Gator Games #3\",\"result\":\"lose\",\"opponent_id\":\"587a951dd2994e15c7deaa00\",\"tournament_id\":\"58a9139cd2994e756952ad94\",\"tournament_date\":\"02/18/17\"}"
    }

    @Before
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)

        val matchAdapter = moshi.adapter(Match::class.java)
        match1 = matchAdapter.requireFromJson(JSON_MATCH_1)
        match2 = matchAdapter.requireFromJson(JSON_MATCH_2)
        match3 = matchAdapter.requireFromJson(JSON_MATCH_3)
    }

    @Test
    fun testChronologicalOrder() {
        val list = listOf(match1, match2, match3)
        Collections.sort(list, Match.CHRONOLOGICAL_ORDER)

        assertEquals(match1, list[0])
        assertEquals(match3, list[1])
        assertEquals(match2, list[2])
    }

    @Test
    fun testEquals() {
        assertEquals(match1, match1)
        assertEquals(match2, match2)
        assertEquals(match3, match3)
    }

    @Test
    fun testHashCode() {
        assertEquals(match1.hashCode(), match1.hashCode())
        assertEquals(match2.hashCode(), match2.hashCode())
        assertEquals(match3.hashCode(), match3.hashCode())

        assertNotEquals(match1.hashCode(), match2.hashCode())
        assertNotEquals(match1.hashCode(), match3.hashCode())
        assertNotEquals(match2.hashCode(), match3.hashCode())
    }

    @Test
    fun testReverseChronologicalOrder() {
        val list = listOf(match1, match2, match3)
        Collections.sort(list, Match.REVERSE_CHRONOLOGICAL_ORDER)

        assertEquals(match2, list[0])
        assertEquals(match3, list[1])
        assertEquals(match1, list[2])
    }

}
