package com.garpr.android.managers

import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.data.models.Region

interface IdentityManager {

    interface OnIdentityChangeListener {
        fun onIdentityChange(identityManager: IdentityManager)
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
