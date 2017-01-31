package com.garpr.android.networking;

import android.support.annotation.NonNull;

import com.garpr.android.models.Player;
import com.garpr.android.models.PlayersBundle;
import com.garpr.android.models.RankingsBundle;
import com.garpr.android.models.Tournament;
import com.garpr.android.models.TournamentsBundle;

public interface ServerApi {

    void getPlayer(@NonNull final String region, @NonNull final String playerId,
            @NonNull final ApiCall<Player> call);

    void getPlayers(@NonNull final String region, @NonNull final ApiCall<PlayersBundle> call);

    void getRankings(@NonNull final String region, @NonNull final ApiCall<RankingsBundle> call);

    void getTournament(@NonNull final String region, @NonNull final String tournamentId,
            final ApiCall<Tournament> call);

    void getTournaments(@NonNull final String region, @NonNull final ApiCall<TournamentsBundle> call);

}
