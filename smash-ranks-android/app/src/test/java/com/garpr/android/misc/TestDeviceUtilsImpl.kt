package com.garpr.android.misc

import android.app.Application

class TestDeviceUtilsImpl(
        application: Application
) : DeviceUtilsImpl(application) {

    private var _hasNetworkConnection: Boolean? = null


    override val hasNetworkConnection: Boolean
        get() = _hasNetworkConnection ?: super.hasNetworkConnection

    fun setHasNetworkConnection(hasNetworkConnection: Boolean) {
        _hasNetworkConnection = hasNetworkConnection
    }

}
