package com.garpr.android.misc

import com.garpr.android.models.Ranking

interface PreviousRankUtils {

    fun checkRanking(ranking: Ranking?): Info?


    enum class Info {
        DECREASE, INCREASE
    }

}
