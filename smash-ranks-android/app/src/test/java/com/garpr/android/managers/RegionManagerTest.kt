package com.garpr.android.managers

import android.app.Application
import com.garpr.android.BaseTest
import com.garpr.android.data.models.Region
import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class RegionManagerTest : BaseTest() {

    private lateinit var alabama: Region
    private lateinit var georgia: Region
    private lateinit var nyc: Region

    @Inject
    protected lateinit var application: Application

    @Inject
    protected lateinit var gson: Gson

    @Inject
    protected lateinit var regionManager: RegionManager


    companion object {
        private const val JSON_REGION_ALABAMA = "{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":60,\"display_name\":\"Alabama\",\"id\":\"alabama\",\"tournament_qualified_day_limit\":999,\"endpoint\":\"not_gar_pr\"}"
        private const val JSON_REGION_GEORGIA = "{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":75,\"display_name\":\"Georgia\",\"id\":\"georgia\",\"tournament_qualified_day_limit\":180,\"endpoint\":\"not_gar_pr\"}"
        private const val JSON_REGION_NYC = "{\"ranking_num_tourneys_attended\":6,\"ranking_activity_day_limit\":90,\"display_name\":\"NYC Metro Area\",\"id\":\"nyc\",\"tournament_qualified_day_limit\":999,\"endpoint\":\"not_gar_pr\"}"
    }

    @Before
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)

        alabama = gson.fromJson(JSON_REGION_ALABAMA, Region::class.java)
        georgia = gson.fromJson(JSON_REGION_GEORGIA, Region::class.java)
        nyc = gson.fromJson(JSON_REGION_NYC, Region::class.java)
    }

    @Test
    @Throws(Exception::class)
    fun testAddListener() {
        var region: Region? = null

        val listener = object : RegionManager.OnRegionChangeListener {
            override fun onRegionChange(regionManager: RegionManager) {
                region = regionManager.getRegion()
            }
        }

        regionManager.addListener(listener)
        assertNull(region)

        regionManager.setRegion(alabama)
        assertEquals(alabama, region)
    }

    @Test
    @Throws(Exception::class)
    fun testAddListenerTwice() {
        var count = 0

        val listener = object : RegionManager.OnRegionChangeListener {
            override fun onRegionChange(regionManager: RegionManager) {
                ++count
            }
        }

        regionManager.addListener(listener)
        regionManager.addListener(listener)
        regionManager.setRegion(alabama)
        assertEquals(1, count)
    }

    @Test
    @Throws(Exception::class)
    fun testGetRegion() {
        assertNotNull(regionManager.getRegion())
    }

    @Test
    @Throws(Exception::class)
    fun testGetRegionWithContext() {
        assertNotNull(regionManager.getRegion(application))
    }

    @Test
    @Throws(Exception::class)
    fun testRemoveListener() {
        var region: Region? = null

        val listener = object : RegionManager.OnRegionChangeListener {
            override fun onRegionChange(regionManager: RegionManager) {
                region = regionManager.getRegion()
            }
        }

        regionManager.addListener(listener)
        assertNull(region)

        regionManager.setRegion(nyc)
        assertEquals(nyc, region)

        regionManager.removeListener(listener)
        regionManager.setRegion(georgia)
        assertEquals(nyc, region)
    }

    @Test
    @Throws(Exception::class)
    fun testSetRegion() {
        assertNotNull(regionManager.getRegion())

        regionManager.setRegion(georgia)
        assertEquals(georgia, regionManager.getRegion())
    }

}
