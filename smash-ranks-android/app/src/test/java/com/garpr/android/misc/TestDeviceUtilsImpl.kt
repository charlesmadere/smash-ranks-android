package com.garpr.android.misc

import android.app.Application

class TestDeviceUtilsImpl(
        application: Application
) : DeviceUtilsImpl(application) {

    private var _hasNetworkConnection: Boolean = true


    override val hasNetworkConnection: Boolean
        get() = _hasNetworkConnection

    fun setHasNetworkConnection(hasNetworkConnection: Boolean) {
        _hasNetworkConnection = hasNetworkConnection
    }

}
