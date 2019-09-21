package com.garpr.android.data.converters

import com.garpr.android.BaseTest
import com.garpr.android.data.models.AbsRegion
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.LiteRegion
import com.garpr.android.data.models.Region
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.inject
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class AbsRegionConverterTest : BaseTest() {

    protected val moshi: Moshi by inject()

    private lateinit var regionAdapter: JsonAdapter<AbsRegion>

    companion object {
        private const val JSON_LITE_REGION = "{\"ranking_activity_day_limit\":90,\"display_name\":\"New Jersey\",\"id\":\"newjersey\",\"tournament_qualified_day_limit\":9999999}"
        private const val JSON_REGION = "{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":200,\"display_name\":\"Oregon\",\"id\":\"oregon\",\"tournament_qualified_day_limit\":999,\"endpoint\":\"not_gar_pr\"}"

        private val LITE_REGION = LiteRegion(
                displayName = "Norcal",
                id = "norcal"
        )

        private val REGION = Region(
                displayName = "Google MTV",
                id = "googlemtv",
                endpoint = Endpoint.GAR_PR
        )
    }

    @Before
    override fun setUp() {
        super.setUp()

        regionAdapter = moshi.adapter(AbsRegion::class.java)
    }

    @Test
    fun testFromJsonWithLiteRegion() {
        val region = regionAdapter.fromJson(JSON_LITE_REGION)
        assertTrue(region is LiteRegion)
        assertEquals(AbsRegion.Kind.LITE, region?.kind)
    }

    @Test
    fun testFromJsonWithRegion() {
        val region = regionAdapter.fromJson(JSON_REGION)
        assertTrue(region is Region)
        assertEquals(AbsRegion.Kind.FULL, region?.kind)
    }

    @Test
    fun testToJsonWithLiteRegion() {
        val json = regionAdapter.toJson(LITE_REGION)
        assertFalse(json.isNullOrBlank())

        val region = regionAdapter.fromJson(json)
        assertEquals(LITE_REGION, region)
    }

    @Test
    fun testToJsonWithNull() {
        val json = regionAdapter.toJson(null)
        assertTrue(json.isNullOrEmpty())
    }

    @Test
    fun testToJsonWithRegion() {
        val json = regionAdapter.toJson(REGION)
        assertFalse(json.isNullOrBlank())

        val region = regionAdapter.fromJson(json)
        assertEquals(REGION, region)
    }

}
