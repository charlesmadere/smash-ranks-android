package com.garpr.android.misc

import com.garpr.android.models.Ranking

interface PreviousRankUtils {

    enum class Info {
        DECREASE, INCREASE
    }

    fun checkRanking(ranking: Ranking?): Info?

}
