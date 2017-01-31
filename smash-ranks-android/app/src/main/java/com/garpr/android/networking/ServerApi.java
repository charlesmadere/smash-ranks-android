package com.garpr.android.networking;

import android.support.annotation.NonNull;

import com.garpr.android.models.Player;
import com.garpr.android.models.PlayersBundle;
import com.garpr.android.models.RankingsBundle;
import com.garpr.android.models.Tournament;
import com.garpr.android.models.TournamentsBundle;

public interface ServerApi {

    void getPlayer(@NonNull final String region, @NonNull final String playerId,
            @NonNull final ApiListener<Player> apiCall);

    void getPlayers(@NonNull final String region, @NonNull final ApiListener<PlayersBundle> apiCall);

    void getRankings(@NonNull final String region, @NonNull final ApiListener<RankingsBundle> apiCall);

    void getTournament(@NonNull final String region, @NonNull final String tournamentId,
            final ApiListener<Tournament> apiCall);

    void getTournaments(@NonNull final String region,
            @NonNull final ApiListener<TournamentsBundle> apiCall);

}
