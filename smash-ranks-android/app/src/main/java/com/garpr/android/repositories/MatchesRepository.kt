package com.garpr.android.repositories

import com.garpr.android.data.models.MatchesBundle
import com.garpr.android.data.models.Region
import io.reactivex.Single

interface MatchesRepository {

    fun getMatches(region: Region, playerId: String): Single<MatchesBundle>

}
