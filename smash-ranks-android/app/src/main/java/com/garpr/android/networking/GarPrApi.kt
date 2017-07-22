package com.garpr.android.networking

import com.garpr.android.models.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface GarPrApi {

    @GET
    fun getHeadToHead(@Url url: String): Call<HeadToHead>

    @GET
    fun getMatches(@Url url: String): Call<MatchesBundle>

    @GET
    fun getPlayer(@Url url: String): Call<FullPlayer>

    @GET
    fun getPlayers(@Url url: String): Call<PlayersBundle>

    @GET
    fun getRankings(@Url url: String): Call<RankingsBundle>

    @GET
    fun getRegions(@Url url: String): Call<RegionsBundle>

    @GET
    fun getTournament(@Url url: String): Call<FullTournament>

    @GET
    fun getTournaments(@Url url: String): Call<TournamentsBundle>

}
