package com.garpr.android.networking;

import com.garpr.android.models.FullPlayer;
import com.garpr.android.models.FullTournament;
import com.garpr.android.models.HeadToHead;
import com.garpr.android.models.MatchesBundle;
import com.garpr.android.models.PlayersBundle;
import com.garpr.android.models.RankingsBundle;
import com.garpr.android.models.RegionsBundle;
import com.garpr.android.models.TournamentsBundle;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GarPrApi {

    @GET("{region}/matches/{playerId}")
    Call<HeadToHead> getHeadToHead(@Path("region") String region, @Path("playerId") String playerId,
            @Query("opponent") String opponentId);

    @GET("{region}/matches/{playerId}")
    Call<MatchesBundle> getMatches(@Path("region") String region, @Path("playerId") String playerId);

    @GET("{region}/players/{playerId}")
    Call<FullPlayer> getPlayer(@Path("region") String region, @Path("playerId") String playerId);

    @GET("{region}/players")
    Call<PlayersBundle> getPlayers(@Path("region") String region);

    @GET("{region}/rankings")
    Call<RankingsBundle> getRankings(@Path("region") String region);

    @GET("regions")
    Call<RegionsBundle> getRegions();

    @GET("{region}/tournaments/{tournamentId}")
    Call<FullTournament> getTournament(@Path("region") String region,
            @Path("tournamentId") String tournamentId);

    @GET("{region}/tournaments")
    Call<TournamentsBundle> getTournaments(@Path("region") String region);

}
