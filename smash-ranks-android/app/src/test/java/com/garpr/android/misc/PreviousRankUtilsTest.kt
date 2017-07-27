package com.garpr.android.misc

import com.garpr.android.BaseTest
import com.garpr.android.models.Ranking
import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class PreviousRankUtilsTest : BaseTest() {

    lateinit private var mDecreased: Ranking
    lateinit private var mIncreased: Ranking
    lateinit private var mNull: Ranking
    lateinit private var mUnchanged: Ranking

    @Inject
    lateinit protected var mGson: Gson

    @Inject
    lateinit protected var mPreviousRankUtils: PreviousRankUtils


    companion object {
        private const val JSON_RANKING_DECREASED = "{\"rating\":30.25666689276485,\"name\":\"boback\",\"rank\":57,\"previous_rank\":42,\"id\":\"5888542ad2994e3bbfa52e1f\"}"
        private const val JSON_RANKING_INCREASED = "{\"rating\":37.46725497606898,\"name\":\"SAB | Ralph\",\"rank\":6,\"previous_rank\":9,\"id\":\"588852e8d2994e3bbfa52dcf\"}"
        private const val JSON_RANKING_NULL = "{\"id\":\"53c64dba8ab65f6e6651f7bc\",\"name\":\"Hax\",\"rank\":3,\"rating\":38.977594430937145}"
        private const val JSON_RANKING_UNCHANGED = "{\"rating\":40.97978935079751,\"name\":\"CLG. | PewPewU\",\"rank\":3,\"previous_rank\":3,\"id\":\"588852e8d2994e3bbfa52da7\"}"
    }

    @Before
    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)

        mDecreased = mGson.fromJson(JSON_RANKING_DECREASED, Ranking::class.java)
        mIncreased = mGson.fromJson(JSON_RANKING_INCREASED, Ranking::class.java)
        mNull = mGson.fromJson(JSON_RANKING_NULL, Ranking::class.java)
        mUnchanged = mGson.fromJson(JSON_RANKING_UNCHANGED, Ranking::class.java)
    }

    @Test
    @Throws(Exception::class)
    fun testCheckRankingWithDecreasedRanking() {
        assertEquals(mPreviousRankUtils.checkRanking(mDecreased), PreviousRankUtils.Info.DECREASE)
    }

    @Test
    @Throws(Exception::class)
    fun testCheckRankingWithIncreasedRanking() {
        assertEquals(mPreviousRankUtils.checkRanking(mIncreased), PreviousRankUtils.Info.INCREASE)
    }

    @Test
    @Throws(Exception::class)
    fun testCheckRankingWithNull() {
        assertNull(mPreviousRankUtils.checkRanking(null))
    }

    @Test
    @Throws(Exception::class)
    fun testCheckRankingWithNullRanking() {
        assertNull(mPreviousRankUtils.checkRanking(mNull))
    }

    @Test
    @Throws(Exception::class)
    fun testCheckRankingWithUnchangedRanking() {
        assertNull(mPreviousRankUtils.checkRanking(mUnchanged))
    }

}
