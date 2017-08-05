package com.garpr.android.networking

import com.garpr.android.misc.Heartbeat
import com.garpr.android.models.Endpoint
import com.garpr.android.models.RegionsBundle

class RegionsBundleApiCall(
        private val listener: ApiListener<RegionsBundle>,
        private val serverApi: ServerApi
) : Heartbeat {

    private var mGarPrRegionsFound = false
    private var mGarPrRegions: RegionsBundle? = null
    private var mNotGarPrRegionsFound = false
    private var mNotGarPrRegions: RegionsBundle? = null


    fun fetch() {
        serverApi.getRegions(Endpoint.GAR_PR, mGarPrListener)
        serverApi.getRegions(Endpoint.NOT_GAR_PR, mNotGarPrListener)
    }

    override val isAlive: Boolean
        get() = listener.isAlive

    @Synchronized
    private fun proceed() {
        if (!mGarPrRegionsFound || !mNotGarPrRegionsFound) {
            return
        }

        // TODO
    }

    /*
        val endpoints = Endpoint.values()
        val array = BooleanArray(endpoints.size)
        val regionsBundle = RegionsBundle()

        for (i in endpoints.indices) {
            val index = i

            getRegions(endpoints[i], object : ApiListener<RegionsBundle> {
                override fun failure(errorCode: Int) {
                    array[index] = true
                    proceed()
                }

                override val isAlive: Boolean
                    get() = listener.isAlive

                @Synchronized private fun proceed() {
                    for (completed in array) {
                        if (!completed) {
                            return
                        }
                    }

                    if (isAlive) {
                        listener.success(regionsBundle)
                    }
                }

                override fun success(`object`: RegionsBundle?) {
                    regionsBundle.merge(`object`)
                    array[index] = true
                    proceed()
                }
            })
        }

    private fun getRegions(endpoint: Endpoint, listener: ApiListener<RegionsBundle>) {
        val url = endpoint.regionsApiPath

        mGarPrApi.getRegions(url).enqueue(object : Callback<RegionsBundle> {
            override fun onResponse(call: Call<RegionsBundle>, response: Response<RegionsBundle>) {
                val body = if (response.isSuccessful) response.body() else null

                if (body == null) {
                    mTimber.e(TAG, "getRegions failed (code " + response.code() + ")")
                    listener.failure(response.code())
                } else {
                    body.regions?.let {
                        for (region in it) {
                            region.endpoint = endpoint
                        }
                    }

                    listener.success(body)
                }
            }

            override fun onFailure(call: Call<RegionsBundle>, t: Throwable) {
                mTimber.e(TAG, "getRegions failed", t)
                listener.failure(Constants.ERROR_CODE_UNKNOWN)
            }
        })
    }
     */

    private val mGarPrListener = object : ApiListener<RegionsBundle> {
        override fun failure(errorCode: Int) {
            mGarPrRegions = null
            mGarPrRegionsFound = true
            proceed()
        }

        override val isAlive: Boolean
            get() = this@RegionsBundleApiCall.isAlive

        override fun success(`object`: RegionsBundle?) {
            mGarPrRegions = `object`
            mGarPrRegionsFound = true
            proceed()
        }
    }

    private val mNotGarPrListener = object : ApiListener<RegionsBundle> {
        override fun failure(errorCode: Int) {
            mNotGarPrRegions = null
            mNotGarPrRegionsFound = true
            proceed()
        }

        override val isAlive: Boolean
            get() = this@RegionsBundleApiCall.isAlive

        override fun success(`object`: RegionsBundle?) {
            mNotGarPrRegions = `object`
            mNotGarPrRegionsFound = true
            proceed()
        }
    }

}
