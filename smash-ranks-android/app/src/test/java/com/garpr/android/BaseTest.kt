package com.garpr.android

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import com.garpr.android.dagger.TestAppComponent
import com.garpr.android.dagger.TestAppComponentHandle
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(application = TestApp::class)
@RunWith(RobolectricTestRunner::class)
abstract class BaseTest : TestAppComponentHandle {

    override val testAppComponent: TestAppComponent
        get() {
            val application = ApplicationProvider.getApplicationContext<Application>()
            return (application as TestAppComponentHandle).testAppComponent
        }

    @Before
    open fun setUp() {
        // intentionally empty
    }

}
