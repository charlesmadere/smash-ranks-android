package com.garpr.android

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import com.garpr.android.dagger.AppModule
import com.garpr.android.dagger.DaggerTestAppComponent
import com.garpr.android.dagger.TestAppComponent
import com.garpr.android.dagger.TestAppComponentHandle
import com.garpr.android.dagger.TestConfigModule
import com.garpr.android.misc.Constants

class TestApp : Application(), TestAppComponentHandle {

    private var _testAppComponent: TestAppComponent? = null


    override val testAppComponent: TestAppComponent
        get() = _testAppComponent ?: throw IllegalStateException("_testAppComponent is null")

    private fun initializeAppComponent() {
        val application = ApplicationProvider.getApplicationContext<Application>()
        _testAppComponent = DaggerTestAppComponent.builder()
                .appModule(AppModule(application, Constants.DEFAULT_REGION, Constants.SMASH_ROSTER_BASE_PATH))
                .testConfigModule(TestConfigModule())
                .build()
    }

    override fun onCreate() {
        super.onCreate()
        initializeAppComponent()
    }

}
