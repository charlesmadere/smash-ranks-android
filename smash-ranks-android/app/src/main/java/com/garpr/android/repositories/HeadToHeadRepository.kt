package com.garpr.android.repositories

import com.garpr.android.data.models.HeadToHead
import com.garpr.android.data.models.Region
import io.reactivex.Single

interface HeadToHeadRepository {

    fun getHeadToHead(region: Region, playerId: String, opponentId: String): Single<HeadToHead>

}
