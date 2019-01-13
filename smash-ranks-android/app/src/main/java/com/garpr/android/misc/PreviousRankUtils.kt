package com.garpr.android.misc

import com.garpr.android.data.models.RankedPlayer

interface PreviousRankUtils {

    fun getRankInfo(rankedPlayer: RankedPlayer?): Info?

    enum class Info {
        DECREASE, INCREASE, NO_CHANGE
    }

}
