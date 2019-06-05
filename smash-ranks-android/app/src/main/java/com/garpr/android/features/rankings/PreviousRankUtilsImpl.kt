package com.garpr.android.features.rankings

import com.garpr.android.data.models.RankedPlayer
import com.garpr.android.features.rankings.PreviousRankUtils.Info
import com.garpr.android.features.rankings.PreviousRankUtils.Info.DECREASE
import com.garpr.android.features.rankings.PreviousRankUtils.Info.INCREASE
import com.garpr.android.features.rankings.PreviousRankUtils.Info.NO_CHANGE

class PreviousRankUtilsImpl : PreviousRankUtils {

    override fun getRankInfo(rankedPlayer: RankedPlayer?): Info? {
        if (rankedPlayer?.previousRank == null) {
            return null
        }

        val previousRank = rankedPlayer.previousRank
        val rank = rankedPlayer.rank

        // The below if statement appears backwards because a HIGHER rank corresponds to a LOWER
        // number. As a player's rank moves closer to 1, their rank INCREASES.

        return if (previousRank == rank || previousRank == Int.MIN_VALUE) {
            NO_CHANGE
        } else if (previousRank > rank) {
            INCREASE
        } else {
            DECREASE
        }
    }

}
