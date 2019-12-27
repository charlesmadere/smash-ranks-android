package com.garpr.android

import android.annotation.SuppressLint
import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.work.Configuration
import com.garpr.android.koin.configModule
import com.garpr.android.koin.managersModule
import com.garpr.android.koin.miscModule
import com.garpr.android.koin.networkingModule
import com.garpr.android.koin.preferencesModule
import com.garpr.android.koin.repositoriesModule
import com.garpr.android.koin.syncModule
import com.garpr.android.koin.viewModelsModule
import com.garpr.android.managers.AppUpgradeManager
import com.garpr.android.misc.DeviceUtils
import com.garpr.android.misc.Timber
import com.garpr.android.repositories.NightModeRepository
import com.garpr.android.wrappers.CrashlyticsWrapper
import com.garpr.android.wrappers.ImageLibraryWrapper
import com.garpr.android.wrappers.WorkManagerWrapper
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application(), Configuration.Provider {

    protected val appUpgradeManager: AppUpgradeManager by inject()
    protected val crashlyticsWrapper: CrashlyticsWrapper by inject()
    protected val deviceUtils: DeviceUtils by inject()
    protected val imageLibraryWrapper: ImageLibraryWrapper by inject()
    protected val nightModeRepository: NightModeRepository by inject()
    protected val timber: Timber by inject()
    protected val workManagerWrapper: WorkManagerWrapper by inject()

    companion object {
        private const val TAG = "App"
    }

    @SuppressLint("CheckResult")
    private fun applyNightMode() {
        AppCompatDelegate.setDefaultNightMode(nightModeRepository.nightMode.themeValue)

        nightModeRepository.observable.subscribe { nightMode ->
            AppCompatDelegate.setDefaultNightMode(nightMode.themeValue)
        }
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return workManagerWrapper.configuration
    }

    private fun initializeCrashlytics() {
        crashlyticsWrapper.initialize(BuildConfig.DEBUG)
        crashlyticsWrapper.setBool("low_ram_device", deviceUtils.hasLowRam)
    }

    private fun initializeKoin() {
        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.DEBUG else Level.ERROR)
            androidContext(this@App)
            modules(listOf(configModule, managersModule, miscModule, networkingModule,
                    preferencesModule, repositoriesModule, syncModule, viewModelsModule))
        }
    }

    override fun onCreate() {
        super.onCreate()

        // The order of the following lines is important!
        initializeKoin()
        initializeCrashlytics()

        timber.d(TAG, "App created", null)

        applyNightMode()
        imageLibraryWrapper.initialize()
        appUpgradeManager.upgradeApp()
    }

}
