package com.garpr.android.misc

import android.content.Context

import com.garpr.android.models.AbsPlayer
import com.garpr.android.models.FavoritePlayer
import com.garpr.android.models.LiteRegion

interface FavoritePlayersManager {

    interface OnFavoritePlayersChangeListener {
        fun onFavoritePlayersChanged(manager: FavoritePlayersManager)
    }

    val absPlayers: List<AbsPlayer>?

    fun addListener(listener: OnFavoritePlayersChangeListener)

    fun addPlayer(player: AbsPlayer, region: LiteRegion)

    fun clear()

    operator fun contains(player: AbsPlayer): Boolean

    operator fun contains(playerId: String): Boolean

    val isEmpty: Boolean

    val players: List<FavoritePlayer>?

    fun removeListener(listener: OnFavoritePlayersChangeListener?)

    fun removePlayer(player: AbsPlayer)

    fun removePlayer(playerId: String)

    fun showAddOrRemovePlayerDialog(context: Context, player: AbsPlayer?, region: LiteRegion): Boolean

    val size: Int

}
