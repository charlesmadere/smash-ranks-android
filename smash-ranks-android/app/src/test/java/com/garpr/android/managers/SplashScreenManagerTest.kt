package com.garpr.android.managers

import com.garpr.android.BaseTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class SplashScreenManagerTest : BaseTest() {

    @Inject
    protected lateinit var splashScreenManager: SplashScreenManager


    @Before
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)
    }

    @Test
    fun testRealLaunch() {
        assertTrue(splashScreenManager.showSplashScreen)

        splashScreenManager.setSplashScreenComplete()
        assertFalse(splashScreenManager.showSplashScreen)
    }

    @Test
    fun test2RealLaunches() {
        assertTrue(splashScreenManager.showSplashScreen)

        splashScreenManager.setSplashScreenComplete()
        assertFalse(splashScreenManager.showSplashScreen)

        splashScreenManager.setSplashScreenComplete()
        assertFalse(splashScreenManager.showSplashScreen)
    }

    @Test
    fun test3RealLaunches() {
        assertTrue(splashScreenManager.showSplashScreen)

        splashScreenManager.setSplashScreenComplete()
        assertFalse(splashScreenManager.showSplashScreen)

        splashScreenManager.setSplashScreenComplete()
        assertFalse(splashScreenManager.showSplashScreen)

        splashScreenManager.setSplashScreenComplete()
        assertFalse(splashScreenManager.showSplashScreen)
    }

    @Test
    fun testShowSplashScreenOnFirstLaunch() {
        assertTrue(splashScreenManager.showSplashScreen)
    }

    @Test
    fun testSetSplashScreenComplete() {
        splashScreenManager.setSplashScreenComplete()
        assertFalse(splashScreenManager.showSplashScreen)
    }

}
