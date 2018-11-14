package com.garpr.android.misc

import android.app.Application
import android.os.Build
import androidx.core.app.ActivityManagerCompat
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

}
