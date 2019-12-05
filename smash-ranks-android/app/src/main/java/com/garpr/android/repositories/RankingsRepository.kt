package com.garpr.android.repositories

import com.garpr.android.data.models.RankingsBundle
import com.garpr.android.data.models.Region
import io.reactivex.Single

interface RankingsRepository {

    fun getRankings(region: Region): Single<RankingsBundle>

}
