package com.garpr.android.features.tournaments

import android.content.Context
import com.garpr.android.BaseTest
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.Region
import com.garpr.android.features.common.activities.BaseActivity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.inject
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TournamentsActivityTest : BaseTest() {

    protected val context: Context by inject()

    companion object {
        private val GOOGLE_MTV = Region(
                displayName = "Google MTV",
                id = "googlemtv",
                endpoint = Endpoint.GAR_PR
        )
    }

    @Test
    fun testGetLaunchIntentWithNoRegion() {
        val intent = TournamentsActivity.getLaunchIntent(
                context = context
        )
        assertFalse(intent.hasExtra(BaseActivity.EXTRA_REGION))
        assertNull(intent.getParcelableExtra(BaseActivity.EXTRA_REGION))
    }

    @Test
    fun testGetLaunchIntentWithRegion() {
        val intent = TournamentsActivity.getLaunchIntent(
                context = context,
                region = GOOGLE_MTV
        )
        assertTrue(intent.hasExtra(BaseActivity.EXTRA_REGION))
        assertEquals(GOOGLE_MTV, intent.getParcelableExtra(BaseActivity.EXTRA_REGION))
    }

}
