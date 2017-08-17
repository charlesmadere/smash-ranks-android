package com.garpr.android.misc

import android.app.Application
import com.garpr.android.BaseTest
import com.garpr.android.models.AbsRegion
import com.garpr.android.models.Region
import com.google.gson.Gson
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class RegionManagerTest : BaseTest() {

    lateinit private var mAlabama: Region
    lateinit private var mGeorgia: Region
    lateinit private var mNyc: Region

    @Inject
    lateinit protected var mApplication: Application

    @Inject
    lateinit protected var mGson: Gson

    @Inject
    lateinit protected var mRegionManager: RegionManager


    companion object {
        private const val JSON_REGION_ALABAMA = "{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":60,\"display_name\":\"Alabama\",\"id\":\"alabama\",\"tournament_qualified_day_limit\":999,\"endpoint\":\"not_gar_pr\"}"
        private const val JSON_REGION_GEORGIA = "{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":75,\"display_name\":\"Georgia\",\"id\":\"georgia\",\"tournament_qualified_day_limit\":180,\"endpoint\":\"not_gar_pr\"}"
        private const val JSON_REGION_NYC = "{\"ranking_num_tourneys_attended\":6,\"ranking_activity_day_limit\":90,\"display_name\":\"NYC Metro Area\",\"id\":\"nyc\",\"tournament_qualified_day_limit\":999,\"endpoint\":\"not_gar_pr\"}"
    }

    @Before
    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)

        mAlabama = mGson.fromJson(JSON_REGION_ALABAMA, Region::class.java)
        mGeorgia = mGson.fromJson(JSON_REGION_GEORGIA, Region::class.java)
        mNyc = mGson.fromJson(JSON_REGION_NYC, Region::class.java)
    }

    @Test
    @Throws(Exception::class)
    fun testAddListener() {
        val array = arrayOfNulls<AbsRegion>(1)

        val listener = object : RegionManager.OnRegionChangeListener {
            override fun onRegionChange(regionManager: RegionManager) {
                array[0] = regionManager.getRegion()
            }
        }

        mRegionManager.addListener(listener)
        assertNull(array[0])

        mRegionManager.setRegion(mAlabama)
        assertEquals(mAlabama, array[0])
    }

    @Test
    @Throws(Exception::class)
    fun testGetRegion() {
        assertNotNull(mRegionManager.getRegion())
    }

    @Test
    @Throws(Exception::class)
    fun testGetRegionWithContext() {
        assertNotNull(mRegionManager.getRegion(mApplication))
    }

    @Test
    @Throws(Exception::class)
    fun testRemoveListener() {
        val array = arrayOfNulls<Region>(1)

        val listener = object : RegionManager.OnRegionChangeListener {
            override fun onRegionChange(regionManager: RegionManager) {
                array[0] = regionManager.getRegion()
            }
        }

        mRegionManager.addListener(listener)
        assertNull(array[0])

        mRegionManager.setRegion(mNyc)
        assertEquals(mNyc, array[0])

        mRegionManager.removeListener(listener)
        mRegionManager.setRegion(mGeorgia)
        assertEquals(mNyc, array[0])
    }

    @Test
    @Throws(Exception::class)
    fun testSetRegion() {
        assertNotNull(mRegionManager.getRegion())

        mRegionManager.setRegion(mGeorgia)
        assertEquals(mGeorgia, mRegionManager.getRegion())
    }

}
