package com.garpr.android.repositories

import com.garpr.android.BaseTest
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.Region
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.inject
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class RegionRepositoryTest : BaseTest() {

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

        private val NORCAL = Region(
                displayName = "Norcal",
                id = "norcal",
                endpoint = Endpoint.GAR_PR,
                rankingActivityDayLimit = 60,
                rankingNumTourneysAttended = 2,
                tournamentQualifiedDayLimit = 1000
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
    fun testInitialRegion() {
        assertEquals(NORCAL, regionRepository.getRegion())
    }

    @Test
    fun testObservable() {
        var value: Region? = null

        regionRepository.observable.subscribe {
            value = it
        }

        assertNull(value)

        regionRepository.setRegion(NYC)
        assertEquals(NYC, value)

        regionRepository.setRegion(GEORGIA)
        assertEquals(GEORGIA, value)
    }

    @Test
    fun testSetRegionAndGetRegion() {
        regionRepository.setRegion(ALABAMA)
        assertEquals(ALABAMA, regionRepository.getRegion())

        regionRepository.setRegion(NORCAL)
        assertEquals(NORCAL, regionRepository.getRegion())

        regionRepository.setRegion(GEORGIA)
        assertEquals(GEORGIA, regionRepository.getRegion())

        regionRepository.setRegion(NYC)
        assertEquals(NYC, regionRepository.getRegion())
    }

}
