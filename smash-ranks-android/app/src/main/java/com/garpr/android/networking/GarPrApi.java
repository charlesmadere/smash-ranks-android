package com.garpr.android.networking;

import com.garpr.android.models.RankingsBundle;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GarPrApi {

    @GET("{region}/rankings")
    Call<RankingsBundle> getRankings(@Path("region") String region);

}
