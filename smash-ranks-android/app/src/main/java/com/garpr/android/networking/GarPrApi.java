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
import retrofit2.http.Url;

public interface GarPrApi {

    @GET
    Call<HeadToHead> getHeadToHead(@Url String url);

    @GET
    Call<MatchesBundle> getMatches(@Url String url);

    @GET
    Call<FullPlayer> getPlayer(@Url String url);

    @GET
    Call<PlayersBundle> getPlayers(@Url String url);

    @GET
    Call<RankingsBundle> getRankings(@Url String url);

    @GET
    Call<RegionsBundle> getRegions(@Url String url);

    @GET
    Call<FullTournament> getTournament(@Url String url);

    @GET
    Call<TournamentsBundle> getTournaments(@Url String url);

}
