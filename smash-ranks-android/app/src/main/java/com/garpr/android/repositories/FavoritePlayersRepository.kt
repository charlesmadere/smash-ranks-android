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

    fun migrate()

    fun removePlayer(player: AbsPlayer, region: Region)

}
