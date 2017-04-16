package com.garpr.android.misc;

import android.support.annotation.Nullable;

import com.garpr.android.models.Ranking;

public interface PreviousRankUtils {

    @Nullable
    Info checkRanking(@Nullable final Ranking ranking);


    enum Info {
        DECREASE, INCREASE
    }

}
