package com.garpr.android.misc

import android.app.Application
import android.os.Build
import androidx.annotation.IdRes
import androidx.core.app.ActivityManagerCompat
import com.garpr.android.R
import com.garpr.android.extensions.activityManager
import com.garpr.android.extensions.connectivityManager

open class DeviceUtilsImpl(
        private val application: Application
) : DeviceUtils {

    override val hasLowRam: Boolean
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    ActivityManagerCompat.isLowRamDevice(application.activityManager)
                } else {
                    true
                }

    override val hasNavigationBar: Boolean
        get() = if (isEmulator) {
                    true
                } else {
                    @IdRes val resId = application.resources.getIdentifier(
                            "config_showNavigationBar", "bool", "android")
                    resId != 0 && application.resources.getBoolean(resId)
                }

    override val hasNetworkConnection: Boolean
        get() = application.connectivityManager.activeNetworkInfo?.isConnected == true

    override val isEmulator: Boolean
        get() = Build.FINGERPRINT.startsWith("generic", true)
                || Build.FINGERPRINT.startsWith("unknown", true)
                || Build.MANUFACTURER.contains("Genymotion", true)
                || Build.MODEL.contains("google_sdk", true)
                || Build.MODEL.contains("Emulator", true)
                || Build.MODEL.contains("Android SDK built for x86", true)
                || Build.BRAND.startsWith("generic", true) && Build.DEVICE.startsWith("generic", true)
                || Build.PRODUCT.equals("google_sdk", true)

    override val navigationBarHeight: Int
        get() = if (hasNavigationBar) {
                    @IdRes var resId = application.resources.getIdentifier(
                            "navigation_bar_height", "dimen", "android")

                    if (resId == 0) {
                        resId = R.dimen.navigation_bar_height
                    }

                    application.resources.getDimensionPixelSize(resId)
                } else {
                    0
                }

    override val supportsTranslucentNavigationBar: Boolean
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    @IdRes val resId = application.resources.getIdentifier(
                            "config_enableTranslucentDecor", "bool", "android")
                    resId != 0 && application.resources.getBoolean(resId)
                } else {
                    false
                }

}
