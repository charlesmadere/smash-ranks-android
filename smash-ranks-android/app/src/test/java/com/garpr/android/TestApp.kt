package com.garpr.android

import android.app.Application
import com.garpr.android.koin.configModule
import com.garpr.android.koin.managersModule
import com.garpr.android.koin.miscModule
import com.garpr.android.koin.networkingModule
import com.garpr.android.koin.preferencesModule
import com.garpr.android.koin.repositoriesModule
import com.garpr.android.koin.syncModule
import com.garpr.android.koin.viewModelsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TestApp : Application() {

    private fun initializeKoin() {
        startKoin {
            androidContext(this@TestApp)
            modules(listOf(configModule, managersModule, miscModule, networkingModule,
                    preferencesModule, repositoriesModule, syncModule, viewModelsModule))
        }
    }

    override fun onCreate() {
        super.onCreate()

        initializeKoin()
    }

}
