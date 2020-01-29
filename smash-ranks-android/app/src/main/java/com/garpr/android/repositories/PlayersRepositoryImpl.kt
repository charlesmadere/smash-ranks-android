package com.garpr.android.repositories

import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.FullPlayer
import com.garpr.android.data.models.PlayersBundle
import com.garpr.android.data.models.Region
import com.garpr.android.networking.ServerApi
import io.reactivex.Single
import java.util.Collections

class PlayersRepositoryImpl(
        private val serverApi: ServerApi
) : PlayersRepository {

    override fun getPlayer(region: Region, playerId: String): Single<FullPlayer> {
        return serverApi.getPlayer(region, playerId)
    }

    override fun getPlayers(region: Region): Single<PlayersBundle> {
        return serverApi.getPlayers(region)
                .doOnSuccess { bundle ->
                    if (!bundle.players.isNullOrEmpty()) {
                        Collections.sort(bundle.players, AbsPlayer.ALPHABETICAL_ORDER)
                    }
                }
    }

}
