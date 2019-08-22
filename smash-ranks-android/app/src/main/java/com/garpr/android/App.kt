package com.garpr.android

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.work.Configuration
import com.garpr.android.dagger.AppComponent
import com.garpr.android.dagger.AppComponentHandle
import com.garpr.android.dagger.AppModule
import com.garpr.android.dagger.ConfigModule
import com.garpr.android.dagger.DaggerAppComponent
import com.garpr.android.features.splash.AppUpgradeManager
import com.garpr.android.misc.Constants
import com.garpr.android.misc.DeviceUtils
import com.garpr.android.misc.Timber
import com.garpr.android.repositories.NightModeRepository
import com.garpr.android.wrappers.CrashlyticsWrapper
import com.garpr.android.wrappers.ImageLibraryWrapper
import com.garpr.android.wrappers.WorkManagerWrapper
import javax.inject.Inject

class App : Application(), AppComponentHandle, Configuration.Provider,
        NightModeRepository.OnNightModeChangeListener {

    private var _appComponent: AppComponent? = null

    @Inject
    protected lateinit var appUpgradeManager: AppUpgradeManager

    @Inject
    protected lateinit var crashlyticsWrapper: CrashlyticsWrapper

    @Inject
    protected lateinit var deviceUtils: DeviceUtils

    @Inject
    protected lateinit var imageLibraryWrapper: ImageLibraryWrapper

    @Inject
    protected lateinit var nightModeRepository: NightModeRepository

    @Inject
    protected lateinit var timber: Timber

    @Inject
    protected lateinit var workManagerWrapper: WorkManagerWrapper


    companion object {
        private const val TAG = "App"
    }

    override val appComponent: AppComponent
        get() = _appComponent ?: throw IllegalStateException("_appComponent is null")

    private fun applyNightMode() {
        AppCompatDelegate.setDefaultNightMode(nightModeRepository.nightMode.themeValue)
        nightModeRepository.addListener(this)
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return workManagerWrapper.configuration
    }

    private fun initializeAppComponent() {
        val appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this, Constants.DEFAULT_REGION, Constants.SMASH_ROSTER_BASE_PATH))
                .configModule(ConfigModule())
                .build()

        appComponent.inject(this)
        _appComponent = appComponent
    }

    private fun initializeCrashlytics() {
        crashlyticsWrapper.initialize(BuildConfig.DEBUG)
        crashlyticsWrapper.setBool("low_ram_device", deviceUtils.hasLowRam)
    }

    override fun onCreate() {
        super.onCreate()

        // The order of the following lines is important!

        initializeAppComponent()
        initializeCrashlytics()

        timber.d(TAG, "App created", null)

        applyNightMode()

        imageLibraryWrapper.initialize()
        appUpgradeManager.upgradeApp()
    }

    override fun onNightModeChange(nightModeRepository: NightModeRepository) {
        AppCompatDelegate.setDefaultNightMode(nightModeRepository.nightMode.themeValue)
    }

}
