package com.garpr.android.misc

import android.app.Application
import android.os.Build
import android.support.v4.app.ActivityManagerCompat
import com.garpr.android.extensions.activityManager
import com.garpr.android.extensions.connectivityManager

class DeviceUtilsImpl(
        private val mApplication: Application
) : DeviceUtils {

    override fun hasLowRam(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return true
        } else {
            return ActivityManagerCompat.isLowRamDevice(mApplication.activityManager)
        }
    }

    override fun hasNetworkConnection(): Boolean {
        val networkInfo = mApplication.connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

}
