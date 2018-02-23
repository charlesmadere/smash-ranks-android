package com.garpr.android.misc

import android.app.Application

class TestDeviceUtilsImpl(
        application: Application
) : DeviceUtilsImpl(application) {

    private var _hasLowRam: Boolean = false
    private var _hasNetworkConnection: Boolean = true


    override val hasLowRam: Boolean
        get() = _hasLowRam

    override val hasNetworkConnection: Boolean
        get() = _hasNetworkConnection

    fun setHasLowRam(hasLowRam: Boolean) {
        _hasLowRam = hasLowRam
    }

    fun setHasNetworkConnection(hasNetworkConnection: Boolean) {
        _hasNetworkConnection = hasNetworkConnection
    }

}
