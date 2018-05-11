package com.garpr.android.models

import com.garpr.android.BaseTest
import com.google.gson.Gson
import com.google.gson.JsonElement
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.*
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class AbsRegionTest : BaseTest() {

    private lateinit var fullRegion1: AbsRegion
    private lateinit var fullRegion2: AbsRegion
    private lateinit var fullRegion3: AbsRegion
    private lateinit var liteRegion1: AbsRegion
    private lateinit var liteRegion2: AbsRegion
    private lateinit var liteRegion3: AbsRegion

    @Inject
    protected lateinit var gson: Gson


    companion object {
        private const val JSON_FULL_REGION_1 = "{\"ranking_num_tourneys_attended\":1,\"ranking_activity_day_limit\":60,\"display_name\":\"Google MTV\",\"id\":\"googlemtv\",\"tournament_qualified_day_limit\":999,\"endpoint\":\"gar_pr\"}"
        private const val JSON_FULL_REGION_2 = "{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":200,\"display_name\":\"Oregon\",\"id\":\"oregon\",\"tournament_qualified_day_limit\":999,\"endpoint\":\"not_gar_pr\"}"
        private const val JSON_FULL_REGION_3 = "{\"display_name\":\"Norcal\",\"id\":\"norcal\",\"tournament_qualified_day_limit\":1000,\"endpoint\":\"gar_pr\"}"
        private const val JSON_LITE_REGION_1 = "{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":45,\"display_name\":\"Norcal\",\"id\":\"norcal\",\"tournament_qualified_day_limit\":1000}"
        private const val JSON_LITE_REGION_2 = "{\"ranking_activity_day_limit\":90,\"display_name\":\"New Jersey\",\"id\":\"newjersey\",\"tournament_qualified_day_limit\":9999999}"
        private const val JSON_LITE_REGION_3 = "{\"ranking_num_tourneys_attended\":6,\"display_name\":\"Google MTV\",\"id\":\"googlemtv\",\"tournament_qualified_day_limit\":999}"
    }

    @Before
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)

        fullRegion1 = gson.fromJson(JSON_FULL_REGION_1, AbsRegion::class.java)
        fullRegion2 = gson.fromJson(JSON_FULL_REGION_2, AbsRegion::class.java)
        fullRegion3 = gson.fromJson(JSON_FULL_REGION_3, AbsRegion::class.java)
        liteRegion1 = gson.fromJson(JSON_LITE_REGION_1, AbsRegion::class.java)
        liteRegion2 = gson.fromJson(JSON_LITE_REGION_2, AbsRegion::class.java)
        liteRegion3 = gson.fromJson(JSON_LITE_REGION_3, AbsRegion::class.java)
    }

    @Test
    @Throws(Exception::class)
    fun testComparatorAlphabeticalOrder() {
        val list = listOf(fullRegion1, liteRegion2, fullRegion2, fullRegion3)
        Collections.sort(list, AbsRegion.ALPHABETICAL_ORDER)

        assertEquals(fullRegion1, list[0])
        assertEquals(liteRegion2, list[1])
        assertEquals(fullRegion3, list[2])
        assertEquals(fullRegion2, list[3])
    }

    @Test
    @Throws(Exception::class)
    fun testComparatorEndpointOrder() {
        val list = listOf(fullRegion1, liteRegion2, fullRegion2, fullRegion3, liteRegion1,
                liteRegion3)
        Collections.sort(list, AbsRegion.ENDPOINT_ORDER)

        assertEquals(fullRegion1, list[0])
        assertEquals(fullRegion3, list[1])
        assertEquals(fullRegion2, list[2])
        assertEquals(liteRegion3, list[3])
        assertEquals(liteRegion2, list[4])
        assertEquals(liteRegion1, list[5])
    }

    @Test
    @Throws(Exception::class)
    fun testFromJsonFullRegion1() {
        assertEquals(AbsRegion.Kind.FULL, fullRegion1.kind)
    }

    @Test
    @Throws(Exception::class)
    fun testFromJsonFullRegion2() {
        assertEquals(AbsRegion.Kind.FULL, fullRegion2.kind)
    }

    @Test
    @Throws(Exception::class)
    fun testFromJsonFullRegion3() {
        assertEquals(AbsRegion.Kind.FULL, fullRegion3.kind)
    }

    @Test
    @Throws(Exception::class)
    fun testFromJsonLiteRegion1() {
        assertEquals(AbsRegion.Kind.LITE, liteRegion1.kind)
    }

    @Test
    @Throws(Exception::class)
    fun testFromJsonLiteRegion2() {
        assertEquals(AbsRegion.Kind.LITE, liteRegion2.kind)
    }

    @Test
    @Throws(Exception::class)
    fun testFromJsonLiteRegion3() {
        assertEquals(AbsRegion.Kind.LITE, liteRegion3.kind)
    }

    @Test
    @Throws(Exception::class)
    fun testFromJsonNullJsonElement() {
        val region = gson.fromJson(null as JsonElement?, AbsRegion::class.java)
        assertNull(region)
    }

    @Test
    @Throws(Exception::class)
    fun testFromJsonNullString() {
        val region = gson.fromJson(null as String?, AbsRegion::class.java)
        assertNull(region)
    }

    @Test
    @Throws(Exception::class)
    fun testHasActivityRequirements() {
        assertTrue(fullRegion1.hasActivityRequirements)
        assertTrue(fullRegion2.hasActivityRequirements)
        assertFalse(fullRegion3.hasActivityRequirements)
        assertTrue(liteRegion1.hasActivityRequirements)
        assertFalse(liteRegion2.hasActivityRequirements)
        assertFalse(liteRegion3.hasActivityRequirements)
    }

    @Test
    @Throws(Exception::class)
    fun testKind() {
        assertEquals(AbsRegion.Kind.FULL, fullRegion1.kind)
        assertEquals(AbsRegion.Kind.FULL, fullRegion2.kind)
        assertEquals(AbsRegion.Kind.FULL, fullRegion3.kind)
        assertEquals(AbsRegion.Kind.LITE, liteRegion1.kind)
        assertEquals(AbsRegion.Kind.LITE, liteRegion2.kind)
        assertEquals(AbsRegion.Kind.LITE, liteRegion3.kind)
    }

}
