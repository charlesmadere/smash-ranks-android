package com.garpr.android.networking;

import android.support.annotation.NonNull;

import com.garpr.android.models.Player;
import com.garpr.android.models.PlayersBundle;
import com.garpr.android.models.RankingsBundle;
import com.garpr.android.models.Tournament;
import com.garpr.android.models.TournamentsBundle;

public class ServerApiImpl implements ServerApi {

    @Override
    public void getPlayer(@NonNull final String region, @NonNull final String playerId,
            @NonNull final ApiCall<Player> call) {
        // TODO
    }

    @Override
    public void getPlayers(@NonNull final String region,
            @NonNull final ApiCall<PlayersBundle> call) {
        // TODO
    }

    @Override
    public void getRankings(@NonNull final String region,
            @NonNull final ApiCall<RankingsBundle> call) {
        // TODO
    }

    @Override
    public void getTournament(@NonNull final String region, @NonNull final String tournamentId,
            @NonNull final ApiCall<Tournament> call) {

    }

    @Override
    public void getTournaments(@NonNull final String region,
            @NonNull final ApiCall<TournamentsBundle> call) {
        // TODO
    }

}
