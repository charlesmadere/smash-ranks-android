package com.garpr.android.repositories

import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.data.models.Optional
import com.garpr.android.data.models.Region
import io.reactivex.Observable

interface IdentityRepository {

    val hasIdentity: Boolean

    val identity: FavoritePlayer?

    val identityObservable: Observable<Optional<FavoritePlayer>>

    fun isPlayer(player: AbsPlayer?): Boolean

    fun isPlayer(id: String?): Boolean

    fun removeIdentity()

    fun setIdentity(player: AbsPlayer, region: Region)

}
