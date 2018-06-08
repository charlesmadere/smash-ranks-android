package com.garpr.android.networking

import com.garpr.android.models.SmashCompetitor
import retrofit2.Call
import retrofit2.http.GET

interface SmashRosterApi {

    @GET("json/gar_pr.json")
    fun getGarPrJson(): Call<Map<String, SmashCompetitor>>

    @GET("json/not_gar_pr.json")
    fun getNotGarPrJson(): Call<Map<String, SmashCompetitor>>

}
