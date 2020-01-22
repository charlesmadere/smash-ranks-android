package com.garpr.android

import android.annotation.SuppressLint
import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.work.Configuration
import com.garpr.android.managers.AppUpgradeManager
import com.garpr.android.misc.DeviceUtils
import com.garpr.android.misc.Timber
import com.garpr.android.repositories.NightModeRepository
import com.garpr.android.wrappers.CrashlyticsWrapper
import com.garpr.android.wrappers.ImageLibraryWrapper
import com.garpr.android.wrappers.WorkManagerWrapper
import org.koin.android.ext.android.inject

abstract class BaseApp : Application(), Configuration.Provider {

    protected val appUpgradeManager: AppUpgradeManager by inject()
    protected val crashlyticsWrapper: CrashlyticsWrapper by inject()
    protected val deviceUtils: DeviceUtils by inject()
    protected val imageLibraryWrapper: ImageLibraryWrapper by inject()
    protected val nightModeRepository: NightModeRepository by inject()
    protected val timber: Timber by inject()
    protected val workManagerWrapper: WorkManagerWrapper by inject()

    override fun getWorkManagerConfiguration(): Configuration {
        return workManagerWrapper.configuration
    }

    private fun initializeCrashlytics() {
        crashlyticsWrapper.initialize(BuildConfig.DEBUG)
        crashlyticsWrapper.setBool("low_ram_device", deviceUtils.hasLowRam)
    }

    protected abstract fun initializeKoin()

    @SuppressLint("CheckResult")
    private fun initializeNightMode() {
        AppCompatDelegate.setDefaultNightMode(nightModeRepository.nightMode.themeValue)

        nightModeRepository.observable.subscribe { nightMode ->
            AppCompatDelegate.setDefaultNightMode(nightMode.themeValue)
        }
    }

    override fun onCreate() {
        super.onCreate()

        // The order of the following lines is important!
        initializeKoin()
        initializeCrashlytics()

        timber.d(TAG, "BaseApp created")

        initializeNightMode()
        imageLibraryWrapper.initialize()
        appUpgradeManager.upgradeApp()
    }

    companion object {
        private const val TAG = "BaseApp"
    }

}
