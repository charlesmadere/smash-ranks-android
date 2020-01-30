package com.garpr.android.repositories

import androidx.annotation.AnyThread
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.data.models.Optional
import com.garpr.android.data.models.Region
import io.reactivex.Observable

interface IdentityRepository {

    val hasIdentityObservable: Observable<Boolean>

    val identityObservable: Observable<Optional<FavoritePlayer>>

    @AnyThread
    fun removeIdentity()

    @AnyThread
    fun setIdentity(player: AbsPlayer, region: Region)

}
