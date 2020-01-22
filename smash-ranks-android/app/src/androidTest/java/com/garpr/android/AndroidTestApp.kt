package com.garpr.android

import com.garpr.android.koin.androidTestConfigModule
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

class AndroidTestApp : BaseApp() {

    override fun initializeKoin() {
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@AndroidTestApp)
            modules(listOf(androidTestConfigModule, databaseModule, managersModule, miscModule,
                    networkingModule, preferencesModule, repositoriesModule, syncModule,
                    viewModelsModule))
        }
    }

}
