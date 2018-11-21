package com.garpr.android.misc

interface DeviceUtils {

    val hasLowRam: Boolean

    val hasNavigationBar: Boolean

    val hasNetworkConnection: Boolean

    val isEmulator: Boolean

    val navigationBarHeight: Int

    val supportsTranslucentNavigationBar: Boolean

}
