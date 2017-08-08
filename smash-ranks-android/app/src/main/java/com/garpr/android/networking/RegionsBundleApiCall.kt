package com.garpr.android.networking

import com.garpr.android.misc.Constants
import com.garpr.android.misc.Heartbeat
import com.garpr.android.models.AbsRegion
import com.garpr.android.models.Endpoint
import com.garpr.android.models.Region
import com.garpr.android.models.RegionsBundle
import java.util.*

class RegionsBundleApiCall(
        private val listener: ApiListener<RegionsBundle>,
        private val serverApi: ServerApi
) : Heartbeat {

    private var garPrRegionsFound = false
    private var garPrRegions: RegionsBundle? = null
    private var notGarPrRegionsFound = false
    private var notGarPrRegions: RegionsBundle? = null


    fun fetch() {
        serverApi.getRegions(Endpoint.GAR_PR, mGarPrListener)
        serverApi.getRegions(Endpoint.NOT_GAR_PR, mNotGarPrListener)
    }

    override val isAlive: Boolean
        get() = listener.isAlive

    @Synchronized
    private fun proceed() {
        if (!garPrRegionsFound || !notGarPrRegionsFound) {
            return
        }

        val regionsSet = mutableSetOf<AbsRegion>()

        garPrRegions?.regions?.let {
            it.mapTo(regionsSet) { Region(it, Endpoint.GAR_PR) }
        }

        notGarPrRegions?.regions?.let {
            it.mapTo(regionsSet) { Region(it, Endpoint.NOT_GAR_PR) }
        }

        if (regionsSet.isEmpty()) {
            listener.failure(Constants.ERROR_CODE_UNKNOWN)
            return
        }

        val regionsList = regionsSet.toList()
        Collections.sort(regionsList, AbsRegion.ALPHABETICAL_ORDER)
        listener.success(RegionsBundle(regionsList))
    }

    private val mGarPrListener = object : ApiListener<RegionsBundle> {
        override fun failure(errorCode: Int) {
            garPrRegions = null
            garPrRegionsFound = true
            proceed()
        }

        override val isAlive: Boolean
            get() = this@RegionsBundleApiCall.isAlive

        override fun success(`object`: RegionsBundle?) {
            garPrRegions = `object`
            garPrRegionsFound = true
            proceed()
        }
    }

    private val mNotGarPrListener = object : ApiListener<RegionsBundle> {
        override fun failure(errorCode: Int) {
            notGarPrRegions = null
            notGarPrRegionsFound = true
            proceed()
        }

        override val isAlive: Boolean
            get() = this@RegionsBundleApiCall.isAlive

        override fun success(`object`: RegionsBundle?) {
            notGarPrRegions = `object`
            notGarPrRegionsFound = true
            proceed()
        }
    }

}
