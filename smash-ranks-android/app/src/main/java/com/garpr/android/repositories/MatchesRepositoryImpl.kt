package com.garpr.android.repositories

import com.garpr.android.data.models.MatchesBundle
import com.garpr.android.data.models.Region
import com.garpr.android.misc.Schedulers
import com.garpr.android.networking.ServerApi2
import io.reactivex.Single

class MatchesRepositoryImpl(
        private val schedulers: Schedulers,
        private val serverApi: ServerApi2
) : MatchesRepository {

    override fun getMatches(region: Region, playerId: String): Single<MatchesBundle> {
        return serverApi.getMatches(region, playerId)
                .subscribeOn(schedulers.background)
    }

}
