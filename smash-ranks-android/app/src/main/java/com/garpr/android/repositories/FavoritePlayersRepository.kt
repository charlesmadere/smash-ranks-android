package com.garpr.android.repositories

import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.data.models.Optional
import com.garpr.android.data.models.Region
import io.reactivex.Observable

interface FavoritePlayersRepository {

    val isEmpty: Boolean

    val size: Int

    val players: List<FavoritePlayer>?

    val playersObservable: Observable<Optional<List<FavoritePlayer>>>

    fun addPlayer(player: AbsPlayer, region: Region)

    fun clear()

    operator fun contains(player: AbsPlayer): Boolean

    operator fun contains(playerId: String): Boolean

    fun removePlayer(player: AbsPlayer)

    fun removePlayer(playerId: String)

}
