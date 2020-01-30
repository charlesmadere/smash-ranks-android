package com.garpr.android.repositories

import androidx.annotation.AnyThread
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.data.models.Region
import io.reactivex.Observable

interface FavoritePlayersRepository {

    val sizeObservable: Observable<Int>

    val playersObservable: Observable<List<FavoritePlayer>>

    @AnyThread
    fun addPlayer(player: AbsPlayer, region: Region)

    @AnyThread
    fun clear()

    @AnyThread
    fun migrate()

    @AnyThread
    fun removePlayer(player: AbsPlayer, region: Region)

}
