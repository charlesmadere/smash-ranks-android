package com.garpr.android.repositories

import com.garpr.android.data.models.FullPlayer
import com.garpr.android.data.models.PlayersBundle
import com.garpr.android.data.models.Region
import io.reactivex.Single

interface PlayersRepository {

    fun getPlayer(region: Region, playerId: String): Single<FullPlayer>

    fun getPlayers(region: Region): Single<PlayersBundle>

}
