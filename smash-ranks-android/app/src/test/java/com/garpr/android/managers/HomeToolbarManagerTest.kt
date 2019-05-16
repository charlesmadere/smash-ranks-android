package com.garpr.android.managers

import com.garpr.android.BaseTest
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.FullPlayer
import com.garpr.android.data.models.LiteRegion
import com.garpr.android.data.models.RankedPlayer
import com.garpr.android.data.models.Rating
import com.garpr.android.data.models.Region
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class HomeToolbarManagerTest : BaseTest() {

    @Inject
    protected lateinit var homeToolbarManager: HomeToolbarManager

    @Inject
    protected lateinit var identityManager: IdentityManager


    companion object {
        private val HAX: AbsPlayer = RankedPlayer(
                id = "53c64dba8ab65f6e6651f7bc",
                name = "Hax",
                rank = 2,
                rating = 37.975921649503086f
        )

        private val IMYT: AbsPlayer = FullPlayer(
                id = "5877eb55d2994e15c7dea98b",
                name = "Imyt",
                ratings = mapOf(
                        "norcal" to Rating(37.52665547291405f, 0.9061366338189941f)
                ),
                regions = listOf("norcal")
        )

        private val GOOGLE_MTV = LiteRegion(
                activeTf = null,
                rankingActivityDayLimit = null,
                rankingNumTourneysAttended = 1,
                tournamentQualifiedDayLimit = 60,
                displayName = "Google MTV",
                id = "googlemtv"
        )

        private val NORCAL = Region(
                displayName = "Norcal",
                id = "norcal",
                endpoint = Endpoint.GAR_PR
        )

        private val NYC = Region(
                activeTf = null,
                rankingActivityDayLimit = 60,
                rankingNumTourneysAttended = 2,
                tournamentQualifiedDayLimit = 90,
                displayName = "New York City",
                id = "nyc",
                endpoint = Endpoint.NOT_GAR_PR
        )
    }

    @Before
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)
    }

    @Test
    fun testGetPresentationWithGoogleMtv() {
        identityManager.removeIdentity()

        val presentation = homeToolbarManager.getPresentation(GOOGLE_MTV)
        assertFalse(presentation.isActivityRequirementsVisible)
        assertFalse(presentation.isViewYourselfVisible)
    }

    @Test
    fun testGetPresentationWithNorcalWithIdentityImyt() {
        identityManager.setIdentity(IMYT, NORCAL)

        val presentation = homeToolbarManager.getPresentation(NORCAL)
        assertFalse(presentation.isActivityRequirementsVisible)
        assertTrue(presentation.isViewYourselfVisible)
    }

    @Test
    fun testGetPresentationWithNorcalWithoutIdentity() {
        identityManager.removeIdentity()

        val presentation = homeToolbarManager.getPresentation(NORCAL)
        assertFalse(presentation.isActivityRequirementsVisible)
        assertFalse(presentation.isViewYourselfVisible)
    }

    @Test
    fun testGetPresentationWithNycWithIdentityHax() {
        identityManager.setIdentity(HAX, NYC)

        val presentation = homeToolbarManager.getPresentation(NYC)
        assertTrue(presentation.isActivityRequirementsVisible)
        assertTrue(presentation.isViewYourselfVisible)
    }

    @Test
    fun testGetPresentationWithNycWithoutIdentity() {
        identityManager.removeIdentity()

        val presentation = homeToolbarManager.getPresentation(NYC)
        assertTrue(presentation.isActivityRequirementsVisible)
        assertFalse(presentation.isViewYourselfVisible)
    }

}
