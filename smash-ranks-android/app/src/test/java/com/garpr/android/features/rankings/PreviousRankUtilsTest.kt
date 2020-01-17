package com.garpr.android.features.rankings

import com.garpr.android.BaseTest
import com.garpr.android.data.models.RankedPlayer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.koin.test.inject

class PreviousRankUtilsTest : BaseTest() {

    protected val previousRankUtils: PreviousRankUtils by inject()

    companion object {
        private val DECREASED_RANK = RankedPlayer(
                rating = 30.25666689276485f,
                rank = 57,
                previousRank = 42,
                name = "boback",
                id = "5888542ad2994e3bbfa52e1f"
        )

        private val INCREASED_RANK = RankedPlayer(
                rating = 37.46725497606898f,
                rank = 6,
                previousRank = 9,
                name = "Ralph",
                id = "588852e8d2994e3bbfa52dcf"
        )

        private val NO_PREVIOUS_RANK = RankedPlayer(
                rating = 38.977594430937145f,
                rank = 3,
                previousRank = null,
                name = "Hax",
                id = "53c64dba8ab65f6e6651f7bc"
        )

        private val NULL_PREVIOUS_RANK = RankedPlayer(
                rating = 37.46725497606898f,
                rank = 7,
                previousRank = Int.MIN_VALUE,
                name = "Azel",
                id = "588852e8d2994e3bbfa52d9f"
        )

        private val UNCHANGED_RANK = RankedPlayer(
                rating = 40.97978935079751f,
                rank = 3,
                previousRank = 3,
                name = "PewPewU",
                id = "588852e8d2994e3bbfa52da7"
        )
    }

    @Test
    fun testGetRankInfoWithDecreasedRank() {
        assertEquals(PreviousRankUtils.Info.DECREASE, previousRankUtils.getRankInfo(
                DECREASED_RANK))
    }

    @Test
    fun testGetRankInfoWithIncreasedRank() {
        assertEquals(PreviousRankUtils.Info.INCREASE, previousRankUtils.getRankInfo(
                INCREASED_RANK))
    }

    @Test
    fun testGetRankInfoWithNull() {
        assertNull(previousRankUtils.getRankInfo(null))
    }

    @Test
    fun testGetRankInfoWithNoPreviousRank() {
        assertNull(previousRankUtils.getRankInfo(NO_PREVIOUS_RANK))
    }

    @Test
    fun testGetRankInfoWithNullPreviousRank() {
        assertEquals(PreviousRankUtils.Info.NO_CHANGE, previousRankUtils.getRankInfo(
                NULL_PREVIOUS_RANK))
    }

    @Test
    fun testGetRankInfoWithUnchangedRank() {
        assertEquals(PreviousRankUtils.Info.NO_CHANGE, previousRankUtils.getRankInfo(
                UNCHANGED_RANK))
    }

}
