package com.garpr.android

import android.app.Application
import android.support.v7.app.AppCompatDelegate
import com.garpr.android.dagger.AppComponent
import com.garpr.android.dagger.AppModule
import com.garpr.android.dagger.DaggerAppComponent
import com.garpr.android.misc.Constants
import com.garpr.android.misc.CrashlyticsWrapper
import com.garpr.android.misc.DeviceUtils
import com.garpr.android.misc.FavoritePlayersManager
import com.garpr.android.misc.Timber
import com.garpr.android.models.NightMode
import com.garpr.android.preferences.GeneralPreferenceStore
import javax.inject.Inject

class App : Application() {

    lateinit var mAppComponent: AppComponent
        private set

    @Inject
    lateinit protected var mCrashlyticsWrapper: CrashlyticsWrapper

    @Inject
    lateinit protected var mDeviceUtils: DeviceUtils

    @Inject
    lateinit protected var mFavoritePlayersManager: FavoritePlayersManager

    @Inject
    lateinit protected var mGeneralPreferenceStore: GeneralPreferenceStore

    @Inject
    lateinit protected var mTimber: Timber


    companion object {
        private val TAG = "App"

        lateinit var sInstance: App
            private set
    }

    private fun applyNightMode() {
        val nightMode = mGeneralPreferenceStore.nightMode.get()
        AppCompatDelegate.setDefaultNightMode(nightMode?.themeValue ?: NightMode.SYSTEM.themeValue)
    }

    private fun initializeAppComponent() {
        mAppComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this, Constants.DEFAULT_REGION))
                .build()

        mAppComponent.inject(this)
    }

    private fun initializeCrashlytics() {
        mCrashlyticsWrapper.initialize(BuildConfig.DEBUG)
        mCrashlyticsWrapper.setBool("low_ram_device", mDeviceUtils.hasLowRam())
    }

    override fun onCreate() {
        super.onCreate()
        Instance = this

        initializeAppComponent()
        initializeCrashlytics()

        mTimber.d(TAG, "App created")

        applyNightMode()
        upgradeApp()
    }

    private fun upgradeApp() {
        val lastVersionPref = mGeneralPreferenceStore.lastVersion
        val lastVersion = lastVersionPref.get()

        if (lastVersion == null) {
            mTimber.d(TAG, "App has no previous version, is now " + BuildConfig.VERSION_CODE)
        } else if (lastVersion < BuildConfig.VERSION_CODE) {
            mTimber.d(TAG, "App's previous version was $lastVersion, is now " +
                    BuildConfig.VERSION_CODE)
        } else {
            mTimber.d(TAG, "App doesn't need to upgrade (on version $lastVersion)")
            return
        }

        lastVersionPref.set(BuildConfig.VERSION_CODE)

        if (lastVersion == null || lastVersion < 1011) {
            // this preference used to be a String but was changed to a Region
            mGeneralPreferenceStore.currentRegion.delete()

            // it used to be an AbsPlayer, is now a FavoritePlayer
            mGeneralPreferenceStore.identity.delete()

            // same as above :(
            mFavoritePlayersManager.clear()
        }
    }

}
