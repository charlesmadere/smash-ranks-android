package com.garpr.android.misc

import com.garpr.android.models.AbsPlayer
import com.garpr.android.models.Region

interface IdentityManager {

    interface OnIdentityChangeListener {
        fun onIdentityChange(identityManager: IdentityManager)
    }

    fun addListener(listener: OnIdentityChangeListener)

    val identity: AbsPlayer?

    val hasIdentity: Boolean

    fun isPlayer(player: AbsPlayer?): Boolean

    fun isPlayer(id: String?): Boolean

    fun removeIdentity()

    fun removeListener(listener: OnIdentityChangeListener?)

    fun setIdentity(player: AbsPlayer, region: Region)

}
