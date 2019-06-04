package com.garpr.android.repositories

import android.app.Application
import com.garpr.android.BaseTest
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.Region
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class RegionRepositoryTest : BaseTest() {

    @Inject
    protected lateinit var application: Application

    @Inject
    protected lateinit var regionRepository: RegionRepository


    companion object {
        private val ALABAMA = Region(
                displayName = "Alabama",
                id = "alabama",
                endpoint = Endpoint.NOT_GAR_PR,
                rankingActivityDayLimit = 60,
                rankingNumTourneysAttended = 2,
                tournamentQualifiedDayLimit = 999
        )

        private val GEORGIA = Region(
                displayName = "Georgia",
                id = "georgia",
                endpoint = Endpoint.NOT_GAR_PR,
                rankingActivityDayLimit = 90,
                rankingNumTourneysAttended = 6,
                tournamentQualifiedDayLimit = 180
        )

        private val NYC = Region(
                displayName = "NYC Metro Area",
                id = "nyc",
                endpoint = Endpoint.NOT_GAR_PR,
                rankingActivityDayLimit = 90,
                rankingNumTourneysAttended = 6,
                tournamentQualifiedDayLimit = 999
        )
    }

    @Before
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)
    }

    @Test
    fun testAddListener() {
        var region: Region? = null

        val listener = object : RegionRepository.OnRegionChangeListener {
            override fun onRegionChange(regionRepository: RegionRepository) {
                region = regionRepository.getRegion()
            }
        }

        regionRepository.addListener(listener)
        assertNull(region)

        regionRepository.setRegion(ALABAMA)
        assertEquals(ALABAMA, region)
    }

    @Test
    fun testAddListenerTwice() {
        var count = 0

        val listener = object : RegionRepository.OnRegionChangeListener {
            override fun onRegionChange(regionRepository: RegionRepository) {
                ++count
            }
        }

        regionRepository.addListener(listener)
        regionRepository.addListener(listener)
        regionRepository.setRegion(ALABAMA)
        assertEquals(1, count)
    }

    @Test
    fun testGetRegion() {
        assertNotNull(regionRepository.getRegion())
    }

    @Test
    fun testGetRegionWithContext() {
        assertNotNull(regionRepository.getRegion(application))
    }

    @Test
    fun testRemoveListener() {
        var region: Region? = null

        val listener = object : RegionRepository.OnRegionChangeListener {
            override fun onRegionChange(regionRepository: RegionRepository) {
                region = regionRepository.getRegion()
            }
        }

        regionRepository.addListener(listener)
        assertNull(region)

        regionRepository.setRegion(NYC)
        assertEquals(NYC, region)

        regionRepository.removeListener(listener)
        regionRepository.setRegion(GEORGIA)
        assertEquals(NYC, region)
    }

    @Test
    fun testSetRegion() {
        assertNotNull(regionRepository.getRegion())

        regionRepository.setRegion(GEORGIA)
        assertEquals(GEORGIA, regionRepository.getRegion())
    }

}