package com.garpr.android.repositories

import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.FullPlayer
import com.garpr.android.data.models.PlayersBundle
import com.garpr.android.data.models.Region
import com.garpr.android.misc.Schedulers
import com.garpr.android.networking.ServerApi
import io.reactivex.Single
import java.util.Collections

class PlayersRepositoryImpl(
        private val schedulers: Schedulers,
        private val serverApi: ServerApi
) : PlayersRepository {

    override fun getPlayer(region: Region, playerId: String): Single<FullPlayer> {
        return serverApi.getPlayer(region, playerId)
                .subscribeOn(schedulers.background)
    }

    override fun getPlayers(region: Region): Single<PlayersBundle> {
        return serverApi.getPlayers(region)
                .subscribeOn(schedulers.background)
                .doOnSuccess {
                    if (!it.players.isNullOrEmpty()) {
                        Collections.sort(it.players, AbsPlayer.ALPHABETICAL_ORDER)
                    }
                }
    }

}
