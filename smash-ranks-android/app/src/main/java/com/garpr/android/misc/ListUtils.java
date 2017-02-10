package com.garpr.android.misc;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.garpr.android.R;
import com.garpr.android.models.AbsTournament;
import com.garpr.android.models.LiteTournament;
import com.garpr.android.models.Match;
import com.garpr.android.models.MatchesBundle;
import com.garpr.android.models.Rating;
import com.garpr.android.models.TournamentsBundle;

import java.util.ArrayList;
import java.util.Collections;

public final class ListUtils {

    @Nullable
    public static ArrayList<AbsTournament> createTournamentList(
            @Nullable final TournamentsBundle bundle) {
        if (bundle == null || !bundle.hasTournaments()) {
            return null;
        }

        // noinspection ConstantConditions
        final ArrayList<AbsTournament> tournaments = new ArrayList<>(bundle.getTournaments());
        Collections.sort(tournaments, AbsTournament.REVERSE_CHRONOLOGICAL_ORDER);

        return tournaments;
    }

    @Nullable
    public static ArrayList<Object> createPlayerList(@NonNull final Resources resources,
            @Nullable final Rating rating, @Nullable final MatchesBundle bundle) {
        if (rating == null && (bundle == null || !bundle.hasMatches())) {
            return  null;
        }

        final ArrayList<Object> list = new ArrayList<>();

        if (rating != null) {
            list.add(rating);
        }

        if (bundle == null || !bundle.hasMatches()) {
            if (list.isEmpty()) {
                return null;
            } else {
                list.add(resources.getString(R.string.no_matches));
                list.trimToSize();
                return list;
            }
        }

        // noinspection ConstantConditions
        final ArrayList<Match> matches = new ArrayList<>(bundle.getMatches());
        Collections.sort(matches, Match.REVERSE_CHRONOLOGICAL_ORDER);

        String tournamentId = null;

        for (final Match match : matches) {
            if (tournamentId == null || !match.getTournamentId().equals(tournamentId)) {
                tournamentId = match.getTournamentId();
                list.add(new LiteTournament(tournamentId, match.getTournamentName(),
                        match.getTournamentDate()));
            }

            list.add(match);
        }

        list.trimToSize();
        return list;
    }

}
