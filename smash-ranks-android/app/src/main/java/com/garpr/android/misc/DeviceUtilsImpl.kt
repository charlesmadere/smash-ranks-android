package com.garpr.android.misc

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.support.v4.app.ActivityManagerCompat

class DeviceUtilsImpl(
        private val mApplication: Application
) : DeviceUtils {

    override fun hasLowRam(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return true
        } else {
            val activityManager = mApplication.getSystemService(
                    Context.ACTIVITY_SERVICE) as ActivityManager
            return ActivityManagerCompat.isLowRamDevice(activityManager)
        }
    }

    override fun hasNetworkConnection(): Boolean {
        val connectivityManager = mApplication.getSystemService(
                Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

}
