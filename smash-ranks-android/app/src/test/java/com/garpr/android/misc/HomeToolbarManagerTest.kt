package com.garpr.android.misc

import android.app.Application
import com.garpr.android.BaseTest
import com.garpr.android.models.AbsPlayer
import com.garpr.android.models.Region
import com.google.gson.Gson
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class HomeToolbarManagerTest : BaseTest() {

    lateinit private var hax: AbsPlayer
    lateinit private var imyt: AbsPlayer
    lateinit private var norcal: Region
    lateinit private var nyc: Region

    @Inject
    lateinit protected var application: Application

    @Inject
    lateinit protected var homeToolbarManager: HomeToolbarManager

    @Inject
    lateinit protected var gson: Gson

    @Inject
    lateinit protected var identityManager: IdentityManager

    @Inject
    lateinit protected var regionManager: RegionManager


    companion object {
        private const val JSON_PLAYER_HAX = "{\"id\":\"53c64dba8ab65f6e6651f7bc\",\"name\":\"Hax\",\"rank\":2,\"rating\":37.975921649503086}"
        private const val JSON_PLAYER_IMYT = "{\"ratings\":{\"norcal\":{\"mu\":37.52665547291405,\"sigma\":0.9061366338189941}},\"name\":\"Imyt\",\"regions\":[\"norcal\"],\"merge_children\":[\"5877eb55d2994e15c7dea98b\"],\"id\":\"5877eb55d2994e15c7dea98b\",\"merged\":false,\"merge_parent\":null}"
        private const val JSON_REGION_NORCAL = "{\"ranking_num_tourneys_attended\":2,\"ranking_activity_day_limit\":45,\"display_name\":\"Norcal\",\"id\":\"norcal\",\"tournament_qualified_day_limit\":1000,\"endpoint\":\"gar_pr\"}"
        private const val JSON_REGION_NYC = "{\"display_name\":\"NYC Metro Area\",\"id\":\"nyc\",\"tournament_qualified_day_limit\":90,\"endpoint\":\"not_gar_pr\"}"
    }

    @Before
    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)

        hax = gson.fromJson(JSON_PLAYER_HAX, AbsPlayer::class.java)
        imyt = gson.fromJson(JSON_PLAYER_IMYT, AbsPlayer::class.java)
        norcal = gson.fromJson(JSON_REGION_NORCAL, Region::class.java)
        nyc = gson.fromJson(JSON_REGION_NYC, Region::class.java)
    }

    @Test
    @Throws(Exception::class)
    fun testGetPresentationWithNorcalWithIdentity() {
        identityManager.setIdentity(imyt, norcal)
        regionManager.setRegion(norcal)

        val presentation = homeToolbarManager.getPresentation(application)
        assertTrue(presentation.mIsActivityRequirementsVisible)
        assertTrue(presentation.mIsViewYourselfVisible)
    }

    @Test
    @Throws(Exception::class)
    fun testGetPresentationWithNorcalWithoutIdentity() {
        identityManager.removeIdentity()
        regionManager.setRegion(norcal)

        val presentation = homeToolbarManager.getPresentation(application)
        assertTrue(presentation.mIsActivityRequirementsVisible)
        assertFalse(presentation.mIsViewYourselfVisible)
    }

    @Test
    @Throws(Exception::class)
    fun testGetPresentationWithNycWithHax() {
        identityManager.setIdentity(hax, nyc)
        regionManager.setRegion(nyc)

        val presentation = homeToolbarManager.getPresentation(application)
        assertFalse(presentation.mIsActivityRequirementsVisible)
        assertTrue(presentation.mIsViewYourselfVisible)
    }

    @Test
    @Throws(Exception::class)
    fun testGetPresentationWithNycWithoutIdentity() {
        identityManager.removeIdentity()
        regionManager.setRegion(nyc)

        val presentation = homeToolbarManager.getPresentation(application)
        assertFalse(presentation.mIsActivityRequirementsVisible)
        assertFalse(presentation.mIsViewYourselfVisible)
    }

}
