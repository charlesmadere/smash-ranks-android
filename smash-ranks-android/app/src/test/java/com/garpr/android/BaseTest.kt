package com.garpr.android

import org.junit.Before
import org.junit.runner.RunWith
import org.koin.test.AutoCloseKoinTest
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(application = TestApp::class)
@RunWith(RobolectricTestRunner::class)
abstract class BaseTest : AutoCloseKoinTest() {

    @Before
    open fun setUp() {
        // intentionally empty, children can override
    }

}
