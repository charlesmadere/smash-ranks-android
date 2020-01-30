package com.garpr.android.repositories

import com.garpr.android.data.models.MatchesBundle
import com.garpr.android.data.models.Region
import com.garpr.android.data.models.TournamentMatch
import com.garpr.android.networking.ServerApi
import io.reactivex.Single
import java.util.Collections

class MatchesRepositoryImpl(
        private val serverApi: ServerApi
) : MatchesRepository {

    override fun getMatches(region: Region, playerId: String): Single<MatchesBundle> {
        return serverApi.getMatches(region, playerId)
                .doOnSuccess { bundle ->
                    if (!bundle.matches.isNullOrEmpty()) {
                        Collections.sort(bundle.matches, TournamentMatch.REVERSE_CHRONOLOGICAL_ORDER)
                    }
                }
    }

}
