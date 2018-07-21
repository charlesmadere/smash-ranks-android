package com.garpr.android.misc

import android.app.Application
import android.content.res.Configuration
import android.os.Build
import android.support.v4.app.ActivityManagerCompat
import com.garpr.android.R
import com.garpr.android.extensions.activityManager
import com.garpr.android.extensions.connectivityManager

open class DeviceUtilsImpl(
        private val application: Application
) : DeviceUtils {

    override val hasLowRam: Boolean
        get() = ActivityManagerCompat.isLowRamDevice(application.activityManager)

    override val hasNavigationBar: Boolean
        get() {
            return if (isEmulator) {
                true
            } else {
                val resId = application.resources.getIdentifier("config_showNavigationBar",
                        "bool", "android")
                resId != 0 && application.resources.getBoolean(resId)
            }
        }

    override val hasNetworkConnection: Boolean
        get() = application.connectivityManager.activeNetworkInfo?.isConnected == true

    override val isDeviceLandscape: Boolean
        get() = application.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    override val isEmulator: Boolean
        get() = Build.FINGERPRINT.startsWith("generic", true)
                || Build.FINGERPRINT.startsWith("unknown", true)
                || Build.MODEL.contains("google_sdk", true)
                || Build.MODEL.contains("Emulator", true)
                || Build.MODEL.contains("Android SDK built for x86", true)
                || Build.MANUFACTURER.contains("Genymotion", true)
                || Build.BRAND.startsWith("generic", true) && Build.DEVICE.startsWith("generic", true)
                || Build.PRODUCT.equals("google_sdk", true)

    override val navigationBarHeight: Int
        get() {
            return if (hasNavigationBar) {
                var resId = application.resources.getIdentifier("navigation_bar_height",
                        "dimen", "android")

                if (resId == 0) {
                    resId = R.dimen.navigation_bar_height
                }

                application.resources.getDimensionPixelSize(resId)
            } else {
                0
            }
        }

    override val supportsTranslucentNavigationBar: Boolean
        get() {
            val resId = application.resources.getIdentifier("config_enableTranslucentDecor",
                    "bool", "android")
            return resId != 0 && application.resources.getBoolean(resId)
        }

}
