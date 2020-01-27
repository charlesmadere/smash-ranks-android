package com.garpr.android.data.database

import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.Region
import com.garpr.android.test.BaseTest
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.koin.test.inject

class RegionConverterTest : BaseTest() {

    private lateinit var jsonAdapter: JsonAdapter<Region>
    private val converter = RegionConverter()

    protected val moshi: Moshi by inject()

    @Before
    override fun setUp() {
        super.setUp()

        jsonAdapter = moshi.adapter(Region::class.java)
    }

    @Test
    fun testRegionFromStringWithEmptyString() {
        assertNull(converter.regionFromString(""))
    }

    @Test
    fun testRegionFromStringWithNorcal() {
        val region = converter.regionFromString(NORCAL_JSON)
        assertNotNull(region)
        assertEquals(NORCAL, region)
    }

    @Test
    fun testRegionFromStringWithNull() {
        assertNull(converter.regionFromString(null))
    }

    @Test
    fun testRegionFromStringWithNyc() {
        val region = converter.regionFromString(NYC_JSON)
        assertNotNull(region)
        assertEquals(NYC, region)
    }

    @Test
    fun testStringFromRegionWithNorcal() {
        val string = converter.stringFromRegion(NORCAL)
        assertFalse(string.isNullOrBlank())

        val region = jsonAdapter.fromJson(string!!)
        assertEquals(NORCAL, region)
    }

    @Test
    fun testStringFromRegionWithNull() {
        assertNull(converter.stringFromRegion(null))
    }

    @Test
    fun testStringFromRegionWithNyc() {
        val string = converter.stringFromRegion(NYC)
        assertFalse(string.isNullOrBlank())

        val region = jsonAdapter.fromJson(string!!)
        assertEquals(NYC, region)
    }

    companion object {
        private val NORCAL = Region(
                displayName = "Norcal",
                id = "norcal",
                endpoint = Endpoint.GAR_PR
        )

        private val NYC = Region(
                displayName = "New York City",
                id = "nyc",
                endpoint = Endpoint.NOT_GAR_PR
        )

        private val NORCAL_JSON = "{\"display_name\":\"${NORCAL.displayName}\",\"id\":\"${NORCAL.id}\",\"endpoint\":\"gar_pr\"}"
        private val NYC_JSON = "{\"display_name\":\"${NYC.displayName}\",\"id\":\"${NYC.id}\",\"endpoint\":\"not_gar_pr\"}"
    }

}
