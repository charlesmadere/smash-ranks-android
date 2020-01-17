package com.garpr.android

import androidx.test.core.app.ApplicationProvider
import com.garpr.android.koin.configModule
import com.garpr.android.koin.databaseModule
import com.garpr.android.koin.managersModule
import com.garpr.android.koin.miscModule
import com.garpr.android.koin.networkingModule
import com.garpr.android.koin.preferencesModule
import com.garpr.android.koin.repositoriesModule
import com.garpr.android.koin.syncModule
import com.garpr.android.koin.viewModelsModule
import org.junit.Before
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.test.AutoCloseKoinTest

abstract class BaseAndroidTest : AutoCloseKoinTest() {

    private fun initializeKoin() {
        startKoin {
            androidContext(ApplicationProvider.getApplicationContext())
            modules(listOf(configModule, databaseModule, managersModule, miscModule,
                    networkingModule, preferencesModule, repositoriesModule, syncModule,
                    viewModelsModule))
        }
    }

    @Before
    open fun setUp() {
        initializeKoin()
    }

}
