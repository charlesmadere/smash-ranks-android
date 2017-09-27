package com.garpr.android.misc

import android.app.Application
import android.os.Build
import android.support.v4.app.ActivityManagerCompat
import com.garpr.android.extensions.activityManager
import com.garpr.android.extensions.connectivityManager

class DeviceUtilsImpl(
        private val application: Application
) : DeviceUtils {

    override fun hasLowRam() =
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            true
        } else {
            ActivityManagerCompat.isLowRamDevice(application.activityManager)
        }

    override fun hasNetworkConnection() =
        application.connectivityManager.activeNetworkInfo?.isConnected == true

}
