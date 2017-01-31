package com.garpr.android.networking;

import com.garpr.android.models.Player;
import com.garpr.android.models.PlayersBundle;
import com.garpr.android.models.RankingsBundle;
import com.garpr.android.models.Tournament;
import com.garpr.android.models.TournamentsBundle;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GarPrApi {

    @GET("{region}/players/{playerId}")
    Call<Player> getPlayer(@Path("region") String region, @Path("playerId") String playerId);

    @GET("{region}/players")
    Call<PlayersBundle> getPlayers(@Path("region") String region);

    @GET("{region}/rankings")
    Call<RankingsBundle> getRankings(@Path("region") String region);

    @GET("{region}/tournaments/{tournamentId}")
    Call<Tournament> getTournament(@Path("region") String region,
            @Path("tournamentId") String tournamentId);

    @GET("{region}/tournaments")
    Call<TournamentsBundle> getTournaments(@Path("region") String region);

}
