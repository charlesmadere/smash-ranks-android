package com.garpr.android.misc

import android.app.Application
import androidx.core.app.ActivityManagerCompat
import com.garpr.android.extensions.activityManager
import com.garpr.android.extensions.connectivityManager

class DeviceUtilsImpl(
        private val application: Application
) : DeviceUtils {

    override val hasLowRam: Boolean
        get() = ActivityManagerCompat.isLowRamDevice(application.activityManager)

    override val hasNetworkConnection: Boolean
        get() = application.connectivityManager.activeNetworkInfo?.isConnected == true

}
