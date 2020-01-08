package com.garpr.android.repositories

import com.garpr.android.data.models.HeadToHead
import com.garpr.android.data.models.Region
import com.garpr.android.data.models.TournamentMatch
import com.garpr.android.misc.Schedulers
import com.garpr.android.networking.ServerApi
import io.reactivex.Single
import java.util.Collections

class HeadToHeadRepositoryImpl(
        private val schedulers: Schedulers,
        private val serverApi: ServerApi
) : HeadToHeadRepository {

    override fun getHeadToHead(region: Region, playerId: String,
            opponentId: String): Single<HeadToHead> {
        return serverApi.getHeadToHead(region, playerId, opponentId)
                .subscribeOn(schedulers.background)
                .doOnSuccess {
                    if (!it.matches.isNullOrEmpty()) {
                        Collections.sort(it.matches, TournamentMatch.REVERSE_CHRONOLOGICAL_ORDER)
                    }
                }
    }

}
