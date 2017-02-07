package com.garpr.android.misc;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.garpr.android.models.FullPlayer;
import com.garpr.android.models.Match;
import com.garpr.android.models.MatchesBundle;
import com.garpr.android.models.Rating;
import com.garpr.android.models.Ratings;

import java.util.ArrayList;
import java.util.Collections;

public final class ListUtilsImpl {

    @Nullable
    public static ArrayList<Object> createList(@NonNull final String region,
            @NonNull final FullPlayer player, @Nullable final MatchesBundle bundle) {
        final ArrayList<Object> list = new ArrayList<>();

        if (player.hasRatings()) {
            final Ratings ratings = player.getRatings();
            // noinspection ConstantConditions
            final Rating rating = ratings.getRegion(region);

            if (rating != null) {
                list.add(rating);
            }
        }

        if (bundle == null || !bundle.hasMatches()) {
            if (list.isEmpty()) {
                return null;
            } else {
                list.trimToSize();
                return list;
            }
        }

        final ArrayList<Match> matches = bundle.getMatches();
        // noinspection ConstantConditions
        Collections.sort(matches, Match.REVERSE_CHRONOLOGICAL_ORDER);

        String tournamentId = null;

        for (final Match match : matches) {
            if (tournamentId == null || !match.getTournamentId().equals(tournamentId)) {
                tournamentId = match.getTournamentId();
                list.add(match.getTournamentDate());
            }

            list.add(match);
        }

        list.trimToSize();
        return list;
    }

}
