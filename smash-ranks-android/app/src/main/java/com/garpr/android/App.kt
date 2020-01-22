package com.garpr.android

import com.garpr.android.koin.configModule
import com.garpr.android.koin.databaseModule
import com.garpr.android.koin.managersModule
import com.garpr.android.koin.miscModule
import com.garpr.android.koin.networkingModule
import com.garpr.android.koin.preferencesModule
import com.garpr.android.koin.repositoriesModule
import com.garpr.android.koin.syncModule
import com.garpr.android.koin.viewModelsModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : BaseApp() {

    override val tag: String = "App"

    override fun initializeKoin() {
        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.DEBUG else Level.ERROR)
            androidContext(this@App)
            modules(listOf(configModule, databaseModule, managersModule, miscModule,
                    networkingModule, preferencesModule, repositoriesModule, syncModule,
                    viewModelsModule))
        }
    }

}
