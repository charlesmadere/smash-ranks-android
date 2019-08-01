package com.garpr.android.repositories

import com.garpr.android.data.models.HeadToHead
import com.garpr.android.data.models.Region
import com.garpr.android.misc.Schedulers
import com.garpr.android.networking.ServerApi2
import io.reactivex.Single

class HeadToHeadRepositoryImpl(
        private val schedulers: Schedulers,
        private val serverApi: ServerApi2
) : HeadToHeadRepository {

    override fun getHeadToHead(region: Region, playerId: String,
            opponentId: String): Single<HeadToHead> {
        return serverApi.getHeadToHead(region, playerId, opponentId)
                .subscribeOn(schedulers.background)
    }

}
