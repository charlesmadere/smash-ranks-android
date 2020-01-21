package com.garpr.android.data.models

import com.garpr.android.test.BaseTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Collections

class AbsRegionTest : BaseTest() {

    companion object {
        private val LITE_REGION_1: AbsRegion = LiteRegion(
                rankingActivityDayLimit = 45,
                rankingNumTourneysAttended = 2,
                tournamentQualifiedDayLimit = 1000,
                displayName = "Norcal",
                id = "norcal"
        )

        private val LITE_REGION_2: AbsRegion = LiteRegion(
                rankingActivityDayLimit = 90,
                tournamentQualifiedDayLimit = 9999999,
                displayName = "New Jersey",
                id = "newjersey"
        )

        private val LITE_REGION_3: AbsRegion = LiteRegion(
                rankingNumTourneysAttended = 6,
                tournamentQualifiedDayLimit = 999,
                displayName = "Google MTV",
                id = "googlemtv"
        )

        private val REGION_1: AbsRegion = Region(
                rankingActivityDayLimit = 60,
                rankingNumTourneysAttended = 1,
                tournamentQualifiedDayLimit = 999,
                displayName = "Google MTV",
                id = "googlemtv",
                endpoint = Endpoint.GAR_PR
        )

        private val REGION_2: AbsRegion = Region(
                rankingActivityDayLimit = 200,
                rankingNumTourneysAttended = 2,
                tournamentQualifiedDayLimit = 999,
                displayName = "Oregon",
                id = "oregon",
                endpoint = Endpoint.NOT_GAR_PR
        )

        private val REGION_3: AbsRegion = Region(
                tournamentQualifiedDayLimit = 1000,
                displayName = "Norcal",
                id = "norcal",
                endpoint = Endpoint.GAR_PR
        )
    }

    @Test
    fun testComparatorAlphabeticalOrder() {
        val list = listOf(REGION_1, LITE_REGION_2, REGION_2, REGION_3)
        Collections.sort(list, AbsRegion.ALPHABETICAL_ORDER)

        assertEquals(REGION_1, list[0])
        assertEquals(LITE_REGION_2, list[1])
        assertEquals(REGION_3, list[2])
        assertEquals(REGION_2, list[3])
    }

    @Test
    fun testComparatorEndpointOrder() {
        val list = listOf(REGION_1, LITE_REGION_2, REGION_2, REGION_3, LITE_REGION_1,
                LITE_REGION_3)
        Collections.sort(list, AbsRegion.ENDPOINT_ORDER)

        assertEquals(REGION_1, list[0])
        assertEquals(REGION_3, list[1])
        assertEquals(REGION_2, list[2])
        assertEquals(LITE_REGION_3, list[3])
        assertEquals(LITE_REGION_2, list[4])
        assertEquals(LITE_REGION_1, list[5])
    }

    @Test
    fun testHasActivityRequirements() {
        assertTrue(LITE_REGION_1.hasActivityRequirements)
        assertFalse(LITE_REGION_2.hasActivityRequirements)
        assertFalse(LITE_REGION_3.hasActivityRequirements)
        assertTrue(REGION_1.hasActivityRequirements)
        assertTrue(REGION_2.hasActivityRequirements)
        assertFalse(REGION_3.hasActivityRequirements)
    }

    @Test
    fun testHashCode() {
        assertEquals(LITE_REGION_1.id.hashCode(), LITE_REGION_1.hashCode())
        assertEquals(LITE_REGION_2.id.hashCode(), LITE_REGION_2.hashCode())
        assertEquals(LITE_REGION_3.id.hashCode(), LITE_REGION_3.hashCode())
        assertNotEquals(REGION_1.id.hashCode(), REGION_1.hashCode())
        assertNotEquals(REGION_2.id.hashCode(), REGION_2.hashCode())
        assertNotEquals(REGION_3.id.hashCode(), REGION_3.hashCode())
    }

    @Test
    fun testKind() {
        assertEquals(AbsRegion.Kind.LITE, LITE_REGION_1.kind)
        assertEquals(AbsRegion.Kind.LITE, LITE_REGION_2.kind)
        assertEquals(AbsRegion.Kind.LITE, LITE_REGION_3.kind)
        assertEquals(AbsRegion.Kind.FULL, REGION_1.kind)
        assertEquals(AbsRegion.Kind.FULL, REGION_2.kind)
        assertEquals(AbsRegion.Kind.FULL, REGION_3.kind)
    }

}
