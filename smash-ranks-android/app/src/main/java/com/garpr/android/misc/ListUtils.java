package com.garpr.android.misc;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.garpr.android.R;
import com.garpr.android.models.AbsPlayer;
import com.garpr.android.models.AbsTournament;
import com.garpr.android.models.FullPlayer;
import com.garpr.android.models.FullTournament;
import com.garpr.android.models.HeadToHead;
import com.garpr.android.models.LiteTournament;
import com.garpr.android.models.Match;
import com.garpr.android.models.MatchesBundle;
import com.garpr.android.models.Ranking;
import com.garpr.android.models.Rating;
import com.garpr.android.models.TournamentsBundle;
import com.garpr.android.models.WinsLosses;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ListUtils {

    @NonNull
    public static ArrayList<Object> createHeadToHeadList(@NonNull final Context context,
            @Nullable final HeadToHead headToHead) {
        final ArrayList<Object> list = new ArrayList<>();

        if (headToHead == null) {
            list.add(new WinsLosses(0, 0));
            list.add(context.getString(R.string.no_matches));
            list.trimToSize();
            return list;
        }

        list.add(new WinsLosses(headToHead.getWins(), headToHead.getLosses()));

        if (!headToHead.hasMatches()) {
            list.add(context.getString(R.string.no_matches));
            list.trimToSize();
            return list;
        }

        // noinspection ConstantConditions
        list.addAll(createSortedTournamentAndMatchList(headToHead.getMatches()));
        list.trimToSize();

        return list;
    }

    @Nullable
    public static ArrayList<Object> createPlayerMatchesList(@NonNull final Context context,
            @NonNull final RegionManager regionManager, @NonNull final FullPlayer fullPlayer,
            @Nullable final MatchesBundle bundle) {
        final String region = regionManager.getRegion(context);

        // noinspection ConstantConditions
        final Rating rating = fullPlayer.hasRatings() ? fullPlayer.getRatings().getRegion(region)
                : null;

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
                list.add(context.getString(R.string.no_matches));
                list.trimToSize();
                return list;
            }
        }

        // noinspection ConstantConditions
        list.addAll(createSortedTournamentAndMatchList(bundle.getMatches()));
        list.trimToSize();

        return list;
    }

    private static ArrayList<Object> createSortedTournamentAndMatchList(
            @NonNull final ArrayList<Match> matches) {
        final ArrayList<Match> matchesCopy = new ArrayList<>(matches);
        Collections.sort(matchesCopy, Match.REVERSE_CHRONOLOGICAL_ORDER);

        final ArrayList<Object> list = new ArrayList<>();
        String tournamentId = null;

        for (final Match match : matchesCopy) {
            if (tournamentId == null || !match.getTournamentId().equals(tournamentId)) {
                tournamentId = match.getTournamentId();
                list.add(new LiteTournament(tournamentId, match.getTournamentName(),
                        match.getTournamentDate()));
            }

            list.add(match);
        }

        return list;
    }

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
    public static ArrayList<Object> filterPlayerMatchesList(@Nullable final Match.Result result,
            @Nullable final List<Object> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }

        final ArrayList<Object> newList = new ArrayList<>(list.size());

        if (result == null) {
            newList.addAll(list);
            return newList;
        }

        boolean addedCharSequence = false;
        boolean addedRating = false;
        boolean addedWinsLosses = false;

        for (int i = 0; i < list.size(); ++i) {
            final Object objectI = list.get(i);

            if (objectI instanceof CharSequence) {
                if (!addedCharSequence) {
                    addedCharSequence = true;
                    newList.add(objectI);
                }
            } else if (objectI instanceof Rating) {
                if (!addedRating) {
                    addedRating = true;
                    newList.add(objectI);
                }
            } else if (objectI instanceof WinsLosses) {
                if (!addedWinsLosses) {
                    addedWinsLosses = true;
                    newList.add(objectI);
                }
            } else if (objectI instanceof AbsTournament) {
                final AbsTournament tournament = (AbsTournament) objectI;
                boolean addedTournament = false;
                int j = i + 1;

                while (j < list.size()) {
                    final Object objectJ = list.get(j);

                    if (objectJ instanceof Match) {
                        final Match match = (Match) objectJ;

                        if (match.getResult() == result) {
                            if (!addedTournament) {
                                addedTournament = true;
                                newList.add(tournament);
                            }

                            newList.add(match);
                        }

                        ++j;
                    } else {
                        j = list.size();
                    }
                }
            }
        }

        return newList;
    }

    @Nullable
    public static ArrayList<AbsPlayer> searchPlayerList(@Nullable String query,
            @Nullable final List<AbsPlayer> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }

        final ArrayList<AbsPlayer> newList = new ArrayList<>(list.size());

        if (TextUtils.isEmpty(query) || TextUtils.getTrimmedLength(query) == 0) {
            newList.addAll(list);
            return newList;
        }

        query = query.trim().toLowerCase();

        for (final AbsPlayer player : list) {
            if (player.getName().toLowerCase().contains(query)) {
                newList.add(player);
            }
        }

        return newList;
    }

    @Nullable
    public static ArrayList<Object> searchPlayerMatchesList(@Nullable String query,
            @Nullable final List<Object> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }

        final ArrayList<Object> newList = new ArrayList<>(list.size());

        if (TextUtils.isEmpty(query) || TextUtils.getTrimmedLength(query) == 0) {
            newList.addAll(list);
            return newList;
        }

        query = query.trim().toLowerCase();
        boolean addedRating = false;

        for (int i = 0; i < list.size(); ++i) {
            final Object objectI = list.get(i);

            if (objectI instanceof Rating) {
                if (!addedRating) {
                    addedRating = true;
                    newList.add(objectI);
                }
            } else if (objectI instanceof AbsTournament) {
                final AbsTournament tournament = (AbsTournament) objectI;
                boolean addedTournament = false;
                int j = i + 1;

                while (j < list.size()) {
                    final Object objectJ = list.get(j);

                    if (objectJ instanceof Match) {
                        final Match match = (Match) objectJ;

                        if (match.getOpponentName().toLowerCase().contains(query)) {
                            if (!addedTournament) {
                                addedTournament = true;
                                newList.add(tournament);
                            }

                            newList.add(match);
                        }

                        ++j;
                    } else {
                        j = list.size();
                    }
                }
            }
        }

        return newList;
    }

    @Nullable
    public static ArrayList<Ranking> searchRankingList(@Nullable String query,
            @Nullable final List<Ranking> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }

        final ArrayList<Ranking> newList = new ArrayList<>(list.size());

        if (TextUtils.isEmpty(query) || TextUtils.getTrimmedLength(query) == 0) {
            newList.addAll(list);
            return newList;
        }

        query = query.trim().toLowerCase();

        for (final Ranking ranking : list) {
            if (ranking.getName().toLowerCase().contains(query)) {
                newList.add(ranking);
            }
        }

        return newList;
    }

    @Nullable
    public static ArrayList<AbsTournament> searchTournamentList(@Nullable String query,
            @Nullable final List<AbsTournament> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }

        final ArrayList<AbsTournament> newList = new ArrayList<>(list.size());

        if (TextUtils.isEmpty(query) || TextUtils.getTrimmedLength(query) == 0) {
            newList.addAll(list);
            Collections.sort(newList, AbsTournament.REVERSE_CHRONOLOGICAL_ORDER);
            return newList;
        }

        query = query.trim().toLowerCase();

        for (final AbsTournament tournament : list) {
            if (tournament.getName().toLowerCase().contains(query)) {
                newList.add(tournament);
            }
        }

        Collections.sort(newList, AbsTournament.REVERSE_CHRONOLOGICAL_ORDER);
        return newList;
    }

    @Nullable
    public static ArrayList<FullTournament.Match> searchTournamentMatchesList(@Nullable String query,
            @Nullable final List<FullTournament.Match> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }

        final ArrayList<FullTournament.Match> newList = new ArrayList<>(list.size());

        if (TextUtils.isEmpty(query) || TextUtils.getTrimmedLength(query) == 0) {
            newList.addAll(list);
            return newList;
        }

        query = query.trim().toLowerCase();

        for (final FullTournament.Match match : list) {
            if (match.getWinnerName().toLowerCase().contains(query) ||
                    match.getLoserName().toLowerCase().contains(query)) {
                newList.add(match);
            }
        }

        return newList;
    }

}
