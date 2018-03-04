package com.garpr.android.misc

import android.app.Application
import com.garpr.android.misc.PlayerProfileManager.Presentation
import com.garpr.android.models.AbsRegion
import com.garpr.android.models.FullPlayer

class PlayerProfileManagerImpl(
        val application: Application,
        val favoritePlayersManager: FavoritePlayersManager,
        val identityManager: IdentityManager,
        val regionManager: RegionManager
) : PlayerProfileManager {

    override fun getPresentation(player: FullPlayer, region: AbsRegion): Presentation {
        return Presentation()
    }

}
