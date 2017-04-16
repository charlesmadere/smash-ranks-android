package com.garpr.android.misc;

import android.support.annotation.Nullable;

import com.garpr.android.models.Ranking;

public class PreviousRankUtilsImpl implements PreviousRankUtils {

    @Nullable
    @Override
    public Info checkRanking(@Nullable final Ranking ranking) {
        if (ranking == null) {
            return null;
        }

        final Integer previousRank = ranking.getPreviousRank();
        final int rank = ranking.getRank();

        if (previousRank == null || previousRank == rank) {
            return null;
        }

        return rank < previousRank ? Info.INCREASE : Info.DECREASE;
    }

}
