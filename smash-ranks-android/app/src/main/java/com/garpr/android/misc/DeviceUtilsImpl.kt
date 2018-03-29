package com.garpr.android.misc

import android.app.Application
import android.os.Build
import android.support.v4.app.ActivityManagerCompat
import com.garpr.android.extensions.activityManager
import com.garpr.android.extensions.connectivityManager

open class DeviceUtilsImpl(
        private val application: Application
) : DeviceUtils {

    override val hasLowRam: Boolean
        get() = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                    true
                } else {
                    ActivityManagerCompat.isLowRamDevice(application.activityManager)
                }

    override val hasNetworkConnection: Boolean
        get() = application.connectivityManager.activeNetworkInfo?.isConnected == true

    override val isEmulator: Boolean
        get() = Build.FINGERPRINT.startsWith("generic", true)
                || Build.FINGERPRINT.startsWith("unknown", true)
                || Build.MODEL.contains("google_sdk", true)
                || Build.MODEL.contains("Emulator", true)
                || Build.MODEL.contains("Android SDK built for x86", true)
                || Build.MANUFACTURER.contains("Genymotion", true)
                || Build.BRAND.startsWith("generic", true) && Build.DEVICE.startsWith("generic", true)
                || Build.PRODUCT.equals("google_sdk", true)

}
