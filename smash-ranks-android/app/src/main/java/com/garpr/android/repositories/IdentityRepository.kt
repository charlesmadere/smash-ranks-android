package com.garpr.android.repositories

import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.data.models.Region

interface IdentityRepository {

    interface OnIdentityChangeListener {
        fun onIdentityChange(identityRepository: IdentityRepository)
    }

    fun addListener(listener: OnIdentityChangeListener)

    val hasIdentity: Boolean

    val identity: FavoritePlayer?

    fun isPlayer(player: AbsPlayer?): Boolean

    fun isPlayer(id: String?): Boolean

    fun removeIdentity()

    fun removeListener(listener: OnIdentityChangeListener?)

    fun setIdentity(player: AbsPlayer, region: Region)

}
