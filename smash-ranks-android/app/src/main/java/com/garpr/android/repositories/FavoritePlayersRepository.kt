package com.garpr.android.repositories

import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.data.models.Region
import io.reactivex.Observable

interface FavoritePlayersRepository {

    val sizeObservable: Observable<Int>

    val playersObservable: Observable<List<FavoritePlayer>>

    fun addPlayer(player: AbsPlayer, region: Region)

    fun clear()

    operator fun contains(player: AbsPlayer): Boolean

    fun removePlayer(player: AbsPlayer)

}
