package com.garpr.android.managers

import android.content.Context

import com.garpr.android.models.AbsPlayer
import com.garpr.android.models.FavoritePlayer
import com.garpr.android.models.Region

interface FavoritePlayersManager {

    interface OnFavoritePlayersChangeListener {
        fun onFavoritePlayersChange(favoritePlayersManager: FavoritePlayersManager)
    }

    val absPlayers: List<AbsPlayer>?

    fun addListener(listener: OnFavoritePlayersChangeListener)

    fun addPlayer(player: AbsPlayer, region: Region)

    fun clear()

    operator fun contains(player: AbsPlayer): Boolean

    operator fun contains(playerId: String): Boolean

    val isEmpty: Boolean

    val players: List<FavoritePlayer>?

    fun removeListener(listener: OnFavoritePlayersChangeListener?)

    fun removePlayer(player: AbsPlayer)

    fun removePlayer(playerId: String)

    fun showAddOrRemovePlayerDialog(context: Context, player: AbsPlayer?, region: Region): Boolean

    val size: Int

}
