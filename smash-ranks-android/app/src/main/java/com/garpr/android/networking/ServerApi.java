package com.garpr.android.networking;

import android.support.annotation.NonNull;

import com.garpr.android.models.RankingsBundle;

public interface ServerApi {

    void getRankings(@NonNull final String region, final ApiCall<RankingsBundle> call);

}
