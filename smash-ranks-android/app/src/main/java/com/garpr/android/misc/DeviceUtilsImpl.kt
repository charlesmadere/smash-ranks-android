package com.garpr.android.misc

import android.content.Context
import androidx.core.app.ActivityManagerCompat
import com.garpr.android.extensions.activityManager

class DeviceUtilsImpl(
        context: Context
) : DeviceUtils {

    override val hasLowRam: Boolean = ActivityManagerCompat.isLowRamDevice(context.activityManager)

}
