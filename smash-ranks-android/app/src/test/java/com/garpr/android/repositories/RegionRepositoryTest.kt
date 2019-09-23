package com.garpr.android.repositories

import android.content.Context
import com.garpr.android.BaseTest
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.Region
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.inject
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class RegionRepositoryTest : BaseTest() {

    protected val context: Context by inject()
    protected val regionRepository: RegionRepository by inject()

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
        assertNotNull(regionRepository.getRegion(context))
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
