package com.garpr.android.misc

import com.garpr.android.models.RankedPlayer

interface PreviousRankUtils {

    enum class Info {
        DECREASE, INCREASE
    }

    fun checkRanking(rankedPlayer: RankedPlayer?): Info?

}
