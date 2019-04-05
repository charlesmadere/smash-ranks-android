package com.garpr.android.data.models

import com.garpr.android.BaseTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.Collections

@RunWith(RobolectricTestRunner::class)
class AbsPlayerTest : BaseTest() {

    companion object {
        private val REGION_GEORGIA = Region(
                displayName = "Georgia",
                id = "georgia",
                endpoint = Endpoint.NOT_GAR_PR
        )

        private val REGION_NORCAL = Region(
                displayName = "Norcal",
                id = "norcal",
                endpoint = Endpoint.GAR_PR
        )

        private val FAVORITE_PLAYER_1: AbsPlayer = FavoritePlayer(
                region = REGION_NORCAL,
                id = "583a4a15d2994e0577b05c74",
                name = "mikkuz"
        )

        private val FAVORITE_PLAYER_2: AbsPlayer = FavoritePlayer(
                region = REGION_GEORGIA,
                id = "583a4a15d2994e0577b05c86",
                name = "druggedfox"
        )

        private val FULL_PLAYER_1: AbsPlayer = FullPlayer(
                id = "58523b44d2994e15c7dea945",
                name = "gaR",
                ratings = mapOf(
                        "googlemtv" to Rating(37.05546025182014f, 2.0824461049194727f),
                        "norcal" to Rating(37.02140742867105f, 2.3075802611877165f)
                ),
                regions = listOf("norcal", "google")
        )

        private val LITE_PLAYER_1: AbsPlayer = LitePlayer(
                id = "583a4a15d2994e0577b05c74",
                name = "homemadewaffles"
        )

        private val LITE_PLAYER_2: AbsPlayer = LitePlayer(
                id = "5877eb55d2994e15c7dea97e",
                name = "Spark"
        )

        private val RANKED_PLAYER_1: AbsPlayer = RankedPlayer(
                id = "5888542ad2994e3bbfa52de4",
                name = "ycz6",
                rating = 36.81988474543549f,
                rank = 13,
                previousRank = 16
        )

        private val RANKED_PLAYER_2: AbsPlayer = RankedPlayer(
                id = "53c64dba8ab65f6e6651f7bc",
                name = "Hax",
                rating = 37.975921649503086f,
                rank = 2
        )
    }

    @Test
    fun testComparatorAlphabeticalOrder() {
        val list = listOf(FULL_PLAYER_1, LITE_PLAYER_2, LITE_PLAYER_1, FAVORITE_PLAYER_2,
                RANKED_PLAYER_1, FAVORITE_PLAYER_1, RANKED_PLAYER_2)
        Collections.sort(list, AbsPlayer.ALPHABETICAL_ORDER)

        assertEquals(FAVORITE_PLAYER_2, list[0])
        assertEquals(FULL_PLAYER_1, list[1])
        assertEquals(RANKED_PLAYER_2, list[2])
        assertEquals(LITE_PLAYER_1, list[3])
        assertEquals(FAVORITE_PLAYER_1, list[4])
        assertEquals(LITE_PLAYER_2, list[5])
        assertEquals(RANKED_PLAYER_1, list[6])
    }

    @Test
    fun testEquals() {
        assertEquals(FAVORITE_PLAYER_1, FAVORITE_PLAYER_1)
        assertEquals(FAVORITE_PLAYER_2, FAVORITE_PLAYER_2)
        assertNotEquals(FAVORITE_PLAYER_1, FAVORITE_PLAYER_2)

        assertEquals(FULL_PLAYER_1, FULL_PLAYER_1)
        assertNotEquals(FULL_PLAYER_1, LITE_PLAYER_1)
        assertNotEquals(FULL_PLAYER_1, RANKED_PLAYER_1)

        assertEquals(LITE_PLAYER_1, LITE_PLAYER_1)
        assertEquals(LITE_PLAYER_2, LITE_PLAYER_2)
        assertNotEquals(LITE_PLAYER_1, LITE_PLAYER_2)

        assertEquals(RANKED_PLAYER_1, RANKED_PLAYER_1)
        assertEquals(RANKED_PLAYER_2, RANKED_PLAYER_2)
        assertNotEquals(RANKED_PLAYER_1, RANKED_PLAYER_2)
    }

    @Test
    fun testHashCode() {
        assertEquals(FAVORITE_PLAYER_1.id.hashCode(), FAVORITE_PLAYER_1.hashCode())
        assertEquals(FAVORITE_PLAYER_2.id.hashCode(), FAVORITE_PLAYER_2.hashCode())
        assertEquals(FULL_PLAYER_1.id.hashCode(), FULL_PLAYER_1.hashCode())
        assertEquals(LITE_PLAYER_1.id.hashCode(), LITE_PLAYER_1.hashCode())
        assertEquals(LITE_PLAYER_2.id.hashCode(), LITE_PLAYER_2.hashCode())
        assertEquals(RANKED_PLAYER_1.id.hashCode(), RANKED_PLAYER_1.hashCode())
        assertEquals(RANKED_PLAYER_2.id.hashCode(), RANKED_PLAYER_2.hashCode())
    }

    @Test
    fun testKind() {
        assertEquals(AbsPlayer.Kind.FAVORITE, FAVORITE_PLAYER_1.kind)
        assertEquals(AbsPlayer.Kind.FAVORITE, FAVORITE_PLAYER_2.kind)
        assertEquals(AbsPlayer.Kind.FULL, FULL_PLAYER_1.kind)
        assertEquals(AbsPlayer.Kind.LITE, LITE_PLAYER_1.kind)
        assertEquals(AbsPlayer.Kind.LITE, LITE_PLAYER_2.kind)
        assertEquals(AbsPlayer.Kind.RANKED, RANKED_PLAYER_1.kind)
        assertEquals(AbsPlayer.Kind.RANKED, RANKED_PLAYER_2.kind)
    }

}
