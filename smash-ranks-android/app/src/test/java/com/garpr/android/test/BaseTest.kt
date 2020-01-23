package com.garpr.android.test

import com.garpr.android.koin.databaseModule
import com.garpr.android.koin.managersModule
import com.garpr.android.koin.miscModule
import com.garpr.android.koin.networkingModule
import com.garpr.android.koin.preferencesModule
import com.garpr.android.koin.repositoriesModule
import com.garpr.android.koin.syncModule
import com.garpr.android.koin.testConfigModule
import com.garpr.android.koin.viewModelsModule
import org.junit.Before
import org.koin.core.context.startKoin
import org.koin.test.AutoCloseKoinTest

abstract class BaseTest : AutoCloseKoinTest() {

    private fun initializeKoin() {
        startKoin {
            modules(listOf(testConfigModule, databaseModule, managersModule, miscModule,
                    networkingModule, preferencesModule, repositoriesModule, syncModule,
                    viewModelsModule))
        }
    }

    @Before
    open fun setUp() {
        initializeKoin()
    }

}
