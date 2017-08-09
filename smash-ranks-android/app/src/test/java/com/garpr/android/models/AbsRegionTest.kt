package com.garpr.android.models

import com.garpr.android.BaseTest
import com.google.gson.Gson
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class AbsRegionTest : BaseTest() {

    lateinit private var fullRegion1: AbsRegion
    lateinit private var fullRegion2: AbsRegion
    lateinit private var fullRegion3: AbsRegion
    lateinit private var liteRegion1: AbsRegion
    lateinit private var liteRegion2: AbsRegion
    lateinit private var liteRegion3: AbsRegion

    @Inject
    lateinit protected var gson: Gson


    companion object {
        private const val JSON_FULL_REGION_1 = "{\"ranking_num_tourneys_attended\":1,\"ranking_activity_day_limit\":60,\"display_name\":\"Google MTV\",\"id\":\"googlemtv\",\"tournament_qualified_day_limit\":999,\"endpoint\":\"gar_pr\"}"
        private const val JSON_FULL_REGION_2 = "{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":200,\"display_name\":\"Oregon\",\"id\":\"oregon\",\"tournament_qualified_day_limit\":999,\"endpoint\":\"not_gar_pr\"}"
        private const val JSON_FULL_REGION_3 = "{\"display_name\":\"Norcal\",\"id\":\"norcal\",\"tournament_qualified_day_limit\":1000,\"endpoint\":\"gar_pr\"}"
        private const val JSON_LITE_REGION_1 = "{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":45,\"display_name\":\"Norcal\",\"id\":\"norcal\",\"tournament_qualified_day_limit\":1000}"
        private const val JSON_LITE_REGION_2 = "{\"ranking_activity_day_limit\":90,\"display_name\":\"New Jersey\",\"id\":\"newjersey\",\"tournament_qualified_day_limit\":9999999}"
        private const val JSON_LITE_REGION_3 = "{\"ranking_num_tourneys_attended\":6,\"display_name\":\"Google MTV\",\"id\":\"googlemtv\",\"tournament_qualified_day_limit\":999}"
    }

    @Before
    @Throws(Exception::class)
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
    fun testFromJsonNull() {
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
