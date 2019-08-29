package com.garpr.android.features.splash

import com.garpr.android.BaseTest
import com.garpr.android.preferences.GeneralPreferenceStore
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class SplashScreenViewModelTest : BaseTest() {

    private lateinit var viewModel: SplashScreenViewModel

    @Inject
    protected lateinit var generalPreferenceStore: GeneralPreferenceStore


    @Before
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)

        viewModel = SplashScreenViewModel(generalPreferenceStore)
    }

    @Test
    fun testRealLaunch() {
        assertTrue(viewModel.showSplashScreen)

        viewModel.setSplashScreenComplete()
        assertFalse(viewModel.showSplashScreen)
    }

    @Test
    fun test2RealLaunches() {
        assertTrue(viewModel.showSplashScreen)

        viewModel.setSplashScreenComplete()
        assertFalse(viewModel.showSplashScreen)

        viewModel.setSplashScreenComplete()
        assertFalse(viewModel.showSplashScreen)
    }

    @Test
    fun test3RealLaunches() {
        assertTrue(viewModel.showSplashScreen)

        viewModel.setSplashScreenComplete()
        assertFalse(viewModel.showSplashScreen)

        viewModel.setSplashScreenComplete()
        assertFalse(viewModel.showSplashScreen)

        viewModel.setSplashScreenComplete()
        assertFalse(viewModel.showSplashScreen)
    }

    @Test
    fun testShowSplashScreenOnFirstLaunch() {
        assertTrue(viewModel.showSplashScreen)
    }

    @Test
    fun testSetSplashScreenComplete() {
        viewModel.setSplashScreenComplete()
        assertFalse(viewModel.showSplashScreen)
    }

}
