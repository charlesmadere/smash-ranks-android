package com.garpr.android.data.models

import com.garpr.android.BaseTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import java.util.Collections
import java.util.Objects

class AbsPlayerTest : BaseTest() {

    companion object {
        private val GEORGIA = Region(
                displayName = "Georgia",
                id = "georgia",
                endpoint = Endpoint.NOT_GAR_PR
        )

        private val NORCAL = Region(
                displayName = "Norcal",
                id = "norcal",
                endpoint = Endpoint.GAR_PR
        )

        private val MIKKUZ: AbsPlayer = FavoritePlayer(
                region = NORCAL,
                id = "583a4a15d2994e0577b05c74",
                name = "mikkuz"
        )

        private val GEORGIA_DRUGGEDFOX: AbsPlayer = FavoritePlayer(
                region = GEORGIA,
                id = "583a4a15d2994e0577b05c86",
                name = "druggedfox"
        )

        private val NORCAL_DRUGGEDFOX: AbsPlayer = FavoritePlayer(
                region = NORCAL,
                id = "583a4a15d2994e0577b05c86",
                name = "druggedfox"
        )

        private val GAR: AbsPlayer = FullPlayer(
                id = "58523b44d2994e15c7dea945",
                name = "gaR",
                ratings = mapOf(
                        "googlemtv" to Rating(37.05546025182014f, 2.0824461049194727f),
                        "norcal" to Rating(37.02140742867105f, 2.3075802611877165f)
                ),
                regions = listOf("norcal", "google")
        )

        private val HMW: AbsPlayer = LitePlayer(
                id = "583a4a15d2994e0577b05c74",
                name = "homemadewaffles"
        )

        private val SPARK: AbsPlayer = LitePlayer(
                id = "5877eb55d2994e15c7dea97e",
                name = "Spark"
        )

        private val NO_REGION_DRUGGEDFOX: AbsPlayer = LitePlayer(
                id = "583a4a15d2994e0577b05c86",
                name = "druggedfox"
        )

        private val YCZ6: AbsPlayer = RankedPlayer(
                id = "5888542ad2994e3bbfa52de4",
                name = "ycz6",
                rating = 36.81988474543549f,
                rank = 13,
                previousRank = 16
        )

        private val HAX: AbsPlayer = RankedPlayer(
                id = "53c64dba8ab65f6e6651f7bc",
                name = "Hax",
                rating = 37.975921649503086f,
                rank = 2
        )
    }

    @Test
    fun testComparatorAlphabeticalOrder() {
        val list = listOf(GAR, SPARK, NO_REGION_DRUGGEDFOX, HMW, GEORGIA_DRUGGEDFOX, YCZ6, MIKKUZ,
                HAX, NORCAL_DRUGGEDFOX)
        Collections.sort(list, AbsPlayer.ALPHABETICAL_ORDER)

        assertEquals(NO_REGION_DRUGGEDFOX, list[0])
        assertEquals(NORCAL_DRUGGEDFOX, list[1])
        assertEquals(GEORGIA_DRUGGEDFOX, list[2])
        assertEquals(GAR, list[3])
        assertEquals(HAX, list[4])
        assertEquals(HMW, list[5])
        assertEquals(MIKKUZ, list[6])
        assertEquals(SPARK, list[7])
        assertEquals(YCZ6, list[8])
    }

    @Test
    fun testEquals() {
        assertEquals(GEORGIA_DRUGGEDFOX, GEORGIA_DRUGGEDFOX)
        assertEquals(GEORGIA_DRUGGEDFOX, NO_REGION_DRUGGEDFOX)
        assertEquals(NORCAL_DRUGGEDFOX, NORCAL_DRUGGEDFOX)
        assertEquals(NORCAL_DRUGGEDFOX, NO_REGION_DRUGGEDFOX)
        assertEquals(NO_REGION_DRUGGEDFOX, NO_REGION_DRUGGEDFOX)
        assertNotEquals(GEORGIA_DRUGGEDFOX, NORCAL_DRUGGEDFOX)

        assertEquals(MIKKUZ, MIKKUZ)
        assertNotEquals(MIKKUZ, GEORGIA_DRUGGEDFOX)
        assertNotEquals(MIKKUZ, NO_REGION_DRUGGEDFOX)

        assertEquals(GAR, GAR)
        assertNotEquals(GAR, HMW)
        assertNotEquals(GAR, YCZ6)

        assertEquals(HMW, HMW)
        assertEquals(SPARK, SPARK)
        assertEquals(NO_REGION_DRUGGEDFOX, NO_REGION_DRUGGEDFOX)
        assertNotEquals(HMW, SPARK)
        assertNotEquals(HMW, NO_REGION_DRUGGEDFOX)
        assertNotEquals(SPARK, NO_REGION_DRUGGEDFOX)

        assertEquals(YCZ6, YCZ6)
        assertEquals(HAX, HAX)
        assertNotEquals(YCZ6, HAX)
    }

    @Test
    fun testHashCode() {
        assertEquals(Objects.hash(MIKKUZ.id, NORCAL.endpoint), MIKKUZ.hashCode())
        assertEquals(Objects.hash(GEORGIA_DRUGGEDFOX.id, GEORGIA.endpoint), GEORGIA_DRUGGEDFOX.hashCode())
        assertEquals(Objects.hash(NORCAL_DRUGGEDFOX.id, NORCAL.endpoint), NORCAL_DRUGGEDFOX.hashCode())
        assertEquals(GAR.id.hashCode(), GAR.hashCode())
        assertEquals(HMW.id.hashCode(), HMW.hashCode())
        assertEquals(SPARK.id.hashCode(), SPARK.hashCode())
        assertEquals(YCZ6.id.hashCode(), YCZ6.hashCode())
        assertEquals(HAX.id.hashCode(), HAX.hashCode())
    }

    @Test
    fun testKind() {
        assertEquals(AbsPlayer.Kind.FAVORITE, MIKKUZ.kind)
        assertEquals(AbsPlayer.Kind.FAVORITE, GEORGIA_DRUGGEDFOX.kind)
        assertEquals(AbsPlayer.Kind.FAVORITE, NORCAL_DRUGGEDFOX.kind)
        assertEquals(AbsPlayer.Kind.FULL, GAR.kind)
        assertEquals(AbsPlayer.Kind.LITE, HMW.kind)
        assertEquals(AbsPlayer.Kind.LITE, SPARK.kind)
        assertEquals(AbsPlayer.Kind.RANKED, YCZ6.kind)
        assertEquals(AbsPlayer.Kind.RANKED, HAX.kind)
    }

}
