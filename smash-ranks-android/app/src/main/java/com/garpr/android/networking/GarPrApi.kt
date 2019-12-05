package com.garpr.android.networking

import com.garpr.android.data.models.FullPlayer
import com.garpr.android.data.models.FullTournament
import com.garpr.android.data.models.HeadToHead
import com.garpr.android.data.models.MatchesBundle
import com.garpr.android.data.models.PlayersBundle
import com.garpr.android.data.models.RankingsBundle
import com.garpr.android.data.models.RegionsBundle
import com.garpr.android.data.models.TournamentsBundle
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GarPrApi {

    @GET("{regionId}/matches/{playerId}")
    fun getHeadToHead(
            @Path("regionId") regionId: String,
            @Path("playerId") playerId: String,
            @Query("opponent") opponentId: String
    ): Single<HeadToHead>

    @GET("{regionId}/matches/{playerId}")
    fun getMatches(
            @Path("regionId") regionId: String,
            @Path("playerId") playerId: String
    ): Single<MatchesBundle>

    @GET("{regionId}/players/{playerId}")
    fun getPlayer(
            @Path("regionId") regionId: String,
            @Path("playerId") playerId: String
    ): Single<FullPlayer>

    @GET("{regionId}/players")
    fun getPlayers(
            @Path("regionId") regionId: String
    ): Single<PlayersBundle>

    @GET("{regionId}/rankings")
    fun getRankings(
            @Path("regionId") regionId: String
    ): Single<RankingsBundle>

    @GET("regions")
    fun getRegions(): Single<RegionsBundle>

    @GET("{regionId}/tournaments/{tournamentId}")
    fun getTournament(
            @Path("regionId") regionId: String,
            @Path("tournamentId") tournamentId: String
    ): Single<FullTournament>

    @GET("{regionId}/tournaments")
    fun getTournaments(
            @Path("regionId") regionId: String
    ): Single<TournamentsBundle>

}
