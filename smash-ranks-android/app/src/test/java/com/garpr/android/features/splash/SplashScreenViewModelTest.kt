package com.garpr.android.features.splash

import com.garpr.android.BaseTest
import com.garpr.android.preferences.GeneralPreferenceStore
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.inject
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SplashScreenViewModelTest : BaseTest() {

    private lateinit var viewModel: SplashScreenViewModel

    protected val generalPreferenceStore: GeneralPreferenceStore by inject()

    @Before
    override fun setUp() {
        super.setUp()

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
