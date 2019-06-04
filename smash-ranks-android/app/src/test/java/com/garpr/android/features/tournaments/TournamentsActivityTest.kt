package com.garpr.android.features.tournaments

import android.app.Application
import com.garpr.android.BaseTest
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.Region
import com.garpr.android.features.common.activities.BaseActivity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
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
        private val GOOGLE_MTV = Region(
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
        val intent = TournamentsActivity.getLaunchIntent(application, GOOGLE_MTV)
        assertTrue(intent.hasExtra(BaseActivity.EXTRA_REGION))
        assertEquals(GOOGLE_MTV, intent.getParcelableExtra(BaseActivity.EXTRA_REGION))
    }

}