package com.garpr.android.networking

import com.garpr.android.data.models.SmashCompetitor
import io.reactivex.Single
import retrofit2.http.GET

interface SmashRosterApi2 {

    @GET("json/gar_pr.json")
    fun getGarPrJson(): Single<Map<String, SmashCompetitor>>

    @GET("json/not_gar_pr.json")
    fun getNotGarPrJson(): Single<Map<String, SmashCompetitor>>

}
