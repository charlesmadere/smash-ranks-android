package com.garpr.android.test

import org.junit.Before
import org.koin.core.KoinComponent

abstract class BaseAndroidTest : KoinComponent {

    @Before
    open fun setUp() {

    }

}
