package com.garpr.android.networking

import com.garpr.android.models.FullPlayer
import com.garpr.android.models.FullTournament
import com.garpr.android.models.HeadToHead
import com.garpr.android.models.MatchesBundle
import com.garpr.android.models.PlayersBundle
import com.garpr.android.models.RankingsBundle
import com.garpr.android.models.RegionsBundle
import com.garpr.android.models.TournamentsBundle
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GarPrApi {

    @GET("{regionId}/matches/{playerId}")
    fun getHeadToHead(@Path("regionId") regionId: String, @Path("playerId") playerId: String,
            @Query("opponent") opponentId: String): Call<HeadToHead>

    @GET("{regionId}/matches/{playerId}")
    fun getMatches(@Path("regionId") regionId: String, @Path("playerId") playerId: String):
            Call<MatchesBundle>

    @GET("{regionId}/players/{playerId}")
    fun getPlayer(@Path("regionId") regionId: String, @Path("playerId") playerId: String):
            Call<FullPlayer>

    @GET("{regionId}/players")
    fun getPlayers(@Path("regionId") regionId: String): Call<PlayersBundle>

    @GET("{regionId}/rankings")
    fun getRankings(@Path("regionId") regionId: String): Call<RankingsBundle>

    @GET("regions")
    fun getRegions(): Call<RegionsBundle>

    @GET("{regionId}/tournaments/{tournamentId}")
    fun getTournament(@Path("regionId") regionId: String, @Path("tournamentId") tournamentId: String):
            Call<FullTournament>

    @GET("{regionId}/tournaments")
    fun getTournaments(@Path("regionId") regionId: String): Call<TournamentsBundle>

}
