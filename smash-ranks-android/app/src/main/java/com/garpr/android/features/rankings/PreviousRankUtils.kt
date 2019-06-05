package com.garpr.android.features.rankings

import com.garpr.android.data.models.RankedPlayer

interface PreviousRankUtils {

    fun getRankInfo(rankedPlayer: RankedPlayer?): Info?

    enum class Info {
        DECREASE, INCREASE, NO_CHANGE
    }

}
