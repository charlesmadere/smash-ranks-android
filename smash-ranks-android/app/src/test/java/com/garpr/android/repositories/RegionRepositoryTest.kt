package com.garpr.android.repositories

import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.Region
import com.garpr.android.test.BaseTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.koin.test.inject

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
        assertEquals(NORCAL, regionRepository.region)
    }

    @Test
    fun testObservable() {
        var value: Region? = null

        regionRepository.observable.subscribe {
            value = it
        }

        assertNull(value)

        regionRepository.region = NYC
        assertEquals(NYC, value)

        regionRepository.region = GEORGIA
        assertEquals(GEORGIA, value)
    }

    @Test
    fun testSetRegionAndGetRegion() {
        regionRepository.region = ALABAMA
        assertEquals(ALABAMA, regionRepository.region)

        regionRepository.region = NORCAL
        assertEquals(NORCAL, regionRepository.region)

        regionRepository.region = GEORGIA
        assertEquals(GEORGIA, regionRepository.region)

        regionRepository.region = NYC
        assertEquals(NYC, regionRepository.region)
    }

}
