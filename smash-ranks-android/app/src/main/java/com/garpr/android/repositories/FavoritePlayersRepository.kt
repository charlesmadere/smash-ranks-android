package com.garpr.android.repositories

import androidx.fragment.app.FragmentManager
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.data.models.Region

interface FavoritePlayersRepository {

    interface OnFavoritePlayersChangeListener {
        fun onFavoritePlayersChange(favoritePlayersRepository: FavoritePlayersRepository)
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

    fun showAddOrRemovePlayerDialog(fragmentManager: FragmentManager, player: AbsPlayer?,
            region: Region): Boolean

    val size: Int

}
