package com.garpr.android.repositories

import com.garpr.android.data.models.PlayerMatchesBundle
import com.garpr.android.data.models.Region
import io.reactivex.Single

interface PlayerMatchesRepository {

    fun getPlayerAndMatches(region: Region, playerId: String): Single<PlayerMatchesBundle>

}
