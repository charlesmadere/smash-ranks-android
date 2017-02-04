package com.garpr.android.networking;

import android.support.annotation.NonNull;

import com.garpr.android.models.FullPlayer;
import com.garpr.android.models.FullTournament;
import com.garpr.android.models.PlayersBundle;
import com.garpr.android.models.RankingsBundle;
import com.garpr.android.models.RegionsBundle;
import com.garpr.android.models.TournamentsBundle;

public interface ServerApi {

    void getPlayer(@NonNull final String region, @NonNull final String playerId,
            @NonNull final ApiListener<FullPlayer> apiCall);

    void getPlayers(@NonNull final String region, @NonNull final ApiListener<PlayersBundle> apiCall);

    void getRankings(@NonNull final String region, @NonNull final ApiListener<RankingsBundle> apiCall);

    void getRegions(@NonNull final ApiListener<RegionsBundle> apiCall);

    void getTournament(@NonNull final String region, @NonNull final String tournamentId,
            final ApiListener<FullTournament> apiCall);

    void getTournaments(@NonNull final String region,
            @NonNull final ApiListener<TournamentsBundle> apiCall);

}
