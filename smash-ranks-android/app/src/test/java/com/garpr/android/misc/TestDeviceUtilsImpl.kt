package com.garpr.android.misc

class TestDeviceUtilsImpl : DeviceUtils {

    var mHasLowRam: Boolean = false
    var mHasNetworkConnection: Boolean = true


    override fun hasLowRam() = mHasLowRam

    override fun hasNetworkConnection() = mHasNetworkConnection

}
