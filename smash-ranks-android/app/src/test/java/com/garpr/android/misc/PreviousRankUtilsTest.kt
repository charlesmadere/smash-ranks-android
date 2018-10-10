package com.garpr.android.misc

import com.garpr.android.BaseTest
import com.garpr.android.models.RankedPlayer
import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class PreviousRankUtilsTest : BaseTest() {

    private lateinit var decreased: RankedPlayer
    private lateinit var increased: RankedPlayer
    private lateinit var nullRank: RankedPlayer
    private lateinit var unchanged: RankedPlayer

    @Inject
    protected lateinit var gson: Gson

    @Inject
    protected lateinit var previousRankUtils: PreviousRankUtils


    companion object {
        private const val JSON_RANKING_DECREASED = "{\"rating\":30.25666689276485,\"name\":\"boback\",\"rank\":57,\"previous_rank\":42,\"id\":\"5888542ad2994e3bbfa52e1f\"}"
        private const val JSON_RANKING_INCREASED = "{\"rating\":37.46725497606898,\"name\":\"SAB | Ralph\",\"rank\":6,\"previous_rank\":9,\"id\":\"588852e8d2994e3bbfa52dcf\"}"
        private const val JSON_RANKING_NULL = "{\"id\":\"53c64dba8ab65f6e6651f7bc\",\"name\":\"Hax\",\"rank\":3,\"rating\":38.977594430937145}"
        private const val JSON_RANKING_UNCHANGED = "{\"rating\":40.97978935079751,\"name\":\"CLG. | PewPewU\",\"rank\":3,\"previous_rank\":3,\"id\":\"588852e8d2994e3bbfa52da7\"}"
    }

    @Before
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)

        decreased = gson.fromJson(JSON_RANKING_DECREASED, RankedPlayer::class.java)
        increased = gson.fromJson(JSON_RANKING_INCREASED, RankedPlayer::class.java)
        nullRank = gson.fromJson(JSON_RANKING_NULL, RankedPlayer::class.java)
        unchanged = gson.fromJson(JSON_RANKING_UNCHANGED, RankedPlayer::class.java)
    }

    @Test
    @Throws(Exception::class)
    fun testGetRankInfoWithDecreasedRanking() {
        assertEquals(PreviousRankUtils.Info.DECREASE, previousRankUtils.getRankInfo(decreased))
    }

    @Test
    @Throws(Exception::class)
    fun testGetRankInfoWithIncreasedRanking() {
        assertEquals(PreviousRankUtils.Info.INCREASE, previousRankUtils.getRankInfo(increased))
    }

    @Test
    @Throws(Exception::class)
    fun testGetRankInfoWithNull() {
        assertNull(previousRankUtils.getRankInfo(null))
    }

    @Test
    @Throws(Exception::class)
    fun testGetRankInfoWithNullRanking() {
        assertNull(previousRankUtils.getRankInfo(nullRank))
    }

    @Test
    @Throws(Exception::class)
    fun testGetRankInfoWithUnchangedRanking() {
        assertNull(previousRankUtils.getRankInfo(unchanged))
    }

}
