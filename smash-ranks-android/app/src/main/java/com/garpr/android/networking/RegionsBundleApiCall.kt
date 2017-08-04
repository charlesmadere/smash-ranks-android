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

}
