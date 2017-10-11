package com.garpr.android.misc

import com.garpr.android.misc.PreviousRankUtils.Info
import com.garpr.android.misc.PreviousRankUtils.Info.DECREASE
import com.garpr.android.misc.PreviousRankUtils.Info.INCREASE
import com.garpr.android.models.RankedPlayer

class PreviousRankUtilsImpl : PreviousRankUtils {

    override fun getRankInfo(rankedPlayer: RankedPlayer?): Info? {
        if (rankedPlayer == null) {
            return null
        }

        val previousRank = rankedPlayer.previousRank
        val rank = rankedPlayer.rank

        if (previousRank == null || previousRank == rank) {
            return null
        }

        // The below if statement appears backwards because a HIGHER rank corresponds to a LOWER
        // number. As a player's rank moves closer to 1, their rank INCREASES.
        return if (previousRank > rank) INCREASE else DECREASE
    }

}
