package com.garpr.android.networking;

import android.support.annotation.NonNull;

import com.garpr.android.models.FullPlayer;
import com.garpr.android.models.FullTournament;
import com.garpr.android.models.HeadToHead;
import com.garpr.android.models.MatchesBundle;
import com.garpr.android.models.PlayersBundle;
import com.garpr.android.models.RankingsBundle;
import com.garpr.android.models.RegionsBundle;
import com.garpr.android.models.TournamentsBundle;

public interface ServerApi {

    void getHeadToHead(@NonNull final String region, @NonNull final String playerId,
            @NonNull final String opponentId, @NonNull final ApiListener<HeadToHead> listener);

    void getMatches(@NonNull final String region, @NonNull final String playerId,
            @NonNull final ApiListener<MatchesBundle> listener);

    void getPlayer(@NonNull final String region, @NonNull final String playerId,
            @NonNull final ApiListener<FullPlayer> listener);

    void getPlayers(@NonNull final String region, @NonNull final ApiListener<PlayersBundle> listener);

    void getRankings(@NonNull final String region, @NonNull final ApiListener<RankingsBundle> listener);

    void getRegions(@NonNull final ApiListener<RegionsBundle> listener);

    void getTournament(@NonNull final String region, @NonNull final String tournamentId,
            final ApiListener<FullTournament> listener);

    void getTournaments(@NonNull final String region,
            @NonNull final ApiListener<TournamentsBundle> listener);

}
