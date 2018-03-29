package com.garpr.android.managers

import android.content.Context
import com.garpr.android.managers.HomeToolbarManager.Presentation

class HomeToolbarManagerImpl(
        val identityManager: IdentityManager,
        val regionManager: RegionManager
) : HomeToolbarManager {

    override fun getPresentation(context: Context): Presentation {
        val region = regionManager.getRegion(context)
        return Presentation(region.hasActivityRequirements, identityManager.hasIdentity)
    }

}
