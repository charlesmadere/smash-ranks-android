package com.garpr.android.misc

import android.content.Context
import androidx.core.app.ActivityManagerCompat
import com.garpr.android.extensions.activityManager
import com.garpr.android.extensions.connectivityManager

class DeviceUtilsImpl(
        private val context: Context
) : DeviceUtils {

    override val hasLowRam: Boolean
        get() = ActivityManagerCompat.isLowRamDevice(context.activityManager)

    override val hasNetworkConnection: Boolean
        get() = context.connectivityManager.activeNetworkInfo?.isConnected == true

}
