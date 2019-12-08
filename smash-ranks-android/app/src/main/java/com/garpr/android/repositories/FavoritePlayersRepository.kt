package com.garpr.android.repositories

import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.data.models.Region

interface FavoritePlayersRepository {

    interface OnFavoritePlayersChangeListener {
        fun onFavoritePlayersChange(favoritePlayersRepository: FavoritePlayersRepository)
    }

    val isEmpty: Boolean

    val players: List<FavoritePlayer>?

    val size: Int

    fun addListener(listener: OnFavoritePlayersChangeListener)

    fun addPlayer(player: AbsPlayer, region: Region)

    fun clear()

    operator fun contains(player: AbsPlayer): Boolean

    operator fun contains(playerId: String): Boolean

    fun removeListener(listener: OnFavoritePlayersChangeListener?)

    fun removePlayer(player: AbsPlayer)

    fun removePlayer(playerId: String)

}
