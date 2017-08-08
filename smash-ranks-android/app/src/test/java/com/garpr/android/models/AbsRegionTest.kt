package com.garpr.android.models

import com.garpr.android.BaseTest
import com.garpr.android.BuildConfig
import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class AbsRegionTest : BaseTest() {

    @Inject
    lateinit protected var mGson: Gson

    lateinit private var fullRegion1: AbsRegion
    lateinit private var fullRegion2: AbsRegion
    lateinit private var liteRegion1: AbsRegion
    lateinit private var liteRegion2: AbsRegion


    companion object {
        private const val JSON_FULL_REGION_1 = "{\"ranking_num_tourneys_attended\":1,\"ranking_activity_day_limit\":60,\"display_name\":\"Google MTV\",\"id\":\"googlemtv\",\"tournament_qualified_day_limit\":999,\"endpoint\":\"gar_pr\"}"
        private const val JSON_FULL_REGION_2 = "{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":200,\"display_name\":\"Oregon\",\"id\":\"oregon\",\"tournament_qualified_day_limit\":999,\"endpoint\":\"not_gar_pr\"}"
        private const val JSON_LITE_REGION_1 = "{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":45,\"display_name\":\"Norcal\",\"id\":\"norcal\",\"tournament_qualified_day_limit\":1000}"
        private const val JSON_LITE_REGION_2 = "{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":90,\"display_name\":\"New Jersey\",\"id\":\"newjersey\",\"tournament_qualified_day_limit\":9999999}"
    }

    @Before
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)

        fullRegion1 = mGson.fromJson<AbsRegion>(JSON_FULL_REGION_1, AbsRegion::class.java)
        fullRegion2 = mGson.fromJson<AbsRegion>(JSON_FULL_REGION_2, AbsRegion::class.java)
        liteRegion1 = mGson.fromJson<AbsRegion>(JSON_LITE_REGION_1, AbsRegion::class.java)
        liteRegion2 = mGson.fromJson<AbsRegion>(JSON_LITE_REGION_2, AbsRegion::class.java)
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
        val region = mGson.fromJson<AbsRegion>(null as String?, AbsRegion::class.java)
        assertNull(region)
    }

}
