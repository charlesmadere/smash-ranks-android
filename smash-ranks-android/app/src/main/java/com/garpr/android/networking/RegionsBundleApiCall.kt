package com.garpr.android.networking

import com.garpr.android.misc.Heartbeat
import com.garpr.android.models.RegionsBundle

class RegionsBundleApiCall(
        private val listener: ApiListener<RegionsBundle>,
        private val serverApi: ServerApi
) : Heartbeat {

    fun fetch() {

    }

    override val isAlive: Boolean
        get() = listener.isAlive

    private fun mergeRegionsStructs() {

    }

    @Synchronized
    private fun proceed() {

    }

}
