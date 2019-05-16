package com.garpr.android.networking

import com.garpr.android.data.models.AbsRegion
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.Region
import com.garpr.android.data.models.RegionsBundle
import com.garpr.android.misc.Heartbeat
import java.util.Collections

class RegionsBundleApiCall(
        private val listener: ApiListener<RegionsBundle>,
        private val serverApi: ServerApi
) : Heartbeat {

    private var garPrRegionsFound = false
    private var garPrRegions: RegionsBundle? = null
    private var notGarPrRegionsFound = false
    private var notGarPrRegions: RegionsBundle? = null


    fun fetch() {
        serverApi.getRegions(Endpoint.GAR_PR, garPrListener)
        serverApi.getRegions(Endpoint.NOT_GAR_PR, notGarPrListener)
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
            it.mapTo(regionsSet) { region -> Region(region, Endpoint.GAR_PR) }
        }

        notGarPrRegions?.regions?.let {
            it.mapTo(regionsSet) { region -> Region(region, Endpoint.NOT_GAR_PR) }
        }

        if (regionsSet.isEmpty()) {
            listener.failure()
            return
        }

        val regionsList = regionsSet.toList()
        Collections.sort(regionsList, AbsRegion.ALPHABETICAL_ORDER)
        listener.success(RegionsBundle(regionsList))
    }

    private val garPrListener = object : ApiListener<RegionsBundle> {
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

    private val notGarPrListener = object : ApiListener<RegionsBundle> {
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
