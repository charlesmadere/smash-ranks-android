package com.garpr.android.misc

import android.app.Application
import com.garpr.android.BaseTest
import com.garpr.android.BuildConfig
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class PlayerToolbarManagerTest : BaseTest() {

    @Inject
    lateinit protected var mApplication: Application

    @Inject
    lateinit protected var mPlayerToolbarManager: PlayerToolbarManager


    companion object {
        // TODO
    }

    @Before
    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)
    }

    @Test
    @Throws(Exception::class)
    fun testGetPresentationWithNull() {
        val presentation = mPlayerToolbarManager.getPresentation(mApplication.resources,
                null, null, null)
        assertFalse(presentation.mIsAddToFavoritesVisible)
        assertFalse(presentation.mIsAliasesVisible)
        assertFalse(presentation.mIsFilterVisible)
        assertFalse(presentation.mIsFilterAllVisible)
        assertFalse(presentation.mIsFilterLossesVisible)
        assertFalse(presentation.mIsFilterWinsVisible)
        assertFalse(presentation.mIsRemoveFromFavoritesVisible)
        assertFalse(presentation.mIsSetAsYourIdentityVisible)
        assertFalse(presentation.mIsShareVisible)
        assertFalse(presentation.mIsViewYourselfVsThisOpponentVisible)
        assertNull(presentation.mSetAsYourIdentityTitle)
        assertNull(presentation.mViewYourselfVsThisOpponentTitle)
    }

}
