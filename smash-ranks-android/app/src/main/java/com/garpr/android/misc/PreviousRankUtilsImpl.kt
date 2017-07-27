package com.garpr.android.misc

import com.garpr.android.misc.PreviousRankUtils.Info.DECREASE
import com.garpr.android.misc.PreviousRankUtils.Info.INCREASE
import com.garpr.android.models.Ranking

class PreviousRankUtilsImpl : PreviousRankUtils {

    override fun checkRanking(ranking: Ranking?): PreviousRankUtils.Info? {
        if (ranking == null) {
            return null
        }

        val previousRank = ranking.previousRank
        val rank = ranking.rank

        if (previousRank == null || previousRank == rank) {
            return null
        }

        // The below if statement appears backwards because a HIGHER rank corresponds to a LOWER
        // number. As a player's rank moves closer to 1, their rank INCREASES.
        return if (previousRank > rank) INCREASE else DECREASE
    }

}
