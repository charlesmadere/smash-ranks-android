package com.garpr.android

import org.junit.Before
import org.koin.test.AutoCloseKoinTest
import org.robolectric.annotation.Config

@Config(application = TestApp::class)
abstract class BaseTest : AutoCloseKoinTest() {

    @Before
    open fun setUp() {
        // intentionally empty, children can override
    }

}
