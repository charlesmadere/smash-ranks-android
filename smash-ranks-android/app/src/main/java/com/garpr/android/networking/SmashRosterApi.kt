package com.garpr.android.networking

import com.garpr.android.models.SmashRoster
import retrofit2.Call
import retrofit2.http.GET

interface SmashRosterApi {

    @GET("json/gar_pr.json")
    fun getGarPrJson(): Call<SmashRoster>

    @GET("json/not_gar_pr.json")
    fun getNotGarPrJson(): Call<SmashRoster>

}
