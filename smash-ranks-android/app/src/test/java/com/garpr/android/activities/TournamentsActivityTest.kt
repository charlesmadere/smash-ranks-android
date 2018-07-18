package com.garpr.android.activities

import android.app.Application
import com.garpr.android.BaseTest
import com.garpr.android.models.Endpoint
import com.garpr.android.models.Region
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class TournamentsActivityTest : BaseTest() {

    @Inject
    protected lateinit var application: Application


    companion object {
        private val GOOGLEMTV = Region(
                displayName = "Google MTV",
                id = "googlemtv",
                endpoint = Endpoint.GAR_PR
        )
    }

    @Before
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)
    }

    @Test
    fun testGetLaunchIntentWithNoRegion() {
        val intent = TournamentsActivity.getLaunchIntent(application)
        assertFalse(intent.hasExtra(BaseActivity.EXTRA_REGION))
        assertNull(intent.getParcelableExtra(BaseActivity.EXTRA_REGION))
    }

    @Test
    fun testGetLaunchIntentWithRegion() {
        val intent = TournamentsActivity.getLaunchIntent(application, GOOGLEMTV)
        assertTrue(intent.hasExtra(BaseActivity.EXTRA_REGION))
        assertEquals(GOOGLEMTV, intent.getParcelableExtra(BaseActivity.EXTRA_REGION))
    }

}
